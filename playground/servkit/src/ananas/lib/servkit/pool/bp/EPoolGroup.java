package ananas.lib.servkit.pool.bp;

import java.util.List;
import java.util.Vector;

import ananas.lib.blueprint.IElement;
import ananas.lib.servkit.pool.IClassPool;
import ananas.lib.servkit.pool.IPool;
import ananas.lib.servkit.pool.IPoolFactory;
import ananas.lib.servkit.pool.IPoolGroup;
import ananas.lib.servkit.pool.IPoolGroupFactory;

public class EPoolGroup extends EPoolObjectBase implements IEPoolFactory {

	private final List<IEPoolFactory> mSubList;
	private IPoolGroupFactory mIPoolGroupFactory;

	public EPoolGroup() {
		this.mSubList = new Vector<IEPoolFactory>();
	}

	@Override
	public boolean appendChild(IElement element) {
		if (element == null) {
			return false;
		} else if (element instanceof IEPoolFactory) {
			this.mSubList.add((IEPoolFactory) element);
		} else {
			return super.appendChild(element);
		}
		return true;
	}

	protected IPoolGroupFactory getIPoolGroupFactory() {
		IPoolGroupFactory ret = this.mIPoolGroupFactory;
		if (ret == null) {
			Class<?> cls = this.findClass(null);
			try {
				ret = (IPoolGroupFactory) cls.newInstance();
			} catch (Exception e) {
				// e.printStackTrace();
				throw new RuntimeException(e);
			}
			this.mIPoolGroupFactory = ret;
		}
		return ret;
	}

	@Override
	public IPoolFactory toPoolFactory() {
		Vector<IPoolFactory> spf = new Vector<IPoolFactory>();
		for (IEPoolFactory pf : this.mSubList) {
			IPoolFactory sub = pf.toPoolFactory();
			spf.addElement(sub);
		}
		IPoolGroupFactory groupFactory = this.getIPoolGroupFactory();
		IPoolFactory[] array = spf.toArray(new IPoolFactory[spf.size()]);
		return new MyPoolFactory(groupFactory, array);
	}

	private static class MyPoolFactory implements IPoolFactory {

		private final IPoolGroupFactory mGroupFactory;
		private final IPoolFactory[] mArray;

		public MyPoolFactory(IPoolGroupFactory groupFactory,
				IPoolFactory[] array) {
			this.mGroupFactory = groupFactory;
			this.mArray = array;
		}

		@Override
		public IPool newPool() {
			IPoolGroup group = this.mGroupFactory.newPoolGroup();
			for (IPoolFactory pf : this.mArray) {
				IPool pool = pf.newPool();
				group.addPool((IClassPool) pool);
			}
			return group;
		}
	}

}
