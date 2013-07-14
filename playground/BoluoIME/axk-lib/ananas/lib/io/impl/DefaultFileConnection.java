package ananas.lib.io.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import ananas.lib.io.Connection;
import ananas.lib.io.ConnectionFactory;
import ananas.lib.io.ContentConnection;

public class DefaultFileConnection implements ContentConnection {

	// private final URI mURI;
	private InputStream mIn;
	private OutputStream mOut;
	private final File mFile;

	public DefaultFileConnection(URI uri) {
		// this.mURI = uri;
		this.mFile = new File(uri.getPath());
	}

	public static ConnectionFactory getFactory() {
		return new ConnectionFactory() {

			@Override
			public Connection openConnection(URI uri) throws IOException {
				DefaultFileConnection conn = new DefaultFileConnection(uri);
				return conn;
			}
		};
	}

	@Override
	public void close() throws IOException {
		InputStream in = this.mIn;
		if (in != null) {
			in.close();
		}
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		OutputStream out = this.mOut;
		if (out == null) {
			out = new FileOutputStream(this.mFile);
			this.mOut = out;
		}
		return out;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		InputStream in = this.mIn;
		if (in == null) {
			in = new FileInputStream(this.mFile);
			this.mIn = in;
		}
		return in;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getLength() {
		return this.mFile.length();
	}

	@Override
	public String getEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

}
