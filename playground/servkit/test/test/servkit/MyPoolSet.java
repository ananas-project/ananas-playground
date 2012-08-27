package test.servkit;

import ananas.lib.servkit.json.JSON;
import ananas.lib.servkit.pool.DefaultCachedPoolFactory;
import ananas.lib.servkit.pool.DefaultFixedPoolFactory;
import ananas.lib.servkit.pool.DefaultSafeCachedPoolFactory;
import ananas.lib.servkit.pool.DefaultSafeFixedPoolFactory;
import ananas.lib.servkit.pool.DefaultSafePoolGroupFactory;
import ananas.lib.servkit.pool.IClassPoolFactory;
import ananas.lib.servkit.pool.IPool;
import ananas.lib.servkit.pool.IPoolGroup;
import ananas.lib.servkit.pool.IPoolGroupFactory;

public class MyPoolSet {

	public static IPool newInstance() {

		int poolType = 2;
		int size = 20;

		IPoolGroupFactory pgf = new DefaultSafePoolGroupFactory();
		IClassPoolFactory cpf = null;

		switch (poolType) {
		case 1:
			cpf = new DefaultCachedPoolFactory();
			break;
		case 2:
			cpf = new DefaultFixedPoolFactory();
			break;
		case 3:
			cpf = new DefaultSafeCachedPoolFactory();
			break;
		default:
			cpf = new DefaultSafeFixedPoolFactory();
			break;
		}

		IPoolGroup group = pgf.newPoolGroup();

		group.addPool(cpf.newPool(JSON.class_array, size, false));
		group.addPool(cpf.newPool(JSON.class_object, size, false));

		group.addPool(cpf.newPool(JSON.class_string, size, false));

		group.addPool(cpf.newPool(JSON.class_long, size, false));
		group.addPool(cpf.newPool(JSON.class_int, size, false));
		group.addPool(cpf.newPool(JSON.class_double, size, false));

		return group;
	}

}
