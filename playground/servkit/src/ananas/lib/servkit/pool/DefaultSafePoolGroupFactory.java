package ananas.lib.servkit.pool;

import java.util.Hashtable;
import java.util.Map;

public class DefaultSafePoolGroupFactory implements IPoolGroupFactory {

	@Override
	public IPoolGroup newPoolGroup() {
		return new MyPoolGroup();
	}

	private class MyPoolGroup implements IPoolGroup {

		private final Map<Class<?>, IClassPool> mTable;

		public MyPoolGroup() {
			this.mTable = new Hashtable<Class<?>, IClassPool>();
		}

		@Override
		public IPoolable alloc(Class<?> aClass) {
			IClassPool pool = this.mTable.get(aClass);
			if (pool == null)
				return null;
			return pool.alloc(aClass);
		}

		@Override
		public void addPool(IClassPool pool) {
			Class<?> cls = pool.getPoolableClass();
			this.mTable.put(cls, pool);
		}

	}

}
