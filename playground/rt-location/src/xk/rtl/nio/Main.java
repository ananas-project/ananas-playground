package xk.rtl.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Main {

	public static void main(String[] arg) {

		NioServer server = NioServer.Factory.newNioServer();
		server.run();

	}

	private Selector selector = null;
	private static final int BUF_LENGTH = 32;

	public void start() throws IOException {

		int port = 888;

		if (selector == null) {
			selector = Selector.open();
		}
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ServerSocket serverSocket = ssc.socket();
		InetSocketAddress address = new InetSocketAddress(port);
		System.out.println("bind to " + address);
		serverSocket.bind(address);
		ssc.configureBlocking(false);
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		try {
			while (true) {
				int nKeys = Main.this.selector.select();
				if (nKeys > 0) {
					Iterator<SelectionKey> it = selector.selectedKeys().iterator();
					while (it.hasNext()) {
						SelectionKey key = (SelectionKey) it.next();
						if (key.isAcceptable()) {
							ServerSocketChannel server = (ServerSocketChannel) key
									.channel();
							SocketChannel channel = server.accept();
							if (channel == null) {
								continue;
							}
							channel.configureBlocking(false);
							channel.register(selector, SelectionKey.OP_READ);
						}
						if (key.isReadable()) {
							readDataFromSocket(key);
						}
						it.remove();
					}
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * @param key
	 * @throws IOException
	 */
	private void readDataFromSocket(SelectionKey key) throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(BUF_LENGTH);
		SocketChannel sc = (SocketChannel) key.channel();
		int readBytes = 0;
		int ret;
		try {

			buf.put("HTTP/1.1 200 OK\n\r".getBytes());

			while ((ret = sc.read(buf)) > 0) {
				readBytes += ret;

				System.out.println("readBytes = " + readBytes);

			}
		} finally {
			buf.flip();
		}

		sc.write(buf);

		// process buffer
		// buf.clear();
	}
}
