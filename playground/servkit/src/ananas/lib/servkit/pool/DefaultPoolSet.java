package ananas.lib.servkit.pool;

import java.util.HashMap;

public class DefaultPoolSet implements IPoolSet {

	private final HashMap<Class<?>, IPool> mTable = new HashMap<Class<?>, IPool>();

	protected DefaultPoolSet() {
	}

	@Override
	public IPoolable alloc(Class<?> aClass) {
		IPool pool = this.getPool(aClass);
		return pool.alloc(aClass);
	}

	@Override
	public void dealloc(IPoolable object) {
		IPool pool = this.getPool(object.getClass());
		pool.dealloc(object);
	}

	@Override
	public void addPool(IPool pool) {
		this.mTable.put(pool.getTargetClass(), pool);
	}

	@Override
	public IPool getPool(Class<?> aClass) {
		return this.mTable.get(aClass);
	}

}
