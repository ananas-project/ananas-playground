package ananas.lib.axk.engine;

import java.io.IOException;

import org.xml.sax.SAXException;

public interface XEngine {

	void run(XContext context) throws IOException, SAXException;

	public static class Util {

		public static XEngineFactory getFactory() {
			String s = "ananas.lib.axk.connect.impl.TheDefaultXConnectionFactory";
			return getFactory(s);
		}

		public static XEngineFactory getFactory(String className) {
			try {
				Class<?> cls = Class.forName(className);
				return (XEngineFactory) cls.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

	}

}
