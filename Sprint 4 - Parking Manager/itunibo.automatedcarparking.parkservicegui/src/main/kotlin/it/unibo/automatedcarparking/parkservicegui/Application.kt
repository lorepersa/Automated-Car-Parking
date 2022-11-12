package it.unibo.automatedcarparking.parkservicegui

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
	BusinessLogicConfig::class
)
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
