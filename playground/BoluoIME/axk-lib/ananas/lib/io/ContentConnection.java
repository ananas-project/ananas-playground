package ananas.lib.io;

public interface ContentConnection extends StreamConnection {

	String getType();

	long getLength();

	String getEncoding();

}
