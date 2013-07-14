package ananas.lib.io;

import java.io.IOException;
import java.io.InputStream;

public interface InputConnection extends Connection {

	InputStream getInputStream() throws IOException;

}
