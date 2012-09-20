package ananas.udp_port;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.URI;

public class UdpPortClient implements Runnable {

	@Override
	public void run() {
		try {
			this._run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void _run() throws IOException {
		DatagramSocket sock = new DatagramSocket(10217 + 2);
		Runnable rx = new MyRunnableRx(sock);
		Runnable tx = new MyRunnableTx(sock);
		(new Thread(rx)).start();
		tx.run();
	}

	class MyRunnableRx implements Runnable {

		private final DatagramSocket mSock;

		public MyRunnableRx(DatagramSocket sock) {
			this.mSock = sock;
		}

		@Override
		public void run() {
			try {
				this._run();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void _run() throws IOException {
			byte[] buffer = new byte[1024];
			DatagramPacket pack = new DatagramPacket(buffer, buffer.length);
			for (;;) {
				this.mSock.receive(pack);
				System.out.println(pack);
			}
		}
	}

	class MyRunnableTx implements Runnable {

		private final DatagramSocket mSock;
		private String mHost;
		private int mPort;

		public MyRunnableTx(DatagramSocket sock) {
			this.mSock = sock;
		}

		@Override
		public void run() {
			try {
				System.out.println("run(" + this + ")");
				this._run();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

		private void _run() throws IOException {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream is = System.in;
			for (int b = is.read();; b = is.read()) {
				switch (b) {
				case 0x0a:
				case 0x0d:
					byte[] ba = baos.toByteArray();
					String str = new String(ba, "utf-8");
					this._onLine(str);
					baos.reset();
					break;
				default:
					baos.write(b);
				}
			}
		}

		private void _onLine(String str) {
			int index = str.trim().indexOf(' ');
			final String p1, p2;
			if (index > 0) {
				p1 = str.substring(0, index);
				p2 = str.substring(index + 1);
			} else {
				p1 = p2 = "";
			}
			if (p1 == null) {
			} else if (p1.equalsIgnoreCase("send")) {
				this._sendData(p2);
				return;
			} else if (p1.equalsIgnoreCase("url")) {
				this._setURL(p2);
				return;
			} else {
			}
			System.out.println("bad command : " + str);
		}

		private void _setURL(String strURL) {
			try {
				URI url = URI.create(strURL);
				this.mHost = url.getHost();
				this.mPort = url.getPort();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void _sendData(String data) {
			try {
				byte[] buffer = new byte[1024];
				DatagramPacket pack = new DatagramPacket(buffer, buffer.length);

				InetSocketAddress addr = new InetSocketAddress(this.mHost,
						this.mPort);

				byte[] buf = data.getBytes();
				pack.setData(buf);

				pack.setSocketAddress(addr);

				this.mSock.send(pack);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
