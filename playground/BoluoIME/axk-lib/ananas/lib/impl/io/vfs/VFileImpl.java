package ananas.lib.impl.io.vfs;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import ananas.lib.io.vfs.VFile;
import ananas.lib.io.vfs.VFileSystem;

public class VFileImpl implements VFile {

	private final File mFile;
	private final VFileSystem mVFS;

	public VFileImpl(VFileSystem vfs, File file) {
		this.mFile = file;
		this.mVFS = vfs;
	}

	@Override
	public VFileSystem getVFS() {
		return this.mVFS;
	}

	@Override
	public boolean exists() {
		return this.mFile.exists();
	}

	@Override
	public boolean mkdirs() {
		return this.mFile.mkdirs();
	}

	@Override
	public boolean isDirectory() {
		return this.mFile.isDirectory();
	}

	@Override
	public boolean createNewFile() throws IOException {
		return this.mFile.createNewFile();
	}

	@Override
	public VFile getParentFile() {
		File pf = this.mFile.getParentFile();
		if (pf == null) {
			return null;
		}
		return new VFileImpl(this.mVFS, pf);
	}

	@Override
	public boolean isFile() {
		return this.mFile.isFile();
	}

	@Override
	public URI getURI() {
		return this.mFile.toURI();
	}

	@Override
	public String getName() {
		return this.mFile.getName();
	}

	public String toString() {
		return this.mFile.toString();
	}

	@Override
	public boolean mkdir() {
		return this.mFile.mkdir();
	}

	@Override
	public List<VFile> listFiles() {
		File[] files = this.mFile.listFiles();
		if (files == null) {
			return null;
		}
		List<VFile> vlist = new ArrayList<VFile>(files.length);
		for (File file : files) {
			VFile vf = new VFileImpl(this.mVFS, file);
			vlist.add(vf);
		}
		return vlist;
	}

	@Override
	public String getAbsolutePath() {
		return this.mFile.getAbsolutePath();
	}
}
