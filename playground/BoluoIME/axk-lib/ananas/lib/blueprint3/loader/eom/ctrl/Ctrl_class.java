package ananas.lib.blueprint3.loader.eom.ctrl;

import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.loader.eom.target.Tar_attribute;
import ananas.lib.blueprint3.loader.eom.target.Tar_class;
import ananas.lib.blueprint3.loader.eom.target.Tar_element;
import ananas.lib.blueprint3.loader.eom.target.Tar_text;

public class Ctrl_class extends CtrlObject implements ICtrl_class {

	@Override
	public Tar_class getTarget_class() {
		return (Tar_class) this.getTarget(true);
	}

	@Override
	public boolean set_attribute_isElement(BPAttribute attr) {
		boolean b = Boolean.parseBoolean(attr.getValue());
		this.getTarget_class().setIsElement(b);
		return true;
	}

	@Override
	public boolean append_child_attribute(Ctrl_attribute child) {
		Tar_class parent = this.getTarget_class();
		Tar_attribute attr = child.getTarget_attribute();
		parent.addElement_attribute(attr);
		return true;
	}

	@Override
	public boolean append_child_element(Ctrl_element child) {
		Tar_class parent = this.getTarget_class();
		Tar_element element = child.getTarget_element();
		parent.addElement_element(element);
		return true;
	}

	@Override
	public boolean set_attribute_name(BPAttribute attr) {
		this.getTarget_class().setName(attr.getValue());
		return true;
	}

	@Override
	public boolean set_attribute_extends(BPAttribute attr) {
		this.getTarget_class().setExtends(attr.getValue());
		return true;
	}

	@Override
	public boolean set_attribute_targetClass(BPAttribute attr) {
		this.getTarget_class().setTargetClass(attr.getValue());
		return true;
	}

	@Override
	public boolean set_attribute_controllerClass(BPAttribute attr) {
		this.getTarget_class().setControllerClass(attr.getValue());
		return true;
	}

	@Override
	public boolean set_attribute_javaName(BPAttribute attr) {
		this.getTarget_class().setJavaName(attr.getValue());
		return true;
	}

	@Override
	public boolean set_attribute_localName(BPAttribute attr) {
		this.getTarget_class().setLocalName(attr.getValue());
		return true;
	}

	@Override
	public boolean append_child_text(Ctrl_text child) {
		Tar_class parent = this.getTarget_class();
		Tar_text txt = child.getTarget_text();
		parent.addElement_text(txt);
		return true;
	}

}
