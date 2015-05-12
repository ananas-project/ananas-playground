package ananas.app.ots.v2;

import java.io.File;

import android.os.Environment;

final class InnerFileManager implements FileManager {

	private File mBaseDir;
	private File mFragmentDir;

	private void tryInit() {
		if (this.mBaseDir == null) {
			// base
			File base = Environment.getExternalStorageDirectory();
			base = new File(base, "ananas/OTS");
			// fields
			this.mFragmentDir = new File(base, "fragments");
			this.mBaseDir = base;
		}
	}

	@Override
	public File getBaseDir() {
		this.tryInit();
		return this.mBaseDir;
	}

	@Override
	public File getFragmentDir() {
		this.tryInit();
		return this.mFragmentDir;
	}

}
