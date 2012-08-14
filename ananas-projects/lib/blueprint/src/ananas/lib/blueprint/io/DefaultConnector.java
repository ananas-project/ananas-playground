package ananas.lib.blueprint.io;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

public class DefaultConnector implements IConnector {

	private final IConnectionFactoryRegistrar mReg;

	public DefaultConnector() {
		this.mReg = new MyConnFactoryRegistrar();
		this._initDefaultFactories();
	}

	private void _initDefaultFactories() {

		this.getConnectionFactoryRegistrar().registerFactory(
				URI.create("resource:///"),
				DefaultResourceConnection.getFactory());

	}

	@Override
	public IConnection open(String uri) throws IOException {
		URI aURI = URI.create(uri);
		IConnectionFactory factory = this.mReg.getFactory(aURI);
		if (factory == null) {
			throw new IOException("no factory for uri : " + uri);
		}
		return factory.openConnection(aURI);
	}

	@Override
	public IConnectionFactoryRegistrar getConnectionFactoryRegistrar() {
		return this.mReg;
	}

	private class MyConnFactoryRegistrar implements IConnectionFactoryRegistrar {

		private final HashMap<String, IConnectionFactory> mFactoryTable;

		public MyConnFactoryRegistrar() {
			this.mFactoryTable = new HashMap<String, IConnectionFactory>();
		}

		@Override
		public void registerFactory(URI uri, IConnectionFactory factory) {
			String key = this._calcKeyString(uri, SCHEME | HOST | PORT | PATH);
			this.mFactoryTable.put(key, factory);
		}

		@Override
		public IConnectionFactory getFactory(URI uri) {
			String key;
			IConnectionFactory factory;
			// mode-4
			key = this._calcKeyString(uri, SCHEME | HOST | PORT | PATH);
			factory = this.mFactoryTable.get(key);
			if (factory != null)
				return factory;
			// mode-3
			key = this._calcKeyString(uri, SCHEME | HOST | PORT);
			factory = this.mFactoryTable.get(key);
			if (factory != null)
				return factory;
			// mode-2
			key = this._calcKeyString(uri, SCHEME | HOST);
			factory = this.mFactoryTable.get(key);
			if (factory != null)
				return factory;
			// mode-1
			key = this._calcKeyString(uri, SCHEME);
			factory = this.mFactoryTable.get(key);
			if (factory != null)
				return factory;
			// mode-0
			key = this._calcKeyString(uri, 0);
			factory = this.mFactoryTable.get(key);
			return factory;
		}

		public final static int SCHEME = 0x08;
		public final static int HOST = 0x04;
		public final static int PORT = 0x02;
		public final static int PATH = 0x01;

		/**
		 * @param mode
		 *            = (SCHEME|HOST|PORT|PATH)
		 * */
		private String _calcKeyString(URI uri, int mode) {
			String scheme = uri.getScheme();
			String host = uri.getHost();
			int portInt = uri.getPort();
			String port = (portInt < 0) ? null : ("" + portInt);
			String path = uri.getPath();

			if (host != null) {
				if (host.length() < 1)
					host = null;
			}
			if (path != null) {
				if (path.length() <= 1)
					path = null;
			}

			if ((mode & SCHEME) == 0)
				scheme = null;
			if ((mode & HOST) == 0)
				host = null;
			if ((mode & PORT) == 0)
				port = null;
			if ((mode & PATH) == 0)
				path = null;
			return ("[" + scheme + host + port + path + "]");
		}
	};

}
