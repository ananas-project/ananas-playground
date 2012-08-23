package test.servkit;

import java.io.IOException;
import java.io.InputStream;

import ananas.lib.servkit.json.parser.DefaultJsonHandler;
import ananas.lib.servkit.json.parser.DefaultJsonParserFactory;
import ananas.lib.servkit.json.parser.IJsonHandler;
import ananas.lib.servkit.json.parser.IJsonParser;

public class TestJsonParser {

	public void run() {

		try {

			IJsonParser parser = (new DefaultJsonParserFactory()).newParser();
			IJsonHandler h = new DefaultJsonHandler();

			for (int i = 3; i > 0; i--) {
				System.out.println("loop " + i + ":");
				this._run(parser, h);
				System.out.println();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void _run(IJsonParser parser, IJsonHandler h) throws IOException {

		String path = "/res/json-sample.js";
		InputStream is = "".getClass().getResourceAsStream(path);
		is = this._skipJavascriptHeader(is);

		parser.parse(is, h);

	}

	private InputStream _skipJavascriptHeader(InputStream is)
			throws IOException {
		for (int b = is.read(); b > 0; b = is.read()) {
			if (b == '=')
				return is;
		}
		return null;
	}

}
