package ananas.lib.impl.blueprint3.core;

import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.dom.BPElement;
import ananas.lib.blueprint3.dom.BPText;
import ananas.lib.blueprint3.lang.BPNamespace;
import ananas.lib.blueprint3.lang.BPType;

public class BPClassImpl implements BPType {

	private final Class<?> mTargetClass;
	private final Class<?> mCtrlClass;
	private final BPNamespace mNS;
	private final String mLocalName;

	public BPClassImpl(BPNamespace ns, String localName, Class<?> ctrlClass,
			Class<?> targetClass) {

		this.mTargetClass = targetClass;
		this.mCtrlClass = ctrlClass;
		this.mNS = ns;
		this.mLocalName = localName;
	}

	@Override
	public Class<?> getTargetClass() {
		return this.mTargetClass;
	}

	@Override
	public Class<?> getControllerClass() {
		return this.mCtrlClass;
	}

	@Override
	public String getLocalName() {
		return this.mLocalName;
	}

	@Override
	public BPNamespace getOwnerNamespace() {
		return this.mNS;
	}

	@Override
	public boolean setAttributeForParent(BPElement parent, BPAttribute attr) {
		throw new RuntimeException("no impl");
	}

	@Override
	public BPElement createElement(BPDocument doc) {
		throw new RuntimeException("no impl");
	}

	@Override
	public boolean appendTextToParent(BPElement parent, BPText text) {
		throw new RuntimeException("no impl");
	}

	@Override
	public boolean appendElementToParent(BPElement parent, BPElement child) {
		throw new RuntimeException("no impl");
	}

}
