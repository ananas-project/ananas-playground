package ananas.lib.impl.axk.client.ctrl;

import ananas.lib.blueprint3.dom.AbstractElement;
import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.dom.BPElementMap;
import ananas.lib.impl.axk.client.target.Tar_abstractClient;

public class Ctl_abstractClient extends AbstractElement {

	private String mId;

	public boolean append_child_(Ctl_abstractClient child) {
		Tar_abstractClient p1 = this.getTarget_ac();
		Tar_abstractClient c1 = child.getTarget_ac();
		p1.addTarget(c1);
		return true;
	}

	public boolean set_attribute_id(BPAttribute attr) {
		this.mId = attr.getValue();
		return true;
	}

	public void onTagBegin() {
		super.onTagBegin();
		String id = this.mId;
		if (id != null) {
			BPDocument doc = this.getOwnerDocument();
			BPElementMap reg = doc.getElementRegistrar();
			reg.put(id, this);
		}
	}

	public Tar_abstractClient getTarget_ac() {
		return (Tar_abstractClient) this.getTarget(true);
	}

}
