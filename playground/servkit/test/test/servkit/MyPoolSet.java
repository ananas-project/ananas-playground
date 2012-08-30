package test.servkit;

import ananas.lib.servkit.json.JSON;
import ananas.lib.servkit.pool.DefaultCachedPoolFactory;
import ananas.lib.servkit.pool.DefaultFixedPoolFactory;
import ananas.lib.servkit.pool.DefaultSafeCachedPoolFactory;
import ananas.lib.servkit.pool.DefaultSafeFixedPoolFactory;
import ananas.lib.servkit.pool.DefaultSafePoolGroupFactory;
import ananas.lib.servkit.pool.IPool;
import ananas.lib.servkit.pool.IPoolGroup;
import ananas.lib.servkit.pool.IPoolGroupFactory;
import ananas.lib.servkit.pool.IPoolableFactory;
import ananas.lib.servkit.pool.ISinglePool;
import ananas.lib.servkit.pool.ISinglePoolFactory;

public class MyPoolSet {

	public static IPool newInstance() {

		int poolType = 2;
		int size = 20;

		IPoolGroupFactory pgf = new DefaultSafePoolGroupFactory();
		ISinglePoolFactory cpf = null;

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

		// //////////////

		_addPool(group, cpf, JSON.class_array, size, false);
		_addPool(group, cpf, JSON.class_object, size, false);
		_addPool(group, cpf, JSON.class_string, size, false);
		_addPool(group, cpf, JSON.class_long, size, false);
		_addPool(group, cpf, JSON.class_int, size, false);
		_addPool(group, cpf, JSON.class_double, size, false);

		return group;
	}

	private static void _addPool(IPoolGroup group, ISinglePoolFactory pf,
			Class<?> aClass, int size, boolean resetable) {

		IPoolableFactory itemFactory = null;
		ISinglePool pool = pf.newPool(itemFactory, size, resetable);
		group.addPool(aClass, pool);
	}

}
