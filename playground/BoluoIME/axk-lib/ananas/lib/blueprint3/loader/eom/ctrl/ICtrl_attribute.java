package ananas.lib.blueprint3.loader.eom.ctrl;

import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.loader.eom.target.Tar_attribute;

public interface ICtrl_attribute {

	boolean set_attribute_name(BPAttribute attr);

	// other

	Tar_attribute getTarget_attribute();

}
