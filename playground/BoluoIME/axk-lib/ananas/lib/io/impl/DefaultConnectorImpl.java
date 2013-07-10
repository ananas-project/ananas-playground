package ananas.lib.io.impl;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import ananas.lib.io.Connection;
import ananas.lib.io.ConnectionFactory;
import ananas.lib.io.ConnectionFactoryRegistrar;
import ananas.lib.io.Connector;

class DefaultConnectorImpl implements Connector {

	private final ConnectionFactoryRegistrar mReg;

	public DefaultConnectorImpl() {
		this.mReg = new MyConnFactoryRegistrar();
		this._initDefaultFactories();
	}

	private void _initDefaultFactories() {

		this.getConnectionFactoryRegistrar().registerFactory(
				URI.create("resource:///"),
				DefaultResourceConnection.getFactory());

		this.getConnectionFactoryRegistrar().registerFactory(
				URI.create("class:///"),
				DefaultResourceClassConnection.getFactory());

		this.getConnectionFactoryRegistrar().registerFactory(
				URI.create("file:///"), DefaultFileConnection.getFactory());

	}

	@Override
	public ConnectionFactoryRegistrar getConnectionFactoryRegistrar() {
		return this.mReg;
	}

	static class FactoryTableItem {

		private final ConnectionFactory mFactory;
		private final URI mURI;

		public FactoryTableItem(URI uri, ConnectionFactory factory) {
			this.mURI = uri;
			this.mFactory = factory;
		}

		public ConnectionFactory getFactory() {
			return this.mFactory;
		}

		public URI getURI() {
			return this.mURI;
		}
	}

	private class MyConnFactoryRegistrar implements ConnectionFactoryRegistrar {

		private final HashMap<String, FactoryTableItem> mFactoryTable;

		public MyConnFactoryRegistrar() {
			this.mFactoryTable = new HashMap<String, FactoryTableItem>();
		}

		@Override
		public void registerFactory(URI uri, ConnectionFactory factory) {
			String key = this._calcKeyString(uri, SCHEME | HOST | PORT | PATH);
			FactoryTableItem item = new FactoryTableItem(uri, factory);
			this.mFactoryTable.put(key, item);
		}

		@Override
		public ConnectionFactory getFactory(URI uri) {
			FactoryTableItem item = this._getItem(uri);
			if (item == null) {
				return null;
			}
			return item.getFactory();
		}

		private FactoryTableItem _getItem(URI uri) {
			String key;
			FactoryTableItem factory;
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

		@Override
		public void printItems() {
			System.out.println(this + ".items:");
			List<URI> items = this.listItems();
			for (URI item : items) {
				System.out.println("    " + item);
			}
		}

		@Override
		public List<URI> listItems() {
			List<URI> list = new ArrayList<URI>();
			Collection<FactoryTableItem> items = this.mFactoryTable.values();
			for (FactoryTableItem item : items) {
				list.add(item.getURI());
			}
			return list;
		}
	}

	@Override
	public Connection open(String uri) throws IOException {
		return this.open(URI.create(uri));
	}

	@Override
	public Connection open(URI uri) throws IOException {
		ConnectionFactory factory = this.mReg.getFactory(uri);
		if (factory == null) {
			throw new IOException("no factory for uri : " + uri);
		}
		return factory.openConnection(uri);
	}

}
