package itunibo.automatedcarparking.configuration

import itunibo.automatedcarparking.transporttrolley.NamedPositionCollection
import java.io.File
import itunibo.automatedcarparking.transporttrolley.NamedPosition
import com.google.gson.Gson

object MapNamedPositionsReader {
	
	fun read(filename : String) : NamedPositionCollection {
		val string = File(filename).readText(Charsets.UTF_8)
		// json
		val gson = Gson()
		val collection = gson.fromJson<NamedPositionCollection>(string, NamedPositionCollection::class.java)
	
		return collection
	}
} 