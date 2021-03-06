package it.unibo.automatedcarparking.sprint1.gui

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication


@SpringBootApplication
@EnableConfigurationProperties(
	BusinessLogicConfig::class,
	WEnvConfig::class
)
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}

