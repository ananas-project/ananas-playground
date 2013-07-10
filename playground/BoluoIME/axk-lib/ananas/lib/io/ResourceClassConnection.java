package ananas.lib.io;

import java.io.IOException;

public interface ResourceClassConnection extends Connection {

	Class<?> getTargetClass() throws ClassNotFoundException;

	ResourceConnection getResource(String shortFileName) throws IOException;
}
