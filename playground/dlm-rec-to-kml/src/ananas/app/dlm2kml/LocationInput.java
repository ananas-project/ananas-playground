package ananas.app.dlm2kml;

import java.io.IOException;

public interface LocationInput {

	Location read() throws IOException;

	void close() throws IOException;
}
