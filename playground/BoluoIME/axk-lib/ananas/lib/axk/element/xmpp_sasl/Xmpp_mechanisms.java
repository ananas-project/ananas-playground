package ananas.lib.axk.element.xmpp_sasl;

import java.util.ArrayList;
import java.util.List;

public class Xmpp_mechanisms {

	final List<Xmpp_mechanism> mList = new ArrayList<Xmpp_mechanism>();

	public void add(Xmpp_mechanism mechanism) {
		this.mList.add(mechanism);
	}

	public List<Xmpp_mechanism> listItems() {
		return this.mList;
	}

}
