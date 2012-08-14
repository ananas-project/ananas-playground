package ananas.lib.blueprint;

import ananas.lib.blueprint.impl.FinalBlueprintImpl;

public abstract class Blueprint implements IBlueprint {

	private static final Blueprint sInst;

	static {
		sInst = new FinalBlueprintImpl();
	}

	public static Blueprint getInstance() {
		return sInst;
	}

}
