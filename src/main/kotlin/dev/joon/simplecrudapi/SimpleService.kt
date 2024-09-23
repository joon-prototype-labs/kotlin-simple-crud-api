package dev.joon.simplecrudapi

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SimpleService(private val repository: SimpleRepository) {

    @Transactional
    fun create(req: SimpleReq): Simple {
        return repository.save(Simple(name = req.name, description = req.description))
    }

    @Transactional
    fun update(id: Long, req: SimpleReq): Simple? {
        val existingSimple = repository.findById(id).orElse(null) ?: return null
        existingSimple.name = req.name
        existingSimple.description = req.description
        return repository.save(existingSimple)
    }

    @Transactional
    fun delete(id: Long): Boolean {
        return if (repository.existsById(id)) {
            repository.deleteById(id)
            true
        } else {
            false
        }
    }

    @Transactional(readOnly = true)
    fun getById(id: Long): Simple? {
        return repository.findById(id).orElse(null)
    }

    @Transactional(readOnly = true)
    fun getAll(): List<Simple> {
        return repository.findAll()
    }
}
