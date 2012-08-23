package xk.rtl.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public interface NioServer extends Runnable {

	public interface Session {

		void close();

		void onReceive(ByteBuffer buffer);

		SocketChannel getSocketChannel();
	}

	public interface SessionFactory {

		Session newSession(SocketChannel ep);
	}

	public static class Factory {

		public static NioServer newNioServer() {
			return new MyImpl();
		}

		static class MySession implements Session {

			private final SocketChannel mEndpoint;

			public MySession(SocketChannel ep) {
				this.mEndpoint = ep;
			}

			@Override
			public void close() {
				try {
					this.mEndpoint.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onReceive(ByteBuffer buffer) {
				// TODO Auto-generated method stub

				byte[] array = buffer.array();
				int offset = buffer.arrayOffset();
				int limit = buffer.limit();

				System.out.println("array:" + array + " offset:" + offset
						+ " limit:" + limit);
			}

			@Override
			public SocketChannel getSocketChannel() {
				return this.mEndpoint;
			}
		}

		static class MySessionFactory implements SessionFactory {

			@Override
			public Session newSession(SocketChannel ep) {
				System.out.println("new session : " + ep);
				return new MySession(ep);
			}
		}

		static class MyImpl implements NioServer {

			private final SessionFactory mSessionFactory = new MySessionFactory();

			@Override
			public void run() {
				try {
					this._run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			private void _run() throws IOException {

				final int port = 888;
				final ByteBuffer buffer = ByteBuffer.allocate(22);
				final Selector selector = Selector.open();
				ServerSocketChannel ssc = ServerSocketChannel.open();
				InetSocketAddress address = new InetSocketAddress(port);
				System.out.println("bind to " + address);
				ssc.socket().bind(address);
				ssc.configureBlocking(false);
				SelectionKey key0 = ssc.register(selector,
						SelectionKey.OP_ACCEPT);
				this.printKeyInfo(key0);

				while (true) {

					int cntAccept = 0;
					int cntRead = 0;

					if (selector.select(500) <= 0) {
						continue;
					}
					final Iterator<SelectionKey> iter = selector.selectedKeys()
							.iterator();
					for (; iter.hasNext();) {
						SelectionKey key = iter.next();
						if (key.isAcceptable()) {
							ServerSocketChannel server = (ServerSocketChannel) key
									.channel();
							SocketChannel sc = server.accept();
							if (sc == null) {
								continue;
							}
							sc.configureBlocking(false);
							SelectionKey newKey = sc.register(selector,
									SelectionKey.OP_READ);
							Session session = this.mSessionFactory
									.newSession(sc);
							newKey.attach(session);
							cntAccept++;
						}
						if (key.isReadable()) {
							Session session = (Session) key.attachment();
							SocketChannel sc = (SocketChannel) key.channel();
							for (int cb = sc.read(buffer); cb > 0; cb = sc
									.read(buffer)) {
								cntRead += cb;
								session.onReceive(buffer);
								buffer.clear();
							}
							// buffer.flip();
							buffer.clear();
						}
						iter.remove();
					}
					if (cntAccept == 0 && cntRead == 0) {
						this._safeSleep(200);
					} else {
						System.out.println("accept:" + cntAccept + ", read:"
								+ cntRead);
					}
				}
			}

			private void _safeSleep(int ms) {
				try {
					Thread.sleep(ms);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			private void printKeyInfo(SelectionKey s) {
				// System.out.println("key:" + s);
			}
		}
	}
}
