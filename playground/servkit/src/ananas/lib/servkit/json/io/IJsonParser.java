package ananas.lib.servkit.json.io;

import java.io.InputStream;


public interface IJsonParser {

	void parse(InputStream is, IJsonHandler h);

}
