package ananas.lib.blueprint3.loader.eom.ctrl;

import ananas.lib.blueprint3.loader.eom.target.Tar_eom;

public class Ctrl_eom extends CtrlObject implements ICtrl_eom {

	@Override
	public Tar_eom getTarget_eom() {
		return (Tar_eom) this.getTarget(true);
	}

	@Override
	public boolean append_child_namespace(Ctrl_namespace ns) {
		this.getTarget_eom().addNamespace(ns.getTarget_namespace());
		return true;
	}

}
