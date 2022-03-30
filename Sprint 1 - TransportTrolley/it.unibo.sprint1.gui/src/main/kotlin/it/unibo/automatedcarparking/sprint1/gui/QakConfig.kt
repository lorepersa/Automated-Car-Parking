package it.unibo.automatedcarparking.sprint1.gui

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "businesslogic")
data class BusinessLogicConfig(val hostname : String, val port : Int)