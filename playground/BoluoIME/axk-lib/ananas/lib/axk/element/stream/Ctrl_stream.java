package ananas.lib.axk.element.stream;

import ananas.lib.axk.element.XmppObject;
import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.dom.BPElement;

public class Ctrl_stream extends XmppObject {

	private Xmpp_stream mTargetStream;

	public boolean set_attribute_from(BPAttribute attr) {
		return true;
	}

	public boolean set_attribute_id(BPAttribute attr) {
		return true;
	}

	public boolean set_attribute_to(BPAttribute attr) {
		return true;
	}

	public boolean set_attribute_version(BPAttribute attr) {
		return true;
	}

	public boolean set_attribute_lang(BPAttribute attr) {
		return true;
	}

	public boolean append_child_(BPElement child) {
		Object rx = child.getTarget(true);
		this.getTarget_stream().dispatchRxObject(rx);
		return true;
	}

	public Xmpp_stream getTarget_stream() {
		Xmpp_stream tarStream = this.mTargetStream;
		if (tarStream == null) {
			tarStream = (Xmpp_stream) this.getTarget(true);
			this.mTargetStream = tarStream;
		}
		return tarStream;
	}

}
