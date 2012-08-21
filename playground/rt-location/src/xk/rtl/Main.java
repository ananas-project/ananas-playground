package xk.rtl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

	public static void main(String[] args) {

		MyConn conn = new MyConn();
		try {
			conn.run();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public static class MyConn {

		public void run() throws MalformedURLException, IOException,
				InterruptedException {

			String url = "http://localhost:8080/rt-location/PushPort";

			// get
			HttpURLConnection conn = (HttpURLConnection) (new URL(url))
					.openConnection();
			conn.setRequestMethod("GET");
			int code = conn.getResponseCode();
			System.out.println("GET " + url);
			System.out.println("http " + code);
			InputStream is = conn.getInputStream();
			OutputStream os = System.out;

			System.out.println("bytes:[");
			this.pump(is, os);
			System.out.println("]");

			is.close();
			conn.disconnect();

			// post
			conn = (HttpURLConnection) (new URL(url)).openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/plain");
			conn.setDoOutput(true);
			System.out.println("POST " + url);

			byte[] ba = { '1', '2', '3', '4' };
			is = new ByteArrayInputStream(ba);
			os = conn.getOutputStream();
			for (int i = 10; i > 0; i--) {
				this.pump(is, os);
				os.flush();
				System.out.println("timeout " + i);
				Thread.sleep(1000);
			}

			code = conn.getResponseCode();
			System.out.println("http " + code);

			os.close();
			conn.disconnect();

		}

		private void pump(InputStream is, OutputStream os) throws IOException {
			byte[] buf = new byte[123];
			for (int cb = is.read(buf); cb > 0; cb = is.read(buf)) {
				os.write(buf, 0, cb);
			}
			os.flush();
		}
	}

}
