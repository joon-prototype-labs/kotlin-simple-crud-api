package dev.joon.simplecrudapi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles

//@Import를 사용해서 Testcontainers Config(구성정보) 가져옴
@Import(TestcontainersConfiguration::class)
//@SpringBootTest를 사용해 스프링 통합 테스트를 위한 환경설정 구성
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// test profiles 사용하게 설정
@ActiveProfiles("test")
class SimpleCrudApiIntegrationTests(
    @Autowired val restTemplate: TestRestTemplate
) {

    @LocalServerPort
    val port: Int = 0

    private fun getBaseUrl() = "http://localhost:$port/api/simples"

    @Test
    fun `should create a Simple entity`() {
        val request = SimpleReq(name = "Test Name", description = "Test Description")
        val response: ResponseEntity<SimpleRes> = restTemplate.postForEntity(getBaseUrl(), HttpEntity(request), SimpleRes::class.java)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        assertEquals("Test Name", response.body?.name)
        assertEquals("Test Description", response.body?.description)
    }

    @Test
    fun `should get Simple entity by ID`() {
        val request = SimpleReq(name = "Test Name", description = "Test Description")
        val createResponse: ResponseEntity<SimpleRes> = restTemplate.postForEntity(getBaseUrl(), HttpEntity(request), SimpleRes::class.java)

        val id = createResponse.body?.id ?: fail("Simple creation failed")

        val response: ResponseEntity<SimpleRes> = restTemplate.getForEntity("${getBaseUrl()}/$id", SimpleRes::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals("Test Name", response.body?.name)
        assertEquals("Test Description", response.body?.description)
    }

    @Test
    fun `should update Simple entity`() {
        val createRequest = SimpleReq(name = "Old Name", description = "Old Description")
        val createResponse: ResponseEntity<SimpleRes> = restTemplate.postForEntity(getBaseUrl(), HttpEntity(createRequest), SimpleRes::class.java)

        val id = createResponse.body?.id ?: fail("Simple creation failed")

        val updateRequest = SimpleReq(name = "Updated Name", description = "Updated Description")
        restTemplate.put("${getBaseUrl()}/$id", HttpEntity(updateRequest))

        val response: ResponseEntity<SimpleRes> = restTemplate.getForEntity("${getBaseUrl()}/$id", SimpleRes::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Updated Name", response.body?.name)
        assertEquals("Updated Description", response.body?.description)
    }

    @Test
    fun `should delete Simple entity`() {
        val request = SimpleReq(name = "To Be Deleted", description = "To Be Deleted")
        val createResponse: ResponseEntity<SimpleRes> = restTemplate.postForEntity(getBaseUrl(), HttpEntity(request), SimpleRes::class.java)

        val id = createResponse.body?.id ?: fail("Simple creation failed")

        restTemplate.delete("${getBaseUrl()}/$id")

        val response: ResponseEntity<SimpleRes> = restTemplate.getForEntity("${getBaseUrl()}/$id", SimpleRes::class.java)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `should get all Simple entities`() {
        val request1 = SimpleReq(name = "Test 1", description = "Description 1")
        val request2 = SimpleReq(name = "Test 2", description = "Description 2")

        restTemplate.postForEntity(getBaseUrl(), HttpEntity(request1), SimpleRes::class.java)
        restTemplate.postForEntity(getBaseUrl(), HttpEntity(request2), SimpleRes::class.java)

        val response: ResponseEntity<List<SimpleRes>> = restTemplate.exchange(
            getBaseUrl(),
            org.springframework.http.HttpMethod.GET,
            null,
            object : org.springframework.core.ParameterizedTypeReference<List<SimpleRes>>() {}
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue(response.body!!.size >= 2)
    }

    @Test
    fun `should add a relation to Simple entity`() {
        val request = SimpleReq(name = "Test Name", description = "Test Description")
        val createResponse: ResponseEntity<SimpleRes> = restTemplate.postForEntity(getBaseUrl(), HttpEntity(request), SimpleRes::class.java)
        val simpleId = createResponse.body?.id ?: fail("Simple creation failed")

        val relationResponse: ResponseEntity<SimpleRes> = restTemplate.postForEntity(
            "${getBaseUrl()}/$simpleId/relations?relationName=Test Relation",
            null,
            SimpleRes::class.java
        )

        assertEquals(HttpStatus.OK, relationResponse.statusCode)
        assertNotNull(relationResponse.body)
        assertTrue(relationResponse.body?.relations?.any { it.relationName == "Test Relation" } == true)
    }
}
