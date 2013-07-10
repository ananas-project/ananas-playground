package ananas.lib.axk.element;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.blueprint3.lang.BlueprintException;
import ananas.lib.blueprint3.util.nsloader.BPNamespaceInfo;
import ananas.lib.blueprint3.util.nsloader.BPNamespaceLoader;

public class TheLoader implements BPNamespaceLoader {

	@Override
	public void load(BPEnvironment environment, BPNamespaceInfo info)
			throws BlueprintException {

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream in = this.getClass().getResourceAsStream(
					"namespace.list.txt");
			for (int b = in.read(); b >= 0; b = in.read()) {
				if (b == 0x0a || b == 0x0d) {
					this.onLine(baos, environment);
					baos.reset();
				} else {
					baos.write(b);
				}
			}
			this.onLine(baos, environment);
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void onLine(ByteArrayOutputStream baos, BPEnvironment environment)
			throws UnsupportedEncodingException {

		byte[] ba = baos.toByteArray();
		String str = new String(ba, "UTF-8");
		str = str.trim();
		if (str.length() > 0) {
			environment.loadNamespace(str, true);
		}
	}
}
