package ananas.lib.io;

import java.io.IOException;
import java.net.URI;

public interface Connector {

	Connection open(URI uri) throws IOException;

	Connection open(String uri) throws IOException;

	ConnectionFactoryRegistrar getConnectionFactoryRegistrar();

	class Factory {

		public static Connector getConnector() {
			return ConnectorBootstrap.getConnector(null);
		}

		public static Connector getConnector(String classname) {
			return ConnectorBootstrap.getConnector(classname);
		}

	}

}
