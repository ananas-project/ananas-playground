package ananas.lib.io.vfs;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public interface VFile {

	VFileSystem getVFS();

	boolean exists();

	boolean mkdir();

	boolean mkdirs();

	boolean isDirectory();

	boolean createNewFile() throws IOException;

	VFile getParentFile();

	boolean isFile();

	URI getURI();

	String getName();

	String getAbsolutePath();

	class Factory {

		public static VFileSystem getVFS() {
			return VFS.getFactory().defaultFileSystem();
		}
	}

	List<VFile> listFiles();

}
