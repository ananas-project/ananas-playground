package ananas.lib.servkit.pool.bpr;

import ananas.lib.blueprint.elements.reflect.ReflectElement;
import ananas.lib.servkit.pool.IPool;
import ananas.lib.servkit.pool.IPoolable;
import ananas.lib.servkit.pool.IPoolableFactory;
import ananas.lib.servkit.pool.ISinglePoolFactory;

public class BprSinglePool implements IBprPool, IBprPoolableClassProvider {

	// the product of this class is ISinglePool

	private ReflectElement mElement;
	private int mSize;
	private boolean mResetable;
	//
	private String mPoolableFactoryRef;
	private String mPoolFactoryClassRef;
	private String mPoolableClassRef;
	//
	private Class<?> mPoolFactoryClass;
	private Class<?> mPoolableClass;
	private ISinglePoolFactory mPoolFactory;
	private IPoolableFactory mPoolableFactory;

	public boolean bind(Object element) {
		if (element instanceof ReflectElement) {
			this.mElement = (ReflectElement) element;
		} else {
			return false;
		}
		return true;
	}

	/**
	 * set the reference of pool class
	 * */
	public void setClass(String s) {
		this.mPoolFactoryClassRef = s;
	}

	/**
	 * set the reference of pool-able class
	 * */
	public void setItemClass(String s) {
		this.mPoolableClassRef = s;
	}

	/**
	 * set the reference of pool-able factory object
	 * */
	public void setItemFactory(String s) {
		this.mPoolableFactoryRef = s;
	}

	public void setSize(String s) {
		this.mSize = Integer.parseInt(s);
	}

	/**
	 * set the pool can be reset or not.
	 * 
	 * @param s
	 *            a boolean format string, value is true/false.
	 * */

	public void setResetable(String s) {
		this.mResetable = Boolean.parseBoolean(s);
	}

	@Override
	public IPool createPool() {
		try {
			ISinglePoolFactory pf = this._getPoolFactory();
			IPoolableFactory itemFactory = this._getPoolableFactory();
			int size = this.mSize;
			boolean resetable = this.mResetable;
			return pf.newPool(itemFactory, size, resetable);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private ISinglePoolFactory _getPoolFactory() {
		ISinglePoolFactory spf = this.mPoolFactory;
		if (spf == null) {
			Class<?> cls = this._getPoolFactoryClass();
			ISinglePoolFactory factory;
			try {
				factory = (ISinglePoolFactory) cls.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			this.mPoolFactory = spf = factory;
		}
		return spf;
	}

	private Class<?> _getPoolFactoryClass() {
		Class<?> cls = this.mPoolFactoryClass;
		if (cls == null) {
			cls = this.mElement.findClass(this.mPoolFactoryClassRef);
			this.mPoolFactoryClass = cls;
		}
		return cls;
	}

	private IPoolableFactory _getPoolableFactory() {
		IPoolableFactory pf = this.mPoolableFactory;
		if (pf == null) {
			String ref = this.mPoolableFactoryRef;
			if (ref != null) {
				pf = (IPoolableFactory) this.mElement.findTargetObjectById(ref);
			}
			if (pf == null) {
				Class<?> cls = this._getPoolableClass();
				pf = new MyPoolableFactory(cls);
			}
			this.mPoolableFactory = pf;
		}
		return pf;
	}

	private Class<?> _getPoolableClass() {
		Class<?> cls = this.mPoolableClass;
		if (cls == null) {
			cls = this.mElement.findClass(this.mPoolableClassRef);
			this.mPoolableClass = cls;
		}
		return cls;
	}

	private static class MyPoolableFactory implements IPoolableFactory {

		private final Class<?> mClass;

		public MyPoolableFactory(Class<?> cls) {
			this.mClass = cls;
		}

		@Override
		public IPoolable newObject() {
			try {
				return (IPoolable) this.mClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public Class<?> getPoolableClass() {
		return this._getPoolableClass();
	}
}
