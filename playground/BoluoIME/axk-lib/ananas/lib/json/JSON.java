package ananas.lib.json;

import java.io.InputStream;
import java.io.OutputStream;

public interface JSON {

	class Factory {

		public static JSON getImplementation() {
			return JSONImplementationLoader.getImplementation(null);
		}

		public static JSON getImplementation(String classname) {
			return JSONImplementationLoader.getImplementation(classname);
		}

	}

	// creator
	JSONArray createArray();

	JSONObject createObject();

	// serialized
	String toString(JSONObject obj);

	byte[] toBytes(JSONObject obj);

	void toStream(JSONObject obj, OutputStream out);

	// parser
	JSONObject parseObject(byte[] buff, int offset, int length);

	JSONObject parseObject(String string);

	JSONObject parseObject(InputStream in);

}
