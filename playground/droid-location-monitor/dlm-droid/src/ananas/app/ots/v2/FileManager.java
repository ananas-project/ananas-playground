package ananas.app.ots.v2;

import java.io.File;

public interface FileManager {

	class Factory {

		public static FileManager getDefault() {
			return new InnerFileManager();
		}

	}

	File getBaseDir();

	File getFragmentDir();

}
