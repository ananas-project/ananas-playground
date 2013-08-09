package ananas.objecthub.core.impl;

import java.io.File;

import ananas.objecthub.core.IObject;
import ananas.objecthub.core.IObjectPutting;
import ananas.objecthub.core.IObjectRepo;
import ananas.objecthub.core.IObjectRepoConfig;

public class ImplObjectRepo implements IObjectRepo {

	private final IObjectRepoConfig _config;

	public ImplObjectRepo(IObjectRepoConfig config) {
		_config = config;
	}

	@Override
	public IObject getObject(String sha1) {
		return this.__openObject(sha1, false);
	}

	private IObjectPutting __openObject(String sha1, boolean create) {
		sha1 = sha1.toLowerCase();
		File file = this.__fileForSha1(sha1);
		if (file.exists()) {
			return new ImplObjectPlus(file, sha1);
		} else {
			if (create) {
				return new ImplObjectPlus(file, sha1);
			} else {
				return null;
			}
		}
	}

	private File __fileForSha1(String sha1) {
		final File dir = this._config.getRepoDirectory();
		final int i = 2;
		String p1 = sha1.substring(0, i);
		String p2 = sha1.substring(i);
		return new File(dir, "objs/" + p1 + "/" + p2);
	}

	@Override
	public IObjectPutting putObject(String sha1) {
		return this.__openObject(sha1, true);
	}

	@Override
	public IObjectRepoConfig getConfig() {
		return this._config;
	}
}
