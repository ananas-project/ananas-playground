package ananas.lib.io.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import ananas.lib.io.Connection;
import ananas.lib.io.ConnectionFactory;
import ananas.lib.io.ResourceConnection;

public class DefaultResourceConnection implements ResourceConnection {

	private final InputStream mIS;

	private DefaultResourceConnection(String path) {
		this.mIS = "".getClass().getResourceAsStream(path);
	}

	public static class Factory implements ConnectionFactory {

		@Override
		public Connection openConnection(URI uri) throws IOException {
			String path = uri.getPath();
			DefaultResourceConnection conn = new DefaultResourceConnection(path);
			InputStream is = conn.getInputStream();
			if (is == null) {
				System.err.println("cannot find resource by uri : " + uri);
			}
			return conn;
		}
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public InputStream getInputStream() throws IOException {
		return this.mIS;
	}

	public static ConnectionFactory getFactory() {
		return new Factory();
	}

}
