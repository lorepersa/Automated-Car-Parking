package itunibo.automatedcarparking.utilities

import com.google.gson.Gson

object JsonStatus {
	val gson = Gson()
	
	inline fun <reified T:Any> fromJsonString(jsonString : String) : T {
		return gson.fromJson(jsonString, T::class.java)
	}
	
	inline fun <reified T:Any> getJsonString(status : T) : String {
		return gson.toJson(status)
	}
}