package itunibo.automatedcarparking.transporttrolley

data class NamedPositionCollection(val positions : Collection<NamedPosition>) {
	
	fun get(name : String) : Position? {
		val namedPosition =  positions.find { it.name.equals(name, true) }
		
		if (namedPosition != null) {
			return namedPosition.position
		}
		return null
	}
	
	override fun toString() : String {
		var string = ""
		positions.forEach {
			string += it.toString() + System.lineSeparator()
		}
		return string
	}
}