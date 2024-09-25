package dev.joon.simplecrudapi

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.hibernate.Hibernate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@SpringBootTest
@Import(TestcontainersConfiguration::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CountryLazyLoadingTest {

    @Autowired
    private lateinit var countryRepository: CountryRepository

    @Autowired
    private lateinit var continentRepository: ContinentRepository

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    // allOpen 주석으로 감싸고 실행하면 실패함.
    // 즉, allOpen 대상에 @Entity가 있어야만 open class로 정의되고, lazy loading이 가능함.

    @Test
    @Transactional
    fun `test lazy loading of continent`() {
        val continent = continentRepository.save(Continent(id = "1", code = "AS", name = "Asia"))
        val country = countryRepository.save(Country(id = "1", alpha2Code = "KR", alpha3Code = "KOR", name = "Korea", continent = continent))

        entityManager.flush()
        logger.info { "after call entityManager.flush()" }
        entityManager.clear()
        logger.info { "after call entityManager.clear()" }

        logger.info { "before call fetchedCountry" }
        val fetchedCountry = countryRepository.findById(country.id).orElse(null)
        logger.info { "after call fetchedCountry" }

        assertNotNull(fetchedCountry)
        assertFalse(Hibernate.isInitialized(fetchedCountry.continent)) //Hibernate의 기능
        assertFalse(isContinentLoaded(fetchedCountry)) // JPA의 기능 (추천)

        logger.info { "before call fetchedCountry.continent.name" }
        val continentName = fetchedCountry.continent.name
        logger.info { "after call fetchedCountry.continent.name" }

        // Now the continent should be loaded
        assertTrue(isContinentLoaded(fetchedCountry))
    }

    private fun isContinentLoaded(country: Country): Boolean {
        return entityManager.entityManagerFactory.persistenceUnitUtil.isLoaded(country, "continent")
    }
}
