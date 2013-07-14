package ananas.lib.blueprint3.util.nsloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import ananas.lib.blueprint3.lang.BlueprintException;

public abstract class AbstractBPNamespaceInfo implements BPNamespaceInfo {

	private Properties mProperties;

	public AbstractBPNamespaceInfo(String fileName) {
		this.loadProperties(fileName);
	}

	private final boolean loadProperties(String fileName) {
		boolean rlt = false;
		InputStream in = null;
		try {
			in = this.getClass().getResourceAsStream(fileName);
			if (in == null) {
				throw new BlueprintException("cannot open inputstream with : "
						+ this + "#" + fileName);
			}
			Properties prop = new Properties();
			prop.load(in);
			rlt = true;
			this.mProperties = prop;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rlt;
	}

	public final String getProperty(String key) {
		return this.mProperties.getProperty(key);
	}

}
