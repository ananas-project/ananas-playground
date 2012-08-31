package ananas.lib.servkit.pool.bpr;

import java.util.List;
import java.util.Vector;

import ananas.lib.blueprint.elements.reflect.ReflectElement;
import ananas.lib.servkit.pool.IPool;
import ananas.lib.servkit.pool.IPoolGroup;
import ananas.lib.servkit.pool.IPoolGroupFactory;
import ananas.lib.servkit.pool.ISinglePool;

public class BprPoolGroup implements IBprPool {

	private List<IBprPool> mList;
	private ReflectElement mElement;
	private Class<?> mTargetClass;

	public BprPoolGroup() {
		this.mList = new Vector<IBprPool>();
	}

	public boolean bind(Object child) {

		if (child == null) {
			return false;

		} else if (child instanceof IBprPool) {
			this.mList.add((IBprPool) child);
			return true;

		} else if (child instanceof ReflectElement) {
			this.mElement = (ReflectElement) child;
			return true;

		} else {
			return false;
		}
	}

	public void setClass(String cls) {
		this.mTargetClass = this.mElement.findClass(cls);
	}

	@Override
	public IPool createPool() {
		try {
			// this.mElement = null;
			IPoolGroupFactory pf = (IPoolGroupFactory) this.mTargetClass
					.newInstance();
			IPoolGroup group = pf.newPoolGroup();
			for (IBprPool bprPool : this.mList) {
				IPool pool = bprPool.createPool();
				Class<?> aClass = ((ISinglePool) pool).getClass();
				group.addPool(aClass, pool);
			}
			return group;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
