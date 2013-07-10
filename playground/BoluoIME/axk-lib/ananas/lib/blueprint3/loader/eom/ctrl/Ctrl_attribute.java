package ananas.lib.blueprint3.loader.eom.ctrl;

import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.loader.eom.target.Tar_attribute;

public class Ctrl_attribute extends CtrlObject implements ICtrl_attribute {

	@Override
	public Tar_attribute getTarget_attribute() {
		return (Tar_attribute) this.getTarget(true);
	}

	@Override
	public boolean set_attribute_name(BPAttribute attr) {
		this.getTarget_attribute().setName(attr.getValue());
		return true;
	}

}
