package it.unibo.automatedcarparking.mockdatagui

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "weightsensor")
data class WeightSensorConfig(val hostname : String, val port : Int)

@ConstructorBinding
@ConfigurationProperties(prefix = "outsonar")
data class OutSonarConfig(val hostname : String, val port : Int)

@ConstructorBinding
@ConfigurationProperties(prefix = "businesslogic")
data class BusinessLogicConfig(val hostname : String, val port : Int)
