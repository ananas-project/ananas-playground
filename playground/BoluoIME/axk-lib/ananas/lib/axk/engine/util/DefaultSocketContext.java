package ananas.lib.axk.engine.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import ananas.lib.axk.engine.XSocketContext;

public class DefaultSocketContext implements XSocketContext {

	private Socket mSocket;
	private InputStream mInput;
	private OutputStream mOutput;

	public DefaultSocketContext(Socket socket) {
		this.mSocket = socket;
	}

	@Override
	public Socket getSocket() throws IOException {
		return this.mSocket;
	}

	@Override
	public InputStream getInput() throws IOException {
		InputStream in = this.mInput;
		if (in == null) {
			this.mInput = in = this.mSocket.getInputStream();
		}
		return in;
	}

	@Override
	public OutputStream getOutput() throws IOException {
		OutputStream out = this.mOutput;
		if (out == null) {
			this.mOutput = out = this.mSocket.getOutputStream();
		}
		return out;
	}

}
