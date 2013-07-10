package ananas.lib.blueprint3.dom;

import ananas.lib.blueprint3.lang.BPController;

public interface BPElement extends BPNode, BPController {

	boolean setAttribute(BPAttribute attr);

	void tagBegin();

	void tagEnd();

}
