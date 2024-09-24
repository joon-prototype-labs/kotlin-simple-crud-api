package dev.joon.simplecrudapi

import jakarta.persistence.*

@Entity
class SimpleRelation(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simple_id")
    var simple: Simple,
    var relationName: String
)
