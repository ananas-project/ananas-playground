package ananas.remote_jar_runner;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

public class TaskUtil {

	public static byte[] downloadFile(String url) {
		return downloadFile(url, 5);
	}

	public static byte[] downloadFile(String url, int retry) {
		try {
			if (retry <= 0) {
				throw new RuntimeException("retry too more.");
			}
			URL aURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) aURL.openConnection();
			int code = conn.getResponseCode();
			if (code != 200) {
				conn.disconnect();
				switch (code) {
				case HttpURLConnection.HTTP_MOVED_PERM:
				case HttpURLConnection.HTTP_MOVED_TEMP:
					// redir
					String loc = conn.getHeaderField("Location");
					return downloadFile(loc, retry - 1);
				default:
					System.out.println(url + " response HTTP " + code);
					return null;
				}
			}
			int limit = 1024 * 1024 * 10;
			if (conn.getContentLength() > limit) {
				System.out.println("the file is too large (more than " + limit
						+ " mb) : " + url);
				return null;
			}
			InputStream in = conn.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buff = new byte[1024];
			for (int cb = in.read(buff); cb > 0; cb = in.read(buff)) {
				baos.write(buff, 0, cb);
			}
			in.close();
			conn.disconnect();
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String calcSha1(byte[] data) {
		if (data != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				byte[] result = md.digest(data);
				char[] chs = "0123456789abcdef".toCharArray();
				StringBuilder sb = new StringBuilder();
				for (byte b : result) {
					int n1, n0;
					n0 = ((b & 0x00ff) >> 4) & 0x0f;
					n1 = ((b & 0x00ff) >> 0) & 0x0f;
					sb.append(chs[n0] + "" + chs[n1]);
				}
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

}
