package xk.rtl.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public interface NioServer extends Runnable {

	public static class Factory {

		public static NioServer newNioServer() {
			return new MyImpl();
		}

		static class MyImpl implements NioServer {

			@Override
			public void run() {
				try {
					this._run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			private void _run() throws IOException {

				int channels = 0;
				int nKeys = 0;
				int currentSelector = 0;
				int port = 888;

				// create Selector
				Selector selector = Selector.open();

				// create Channel & bind
				ServerSocketChannel ssc = ServerSocketChannel.open();
				InetSocketAddress address = new InetSocketAddress(
						InetAddress.getLocalHost(), port);
				ssc.socket().bind(address);

				// set non-blocking
				ssc.configureBlocking(false);

				// register Channel-event to Selector
				SelectionKey s = ssc.register(selector, SelectionKey.OP_ACCEPT);
				this.printKeyInfo(s);

			}

			private void printKeyInfo(SelectionKey s) {
				// TODO Auto-generated method stub

			}
		}
	}
}
