package ananas.lib.servkit.pool;

import ananas.lib.servkit.monitor.IMonitorProbe;
import ananas.lib.servkit.monitor.MonitorAgent;

public class DefaultFixedPoolFactory implements ISinglePoolFactory {

	private class MyPool implements ISinglePool {

		private final IPoolableFactory mItemFactory;
		private final int mSize;
		private final boolean mResetable;

		private final IProbe[] mArrayAll;
		private final IProbe[] mArrayFree; // work as a stack
		private int mCountFree;
		private int mCountAll;
		private final IMonitorProbe mMoProbe;

		public MyPool(IPoolableFactory itemFactory, int size, boolean resetable) {
			this.mItemFactory = itemFactory;
			this.mSize = size;
			this.mResetable = resetable;

			this.mArrayAll = new IProbe[size];
			this.mArrayFree = new IProbe[size];

			this.mMoProbe = MonitorAgent.getAgent().newProbe(this);
		}

		@Override
		public IPoolable alloc(Class<?> aClass) {
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
			IPoolable poolable = null;
			try {
				poolable = this.mItemFactory.newObject();
				pb = poolable.toProbe();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (pb == null)
				return null;
			this.mArrayAll[index] = pb;
			this.mCountAll = index + 1;
			if (this.mMoProbe.enable()) {
				this.mMoProbe.print("new " + poolable);
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
