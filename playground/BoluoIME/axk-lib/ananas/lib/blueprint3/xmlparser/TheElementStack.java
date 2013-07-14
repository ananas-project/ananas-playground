package ananas.lib.blueprint3.xmlparser;

public class TheElementStack implements INamespaceMapper {

	public final static String defaultPrefix = "[default]";

	public static int parseQName(String qName) {
		return qName.indexOf(':');
	}

	public static String parseQNameGetPrefix(String qName, int pos) {
		if (pos < 0) {
			return "";
		} else {
			return qName.substring(0, pos);
		}
	}

	public static String parseQNameGetLocalName(String qName, int pos) {
		if (pos < 0) {
			return qName;
		} else {
			return qName.substring(pos + 1);
		}
	}

	class ItemElement {

		ItemElement parent;
		ItemNS pNS;
		String qName;
	}

	ItemElement mCurElement;

	class ItemNS {

		ItemNS parent;
		private String mURI;
		private String mPrefix;

		public void setQName(String qName) {
			if (qName.equals("xmlns")) {
				this.mPrefix = defaultPrefix;
			} else {
				int pos = TheElementStack.parseQName(qName);
				this.mPrefix = TheElementStack.parseQNameGetLocalName(qName,
						pos);
			}
		}

		public void setURI(String uri) {
			this.mURI = uri;
		}
	}

	public TheElementStack(int i) {

	}

	public void reset() {
		this.mCurElement = null;
	}

	public void pushElement(String qName) {
		ItemElement ele = this.newElement();
		ele.parent = this.mCurElement;
		ele.qName = qName;
		this.mCurElement = ele;
		if (ele.parent != null) {
			ele.pNS = ele.parent.pNS;
		}
	}

	private ItemElement newElement() {
		return new ItemElement();
	}

	/**
	 * @param qName
	 *            : must be format of 'xmlns:xxx'
	 * */
	public void pushNS(String qName, String uri) {
		ItemNS ns = this.newNS();
		ns.parent = this.mCurElement.pNS;
		this.mCurElement.pNS = ns;
		ns.setQName(qName);
		ns.setURI(uri);
	}

	private ItemNS newNS() {
		return new ItemNS();
	}

	@Override
	public String prefixToURI(String prefix) {

		if (prefix == null) {
			prefix = defaultPrefix;
		} else {
			if (prefix.length() <= 0) {
				prefix = defaultPrefix;
			}
		}
		ItemNS ns = this.mCurElement.pNS;
		for (; ns != null; ns = ns.parent) {
			if (prefix.equals(ns.mPrefix)) {
				return ns.mURI;
			}
		}
		return null;
	}

	@Override
	public String findUriByQName(String qName) {
		int pos = TheElementStack.parseQName(qName);
		String prefix = TheElementStack.parseQNameGetPrefix(qName, pos);
		return this.prefixToURI(prefix);
	}

	/**
	 * @return qName
	 * */
	public String popElement() {
		ItemElement ele = this.mCurElement;
		this.mCurElement = ele.parent;
		return ele.qName;
	}

}
