package ananas.lib.axk.element.iq_roster;

import java.util.ArrayList;
import java.util.List;

public class Xmpp_query {

	private List<Xmpp_item> mList = new ArrayList<Xmpp_item>();

	public List<Xmpp_item> listItems() {
		return this.mList;
	}

	public void addItem(Xmpp_item item) {
		this.mList.add(item);
	}

}
