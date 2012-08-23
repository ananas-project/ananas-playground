package xk.rtl.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class Main {

	public static void main(String[] arg) {

		DatagramSocket s;
		try {
			byte[] buf = new byte[256];
			DatagramPacket pkg = new DatagramPacket(buf, buf.length);

			s = new DatagramSocket(null);
			s.bind(new InetSocketAddress(8888));

			s.receive(pkg);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
