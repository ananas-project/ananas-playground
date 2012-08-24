package ananas.lib.servkit.pool;

public class DefaultPool implements IPool {

	private final Class<?> mClass;
	private final int mSize;
	private int mCount;
	private final IPoolable[] mStack;
	private int mCountNew;

	public DefaultPool(Class<?> aClass, int size) {
		this.mClass = aClass;
		this.mSize = size;
		this.mStack = new IPoolable[size];
	}

	@Override
	public IPoolable alloc(Class<?> aClass) {

		if (!this.mClass.equals(aClass)) {
			return null;
		}

		IPoolable rlt = null;

		if (this.mCount > 0) {
			rlt = this.mStack[--this.mCount];
		}

		if (rlt == null) {
			rlt = this._pureNew();
		}

		rlt.onAlloc();
		return rlt;
	}

	private IPoolable _pureNew() {
		IPoolable rlt = null;
		try {
			rlt = (IPoolable) this.mClass.newInstance();
			rlt.bindToPool(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("new[index:" + (this.mCountNew++) + " , class:"
				+ this.mClass + " ]");

		return rlt;
	}

	@Override
	public void dealloc(IPoolable object) {
		if (this.mCount < this.mSize)
			if (object.getPool().equals(this))
				if (this.mClass.isInstance(object)) {
					this.mStack[this.mCount++] = object;
				}
	}

	@Override
	public Class<?> getTargetClass() {
		return this.mClass;
	}

	@Override
	public int getSize() {
		return this.mSize;
	}

}
