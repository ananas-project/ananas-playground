package ananas.lib.axk.engine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface XSocketContext {

	Socket getSocket() throws IOException;

	InputStream getInput() throws IOException;

	OutputStream getOutput() throws IOException;
}
