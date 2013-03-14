package ananas.tools.dtt;

import java.io.File;
import java.util.Properties;

import ananas.tools.dtt.impl.DailyContextImpl;

public interface DailyContext {

	File getConfigFile();

	File getRecordFile();

	DailyFactory getFactory();

	class Factory {

		public static DailyContext getDefault(Properties prop) {
			return new DailyContextImpl(prop);
		}
	}
}
