package ananas.lib.blueprint3.loader.eom.ctrl;

import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.loader.eom.target.Tar_property;

public interface ICtrl_property {

	boolean set_attribute_key(BPAttribute attr);

	boolean set_attribute_value(BPAttribute attr);

	// other

	Tar_property getTarget_property();

}
