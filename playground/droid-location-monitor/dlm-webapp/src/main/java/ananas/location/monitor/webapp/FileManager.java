package ananas.location.monitor.webapp;

public interface FileManager {

	class Factory {

		public static FileManager getManager() {
			return DefaultFileManager.getInstance();
		}

	}

}
