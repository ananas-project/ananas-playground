package ananas.lib.impl.axk.client.conn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class DefaultSocketKit implements SocketKit {

	private final Socket mSocket;
	private InputStream mIS;
	private OutputStream mOS;

	public DefaultSocketKit(Socket sock) {
		this.mSocket = sock;
	}

	public DefaultSocketKit(Socket sock, InputStream in, OutputStream out) {
		this.mSocket = sock;
		this.mIS = in;
		this.mOS = out;
	}

	@Override
	public Socket getSocket() {
		return this.mSocket;
	}

	@Override
	public InputStream getInput() throws IOException {
		InputStream in = this.mIS;
		if (in == null) {
			this.mIS = in = this.mSocket.getInputStream();
		}
		return in;
	}

	@Override
	public OutputStream getOutput() throws IOException {
		OutputStream out = this.mOS;
		if (out == null) {
			this.mOS = out = this.mSocket.getOutputStream();
		}
		return out;
	}

}
