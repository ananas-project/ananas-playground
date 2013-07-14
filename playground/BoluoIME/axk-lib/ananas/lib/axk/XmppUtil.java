package ananas.lib.axk;

public abstract class XmppUtil implements XmppClientFactory {

	public static XmppEnvironment getDefaultEnvironment() {
		String cls = "ananas.lib.impl.axk.TheXmppEnvironmentLoader";
		return loadEnvironment(cls);
	}

	public static XmppEnvironment loadEnvironment(String className) {
		try {
			Class<?> cls = Class.forName(className);
			XmppEnvironmentLoader ldr = (XmppEnvironmentLoader) cls
					.newInstance();
			return ldr.load();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static XmppEnvironment loadEnvironment(XmppEnvironmentLoader loader) {
		return loader.load();
	}

}
