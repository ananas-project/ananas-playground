package ananas.tools.dtt.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ananas.tools.dtt.DailyRecord;
import ananas.tools.dtt.DailyTask;
import ananas.tools.dtt.DailyTimeSegment;

public class DailyTaskImpl implements DailyTask {

	private final long mTimeQuota;
	private final String mName;
	private final List<DailyTimeSegment> mSegs;
	private DailyTimeSegmentImpl mActiveSeg;
	private Long mTimeSpanCache = null;

	public DailyTaskImpl(String name, long timeQuota) {
		this.mName = name;
		this.mTimeQuota = timeQuota;
		this.mSegs = new ArrayList<DailyTimeSegment>();
	}

	@Override
	public long getTimeQuota() {
		return this.mTimeQuota;
	}

	@Override
	public String getName() {
		return this.mName;
	}

	@Override
	public Collection<DailyTimeSegment> getSegments() {
		return this.mSegs;
	}

	@Override
	public void addRecord(DailyRecord rec) {

		if (this.mName.equals(rec.getName())) {
			// start
			DailyTimeSegmentImpl seg = this.mActiveSeg;
			if (seg == null) {
				seg = new DailyTimeSegmentImpl();
				seg.setBeginTime(rec.getTimestamp());
				this.mActiveSeg = seg;
			}
		} else {
			// stop
			DailyTimeSegmentImpl seg = this.mActiveSeg;
			if (seg != null) {
				seg.setEndTime(rec.getTimestamp());
				this.mActiveSeg = null;
				this.mSegs.add(seg);
				this.mTimeSpanCache = null;
			}
		}

	}

	@Override
	public void reset() {
		this.mActiveSeg = null;
		this.mSegs.clear();
		this.mTimeSpanCache = null;
	}

	@Override
	public long getTimeSpan() {
		return this.getTimeSpan(false);
	}

	@Override
	public long getTimeSpan(boolean noIncludeActive) {
		Long span = this.mTimeSpanCache;
		if (span == null) {
			span = 0L;
			for (DailyTimeSegment seg : this.mSegs) {
				long sp = seg.getTimeSpan(true);
				if (sp < 0) {
					sp = System.currentTimeMillis() - seg.getBeginTime();
				}
				span += sp;
			}
			this.mTimeSpanCache = span;
		}
		if (!noIncludeActive) {
			DailyTimeSegment act = this.mActiveSeg;
			if (act != null) {
				span += (System.currentTimeMillis() - act.getBeginTime());
			}
		}
		return span;
	}
}
