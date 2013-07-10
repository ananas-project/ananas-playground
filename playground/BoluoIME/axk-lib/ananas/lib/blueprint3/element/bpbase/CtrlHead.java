package ananas.lib.blueprint3.element.bpbase;

public class CtrlHead extends CtrlObject {

	public boolean append_child_Import(CtrlImport imp) {
		return this.target_head().addImport(imp.target_import());
	}

	public TarHead target_head() {
		return (TarHead) this.getTarget(true);
	}
}
