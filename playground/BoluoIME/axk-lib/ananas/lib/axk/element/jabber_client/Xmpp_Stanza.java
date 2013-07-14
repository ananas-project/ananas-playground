package ananas.lib.axk.element.jabber_client;

import java.util.ArrayList;
import java.util.List;

import ananas.lib.axk.XmppAddress;

public class Xmpp_Stanza {

	private final List<Object> mItems = new ArrayList<Object>();
	private XmppAddress mTo;
	private XmppAddress mFrom;
	private String mId;
	private String mType;
	private String mXmlLang;

	public void addItem(Object item) {
		this.mItems.add(item);
	}

	public List<Object> listItems() {
		return this.mItems;
	}

	public Object findItemByClass(Class<?> cls) {
		for (Object item : this.mItems) {
			if (cls.isInstance(item)) {
				return item;
			}
		}
		return null;
	}

	public void setTo(XmppAddress addr) {
		this.mTo = addr;
	}

	public void setFrom(XmppAddress addr) {
		this.mFrom = addr;
	}

	public void setType(String type) {
		this.mType = type;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public void setXmlLang(String lang) {
		this.mXmlLang = lang;
	}

	public XmppAddress getFrom() {
		return mFrom;
	}

	public String getId() {
		return mId;
	}

	public String getType() {
		return mType;
	}

	public String getXmlLang() {
		return mXmlLang;
	}

	public XmppAddress getTo() {
		return this.mTo;
	}
}
