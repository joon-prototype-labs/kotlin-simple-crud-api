# Simple CRUD API

간단한 API 서버 예시 코드 (Kotlin + Gradle + Spring Boot)

이 프로젝트는 Spring Initializr를 사용하여 생성된 Spring Boot 3.3.4 버전을 기반으로 하며, Spring Boot, Kotlin, Gradle의 기본 구성을 이해하고 참고하기 위해 작성되었습니다.

### 주요 특징
- **Gradle Plugin의 역할**: Gradle 플러그인 및 관련 설정 설명
- **JPA 사용 시 필수 플러그인 및 설정**: JPA 적용을 위한 플러그인 및 필수로 추가해야하는 설정 설명
- **Java/Kotlin Gradle 설정**: Kotlin과 Java의 Gradle 설정 내용 확인
- **TestContainers를 이용한 통합 테스트**: Spring과 TestContainers를 사용한 간단한 통합 테스트 예시

### 실행 방법
1. 프로젝트를 클론하고 Gradle을 사용하여 의존성을 설치
2. `{project-root}/src/test/kotlin/dev/joon/simplecrudapi` 아래의 테스트 코드를 실행하여 실습
3. 필요에 따라 의존성 추가와 properties 작성을 통해 기능을 확장  
   1. 이 프로젝트는 API 서버를 실행(run)하기 위한 의존성은 제공하지 않음 
