package ananas.lib.blueprint3;

import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import ananas.lib.io.Connector;
import ananas.lib.io.ResourceClassConnection;
import ananas.lib.io.ResourceConnection;
import ananas.lib.util.ClassUriGen;

class Private_BlueprintLoader {

	private static Blueprint s_inst;

	public static Blueprint loadInstance() {
		Blueprint inst = s_inst;
		if (inst == null) {
			Private_BlueprintLoader impl = new Private_BlueprintLoader();
			inst = impl.doLoad();
			s_inst = inst;
		}
		return inst;
	}

	private Private_BlueprintLoader() {
	}

	private Blueprint doLoad() {
		try {

			URI uri = ClassUriGen.getURI(Blueprint.class, "bp3.properties");
			ResourceClassConnection conn = (ResourceClassConnection) Connector.Factory
					.getConnector().open(uri.toString());
			ResourceConnection res = conn.getResource(null);
			InputStream in = res.getInputStream();

			Properties prop = new Properties();
			prop.load(in);
			String key = BlueprintFactory.class.getName();
			String value = prop.getProperty(key);
			Class<?> cls = Class.forName(value);
			BlueprintFactory factory = (BlueprintFactory) cls.newInstance();
			return factory.newBlueprint();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
