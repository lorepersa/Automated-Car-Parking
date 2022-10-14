package itunibo.automatedcarparking.parkingarea

data class TokenId private constructor(val token : String) {
	
	var slotnum : Int = 0
		private set
	
	companion object {
		
		@JvmField
		val tokenIdRandomCharactersNum = 4
		
		@JvmField
		val allowedChars = ('A'..'Z') + ('a'..'z')
		
		operator fun invoke(slotnum: Int): TokenId {
			val positionSlotnum = (1..tokenIdRandomCharactersNum).random()
			
			val builder = StringBuilder()
			
			for (i in 0..tokenIdRandomCharactersNum) {
				var current : String
				if (i == positionSlotnum) {
					current = "$slotnum"
				} else {
					current = allowedChars.random().toString()
				}
				
				builder.append(current)
			}
			
			val tokenId = TokenId(builder.toString())
			
			tokenId.slotnum = slotnum
			
			return tokenId
		}
		
		operator fun invoke(token: String): TokenId {
			// TODO need to check if well-formed?
			
			val slotnum = token.filter { it.isDigit() }.toInt()
			
			val tokenId = TokenId(token)
			tokenId.slotnum = slotnum
			
			return tokenId
		}
	}
	
}