package ananas.lib.json;

public interface JSONObject {

	JSON getImplementation();

	void clear();

	void remove(String key);

	// put
	void put(String key, JSONObject obj);

	void put(String key, JSONArray array);

	void put(String key, String string);

	void putNull(String key);

	void putLong(String key, long n);

	void putDouble(String key, double n);

	void putInt(String key, int n);

	// get
}
