package ananas.objecthub.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamPump {

	private final byte[] buff = new byte[1024];

	public StreamPump() {
	}

	public void doPump(InputStream in, OutputStream out) throws IOException {
		for (int cb = in.read(buff); cb > 0; cb = in.read(buff)) {
			out.write(buff, 0, cb);
		}
	}

}
