package dev.joon.simplecrudapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SimpleCrudApiApplication

fun main(args: Array<String>) {
    runApplication<SimpleCrudApiApplication>(*args)
}
