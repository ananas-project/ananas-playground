package ananas.lib.io;

import ananas.lib.io.impl.DefaultConnector;

public class ConnectorBootstrap {

	private static Connector s_default;

	public static Connector getConnector(String classname) {

		if (classname == null) {
			// get default
			Connector conn = s_default;
			if (conn == null) {
				s_default = conn = DefaultConnector.getDefault();
			}
			return conn;
		}

		try {
			Class<?> cls = Class.forName(classname);
			Connector conn = (Connector) cls.newInstance();
			return conn;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
