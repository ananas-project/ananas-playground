package ananas.lib.blueprint3.loader.eom.ctrl;

import ananas.lib.blueprint3.loader.eom.target.Tar_text;

public class Ctrl_text extends CtrlObject implements ICtrl_text {

	public Ctrl_text() {
	}

	@Override
	public Tar_text getTarget_text() {
		return (Tar_text) this.getTarget(true);
	}

}
