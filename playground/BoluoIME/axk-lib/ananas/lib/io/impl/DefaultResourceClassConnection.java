package ananas.lib.io.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ananas.lib.io.Connection;
import ananas.lib.io.ConnectionFactory;
import ananas.lib.io.ResourceClassConnection;
import ananas.lib.io.ResourceConnection;
import ananas.lib.util.logging.AbstractLoggerFactory;
import ananas.lib.util.logging.Logger;

public class DefaultResourceClassConnection implements ResourceClassConnection {

	final static Logger log = (new AbstractLoggerFactory() {
	}).getLogger();

	private Class<?> mTargetClass;
	private final String mDefaultFile;
	private final String mClassName;
	private final URI mURI;

	public DefaultResourceClassConnection(URI uri) {

		this.mURI = uri;
		final String path = uri.getPath();
		List<String> list = new ArrayList<String>();
		int from = 0;
		for (; from >= 0;) {
			int to = path.indexOf('/', from);
			if (to < 0) {
				String str = path.substring(from);
				list.add(str);
				break;
			} else {
				String str = path.substring(from, to);
				list.add(str);
				from = to + 1;
			}
		}
		this.mClassName = list.get(1);
		this.mDefaultFile = list.get(list.size() - 1);
	}

	public static ConnectionFactory getFactory() {
		return new ConnectionFactory() {
			@Override
			public Connection openConnection(URI uri) throws IOException {
				return new DefaultResourceClassConnection(uri);
			}
		};
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public Class<?> getTargetClass() throws ClassNotFoundException {
		Class<?> cls = this.mTargetClass;
		if (cls == null) {
			cls = Class.forName(this.mClassName);
			this.mTargetClass = cls;
		}
		return cls;
	}

	@Override
	public ResourceConnection getResource(String shortFileName)
			throws IOException {
		if (shortFileName == null) {
			shortFileName = this.mDefaultFile;
		}
		URL in;
		try {
			in = this.getTargetClass().getResource(shortFileName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		if (in == null) {
			log.info("process uri " + this.mURI + " to:");
			log.error("no file:" + shortFileName);
		} else {
		}
		return new MyResConn(in.openStream());
	}

	class MyResConn implements ResourceConnection {

		private final InputStream mIn;

		public MyResConn(InputStream in) {
			this.mIn = in;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return this.mIn;
		}

		@Override
		public void close() throws IOException {
			this.mIn.close();
		}
	}
}
