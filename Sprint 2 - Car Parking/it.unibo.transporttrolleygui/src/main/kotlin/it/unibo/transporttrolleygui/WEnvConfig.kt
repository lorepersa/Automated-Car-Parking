package it.unibo.transporttrolleygui

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "wenv")
data class WEnvConfig(val url : String)