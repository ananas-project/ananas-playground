package ananas.lib.servkit.json.io;

import java.io.OutputStream;

import ananas.lib.servkit.json.JsonException;
import ananas.lib.servkit.json.object.IJsonValue;

public interface IJsonSerializer {

	void serialize(OutputStream os, IJsonValue json) throws JsonException;

}
