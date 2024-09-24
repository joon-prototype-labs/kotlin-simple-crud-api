package dev.joon.simplecrudapi

import org.springframework.data.jpa.repository.JpaRepository

interface SimpleRelationRepository : JpaRepository<SimpleRelation, Long>
