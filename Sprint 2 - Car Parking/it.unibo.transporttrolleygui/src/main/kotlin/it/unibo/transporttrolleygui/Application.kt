package it.unibo.transporttrolleygui

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

