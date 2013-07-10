package ananas.lib.blueprint3.loader.eom.ctrl;

import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.loader.eom.target.Tar_property;

public class Ctrl_property extends CtrlObject implements ICtrl_property {

	@Override
	public Tar_property getTarget_property() {
		return (Tar_property) this.getTarget(true);
	}

	@Override
	public boolean set_attribute_key(BPAttribute attr) {
		this.getTarget_property().setKey(attr.getValue());
		return true;
	}

	@Override
	public boolean set_attribute_value(BPAttribute attr) {
		this.getTarget_property().setValue(attr.getValue());
		return true;
	}

}
