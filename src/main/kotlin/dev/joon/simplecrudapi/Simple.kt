package dev.joon.simplecrudapi

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.OneToMany
import jakarta.persistence.FetchType
import jakarta.persistence.CascadeType

@Entity
class Simple(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var name: String,
    var description: String,
    @OneToMany(mappedBy = "simple", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var relations: List<SimpleRelation> = mutableListOf()
)
