package ananas.lib.servkit.pool;

import java.util.Hashtable;
import java.util.Map;

public class DefaultSafePoolGroupFactory implements IPoolGroupFactory {

	@Override
	public IPoolGroup newPoolGroup() {
		return new MyPoolGroup();
	}

	private class MyPoolGroup implements IPoolGroup {

		private final Map<Class<?>, IPool> mTable;

		public MyPoolGroup() {
			this.mTable = new Hashtable<Class<?>, IPool>();
		}

		@Override
		public IPoolable alloc(Class<?> aClass) {
			IPool pool = this.mTable.get(aClass);
			if (pool == null)
				return null;
			return pool.alloc(aClass);
		}

		@Override
		public void addPool(Class<?> aClass, IPool pool) {
			this.mTable.put(aClass, pool);
		}

	}

}
