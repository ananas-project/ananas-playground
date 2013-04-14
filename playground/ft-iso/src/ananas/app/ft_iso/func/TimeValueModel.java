package ananas.app.ft_iso.func;

import ananas.app.ft_iso.AbstractVBoxModel;
import ananas.app.ft_iso.core.TimeValue;

public class TimeValueModel extends AbstractVBoxModel {

	private TimeValue mTime;

	public void setTime(TimeValue time) {
		this.mTime = time;
	}

	public TimeValue getTime() {
		return this.mTime;
	}

}
