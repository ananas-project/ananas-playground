package ananas.lib.blueprint3.element.bpbase;

public class CtrlBlueprint extends CtrlObject {

	public TarBlueprint target_blueprint() {
		return (TarBlueprint) this.getTarget(true);
	}

	public boolean append_child_Head(CtrlHead head) {
		return this.target_blueprint().setHead(head.target_head());
	}

	public boolean append_child_Body(CtrlBody body) {
		return this.target_blueprint().setBody(body.target_body());
	}
}
