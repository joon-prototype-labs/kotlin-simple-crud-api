package dev.joon.simplecrudapi

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/simples")
class SimpleController(private val service: SimpleService) {

    @PostMapping
    fun create(@RequestBody req: SimpleReq): ResponseEntity<Simple> {
        val createdSimple = service.create(req)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSimple)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Simple> {
        val simple = service.getById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(simple)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody req: SimpleReq): ResponseEntity<Simple> {
        val updatedSimple = service.update(id, req) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(updatedSimple)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        return if (service.delete(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<Simple>> {
        val simples = service.getAll()
        return ResponseEntity.ok(simples)
    }
}
