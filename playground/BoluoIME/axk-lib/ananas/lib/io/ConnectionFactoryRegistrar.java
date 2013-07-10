package ananas.lib.io;

import java.net.URI;
import java.util.List;

public interface ConnectionFactoryRegistrar {

	void registerFactory(URI uri, ConnectionFactory factory);

	ConnectionFactory getFactory(URI uri);

	void printItems();

	List<URI> listItems();

}
