package ananas.lib.servkit.pool;

public class DefaultFixedPoolFactory implements ISinglePoolFactory {

	private class MyPool implements ISinglePool {

		private final IPoolableFactory mItemFactory;
		private final int mSize;
		private final boolean mResetable;

		private final IProbe[] mArrayAll;
		private final IProbe[] mArrayFree; // work as a stack
		private int mCountFree;
		private int mCountAll;
		private int mDebugCountNew;

		public MyPool(IPoolableFactory itemFactory, int size, boolean resetable) {
			this.mItemFactory = itemFactory;
			this.mSize = size;
			this.mResetable = resetable;

			this.mArrayAll = new IProbe[size];
			this.mArrayFree = new IProbe[size];

		}

		@Override
		public IPoolable alloc(Class<?> aClass) {
			if (!this.mItemFactory.equals(aClass)) {
				return null;
			}
			IProbe pb = this._alloc();
			if (pb == null)
				return null;
			pb.onAlloc();
			return pb.toPoolable();
		}

		private/* synchronized */IProbe _alloc() {
			if (this.mCountFree > 0) {
				return this.mArrayFree[--this.mCountFree];
			}
			this._recycle();
			if (this.mCountFree > 0) {
				return this.mArrayFree[--this.mCountFree];
			}
			return this._pureNew();
		}

		private IProbe _pureNew() {
			final int index = this.mCountAll;
			if (index >= this.mSize || index < 0)
				return null;
			IProbe pb = null;
			try {
				IPoolable pa = this.mItemFactory.newObject();
				pb = pa.toProbe();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (pb == null)
				return null;
			this.mArrayAll[index] = pb;
			this.mCountAll = index + 1;
			if (Debug.showInfo) {
				int cnt = (++this.mDebugCountNew);
				String pool = this + "";
				String obj = this.mItemFactory.toString();
				System.err
						.println(pool + " -> " + obj + ".new(); count:" + cnt);
			}
			return pb;
		}

		private void _recycle() {
			int cnt = 0;
			int index = 0;
			for (IProbe item : this.mArrayAll) {
				if (index >= this.mCountAll) {
					break;
				}
				if (item != null) {
					if (item.isFree()) {
						this.mArrayFree[cnt++] = item;
					}
				}
			}
			this.mCountFree = cnt;
		}

		@Override
		public int getSize() {
			return this.mSize;
		}

		@Override
		public void reset() {
			if (!this.mResetable) {
				throw new RuntimeException("no support reset");
			}
			for (IProbe item : this.mArrayAll) {
				if (item != null) {
					item.free();
				}
			}
		}
	}

	@Override
	public ISinglePool newPool(IPoolableFactory itemFactory, int size,
			boolean resetable) {
		return new MyPool(itemFactory, size, resetable);
	}

}
