package ananas.app.ft_iso.func;

import ananas.app.ft_iso.AbstractVBoxModel;
import ananas.app.ft_iso.core.IsoValue;

public class IsoValueModel extends AbstractVBoxModel {

	private IsoValue mIso;

	public void setISO(IsoValue iso) {
		this.mIso = iso;
	}

	public IsoValue getISO() {
		return this.mIso;
	}

}
