package ananas.lib.io.vfs;

public class VFileSystemBootstrap {

	public static VFileSystemFactory getFactory(String classname) {
		if (classname == null) {
			classname = "ananas.lib.impl.io.vfs.DefaultVFileSystemFactoryImpl";
		}
		try {
			Class<?> cls = Class.forName(classname);
			return (VFileSystemFactory) cls.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
