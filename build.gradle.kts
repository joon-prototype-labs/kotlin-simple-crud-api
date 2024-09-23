plugins {
    kotlin("jvm") version "1.9.25"
    // Kotlin All-open compiler plugin / Spring support: https://kotlinlang.org/docs/all-open-plugin.html#spring-support
    // kotlin-allopen의 Wrapper로 스프링과 관련된 어노테이션이 붙은 객체를 open하도록 미리 설정되어있다.
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    // Kotlin No-arg compiler plugin / JPA support: https://kotlinlang.org/docs/no-arg-plugin.html#jpa-support
    // plugin.noarg의 Wrapper로 JPA와 관련된 어노테이션이 붙은 객체를 open 하도록 미리 설정되어있다.
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
}

group = "dev.joon"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
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
