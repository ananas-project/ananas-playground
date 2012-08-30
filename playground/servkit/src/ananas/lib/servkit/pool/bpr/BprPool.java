package ananas.lib.servkit.pool.bpr;

import ananas.lib.blueprint.elements.reflect.ReflectElement;
import ananas.lib.servkit.pool.IClassPoolFactory;
import ananas.lib.servkit.pool.IPool;

public class BprPool implements IBprPool {

	private ReflectElement mElement;
	private Class<?> mTargetClass;
	private Class<?> mItemClass;
	private int mSize;
	private boolean mResetable;

	public boolean bind(Object element) {
		if (element instanceof ReflectElement) {
			this.mElement = (ReflectElement) element;
		} else {
			return false;
		}
		return true;
	}

	public void setClass(String s) {
		this.mTargetClass = this.mElement.findClass(s);
		// System.out.println(this.mTargetClass);
	}

	public void setItemClass(String s) {
		this.mItemClass = this.mElement.findClass(s);
		// System.out.println(this.mItemClass);
	}

	public void setSize(String s) {
		this.mSize = Integer.parseInt(s);
	}

	public void setResetable(String s) {
		this.mResetable = Boolean.parseBoolean(s);
	}

	@Override
	public IPool createPool() {
		try {
			this.mElement = null;
			IClassPoolFactory pf = (IClassPoolFactory) this.mTargetClass
					.newInstance();
			return pf.newPool(this.mItemClass, this.mSize, this.mResetable);
		} catch (Exception e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
