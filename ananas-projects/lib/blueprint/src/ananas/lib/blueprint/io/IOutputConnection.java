package ananas.lib.blueprint.io;

import java.io.IOException;
import java.io.OutputStream;

public interface IOutputConnection extends IConnection {

	OutputStream getOutputStream() throws IOException;

}
