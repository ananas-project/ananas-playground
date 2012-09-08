package ananas.app.zlibfileviewer.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.zip.InflaterInputStream;

public class ConverToBin {

	public void binaryToXml(String fileIn) {
		this._binaryToXml(new File(fileIn));
	}

	public void binaryToXml(File fileIn) {
		this._binaryToXml(fileIn);
	}

	private void _binaryToXml(File fileIn) {

		byte[] output = null;
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(fileIn);
			output = this.decompress(fis);

			String sha1 = this._calcSHA1(output);
			System.out.println("sha-1 = " + sha1);

			System.out.print("begin[");
			System.out.write(output, 0, output.length);
			System.out.flush();
			System.out.println("]end.");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// return str;
	}

	private String _calcSHA1(byte[] input) {
		try {
			MessageDigest md = java.security.MessageDigest.getInstance("SHA-1");
			byte[] output = md.digest(input);
			StringBuilder sb = new StringBuilder();
			char[] chs = "0123456789abcdef".toCharArray();
			for (byte b : output) {
				sb.append(chs[0x0f & (b >> 4)]);
				sb.append(chs[0x0f & b]);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public byte[] decompress(InputStream is) {
		InflaterInputStream in = new InflaterInputStream(is);
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		try {
			int i = 1024;
			byte[] buf = new byte[i];
			while ((i = in.read(buf, 0, i)) > 0) {
				out.write(buf, 0, i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

}
