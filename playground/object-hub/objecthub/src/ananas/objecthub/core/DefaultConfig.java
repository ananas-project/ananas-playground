package ananas.objecthub.core;

import java.io.File;

public class DefaultConfig implements IObjectRepoConfig {

	private File _repo_dir;

	@Override
	public File getRepoDirectory() {
		if (_repo_dir == null) {
			_repo_dir = new File("/var/ananas/objecthub/repo");
		}
		return this._repo_dir;
	}

}
