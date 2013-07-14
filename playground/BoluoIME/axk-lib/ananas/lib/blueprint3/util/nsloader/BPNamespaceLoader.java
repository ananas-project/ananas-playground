package ananas.lib.blueprint3.util.nsloader;

import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.blueprint3.lang.BlueprintException;

public interface BPNamespaceLoader {

	void load(BPEnvironment environment, BPNamespaceInfo info)
			throws BlueprintException;

}
