# step-01. Mockmvc Test - JSON, SSR
Mockmvc를 활용하여 Web layer 기능을 검증하는 예제입니다. 해당 코드는 [Testing the Web Layer](https://spring.io/guides/gs/testing-web)를 참고했습니다.

## 중요 포인트
* Mockmvc 특징
* HTTP 요청/응답 검증(1) - GetMapping
* HTTP 요청/응답 검증(2) - PostMapping
* HTTP 요청/응답 검증(3) - JSON
* HTTP 요청/응답 검증(4) - View(SSR)

## Mockmvc 특징
### 배경
서버를 구동시키는 테스트 방법은 빠른 피드백을 요구하는 unit 테스트에는 비효율적입니다. 그리고 매번 데이터베이스를 포함한 리소스 설정까지 해야하는 번거로움도 있습니다. 그래서 spring MVC 컨트롤러 단에서 서버 구동없이 효율적으로 검증하는 `Mockmvc`가 등장했습니다.

### 장점
* 빠른 테스트 : 실제 서버 구동없이 unit test 가능합니다.
* HTTP 요청과 응답 검증 : 메소드를 사용하여 컨트롤러 맵핑, 요청 처리 로직, 응답 생성 등 쉽게 검증할 수 있습니다.

### 단점
* 검증 가능한 Layer 한계 : 네트워크 조건에서 발생할 수 있는 문제는 검출하지 못할 수 있습니다.

## HTTP 요청/응답 검증(1) - GetMapping
"/" 요청시 "Hello World"를 출력하는 컨트롤러를 검증하는 코드입니다.
```java
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;
    
    @Test
    @DisplayName("/요청시 Hello World 출력")
    public void print_Hello_World() throws Exception {
        mockMvc.perform(get("/")) 
                .andExpect(status().isOk()) // 200
                .andExpect(content().string("Hello World")) 
                .andDo(print());
    }
}
```
* `@SpringBootTest` : 애플리케이션 컨텍스트를 로드하고 모든 빈을 등록합니다. 결과적으로 애플리케이션과 동일한 환경에서 테스트하기 위함입니다.
* `@AutoConfigureMockMvc` : property 기본설정을 자동으로 구성하고, 컨트롤러단 테스트가 가능합니다. 참고로 mockmvc는 Spring Security가 적용된 환경에서 인증과 권한부여 테스트도 가능할만큼 파워풀합니다. 나중에 시큐리티 다룰때 다시 말씀드리겠습니다.
* `perform()` : HTTP 요청을 실행하는 메소드입니다. 요청을 수행하고 결과를 반환합니다.
* `andExpect()` : MockMvc 실행에 대한 예상결과를 출력하는 메소드입니다. 실제값과 비교하여 pass/fail 여부를 검토합니다.
* `andDo()` : print() 호출시 HTTP 요청/응답에 대한 summary가 출력됩니다.


## HTTP 요청/응답 검증(2) - PostMapping
"/account" 요청시 사용자(name, age) 등록을 검증하는 코드입니다.
```java
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("계정 등록 - query parameter")
    public void signUp_account() throws Exception {
        mockMvc.perform(post("/account")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("name", "memberA")
                        .param("age", "20"))
                .andExpect(status().isOk())
                .andExpect(content().string("signUp account"))
                .andDo(print());
    }
}
```
* `contentType` : HTTP 요청/응답시 헤더에 포함되어 보내는 데이터의 유형을 나타내는 필드입니다.
* `param` : post 요청시 쿼리 파라미터로 넘길 변수를 지정하고, ?key1=value1&key2=value2 형태로 서버로 전달합니다.

```java
@RestController
@Slf4j
public class AccountController {

    @PostMapping("/account")
    public String signUp(Account account) {
        log.info("name={}, age={}", account.getName(), account.getAge());
        return "signUp account";
    }
}
```
* spring MVC는 요청 파라미터를 Account 객체의 필드값과 자동으로 매핑합니다. 전달 받은 데이터는 서버에서 비지니스로직에 따라 처리합니다.
* controller가 읽은 데이터는 `@Slf4j`에 의해 아래와 같이 로그 메세지로 확인할 수 있습니다.
* 2024-08-06T14:06:16.214+09:00  INFO 20808 --- [spring-boot-basic] [    Test worker] m.c.s.controller.AccountController       : name=memberA, age=20

## HTTP 요청/응답 검증(3) - JSON
클라이언트에서 JSON 데이터를 서버로 전달했을때 응답을 검증하는 코드입니다.
```java
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;
    
    @Test
    @DisplayName("계정 등록 - JSON")
    public void signUp_json() throws Exception {
        mockMvc.perform(post("/account")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"memberA\", \"age\":\"20\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("memberA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value("20"))
                .andDo(print());
    }
}
```
* `jsonPath` : json 표현식을 사용하여 메세지 바디의 json 데이터를 검증하는 메소드입니다.
* 