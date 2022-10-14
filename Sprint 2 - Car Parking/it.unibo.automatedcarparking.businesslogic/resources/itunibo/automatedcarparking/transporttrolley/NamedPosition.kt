package itunibo.automatedcarparking.transporttrolley

data class NamedPosition(val name : String,
						 val position : Position) : Comparable<NamedPosition> {
	
	override operator fun compareTo(other: NamedPosition) : Int {
		return name.compareTo(other.name)
	}
	
	override fun toString() : String {
		return "name: $name, " + position
	}
}