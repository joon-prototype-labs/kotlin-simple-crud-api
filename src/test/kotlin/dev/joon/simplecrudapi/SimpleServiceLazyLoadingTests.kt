package dev.joon.simplecrudapi

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManager
import org.hibernate.Hibernate
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import org.junit.jupiter.api.Test


private val logger = KotlinLogging.logger {}

@SpringBootTest
@Import(TestcontainersConfiguration::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class SimpleServiceLazyLoadingTests {

    @Autowired
    private lateinit var simpleRepository: SimpleRepository

    @Autowired
    private lateinit var simpleRelationRepository: SimpleRelationRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    // 그럼 이건 왜 항상 성공하는거야? - 아마 연관관계의 주인인 simpleRelation가 아닌 Simple을 대상으로 수행하고 있어서 그런 듯?
    // 뭔가 양방향이라서 다르게 동작하나? 일단...
    // 내가 의도하는건 Simple한 예시 코드라, 그냥 Relation 자체를 지울듯 아마

    // https://www.inflearn.com/community/questions/792383/%ED%85%8C%EC%8A%A4%ED%8A%B8%EC%97%90%EC%84%9C%EC%9D%98-transactional-%EC%82%AC%EC%9A%A9%EC%97%90-%EB%8C%80%ED%95%B4-%EC%A7%88%EB%AC%B8%EC%9D%B4-%EC%9E%88%EC%8A%B5%EB%8B%88%EB%8B%A4?srsltid=AfmBOoqVhK1CJrxEKVIkTFjKYM9R6cjfNqMxbonWymRMlrhAh9ONOaKI
    // Tansatinonal Test 관련 좋은 자료: https://www.linkedin.com/posts/anyjava_%EC%86%90%ED%98%84%ED%83%9C-%EC%8A%A4%ED%94%84%EB%A7%81-%ED%85%8C%EC%8A%A4%ED%8A%B8%EC%97%90%EC%84%9C-transactional-%EC%82%AC%EC%9A%A9%EC%97%90-%EB%8C%80%ED%95%9C-activity-7170213521668993024-kxJ4?utm_source=share&utm_medium=member_desktop
    // 테스트 시에 Tansatinonal을 여러 번 사용하는 걸 테스트하기 어려움...
    // 그래서 사용중인 EntityManager를 clear하면서 lazy loading을 검증함.

    @Test
    @Transactional
    fun `test lazy loading of simple`() {
        val simple = simpleRepository.save(Simple(id = 1, name = "Test Name", description = "Test Description"))
        val simpleRelation = simpleRelationRepository.save(SimpleRelation(id = 1, simple = simple, relationName = "Test Relation"))

        entityManager.flush()
        logger.info { "after call entityManager.flush()" }
        entityManager.clear()
        logger.info { "after call entityManager.clear()" }

        logger.info { "before call fetchedCountry" }
        val fetchedSimpleRelation = simpleRelationRepository.findById(simpleRelation.id).orElse(null)
        logger.info { "after call fetchedCountry" }

        assertNotNull(fetchedSimpleRelation)
        assertFalse(Hibernate.isInitialized(fetchedSimpleRelation.simple)) //Hibernate의 기능
        assertFalse(isContinentLoaded(fetchedSimpleRelation)) // JPA의 기능 (사용 추천)

        logger.info { "before call fetchedSimpleRelation.simple.name" }
        val simpleName = fetchedSimpleRelation.simple.name
        logger.info { "after call fetchedSimpleRelation.simple.name" }

        // Now the continent should be loaded
        assertTrue(isContinentLoaded(fetchedSimpleRelation))
    }

    private fun isContinentLoaded(simpleRelation: SimpleRelation): Boolean {
        return entityManager.entityManagerFactory.persistenceUnitUtil.isLoaded(simpleRelation, "simple")
    }
}
