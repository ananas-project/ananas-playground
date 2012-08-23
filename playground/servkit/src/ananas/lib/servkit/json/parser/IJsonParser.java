package ananas.lib.servkit.json.parser;

import java.io.InputStream;

public interface IJsonParser {

	void parse(InputStream is, IJsonHandler h);

}
