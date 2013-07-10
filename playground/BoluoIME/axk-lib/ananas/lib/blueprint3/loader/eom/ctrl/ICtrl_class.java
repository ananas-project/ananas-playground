package ananas.lib.blueprint3.loader.eom.ctrl;

import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.loader.eom.target.Tar_class;

public interface ICtrl_class {

	// attributes

	boolean set_attribute_isElement(BPAttribute attr);

	boolean set_attribute_name(BPAttribute attr);

	boolean set_attribute_javaName(BPAttribute attr);

	boolean set_attribute_localName(BPAttribute attr);

	boolean set_attribute_extends(BPAttribute attr);

	boolean set_attribute_targetClass(BPAttribute attr);

	boolean set_attribute_controllerClass(BPAttribute attr);

	// children

	boolean append_child_attribute(Ctrl_attribute child);

	boolean append_child_element(Ctrl_element child);

	boolean append_child_text(Ctrl_text child);

	// other

	Tar_class getTarget_class();

}
