package ananas.lib.impl.axk.client.conn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface SocketKit {

	Socket getSocket();

	InputStream getInput() throws IOException;

	OutputStream getOutput() throws IOException;

}
