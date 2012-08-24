package ananas.lib.servkit.json.object;

import ananas.lib.servkit.json.JsonException;
import ananas.lib.servkit.json.io.IJsonHandler;

public interface IJsonValue {

	void output(IJsonHandler h) throws JsonException;

}
