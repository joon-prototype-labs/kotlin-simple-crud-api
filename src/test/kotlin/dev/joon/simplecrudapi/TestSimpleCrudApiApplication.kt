package dev.joon.simplecrudapi

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<SimpleCrudApiApplication>().with(TestcontainersConfiguration::class).run(*args)
}
