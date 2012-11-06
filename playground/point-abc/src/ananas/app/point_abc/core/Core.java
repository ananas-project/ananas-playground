package ananas.app.point_abc.core;

public class Core implements ICore {

	private static Core s_inst;

	public static Core getDefault() {
		if (s_inst == null) {
			s_inst = new Core();
		}
		return s_inst;
	}

	private final IPointABCClient mClient;

	private Core() {
		this.mClient = new PointABCClient();
	}

	public void save() {
	}

	public void load() {
	}

	@Override
	public IPointABCClient getClient() {
		return this.mClient;
	}

}
