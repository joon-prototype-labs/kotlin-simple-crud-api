package dev.joon.simplecrudapi

data class SimpleRes(
    val id: Long,
    val name: String,
    val description: String,
    val relations: List<SimpleRelationRes> = mutableListOf()
)

fun Simple.res() = SimpleRes(
    id = this.id,
    name = this.name,
    description = this.description,
    relations = this.relations.map { it.res() }
)

data class SimpleRelationRes(
    val id: Long,
    val relationName: String
)

fun SimpleRelation.res() = SimpleRelationRes(
    id = this.id,
    relationName = this.relationName
)
