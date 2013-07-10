package ananas.lib.util.logging;

public class LogManager {

	public static Logger getLogger(String name) {

		return new MyLogger(name);

	}

}
