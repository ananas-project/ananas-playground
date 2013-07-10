package ananas.lib.blueprint3.loader.eom;

import ananas.lib.blueprint3.Blueprint;

public class EOM {

	public static void init() {
		Class<?> cls = EomReflectInfo.class;
		Blueprint.Util.getInstance().defaultEnvironment()
				.loadNamespace(cls, true);
	}

}
