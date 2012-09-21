package ananas.udp_port;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;

public class DatagramPacketLogger {

	public static void log(String string, DatagramPacket pack) {

		byte[] buf = pack.getData();
		int off = pack.getOffset();
		int len = pack.getLength();
		String data = null;
		try {
			data = new String(buf, off, len, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(string + " addr:" + pack.getSocketAddress()
				+ " length:" + len + " data:'" + data + "'");
	}

}
