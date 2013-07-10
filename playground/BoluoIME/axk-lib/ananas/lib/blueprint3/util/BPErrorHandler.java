package ananas.lib.blueprint3.util;

public interface BPErrorHandler {

	void error(Exception e) throws Exception;

	void fatalError(Exception e) throws Exception;

	void warning(Exception e) throws Exception;

}
