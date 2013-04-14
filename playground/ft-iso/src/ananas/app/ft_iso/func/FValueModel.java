package ananas.app.ft_iso.func;

import ananas.app.ft_iso.AbstractVBoxModel;
import ananas.app.ft_iso.core.FValue;

public class FValueModel extends AbstractVBoxModel {

	private FValue mF;

	public void setF(FValue f) {
		this.mF = f;
	}

	public FValue getF() {
		return this.mF;
	}

}
