package ananas.lib.servkit.pool.bp;

import ananas.lib.blueprint.IElement;
import ananas.lib.servkit.pool.IPool;
import ananas.lib.servkit.pool.IPoolFactory;

public class EPoolFactory extends EPoolObjectBase implements IEPoolFactory {

	private IEPoolFactory mTargetFactory;

	@Override
	public boolean appendChild(IElement element) {

		if (element == null) {
			return false;

		} else if (element instanceof IEPoolFactory) {
			this.mTargetFactory = (IEPoolFactory) element;

		} else {
			return super.appendChild(element);

		}
		return true;
	}

	@Override
	public IPoolFactory toPoolFactory() {
		IPoolFactory pf = this.mTargetFactory.toPoolFactory();
		return new MyPoolFactory(pf);
	}

	private static class MyPoolFactory implements IPoolFactory {

		private final IPoolFactory mPF;

		public MyPoolFactory(IPoolFactory pf) {
			this.mPF = pf;
		}

		@Override
		public IPool newPool() {
			return this.mPF.newPool();
		}
	}
}
