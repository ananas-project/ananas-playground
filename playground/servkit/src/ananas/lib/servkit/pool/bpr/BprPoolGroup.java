package ananas.lib.servkit.pool.bpr;

import java.util.List;
import java.util.Vector;

import ananas.lib.blueprint.elements.reflect.ReflectElement;
import ananas.lib.servkit.pool.IPool;
import ananas.lib.servkit.pool.IPoolGroup;
import ananas.lib.servkit.pool.IPoolGroupFactory;

public class BprPoolGroup implements IBprPool {

	// raw
	private List<MyPoolItem> mList;
	private ReflectElement mElement;
	private IBprPoolableClassProvider mCurClassProvider;
	private String mGroupFactoryClassRef;
	// cache
	private IPoolGroupFactory mGroupFactory;

	public BprPoolGroup() {
		this.mList = new Vector<MyPoolItem>();
	}

	public boolean bind(Object child) {

		if (child == null) {
			return false;

		} else if (child instanceof ReflectElement) {
			this.mElement = (ReflectElement) child;
			return true;

		} else {
			boolean rlt = false;
			if (child instanceof IBprPoolableClassProvider) {
				rlt = true;
				this._appendClassProvider((IBprPoolableClassProvider) child);
			}
			if (child instanceof IBprPool) {
				rlt = true;
				this._appendPool((IBprPool) child);
			}
			return rlt;
		}
	}

	private void _appendClassProvider(IBprPoolableClassProvider clsPro) {
		if (clsPro != null)
			this.mCurClassProvider = clsPro;
	}

	private void _appendPool(IBprPool pool) {
		IBprPoolableClassProvider clsPro = this.mCurClassProvider;
		this.mCurClassProvider = null;
		if (clsPro != null && pool != null) {
			MyPoolItem item = new MyPoolItem(clsPro, pool);
			this.mList.add(item);
		}
	}

	private static class MyPoolItem {

		private final IBprPoolableClassProvider mClassPro;
		private final IBprPool mPool;
		// cache
		private Class<?> mPoolableClass;

		public MyPoolItem(IBprPoolableClassProvider clsPro, IBprPool pool) {
			this.mClassPro = clsPro;
			this.mPool = pool;
		}

		public Class<?> getPoolableClass() {
			Class<?> ret = this.mPoolableClass;
			if (ret == null) {
				ret = this.mClassPro.getPoolableClass();
				this.mPoolableClass = ret;
			}
			return ret;
		}

		public IPool newPool() {
			return this.mPool.createPool();
		}
	}

	public void setClass(String cls) {
		this.mGroupFactoryClassRef = cls;
	}

	@Override
	public IPool createPool() {
		try {
			IPoolGroupFactory pf = this._getGroupFactory();
			IPoolGroup group = pf.newPoolGroup();
			for (MyPoolItem item : this.mList) {
				IPool pool = item.newPool();
				Class<?> aClass = item.getPoolableClass();
				group.addPool(aClass, pool);
			}
			return group;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private IPoolGroupFactory _getGroupFactory() {
		IPoolGroupFactory ret = this.mGroupFactory;
		if (ret == null) {
			Class<?> groupFactoryClass = this.mElement
					.findClass(this.mGroupFactoryClassRef);
			try {
				ret = (IPoolGroupFactory) groupFactoryClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			this.mGroupFactory = ret;
		}
		return ret;
	}

}
