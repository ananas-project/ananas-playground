package ananas.lib.blueprint3.loader.eom.target;

import java.util.ArrayList;
import java.util.List;

public class Tar_class extends TargetBase {

	final List<Tar_attribute> mAttrList = new ArrayList<Tar_attribute>();
	final List<Tar_element> mElementList = new ArrayList<Tar_element>();
	Tar_text mText = null;

	private boolean mIsElement = false;
	private String mName;
	private String mExtends;

	private String mControllerClassName;
	private String mTargetClassName;

	private String mJavaName;
	private String mLocalName;
	private Tar_namespace mParent;

	public Tar_namespace getOwnerNS() {
		return this.mParent;
	}

	public void setIsElement(boolean value) {
		this.mIsElement = value;
	}

	public void addElement_element(Tar_element element) {
		this.mElementList.add(element);
	}

	public void setName(String name) {
		this.mName = name;
	}

	public void addElement_attribute(Tar_attribute attr) {
		this.mAttrList.add(attr);
	}

	public void setExtends(String value) {
		this.mExtends = value;
	}

	public String getName() {
		return this.mName;
	}

	public String getJavaName() {
		String name = this.mJavaName;
		if (name == null) {
			name = this.getName();
			name = this.toJavaName(name);
			this.mJavaName = name;
		}
		return name;
	}

	private String toJavaName(String name) {
		StringBuilder sb = new StringBuilder();
		for (char ch : name.toCharArray()) {
			char ch2;
			if ('0' <= ch && ch <= '9') {
				ch2 = ch;
			} else if ('a' <= ch && ch <= 'z') {
				ch2 = ch;
			} else if ('A' <= ch && ch <= 'Z') {
				ch2 = ch;
			} else {
				ch2 = '_';
			}
			sb.append(ch2);
		}
		return sb.toString();
	}

	public String getLocalName() {
		String name = this.mLocalName;
		if (name == null) {
			name = this.getName();
			this.mLocalName = name;
		}
		return name;
	}

	public void setTargetClass(String value) {
		this.mTargetClassName = value;
	}

	public void setControllerClass(String value) {
		this.mControllerClassName = value;
	}

	public boolean isElement() {
		return mIsElement;
	}

	public String getExtends() {
		return mExtends;
	}

	public String getControllerClass() {
		return mControllerClassName;
	}

	public String getTargetClass() {
		return mTargetClassName;
	}

	public List<Tar_attribute> listAttributes() {
		return this.mAttrList;
	}

	public List<Tar_element> listChildren() {
		return this.mElementList;
	}

	public void setJavaName(String value) {
		this.mJavaName = value;
	}

	public void setLocalName(String value) {
		this.mLocalName = value;
	}

	@Override
	public ITargetNode[] getChildren() {

		List<ITargetNode> list = new ArrayList<ITargetNode>(
				this.mAttrList.size() + this.mElementList.size());

		list.addAll(this.mAttrList);
		list.addAll(this.mElementList);
		Tar_text txt = this.mText;
		if (txt != null) {
			list.add(txt);
		}

		return list.toArray(new ITargetNode[list.size()]);
	}

	@Override
	public void setParent(ITargetNode node) {
		this.mParent = (Tar_namespace) node;
	}

	public ITargetNode getParent() {
		return this.mParent;
	}

	public void addElement_text(Tar_text txt) {
		this.mText = txt;
	}
}
