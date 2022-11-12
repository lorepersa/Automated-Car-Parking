package it.unibo.automatedcarparking.mockdatagui

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
	WeightSensorConfig::class,
	BusinessLogicConfig::class,
	OutSonarConfig::class,
	ThermometerConfig::class
)
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
