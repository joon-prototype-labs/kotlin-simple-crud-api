package dev.joon.simplecrudapi

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SimpleService(
    private val simpleRepository: SimpleRepository,
    private val simpleRelationRepository: SimpleRelationRepository
) {

    @Transactional
    fun create(req: SimpleReq): Simple {
        val simple = simpleRepository.save(Simple(name = req.name, description = req.description))
        return simple
    }

    @Transactional
    fun addRelation(simpleId: Long, relationName: String): Simple? {
        val simple = simpleRepository.findById(simpleId).orElse(null) ?: return null
        val relation = SimpleRelation(relationName = relationName, simple = simple)
        simpleRelationRepository.save(relation)
        return simple
    }

    @Transactional
    fun update(id: Long, req: SimpleReq): Simple? {
        val existingSimple = simpleRepository.findById(id).orElse(null) ?: return null
        existingSimple.name = req.name
        existingSimple.description = req.description
        return simpleRepository.save(existingSimple)
    }

    @Transactional
    fun delete(id: Long): Boolean {
        return if (simpleRepository.existsById(id)) {
            simpleRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    @Transactional(readOnly = true)
    fun getById(id: Long): Simple? {
        return simpleRepository.findById(id).orElse(null)
    }

    @Transactional(readOnly = true)
    fun getAll(): List<Simple> {
        return simpleRepository.findAll()
    }
}
