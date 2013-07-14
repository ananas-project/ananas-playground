package ananas.lib.blueprint3.xmlparser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

public class TheAttributeList implements Attributes {

	class Item {

		private int mIndex;
		private String mValue;
		private String mQName;
		private String mURI;
		private String mLocalName;

		public void reset() {
			this.mIndex = 0;
			this.mQName = null;
			this.mURI = null;
			this.mLocalName = null;
			this.mValue = null;
		}

		public void setValue(String value) {
			this.mValue = value;
		}

		public void setQName(String qname) {
			this.mQName = qname;
		}

		public void setIndex(int index) {
			this.mIndex = index;
		}

		public String getURI() {
			return this.mURI;
		}

		public String getLocalName() {
			return this.mLocalName;
		}

		public int getIndex() {
			return this.mIndex;
		}

		public String getQName() {
			return this.mQName;
		}

		public String getValue() {
			return this.mValue;
		}

		public void doNSMapping(INamespaceMapper nsMap) {

			int pos = TheElementStack.parseQName(mQName);
			String prefix = TheElementStack.parseQNameGetPrefix(mQName, pos);
			this.mLocalName = TheElementStack.parseQNameGetLocalName(mQName,
					pos);
			if (prefix.length() <= 0) {
				this.mURI = "";
			} else {
				this.mURI = nsMap.prefixToURI(prefix);
			}
		}

	}

	private final List<Item> mList = new ArrayList<Item>();

	// private final int mInitSize;

	public TheAttributeList(int initSize) {
		// this.mInitSize = initSize;
	}

	public void reset() {
		this.mList.clear();
	}

	public void addAttr(String qname, String value) {
		Item item = this.newItem();
		item.setQName(qname);
		item.setValue(value);
		item.setIndex(this.mList.size());
		this.mList.add(item);
	}

	private Item newItem() {
		// TODO : need a pool for better performer
		Item item = new Item();
		item.reset();
		return item;
	}

	@Override
	public int getIndex(String qName) {
		for (Item item : this.mList) {
			if (qName.equals(item.getQName())) {
				return item.getIndex();
			}
		}
		return -1;
	}

	@Override
	public int getIndex(String uri, String localName) {
		for (Item item : this.mList) {
			if ((uri.equals(item.getURI()))
					&& (localName.equals(item.getLocalName()))) {
				return item.getIndex();
			}
		}
		return -1;
	}

	@Override
	public int getLength() {
		return this.mList.size();
	}

	@Override
	public String getLocalName(int index) {
		return this.mList.get(index).getLocalName();
	}

	@Override
	public String getQName(int index) {
		return this.mList.get(index).getQName();
	}

	@Override
	public String getType(int index) {
		return null;
	}

	@Override
	public String getType(String qName) {
		return null;
	}

	@Override
	public String getType(String uri, String localName) {
		return null;
	}

	@Override
	public String getURI(int index) {
		return this.mList.get(index).getURI();
	}

	@Override
	public String getValue(int index) {
		return this.mList.get(index).getValue();
	}

	@Override
	public String getValue(String qName) {
		int index = this.getIndex(qName);
		if (index < 0)
			return null;
		return this.getValue(index);
	}

	@Override
	public String getValue(String uri, String localName) {
		int index = this.getIndex(uri, localName);
		if (index < 0)
			return null;
		return this.getValue(index);
	}

	public void doNSMapping(INamespaceMapper nsMap) {
		for (Item item : this.mList) {
			item.doNSMapping(nsMap);
		}
	}

}
