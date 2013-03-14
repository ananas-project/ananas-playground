package ananas.tools.dtt.impl;

import ananas.tools.dtt.DailyTimeSegment;

public class DailyTimeSegmentImpl implements DailyTimeSegment {

	private long mBeginTime;
	private long mEndTime;

	@Override
	public long getBeginTime() {
		return this.mBeginTime;
	}

	@Override
	public long getEndTime() {
		return this.mEndTime;
	}

	public void setBeginTime(long time) {
		this.mBeginTime = time;
	}

	public void setEndTime(long time) {
		this.mEndTime = time;
	}

	@Override
	public long getTimeSpan() {
		return this.getTimeSpan(false);
	}

	@Override
	public long getTimeSpan(boolean enableNagtive) {
		long sp = this.mEndTime - this.mBeginTime;
		if (sp < 0)
			sp = 0;
		return sp;
	}

}
