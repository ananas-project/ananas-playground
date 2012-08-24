package ananas.lib.servkit.json;

import ananas.lib.servkit.json.object.JsonArray;
import ananas.lib.servkit.json.object.JsonDouble;
import ananas.lib.servkit.json.object.JsonInteger;
import ananas.lib.servkit.json.object.JsonLong;
import ananas.lib.servkit.json.object.JsonObject;
import ananas.lib.servkit.json.object.JsonString;
import ananas.lib.servkit.json.object.JsonValue;

public class JSON {

	public final static Class<?> class_array = JsonArray.class;
	public final static Class<?> class_object = JsonObject.class;
	public final static Class<?> class_string = JsonString.class;
	// public final static Class<?> class_number = JsonNumber.class;
	public final static Class<?> class_int = JsonInteger.class;
	public final static Class<?> class_long = JsonLong.class;
	public final static Class<?> class_double = JsonDouble.class;

	public final static JsonValue value_true = JsonValue.value_true;
	public final static JsonValue value_false = JsonValue.value_false;
	public final static JsonValue value_null = JsonValue.value_null;

}
