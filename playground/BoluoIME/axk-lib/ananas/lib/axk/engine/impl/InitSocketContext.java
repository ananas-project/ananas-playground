package ananas.lib.axk.engine.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.SocketFactory;

import ananas.lib.axk.engine.XAccount;
import ananas.lib.axk.engine.XContext;
import ananas.lib.axk.engine.XSocketContext;

public class InitSocketContext implements XSocketContext {

	private final XContext mContext;
	private MyCore mCore;

	public InitSocketContext(XContext xContext) {
		this.mContext = xContext;
	}

	private XSocketContext __getCore() throws IOException {
		MyCore core = this.mCore;
		if (core == null) {
			Socket socket;
			XAccount account = this.mContext.getAccount();
			if (account.isUseSSL()) {
				socket = this.mContext.getSSLSocketFactory().createSocket();
			} else {
				socket = SocketFactory.getDefault().createSocket();
			}
			String host = account.getHost();
			int port = account.getPort();
			InetSocketAddress addr = new InetSocketAddress(host, port);
			socket.connect(addr);
			core = new MyCore();
			core.in = socket.getInputStream();
			core.out = socket.getOutputStream();
			core.socket = socket;
			this.mCore = core;
		}
		return core;
	}

	@Override
	public Socket getSocket() throws IOException {
		XSocketContext core = this.__getCore();
		return core.getSocket();
	}

	@Override
	public InputStream getInput() throws IOException {
		XSocketContext core = this.__getCore();
		return core.getInput();
	}

	@Override
	public OutputStream getOutput() throws IOException {
		XSocketContext core = this.__getCore();
		return core.getOutput();
	}

	private class MyCore implements XSocketContext {

		private Socket socket;
		private InputStream in;
		private OutputStream out;

		@Override
		public Socket getSocket() {
			return this.socket;
		}

		@Override
		public InputStream getInput() {
			return this.in;
		}

		@Override
		public OutputStream getOutput() {
			return this.out;
		}
	}

}
