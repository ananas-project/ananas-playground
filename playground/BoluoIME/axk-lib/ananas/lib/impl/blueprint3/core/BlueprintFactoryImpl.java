package ananas.lib.impl.blueprint3.core;

import ananas.lib.blueprint3.Blueprint;
import ananas.lib.blueprint3.BlueprintFactory;

public class BlueprintFactoryImpl implements BlueprintFactory {

	@Override
	public Blueprint newBlueprint() {
		return new BlueprintImpl();
	}

}
