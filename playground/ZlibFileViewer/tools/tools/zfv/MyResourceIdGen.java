package tools.zfv;

public class MyResourceIdGen {

	/**
	 * @param arg
	 *            in command-line call this with the parameter of
	 *            "-base-dir ${project_loc} -res-dir res -gen-dir gen -R-class ananas.app.zlibfileviewer.gui.R -accept-file .xml+.png -accept-attr id+command"
	 * */

	public static void main(String[] arg) {
		ananas.lib.blueprint2.tools.ResourceIdGen.main(arg);
	}

}
