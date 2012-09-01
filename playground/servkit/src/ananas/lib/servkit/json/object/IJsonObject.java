package ananas.lib.servkit.json.object;

import java.util.Set;

public interface IJsonObject extends IJsonValue {

	void put(String key, IJsonValue value);

	IJsonValue get(String key);

	void clear();

	Set<String> allKeys();

}
