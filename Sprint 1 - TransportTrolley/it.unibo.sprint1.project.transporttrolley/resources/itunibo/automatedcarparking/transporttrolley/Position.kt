package itunibo.automatedcarparking.transporttrolley

data class Position(var column : Int, var row : Int, var direction : String) {
	
	
	override fun toString() : String {
		return "column: $column, row: $row, direction: $direction"
	}
}