package ananas.lib.impl.io.vfs;

import java.io.File;
import java.net.URI;

import ananas.lib.io.vfs.VFile;
import ananas.lib.io.vfs.VFileSystem;
import ananas.lib.io.vfs.VFileSystemConfiguration;
import ananas.lib.io.vfs.VFileSystemFactory;

public class VFileSystemImpl implements VFileSystem {

	private VFileSystemConfiguration mConfig;
	private VFileSystemFactory mFactory;

	public VFileSystemImpl(VFileSystemFactory factory,
			VFileSystemConfiguration config) {
		this.mConfig = config;
		this.mFactory = factory;
	}

	@Override
	public VFileSystemFactory getFactory() {
		return this.mFactory;
	}

	@Override
	public VFileSystemConfiguration getConfiguration() {
		return this.mConfig;
	}

	@Override
	public VFile newFile(VFile dir, String string) {
		File file = new File(new File(dir.getURI()), string);
		return new VFileImpl(this, file);
	}

	@Override
	public VFile newFile(String path) {
		File file = new File(path);
		return new VFileImpl(this, file);
	}

	@Override
	public VFile newFile(String dir, String string) {
		File file = new File(dir, string);
		return new VFileImpl(this, file);
	}

	@Override
	public VFile newFile(URI uri) {
		File file = new File(uri);
		return new VFileImpl(this, file);
	}

}
