package ananas.lib.io;

import java.io.IOException;
import java.io.OutputStream;

public interface OutputConnection extends Connection {

	OutputStream getOutputStream() throws IOException;

}
