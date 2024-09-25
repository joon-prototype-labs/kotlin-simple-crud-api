package dev.joon.simplecrudapi

import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.hibernate.Hibernate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import org.junit.jupiter.api.Test

@SpringBootTest
@Import(TestcontainersConfiguration::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class SimpleServiceLazyLoadingTests {

    @Autowired
    private lateinit var simpleService: SimpleService

    @Autowired
    private lateinit var entityManager: EntityManager

    // 그럼 이건 왜 항상 성공하는거야?
    // 뭔가 양방향이라서 다르게 동작하나? 일단...
    // 내가 의도하는건 Simple한 예시 코드라, 그냥 Relation 자체를 지울듯 아마

    // https://www.inflearn.com/community/questions/792383/%ED%85%8C%EC%8A%A4%ED%8A%B8%EC%97%90%EC%84%9C%EC%9D%98-transactional-%EC%82%AC%EC%9A%A9%EC%97%90-%EB%8C%80%ED%95%B4-%EC%A7%88%EB%AC%B8%EC%9D%B4-%EC%9E%88%EC%8A%B5%EB%8B%88%EB%8B%A4?srsltid=AfmBOoqVhK1CJrxEKVIkTFjKYM9R6cjfNqMxbonWymRMlrhAh9ONOaKI
    // Tansatinonal Test 관련 좋은 자료: https://www.linkedin.com/posts/anyjava_%EC%86%90%ED%98%84%ED%83%9C-%EC%8A%A4%ED%94%84%EB%A7%81-%ED%85%8C%EC%8A%A4%ED%8A%B8%EC%97%90%EC%84%9C-transactional-%EC%82%AC%EC%9A%A9%EC%97%90-%EB%8C%80%ED%95%9C-activity-7170213521668993024-kxJ4?utm_source=share&utm_medium=member_desktop
    // 테스트 시에 Tansatinonal을 여러 번 사용하는 걸 테스트하기 어려움...
    // 그래서 사용중인 EntityManager를 clear하면서 lazy loading을 검증함.

    @Test
    @Transactional
    fun `should lazy load SimpleRelation in Simple`() {
        val simpleId = createSimpleWithRelation()
        entityManager.clear()
        checkLazyLoading(simpleId)
        entityManager.clear()
        checkRelationsAfterInitialization(simpleId)
    }

    fun createSimpleWithRelation(): Long {
        val simpleReq = SimpleReq(name = "Simple Name", description = "Simple Description")
        val savedSimple = simpleService.create(simpleReq)
        simpleService.addRelation(savedSimple.id, "Relation 1")
        return savedSimple.id
    }

    fun checkLazyLoading(id: Long) {
        val foundSimple = simpleService.getById(id)
        assertThat(foundSimple).isNotNull
        assertThat(Hibernate.isInitialized(foundSimple!!.relations)).isFalse
    }

    fun checkRelationsAfterInitialization(id: Long) {
        val foundSimple = simpleService.getById(id)
        assertThat(foundSimple).isNotNull
        val relations = foundSimple!!.relations
        assertThat(relations).hasSize(1)
        assertThat(Hibernate.isInitialized(relations)).isTrue
        assertThat(relations[0].relationName).isEqualTo("Relation 1")
    }
}
