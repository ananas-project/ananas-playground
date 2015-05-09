package ananas.location.monitor.webapp;

final class DefaultFileManager implements FileManager {

	private static DefaultFileManager _inst;

	public static FileManager getInstance() {
		DefaultFileManager inst = _inst;
		if (inst == null) {
			inst = new DefaultFileManager();
			_inst = inst;
		}
		return inst;
	}

}
