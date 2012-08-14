package ananas.lib.blueprint;

public class DefaultElement implements IElement {

	private IDocument mOwnerDoc;
	private IClass mBpClass;
	private String mId;
	private IElement mParent;
	private Object mTarget;

	public DefaultElement() {
	}

	@Override
	public Object getTarget() {
		return this.mTarget;
	}

	@Override
	public IDocument getOwnerDocument() {
		return this.mOwnerDoc;
	}

	@Override
	public final boolean bindOwnerDocument(IDocument doc) {
		if (doc != null && this.mOwnerDoc == null) {
			this.mOwnerDoc = doc;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public IClass getBlueprintClass() {
		return this.mBpClass;
	}

	@Override
	public final boolean bindBlueprintClass(IClass bpClass) {
		if (bpClass != null && this.mBpClass == null) {
			this.mBpClass = bpClass;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean setAttribute(String nsURI, String name, String value) {
		if ("id".equals(name)) {
			this.mId = value;
			this.mOwnerDoc.registerElement(this);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean appendText(String text) {
		return false;
	}

	@Override
	public boolean appendChild(IElement element) {
		return false;
	}

	@Override
	public String getId() {
		return this.mId;
	}

	@Override
	public void tagBegin() {
	}

	@Override
	public void tagEnd() {
		this.getTarget(true);
	}

	@Override
	public IElement setParent(IElement parent) {
		IElement old;
		synchronized (this) {
			old = this.mParent;
			this.mParent = parent;
		}
		return old;
	}

	@Override
	public IElement getParent() {
		return this.mParent;
	}

	@Override
	public final boolean bindTarget(Object target) {
		if (target != null && this.mTarget == null) {
			this.mTarget = target;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Object createTarget() {
		Object obj = null;
		try {
			obj = this.getBlueprintClass().getTargetClass().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	protected final Object getTarget(boolean createIfNull) {
		Object target = this.mTarget;
		if (target == null && createIfNull) {
			target = this.createTarget();
			this.bindTarget(target);
		}
		return target;
	}

}
