package ananas.objecthub.core;

import ananas.objecthub.core.impl.ImplObjectRepo;

public interface IObjectRepo {

	IObject getObject(String sha1);

	IObjectPutting putObject(String sha1);

	IObjectRepoConfig getConfig();

	class Agent {

		private static IObjectRepo _inst;

		public static IObjectRepo getInstance() {
			IObjectRepo inst = _inst;
			if (inst == null) {
				IObjectRepoConfig conf = new DefaultConfig();
				_inst = inst = new ImplObjectRepo(conf);
			}
			return inst;
		}
	}
}
