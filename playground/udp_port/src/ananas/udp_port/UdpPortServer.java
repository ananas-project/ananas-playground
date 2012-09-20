package ananas.udp_port;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpPortServer implements Runnable {

	@Override
	public void run() {
		try {
			System.out.println("run(" + this + ")");
			this._run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void _run() throws IOException {
		byte[] buffer = new byte[1024];
		DatagramSocket sock = new DatagramSocket(10217);
		DatagramPacket pack = new DatagramPacket(buffer, buffer.length);
		for (;;) {
			sock.receive(pack);
			System.out.println(pack);
			sock.send(pack);
		}
	}

}
