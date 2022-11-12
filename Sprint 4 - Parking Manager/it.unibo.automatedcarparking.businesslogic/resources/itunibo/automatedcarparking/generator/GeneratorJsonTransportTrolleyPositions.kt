package itunibo.automatedcarparking.generator

import itunibo.automatedcarparking.transporttrolley.NamedPositionCollection
import itunibo.automatedcarparking.transporttrolley.NamedPosition
import itunibo.automatedcarparking.transporttrolley.Position
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import itunibo.automatedcarparking.rolodex.TransportTrolleyRolodex

fun generateKnownPositions() {
	val set = sortedSetOf<NamedPosition>()
	set.add(NamedPosition("HOME",    Position(0, 0, "downDir")))
	set.add(NamedPosition("INDOOR",  Position(6, 0, "upDir")))
	set.add(NamedPosition("OUTDOOR", Position(6, 4, "downDir")))
	set.add(NamedPosition("1",       Position(1, 1, "rightDir")))
	set.add(NamedPosition("2",       Position(1, 2, "rightDir")))
	set.add(NamedPosition("3",       Position(1, 3, "rightDir")))
	set.add(NamedPosition("4",       Position(4, 1, "leftDir")))
	set.add(NamedPosition("5",       Position(4, 2, "leftDir")))
	set.add(NamedPosition("6",       Position(4, 3, "leftDir")))
	val collection = NamedPositionCollection(set)
	
	// json 
	val gson = GsonBuilder().setPrettyPrinting().create()
	val string = gson.toJson(collection)
	
	File(TransportTrolleyRolodex.filenameNamedPositions).writeText(string, Charsets.UTF_8)
	
	println(string)
}

fun readKnownPositions() {
	val string = File(TransportTrolleyRolodex.filenameNamedPositions).readText(Charsets.UTF_8)
	
	// json
	val gson = Gson()
	val collection = gson.fromJson<NamedPositionCollection>(string, NamedPositionCollection::class.java)
	
	println(collection.toString())
}

fun main() {
	
	//readKnownPositions()
	generateKnownPositions()
}