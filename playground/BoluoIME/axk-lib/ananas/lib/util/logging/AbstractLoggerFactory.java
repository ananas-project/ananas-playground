package ananas.lib.util.logging;

public abstract class AbstractLoggerFactory {

	public Logger getLogger() {
		Logger logger = LogManager.getLogger(this.getClass().getName());
		logger.setLevel(Level.ALL);
		return logger;
	}

}
