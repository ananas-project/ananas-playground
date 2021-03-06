package ananas.lib.servkit.json.object;

public interface IJsonArray extends IJsonValue {

	void add(IJsonValue child);

	int count();

	IJsonValue get(int index);

	void clear();
}
