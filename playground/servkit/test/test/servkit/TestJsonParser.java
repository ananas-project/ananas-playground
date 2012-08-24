package test.servkit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ananas.lib.servkit.json.io.DefaultJsonObjectBuilder;
import ananas.lib.servkit.json.io.DefaultJsonParserFactory;
import ananas.lib.servkit.json.io.IJsonHandler;
import ananas.lib.servkit.json.io.IJsonParser;
import ananas.lib.servkit.json.object.JsonValue;

public class TestJsonParser {

	public void run() {

		try {

			MyPoolSet pool = new MyPoolSet();

			IJsonParser parser = (new DefaultJsonParserFactory()).newParser();
			DefaultJsonObjectBuilder h = new DefaultJsonObjectBuilder(pool);

			final int loops = 1000 * 50;
			final long ts0 = System.currentTimeMillis();

			byte[] ba = this._prepareData();

			for (int i = loops; i > 0; i--) {
				// System.out.println("loop " + i + ":");
				ByteArrayInputStream bais = new ByteArrayInputStream(ba);
				this._run(parser, bais, h);
				// System.out.println();

				JsonValue root = (JsonValue) h.getRoot();
				root.free();
			}
			final long ts1 = System.currentTimeMillis();
			final double sec = (ts1 - ts0) / 1000.0;
			System.out.println("cast " + sec + " sec");
			System.out.println("do " + loops + " loops");
			System.out.println("speed " + (loops / sec) + " loops/sec");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private byte[] _prepareData() throws IOException {
		String path = "/res/json-sample.js";
		InputStream is = "".getClass().getResourceAsStream(path);
		is = this._skipJavascriptHeader(is);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[128];
		for (int cb = is.read(buf); cb > 0; cb = is.read(buf)) {
			baos.write(buf, 0, cb);
		}
		return baos.toByteArray();
	}

	private void _run(IJsonParser parser, InputStream is, IJsonHandler h)
			throws IOException {
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
