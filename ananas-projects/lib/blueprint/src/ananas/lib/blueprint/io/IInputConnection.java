package ananas.lib.blueprint.io;

import java.io.IOException;
import java.io.InputStream;

public interface IInputConnection extends IConnection {

	InputStream getInputStream() throws IOException;

}
