package ananas.lib.servkit.pool;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import ananas.lib.servkit.monitor.IMonitorProbe;
import ananas.lib.servkit.monitor.MonitorAgent;

public class DefaultSafePoolGroupFactory implements IPoolGroupFactory {

	@Override
	public IPoolGroup newPoolGroup() {
		return new MyPoolGroup();
	}

	private class MyPoolGroup implements IPoolGroup {

		private final Map<Class<?>, IPool> mTable;
		private final List<IPool> mList;
		private final IMonitorProbe mMoProbe;

		public MyPoolGroup() {
			this.mTable = new Hashtable<Class<?>, IPool>();
			this.mList = new Vector<IPool>();
			this.mMoProbe = MonitorAgent.getAgent().newProbe(this);
		}

		@Override
		public IPoolable alloc(Class<?> aClass) {
			IPool pool = this.mTable.get(aClass);
			if (pool == null)
				return null;
			return pool.alloc(aClass);
		}

		@Override
		public void addPool(Class<?> aClass, IPool pool) {
			this.mTable.put(aClass, pool);
			this.mList.add(pool);
		}

		@Override
		public void reset() {
			this.mMoProbe.print("reset");
			for (IPool pool : this.mList) {
				pool.reset();
			}
		}

	}

}
