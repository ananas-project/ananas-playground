package ananas.lib.blueprint3.util;

import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.dom.BPElement;

public interface BPBuilderFactory {

	BPBuilder newBuilder(BPDocument doc);

	BPBuilder newBuilder(BPElement element);

}
