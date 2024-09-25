package dev.joon.simplecrudapi

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

@Entity
class Country(
    @Id
    var id: String,
    var alpha2Code: String,
    var alpha3Code: String,
    var name: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "continent_id")
    var continent: Continent
)

@Entity
class Continent(
    @Id
    var id: String,
    var code: String,
    var name: String
)

interface CountryRepository : JpaRepository<Country, String>
interface ContinentRepository : JpaRepository<Continent, String>
