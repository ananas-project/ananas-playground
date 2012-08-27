package ananas.lib.servkit.pool;

public class DefaultSafeCachedPoolFactory implements IClassPoolFactory {

	@Override
	public IClassPool newPool(Class<?> aClass, int size, boolean resetable) {
		return new MyPool(aClass, size, resetable);
	}

	private class MyPool implements IClassPool {

		private final int mSize;
		private final Class<?> mClass;
		private final boolean mResetable;
		private final IProbe[] mArrayAll;
		private final IProbe[] mArrayFree;
		private int mCountAll;
		private int mCountFree;
		private int mDebugCountNew;

		public MyPool(Class<?> aClass, int size, boolean resetable) {

			this.mSize = size;
			this.mClass = aClass;
			this.mResetable = resetable;

			this.mArrayAll = new IProbe[size];
			this.mArrayFree = new IProbe[size];

			this.mCountAll = 0;
			this.mCountFree = 0;
		}

		@Override
		public IPoolable alloc(Class<?> aClass) {
			if (!this.mClass.equals(aClass)) {
				return null;
			}
			IProbe obj = this._alloc();
			obj.onAlloc();
			return obj.toPoolable();
		}

		private synchronized IProbe _alloc() {
			IProbe obj = null;
			if (this.mCountFree <= 0) {
				this._recycle();
			}
			if (this.mCountFree > 0) {
				obj = this.mArrayFree[--this.mCountFree];
			} else {
				obj = this._PureNew();
			}
			return obj;
		}

		private void _recycle() {
			int cnt = 0;
			int index = 0;
			for (IProbe item : this.mArrayAll) {
				if (item != null) {
					if (item.isFree()) {
						this.mArrayFree[cnt++] = item;
					}
				}
				index++;
				if (index >= this.mCountAll) {
					break;
				}
			}
			this.mCountFree = cnt;
		}

		@Override
		public Class<?> getPoolableClass() {
			return this.mClass;
		}

		@Override
		public int getSize() {
			return this.mSize;
		}

		@Override
		public void reset() {
			if (!this.mResetable) {
				throw new RuntimeException("no support reset.");
			}
			for (IProbe item : this.mArrayAll) {
				if (item != null) {
					item.free();
				}
			}
		}

		private IProbe _PureNew() {
			try {
				IPoolable poolable = (IPoolable) this.mClass.newInstance();
				IProbe probe = poolable.toProbe();
				if (this.mCountAll < this.mSize) {
					this.mArrayAll[this.mCountAll++] = probe;
				}
				if (Debug.showInfo) {
					int cnt = (++this.mDebugCountNew);
					String pool = this + "";
					String obj = this.mClass.getName();
					System.err.println(pool + " -> " + obj + ".new(); count:"
							+ cnt);
				}
				return probe;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

}
