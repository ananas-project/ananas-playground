package xukun.cd_stream_rx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CDStreamRx {

	private final String _url;

	public CDStreamRx(String url) {
		this._url = url;
	}

	public static void main(String[] arg) {
		String url = "http://localhost:8800/";
		CDStreamRx rx = new CDStreamRx(url);
		try {
			rx.run_io();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void run_io() throws MalformedURLException, IOException {

		HttpURLConnection conn = (HttpURLConnection) (new URL(this._url))
				.openConnection();
		int code = conn.getResponseCode();
		String msg = conn.getResponseMessage();
		InputStream in = conn.getInputStream();

		String name = this.getClass().getSimpleName();
		long time = System.currentTimeMillis();
		File file = new File("/tmp/" + name + "_" + time + ".pcm");

		file.getParentFile().mkdirs();
		System.out.println("from : " + conn.getURL());
		System.out.println("to   : " + file);
		OutputStream out = new FileOutputStream(file);

		this.__pump(in, out);
		out.close();
		in.close();
		System.out.println("the end");

	}

	private void __pump(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[1024];
		for (;;) {
			int cb = in.read(buf);
			if (cb < 0)
				break;
			out.write(buf, 0, cb);
		}
	}
}
