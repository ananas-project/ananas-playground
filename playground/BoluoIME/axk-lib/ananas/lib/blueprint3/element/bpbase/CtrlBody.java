package ananas.lib.blueprint3.element.bpbase;

import ananas.lib.blueprint3.dom.BPElement;

public class CtrlBody extends CtrlObject {

	public TarBody target_body() {
		return (TarBody) this.getTarget(true);
	}

	public boolean append_child_(Object ch) {

		if (ch instanceof BPElement) {
			BPElement ch2 = (BPElement) ch;
			this.target_body().add(ch2.getTarget(true));
			return true;
		} else {
			return false;
		}
	}
}
