package ananas.lib.servkit.json.object;

import ananas.lib.servkit.json.JsonException;
import ananas.lib.servkit.json.io.IJsonHandler;
import ananas.lib.servkit.pool.IPoolable;

public interface IJsonValue extends IPoolable {

	void output(IJsonHandler h) throws JsonException;

}
