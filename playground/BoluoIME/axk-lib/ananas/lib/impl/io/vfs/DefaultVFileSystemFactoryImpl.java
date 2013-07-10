package ananas.lib.impl.io.vfs;

import ananas.lib.io.vfs.VFileSystem;
import ananas.lib.io.vfs.VFileSystemConfiguration;
import ananas.lib.io.vfs.VFileSystemFactory;

public class DefaultVFileSystemFactoryImpl implements VFileSystemFactory {

	@Override
	public VFileSystem createFileSystem(VFileSystemConfiguration config) {
		return new VFileSystemImpl(this, config);
	}

	@Override
	public VFileSystem defaultFileSystem() {
		return this.createFileSystem(null);
	}

}
