package ananas.lib.servkit.pool.bp;

import ananas.lib.servkit.pool.IClassPoolFactory;
import ananas.lib.servkit.pool.IPool;
import ananas.lib.servkit.pool.IPoolFactory;

public class EPoolPool extends EPoolObjectBase implements IEPoolFactory {

	private static final Object attr_resetable = "resetable";
	private static final Object attr_size = "size";
	private static final Object attr_item_class = "itemClass";

	private boolean mResetable;
	private int mSize;
	private IClassPoolFactory mIClassPoolFactory;
	private String mItemClassRef;
	private Class<?> mItemClass;

	@Override
	public boolean setAttribute(String nsURI, String name, String value) {
		if (name == null) {
			return false;
		} else if (name.equals(attr_size)) {
			this.mSize = this.parseInt(value);
		} else if (name.equals(attr_item_class)) {
			this.mItemClassRef = value;
		} else if (name.equals(attr_resetable)) {
			this.mResetable = this.parseBoolean(value);
		} else {
			return super.setAttribute(nsURI, name, value);
		}
		return true;
	}

	private int parseInt(String value) {
		return Integer.parseInt(value);
	}

	private boolean parseBoolean(String value) {
		if (value == null) {
			return false;
		} else if (value.equalsIgnoreCase("yes")) {
		} else if (value.equalsIgnoreCase("true")) {
		} else if (value.equals("1")) {
		} else {
			return false;
		}
		return true;
	}

	private Class<?> getItemClass() {
		Class<?> cls = this.mItemClass;
		if (cls == null) {
			String clsRef = this.mItemClassRef;
			clsRef.hashCode();// anti null
			cls = this.findClass(clsRef);
			this.mItemClass = cls;
		}
		return cls;
	}

	protected IClassPoolFactory getIClassPoolFactory() {
		IClassPoolFactory ret = this.mIClassPoolFactory;
		if (ret == null) {
			Class<?> cls = this.findClass(null);
			try {
				ret = (IClassPoolFactory) cls.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			this.mIClassPoolFactory = ret;
		}
		return ret;
	}

	@Override
	public IPoolFactory toPoolFactory() {

		Class<?> itemClass = this.getItemClass();
		int size = this.mSize;
		boolean resetable = this.mResetable;
		IClassPoolFactory cpf = this.getIClassPoolFactory();

		return new MyPoolFactory(cpf, itemClass, size, resetable);
	}

	private static class MyPoolFactory implements IPoolFactory {

		private final IClassPoolFactory mCPF;
		private final int mSize;
		private final boolean mResetable;
		private final Class<?> mItemClass;

		public MyPoolFactory(IClassPoolFactory cpf, Class<?> itemClass,
				int size, boolean resetable) {
			this.mCPF = cpf;
			this.mItemClass = itemClass;
			this.mSize = size;
			this.mResetable = resetable;
		}

		@Override
		public IPool newPool() {
			return this.mCPF.newPool(this.mItemClass, this.mSize,
					this.mResetable);
		}
	}

}
