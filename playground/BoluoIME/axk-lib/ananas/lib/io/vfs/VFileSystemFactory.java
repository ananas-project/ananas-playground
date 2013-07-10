package ananas.lib.io.vfs;

public interface VFileSystemFactory {

	VFileSystem defaultFileSystem();

	VFileSystem createFileSystem(VFileSystemConfiguration config);
}
