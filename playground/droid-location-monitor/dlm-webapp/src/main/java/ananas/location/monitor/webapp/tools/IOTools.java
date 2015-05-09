package ananas.location.monitor.webapp.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOTools {

	public static void close(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void pump(InputStream in, OutputStream out, byte[] buffer)
			throws IOException {
		if (buffer == null) {
			buffer = new byte[256];
		}
		for (;;) {
			int cb = in.read(buffer);
			if (cb < 0) {
				break;
			}
			out.write(buffer, 0, cb);
		}
	}

}
