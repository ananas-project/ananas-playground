package ananas.lib.io.impl;

import java.io.IOException;
import java.net.URI;

import ananas.lib.io.Connection;
import ananas.lib.io.ConnectionFactoryRegistrar;
import ananas.lib.io.Connector;

public class DefaultConnector implements Connector {

	private static Connector sInst;

	public static Connector getDefault() {
		Connector inst = sInst;
		if (inst == null) {
			sInst = inst = new DefaultConnector();
		}
		return inst;
	}

	private final DefaultConnectorImpl mImpl;

	public DefaultConnector() {
		this.mImpl = new DefaultConnectorImpl();
	}

	@Override
	public Connection open(String uri) throws IOException {
		return this.mImpl.open(uri);
	}

	@Override
	public ConnectionFactoryRegistrar getConnectionFactoryRegistrar() {
		return this.mImpl.getConnectionFactoryRegistrar();
	}

	@Override
	public Connection open(URI uri) throws IOException {
		return this.mImpl.open(uri);
	}

}
