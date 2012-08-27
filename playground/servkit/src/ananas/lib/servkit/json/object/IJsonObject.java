package ananas.lib.servkit.json.object;

public interface IJsonObject extends IJsonValue {

	void put(String key, IJsonValue value);

	IJsonValue get(String key);

	void clear();

	Object allKeys();

}
