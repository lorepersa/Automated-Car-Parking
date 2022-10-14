package itunibo.automatedcarparking.configuration

import mapRoomKotlin.RoomMap
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.FileInputStream
import mapRoomKotlin.Box
import java.io.ObjectOutputStream
import java.io.FileOutputStream
import itunibo.automatedcarparking.rolodex.MapRolodex

object ParkingAreaMapReader {
	
	fun generateRoomMapBin() {
		val txtFilename = MapRolodex.filenameMap + ".txt"
		val binFilename = MapRolodex.filenameMap + ".bin"
		val map : RoomMap = readMapFromTxt(txtFilename)
		val os = ObjectOutputStream(FileOutputStream(binFilename))
		os.writeObject(map)
		os.flush()
		os.close()
	}
	
	private fun createBox(c : Char) : Box? {
		when(c) {
			'r' -> return Box(false, false, true)
			'X' -> return Box(true, false, false)
			'0' -> return Box(false, true, false)
			'1' -> return Box(false, false, false)
		}

		return null
	}
	
	private fun readMapFromTxt(file : String) : RoomMap {
		val map = RoomMap.getRoomMap()
		val reader = BufferedReader(InputStreamReader(FileInputStream(file)))
		var line = reader.readLine()

		var row = 0
		var tokens : List<String>
		var columns = 0
		var box : Box? = null
		var s : String

		while(line != null) {

			line = line.trim()
			if(line.endsWith(","))
				line = line.substring(0..(line.length - 2))

			tokens = line.trim().split(",")
			if(row == 0)
				columns = tokens.size - 1
			else
				if((tokens.size-1) != columns) {
					println("ParkingMapReader | Bad file format: wrong number of columns at row $row")
					System.exit(-1)
				}

			for(column in 0..columns) {
				s = tokens[column].trim()
				if(s.length == 1) box = createBox(s.get(0))
				else if(s.length == 2 && s.get(0) == '|') box = createBox(s.get(1))
				else {
					println("ParkingMapReader | Invalid length for sequence \'$s\' in line ${row+1}")
					System.exit(-1)
				}

				if(box != null)
					map.put(column, row, box)
				if(box == null) {
					println("ParkingMapReader | Invalid sequence \'$s\' in line ${row+1}")
					System.exit(-1)
				}
			}
 
			line = reader.readLine()
			row++  
		}
		
		return map  
	}  
}