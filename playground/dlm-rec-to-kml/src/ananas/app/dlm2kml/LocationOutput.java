package ananas.app.dlm2kml;

import java.io.IOException;

public interface LocationOutput {

	void write(Location location) throws IOException;

	void close() throws IOException;
}
