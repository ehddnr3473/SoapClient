# SoapClient

## WSDL

WSDL은 웹 서비스의 사용 설명서를 XML로 정의한 파일이다. 주로 SOAP(Simple Object Access Protocol) 기반의 웹 서비스에서 메서드 이름, 파라미터(데이터 타입), 반환값 등을 정의하는 계약 역할을 한다.

### WSDL 파일 구조

```
<wsdl:definitions>                 ← 전체를 감싸는 루트
  ├─ <wsp:Policy>     (선택) 보안·전송 규칙       ─ "어떤 조건으로"
  ├─ <wsdl:types>     데이터 타입 정의           ─ "무엇을 주고받나" (데이터 모양)
  ├─ <wsdl:message>   메시지 정의               ─ "무엇을 주고받나" (편지 봉투)
  ├─ <wsdl:portType>  기능(operation) 목록      ─ "무엇을 할 수 있나"
  ├─ <wsdl:binding>   전송 방식                 ─ "어떻게 보내나"
  └─ <wsdl:service>   실제 주소                 ─ "어디로 보내나"
```

블록마다 앞 블록을 이름으로 참조하는 사슬 구조라서, 아래에서 위로 거꾸로 따라가면 이해하기 쉽다.

```
service ──참조──▶ binding ──참조──▶ portType ──참조──▶ message ──참조──▶ types
(주소)            (SOAP 방식)       (기능 목록)         (입출력 봉투)      (필드 정의)
```

### WSDL을 읽는다

Visual Studio가 WSDL 파일을 보고 C# 코드를 대신 만들어 준다는 뜻. Visual Studio에서 '웹 참조 추가' 기능을 통해 WSDL 주소를 넣고 Visual Studio가 그대로 C# 클래스로 바꿔 Reference.cs를 생성한다.

SAP 쪽에서 "이 주소로, 이런 이름의 기능을, 이런 입력을 넣어 부르면 이런 결과를 준다."를 정해 이 WSDL 파일로 공개한다. -> 이 파일의 복사본이 닷넷 소스의 document.wsdl

## Spring migration

### 사전 지식

#### JAXB(Java Architecture for XML Binding)

자바 객체와 XML을 서로 변환해 주는 표준 API이다. JSON에서 Jackson이 하는 역할을 XML에서 한다.

* 마샬링(marshalling): 자바 객체 -> XML
* 언마샬링(unmarshalling): XML -> 자바 객체

### build.gradle

```groovy
// Spring Web Services start는 서버와 클라이언트 지원을 모두 가져온다. REST 컨트롤러를 제공하므로 webmvc도 사용한다.
implementation 'org.springframework.boot:spring-boot-starter-webservices'
implementation 'org.springframework.boot:spring-boot-starter-webmvc'
```

REST 컨트롤러 없이 SOAP 클라이언트만 쓸 때는 서버 지원(webmvc)을 제외해 내장 톰캣이 뜨지 않게 할 수 있다.

```groovy
implementation('org.springframework.boot:spring-boot-starter-webservices') {
    exclude group: 'org.springframework.boot', module: 'spring-boot-starter-webmvc'
}
```
