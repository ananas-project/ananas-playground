package ananas.lib.io.vfs;

public final class VFS {

	private VFS() {
	}

	public static VFileSystemFactory getFactory() {
		return VFileSystemBootstrap.getFactory(null);
	}

	public static VFileSystemFactory getFactory(String classname) {
		return VFileSystemBootstrap.getFactory(classname);
	}

}
