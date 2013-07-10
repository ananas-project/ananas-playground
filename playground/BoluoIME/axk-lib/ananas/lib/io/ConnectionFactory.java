package ananas.lib.io;

import java.io.IOException;
import java.net.URI;

public interface ConnectionFactory {

	Connection openConnection(URI uri) throws IOException;

}
