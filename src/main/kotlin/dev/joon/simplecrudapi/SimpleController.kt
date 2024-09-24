package dev.joon.simplecrudapi

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/simples")
class SimpleController(private val service: SimpleService) {

    @PostMapping
    fun createSimple(@RequestBody req: SimpleReq): ResponseEntity<SimpleRes> {
        val simple = service.create(req)
        return ResponseEntity.status(HttpStatus.CREATED).body(simple.res())
    }

    @PostMapping("/{id}/relations")
    fun addRelation(
        @PathVariable id: Long,
        @RequestParam relationName: String
    ): ResponseEntity<SimpleRes> {
        val simple = service.addRelation(id, relationName) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        return ResponseEntity.ok(simple.res())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<SimpleRes> {
        val simple = service.getById(id) ?: return ResponseEntity.notFound().build()
        val simpleTemp = simple
        return ResponseEntity.ok(simple.res())
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<SimpleRes>> {
        val simples = service.getAll()
        // OSIV (Open Session In VIew)가 active이므로 Transaction 바깥에서 Lazy Loading 가능
        return ResponseEntity.ok(simples.map { it.res() })
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody req: SimpleReq): ResponseEntity<SimpleRes> {
        val updatedSimple = service.update(id, req) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(updatedSimple.res())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        return if (service.delete(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
