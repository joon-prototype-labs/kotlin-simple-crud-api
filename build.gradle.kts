// gradle plugins: https://docs.gradle.org/current/userguide/plugins.html
// 플러그인은 새로운 tasks, domain objects, conventions를 도입하고, 플러그인 객체를 확장한다.
plugins {
    kotlin("jvm") version "1.9.25"
    // Kotlin All-open compiler plugin / Spring support: https://kotlinlang.org/docs/all-open-plugin.html#spring-support
    // kotlin-allopen의 Wrapper로 스프링과 관련된 어노테이션이 붙은 객체를 open하도록 미리 설정되어있다.
    kotlin("plugin.spring") version "1.9.25"
    kotlin("kapt") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    // Kotlin No-arg compiler plugin / JPA support: https://kotlinlang.org/docs/no-arg-plugin.html#jpa-support
    // plugin.noarg의 Wrapper로 JPA와 관련된 어노테이션이 붙은 객체를 no-arg를 생성하도록 하도록 미리 설정되어있다.
    // Kotlin + JPA 사용 시 주의와 개인적인 생각이 담긴 블로그: https://colabear754.tistory.com/145
    //   위 블로그에 추가 설명
    //   1. 요즘에는 기본적으로 필요할 설정을 자동으로 제공해주는 덕분에 반복되는 구현을 하지 않아도 된다.
    //   2. JPA Entity로 data class가 적절한가?
    //        관련 추가 자료 - Should I use Kotlin data class as JPA entity?: https://stackoverflow.com/questions/58127353/should-i-use-kotlin-data-class-as-jpa-entity
    //                      https://github.com/spring-guides/tut-spring-boot-kotlin?tab=readme-ov-file#persistence-with-jpa
    //        Spring 공식문서에서는 추천하지 않는다. (처음 커밋에는 data class 예시를 사용하다가 v2로 문서를 업데이트하면서 더 이상 data class를 사용하지 않는다. 참고: https://github.com/spring-guides/tut-spring-boot-kotlin/commit/7041397421f20d251cb2ee0b5e9c5ef809804410)
    //        상속이 불가능하므로 확장성을 고려해야 하고, 이후에 변경해야 하는 불편함을 겪을 수 있음.
    //        그러나 순환참조는 어디서나 발생할 수 있고, equals()와 hashCode() 문제는 BaseEntity를 상속하는 것으로 해결 가능하다.
    //        개인적으로는 data class가 편리한 부분이 많았고, 필요에 따라 충분히 선택할 수 있다고 생각한다. (물론 적극적인 추천은 하지 않지만...)
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("plugin.noarg") version "1.9.25"
    kotlin("plugin.allopen") version "1.9.25"
    // TODO 아니 근데 open을 jpa 관련해서 해준다는 말을 없는데 뭐인거임 대체
    //  no-arg야 명세에 있으니 그렇다 쳐도, all-open은 코드에 없잖아
    //  사실 필요 없는거 아닐까? entity의 all-open? 근데 그럼 프록시를 못 쓰는데...
    //  안되는게 맞음. lazy loading이 안되고 있음. - 나와 같은 고민을 한 사람의 글: https://wslog.dev/kotlin-jpa
    //  그래서 all open은 추가해줘야 함.
}

group = "dev.joon"
version = "0.0.1-SNAPSHOT"

// Gradle - Building Java & JVM projects: https://docs.gradle.org/current/userguide/building_java_projects.html
// Gradle - Toolchains for JVM projects: https://docs.gradle.org/current/userguide/toolchains.html
// Baeldung- Gradle Toolchains Support for JVM Projects:  https://www.baeldung.com/java-gradle-toolchains-jvm-projects
// Toolchains은 소프트웨어를 빌드, 테스트 및 실행하는 데 필요한 도구와 바이너리 세트이다. JDK가 이에 해당됨.
// gradle은 이러한 Toolchains을 정의할 수 있도록 기능을 제공한다. 다양한 JDK vender, 언어 레벨 등을 설정할 수 있다.
java {
    toolchain {
        // Java 언어 버전을 명시 (필수: 아마)
        languageVersion = JavaLanguageVersion.of(17)
    }
    /*
     * 아래 코드는 현재 프로젝트 버전의 Spring Initializr(오타 아님, zr이 맞는 프로젝트 이름이다.) 기본으로 제공되진 않으나 자주 보이는 코드이므로 추가함.
     * https://docs.gradle.org/current/dsl/org.gradle.api.plugins.JavaPluginExtension.html
     * https://www.baeldung.com/gradle-sourcecompatiblity-vs-targetcompatibility
     * 필요에 따라 javac에서 사용하는 source, target 컴파일 옵션을 제어할 수 있다.
     * sourceCompatibility = "21" // 사용할 수 있는 언어 기능을 해당 Java 버전으로 제한한다.
     * targetCompatibility = "21" // 프로그램이 실행할 수 있는 가장 낮은 Java 버전.
     */
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Java의 타입 시스템에서 null-safety를 표현할 수 없지만, Spring Framework는 org.springframe.lang 패키지에 선언된 도구 친화적 주석을 통해(?, via tooling-friendly) 전체 Spring Framework API의 null-safety를 제공한다.
// JSR-305는 Java SE 표준 패키지에 포함되지 않는다. 즉, Java의 기본 API에 포함된 것이 아니며, 외부 라이브러리나 프레임워크가 이를 구현한다.
// Refer to the following links for more information:
// Kotlin Support: https://docs.spring.io/spring-boot/reference/features/kotlin.html#features.kotlin.null-safety
// Calling Java from Kotlin: https://kotlinlang.org/docs/java-interop.html#jsr-305-support
// JSR-305: https://jcp.org/en/jsr/detail?id=305
kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// TODO TestContainer 에 관한 설명 추가하기
// TODO 이후에 Gradle + (Koltin) 관련 프로젝트 추가하고, 프로젝트 링크 걸기. 여기에서는 Gradle 관련한건 간단하게만 다루기
