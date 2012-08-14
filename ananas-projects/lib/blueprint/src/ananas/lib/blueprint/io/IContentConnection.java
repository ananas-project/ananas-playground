package ananas.lib.blueprint.io;

public interface IContentConnection extends IStreamConnection {

	String getType();

	long getLength();

	String getEncoding();

}
