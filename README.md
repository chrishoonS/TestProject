# TestProject

## 📂 프로젝트 구조 (Directory Structure)
프로젝트 루트 디렉터리부터의 전체 구조는 다음과 같습니다.
```
TestProject
├── .gradle
├── .idea
├── build
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.medi.testproject
│   │   │       ├── common
│   │   │       │   ├── ExceptionAdvice
│   │   │       │   ├── FormDataEncoder
│   │   │       │   └── MsgEntity
│   │   │       ├── config
│   │   │       │   ├── ContainerConfig
│   │   │       │   └── RedisConfig
│   │   │       ├── crawl.service
│   │   │       │   ├── NewsCrawlService
│   │   │       │   └── StockCrawlService
│   │   │       ├── oauth
│   │   │       │   ├── OAuthProvider
│   │   │       │   └── OAuthTokenResponseDTO
│   │   │       ├── snsLogin
│   │   │       ├── study
│   │   │       ├── HomeController
│   │   │       ├── SessionController
│   │   │       └── TestProjectApplication
│   │   └── resources
│   │       ├── http
│   │       │   └── login.http
│   │       ├── static
│   │       │   ├── apple.img
│   │       │   ├── kakao.img
│   │       │   ├── naver.img
│   │       │   └── favicon.ico
│   │       ├── templates
│   │       │   └── index.html
│   │       └── application.yml
│   │           ├── application-dev.yml
│   │           ├── application-local.yml
│   │           └── application-real.yml
│   └── test
│       └── java
│           └── com.medi.testproject
│               └── TestProjectApplicationTests
├── .gitattributes
├── .gitignore
├── build.gradle
├── gradlew
├── gradlew.bat
├── README.md
└── settings.gradle
```