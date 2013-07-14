package ananas.lib.blueprint3.loader.eom.ctrl;

import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.loader.eom.target.Tar_namespace;

public interface ICtrl_namespace {

	// attribute

	boolean set_attribute_uri(BPAttribute attr);

	boolean set_attribute_enableExport(BPAttribute attr);

	// child

	boolean append_child_property(Ctrl_property prop);

	boolean append_child_class(Ctrl_class aClass);

	// other

	Tar_namespace getTarget_namespace();
}
