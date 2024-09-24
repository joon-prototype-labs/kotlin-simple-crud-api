package dev.joon.simplecrudapi

import jakarta.persistence.*

@Entity
class Simple(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var name: String,
    var description: String,
    @OneToMany(mappedBy = "simple", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var relations: List<SimpleRelation> = mutableListOf()
)
