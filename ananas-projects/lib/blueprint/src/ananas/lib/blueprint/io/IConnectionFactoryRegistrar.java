package ananas.lib.blueprint.io;

import java.net.URI;

public interface IConnectionFactoryRegistrar {

	void registerFactory(URI uri, IConnectionFactory factory);

	IConnectionFactory getFactory(URI uri);

}
