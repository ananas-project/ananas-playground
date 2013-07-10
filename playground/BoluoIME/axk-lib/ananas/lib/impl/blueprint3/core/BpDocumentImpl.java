package ananas.lib.impl.blueprint3.core;

import java.net.URI;

import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.dom.BPDocumentGroup;
import ananas.lib.blueprint3.dom.BPElement;
import ananas.lib.blueprint3.dom.BPElementMap;
import ananas.lib.blueprint3.dom.BPNode;
import ananas.lib.blueprint3.dom.BPText;
import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.blueprint3.lang.BPNamespace;
import ananas.lib.blueprint3.lang.BPType;

public class BpDocumentImpl implements BPDocument {

	private final BPDocumentGroup mGroup;
	private final URI mDocURI;
	private final BPEnvironment mEnvironment;
	private BPElement mRoot;
	private BPElementMap mElementReg;
	private String mDocType;

	public BpDocumentImpl(BPDocumentGroup group, URI uri) {
		this.mGroup = group;
		this.mEnvironment = group.getEnvironment();
		this.mDocURI = uri;
	}

	@Override
	public boolean appendChild(BPNode newChild) {
		if (newChild instanceof BPElement) {
			this.setRootElement((BPElement) newChild);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeChild(BPNode theChild) {
		if (theChild != null) {
			if (theChild.equals(this.mRoot)) {
				this.mRoot = null;
				return true;
			}
		}
		return false;
	}

	@Override
	public void setParent(BPNode newParent) {
	}

	@Override
	public BPNode getParent() {
		return null;
	}

	@Override
	public BPDocument getOwnerDocument() {
		return null;
	}

	@Override
	public boolean bindOwnerDocument(BPDocument ownerDoc) {
		return false;
	}

	@Override
	public BPElement getRootElement() {
		return this.mRoot;
	}

	@Override
	public URI getDocumentURI() {
		return this.mDocURI;
	}

	@Override
	public BPText createText(String data) {
		return new BpTextImpl(data);
	}

	private BPElement _createElement(String uri, String localName) {

		BPNamespace pkg = this.mEnvironment.getNamespaceRegistrar()
				.getNamespace(uri);

		if (pkg == null) {
			System.err.println("No namespace!");
			System.err.println("    namespaceURI:" + uri);
			System.err.println("       loaclName:" + localName);
			return null;
		}
		BPType bpcls = pkg.getType(localName);
		if (bpcls == null) {
			System.err.println("No class!");
			System.err.println("    namespaceURI:" + uri);
			System.err.println("       loaclName:" + localName);
			return null;
		}
		Class<?> cls = bpcls.getControllerClass();
		try {
			BPElement node = (BPElement) cls.newInstance();
			node.bindType(bpcls);
			node.bindOwnerDocument(this);
			return node;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public BPElement createElement(String uri, String localName) {
		return this._createElement(uri, localName);
	}

	@Override
	public BPElement findElementById(String id) {
		BPElementMap ereg = this.mElementReg;
		if (ereg == null) {
			return null;
		} else {
			return ereg.get(id);
		}
	}

	@Override
	public BPElement findElementByURI(String uri) {
		BPElementMap ereg = this.mElementReg;
		if (ereg == null) {
			return null;
		} else {
			URI urx = URI.create(uri);
			String id = urx.getFragment();
			return ereg.get(id);
		}
	}

	@Override
	public Object findTargetById(String id) {
		BPElement ele = this.findElementById(id);
		if (ele == null) {
			return null;
		} else {
			return ele.getTarget(true);
		}
	}

	@Override
	public Object findTargetByURI(String uri) {
		BPElement ele = this.findElementByURI(uri);
		if (ele == null) {
			return null;
		} else {
			return ele.getTarget(true);
		}
	}

	@Override
	public BPElementMap getElementRegistrar() {
		BPElementMap reg = this.mElementReg;
		if (reg == null) {
			this.mElementReg = reg = new BPElementMapImpl();
		}
		return reg;
	}

	@Override
	public void setElementRegistrar(BPElementMap reg) {
		if (reg != null) {
			this.mElementReg = reg;
		}
	}

	@Override
	public BPAttribute createAttribute(String uri, String localName,
			String value) {

		return new BpAttrImpl(uri, localName, value);
	}

	@Override
	public String getLocalName() {
		return null;
	}

	@Override
	public String getNamespaceURI() {
		return null;
	}

	@Override
	public void setRootElement(BPElement root) {
		this.mRoot = root;
	}

	@Override
	public String getDocumentType() {
		return this.mDocType;
	}

	@Override
	public void setDocumentType(String type) {
		this.mDocType = type;
	}

	@Override
	public BPDocumentGroup getDocumentGroup() {
		return this.mGroup;
	}

}
