package ananas.lib.blueprint3.dom;

import ananas.lib.blueprint3.lang.BPType;

public class AbstractElement extends AbstractNode implements BPElement {

	private Object mTarget;
	private BPType mType;

	@Override
	public final boolean appendChild(BPNode newChild) {
		return this.onAppendChild(newChild);
	}

	@Override
	public final void tagBegin() {
		this.onTagBegin();
	}

	@Override
	public final void tagEnd() {
		this.onTagEnd();
	}

	@Override
	public final boolean setAttribute(BPAttribute attr) {
		return this.onSetAttribute(attr);
	}

	protected boolean onAppendChild(BPNode newChild) {
		if (newChild instanceof BPText) {
			return this.getType().appendTextToParent(this, (BPText) newChild);
		} else {
			return this.getType().appendElementToParent(this,
					(BPElement) newChild);
		}
	}

	protected void onTagBegin() {
	}

	protected void onTagEnd() {
	}

	protected boolean onSetAttribute(BPAttribute attr) {
		return this.getType().setAttributeForParent(this, attr);
	}

	@Override
	public BPType getType() {
		return this.mType;
	}

	@Override
	public boolean bindType(BPType type) {
		if ((this.mType == null) && (type != null)) {
			this.mType = type;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Object getTarget() {
		return this.mTarget;
	}

	@Override
	public Object getTarget(boolean create) {
		Object tar = this.mTarget;
		if ((tar == null) && (create)) {
			tar = this.createTarget();
			this.mTarget = tar;
		}
		return tar;
	}

	@Override
	public Object createTarget() {
		try {
			return this.getType().getTargetClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean bindTarget(Object target) {
		if ((this.mTarget == null) && (target != null)) {
			this.mTarget = target;
			return true;
		} else {
			return false;
		}
	}

	public String getNamespaceURI() {
		return this.getType().getOwnerNamespace().getNamespaceURI();
	}

	public String getLocalName() {
		return this.getType().getLocalName();
	}
}
