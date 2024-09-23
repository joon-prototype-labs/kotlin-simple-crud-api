package dev.joon.simplecrudapi

import org.springframework.data.jpa.repository.JpaRepository

interface SimpleRepository : JpaRepository<Simple, Long>
