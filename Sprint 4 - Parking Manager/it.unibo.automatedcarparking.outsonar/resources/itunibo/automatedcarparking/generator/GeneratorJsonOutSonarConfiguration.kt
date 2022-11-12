package itunibo.automatedcarparking.generator

import itunibo.automatedcarparking.outsonar.OutSonarConfiguration
import com.google.gson.GsonBuilder
import itunibo.automatedcarparking.rolodex.OutSonarRolodex
import java.io.File

fun generateJsonOutSonarConfiguration() {
	// json 
	val gson = GsonBuilder().setPrettyPrinting().create()
	
	val configuration = OutSonarConfiguration(mode="virtual")
	
	val string = gson.toJson(configuration)
	
	File(OutSonarRolodex.filenameConfiguration).writeText(string, Charsets.UTF_8)
	
	println(string)
}

fun main() {
	generateJsonOutSonarConfiguration()
}