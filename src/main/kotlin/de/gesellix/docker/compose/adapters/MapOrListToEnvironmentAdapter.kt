package de.gesellix.docker.compose.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson
import de.gesellix.docker.compose.types.Environment

class MapOrListToEnvironmentAdapter {

    @ToJson
    fun toJson(@EnvironmentType environment: Environment): Map<String, String> {
        throw UnsupportedOperationException()
    }

    @FromJson
    @EnvironmentType
    fun fromJson(reader: JsonReader): Environment {
        val environment = Environment()
        val token = reader.peek()
        if (token == JsonReader.Token.BEGIN_ARRAY) {
            reader.beginArray()
            while (reader.peek() != JsonReader.Token.END_ARRAY) {
                val entry = reader.nextString()
                val keyAndValue = entry.split(Regex("="), 2)
                environment.entries[keyAndValue[0]] = if (keyAndValue.size > 1) keyAndValue[1] else ""
            }
            reader.endArray()
        } else if (token == JsonReader.Token.BEGIN_OBJECT) {
            reader.beginObject()
            while (reader.peek() != JsonReader.Token.END_OBJECT) {
                val name = reader.nextName()
                val value: String? = if (reader.peek() == JsonReader.Token.NULL) {
                    reader.nextNull()
                } else if (reader.peek() == JsonReader.Token.NUMBER) {
                    val d = reader.nextDouble()
                    if ((d % 1) == 0.0) {
                        d.toInt().toString()
                    } else {
                        d.toString()
                    }
                } else {
                    reader.nextString()
                }
                environment.entries[name] = value ?: ""
            }
            reader.endObject()
        } else {
            // ...
        }
        return environment
    }
}
