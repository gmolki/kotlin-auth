package com.gmolki.kotlinauth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class KotlinAuthApplication

fun main(args: Array<String>) {
	runApplication<KotlinAuthApplication>(*args)
}
