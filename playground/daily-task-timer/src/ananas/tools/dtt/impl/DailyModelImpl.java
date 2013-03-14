package ananas.tools.dtt.impl;

import java.util.List;
import java.util.Vector;

import ananas.tools.dtt.DailyContext;
import ananas.tools.dtt.DailyModel;
import ananas.tools.dtt.DailyRecord;
import ananas.tools.dtt.DailyTask;

public class DailyModelImpl implements DailyModel {

	private final DailyContext mContext;
	private final List<DailyTask> mTaskList;
	private DailyTask mCurTask;
	private final DailyTask mDefaultTask;

	public DailyModelImpl(DailyContext context, List<DailyTask> tasks) {
		this.mContext = context;
		this.mTaskList = new Vector<DailyTask>(tasks);
		this.mDefaultTask = new DailyTaskImpl("[default]", 0);
	}

	@Override
	public DailyContext getContext() {
		return this.mContext;
	}

	@Override
	public void addRecord(DailyRecord rec) {
		DailyTask pnew = this._findTask(rec.getName());
		DailyTask pold;
		synchronized (this) {
			pold = this.mCurTask;
			this.mCurTask = pnew;
		}
		if (pold != null) {
			pold.addRecord(rec);
		}
		if (pnew != null) {
			pnew.addRecord(rec);
		}
	}

	private DailyTask _findTask(String name) {
		for (DailyTask task : this.mTaskList) {
			if (name.equals(task.getName())) {
				return task;
			}
		}
		return this.mDefaultTask;
	}

	@Override
	public List<DailyTask> getTasks() {
		return this.mTaskList;
	}

	@Override
	public DailyTask getCurrentTask() {
		return this.mCurTask;
	}

	@Override
	public void reset() {
		this.mCurTask = null;
		for (DailyTask task : this.mTaskList) {
			task.reset();
		}
	}

	@Override
	public DailyTask getTask(String taskName) {
		return this._findTask(taskName);
	}

}
