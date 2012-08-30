package ananas.lib.servkit.pool.bpr;

import ananas.lib.blueprint.elements.reflect.ReflectElement;
import ananas.lib.servkit.pool.IPool;
import ananas.lib.servkit.pool.IPoolableFactory;
import ananas.lib.servkit.pool.ISinglePoolFactory;

public class BprPool implements IBprPool {

	// the product of this class is ISinglePool

	private ReflectElement mElement;
	private int mSize;
	private boolean mResetable;
	//
	private String mItemFactoryRef;
	private String mTargetClassRef;
	private String mItemClassRef;
	//
	private Class<?> mTargetClass;
	private Class<?> mItemClass;

	public boolean bind(Object element) {
		if (element instanceof ReflectElement) {
			this.mElement = (ReflectElement) element;
		} else {
			return false;
		}
		return true;
	}

	public void setClass(String s) {
		this.mTargetClassRef = s;
	}

	public void setItemClass(String s) {
		this.mItemClassRef = s;
	}

	public void setItemFactory(String s) {
		this.mItemFactoryRef = s;
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

			ISinglePoolFactory pf = this._getPoolFactory();
			IPoolableFactory itemFactory = this._getItemFactory();
			int size = this.mSize;
			boolean resetable = this.mResetable;
			return pf.newPool(itemFactory, size, resetable);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private ISinglePoolFactory _getPoolFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	private IPoolableFactory _getItemFactory() {
		// TODO Auto-generated method stub
		return null;
	}

}
