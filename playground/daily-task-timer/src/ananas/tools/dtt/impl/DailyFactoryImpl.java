package ananas.tools.dtt.impl;

import java.util.ArrayList;
import java.util.List;

import ananas.tools.dtt.DailyContext;
import ananas.tools.dtt.DailyController;
import ananas.tools.dtt.DailyFactory;
import ananas.tools.dtt.DailyModel;
import ananas.tools.dtt.DailyTask;
import ananas.tools.dtt.dom.Ctrl_task;
import ananas.tools.dtt.dom.Ctrl_timer;

public class DailyFactoryImpl implements DailyFactory {

	private final DailyContext mContext;
	private final Ctrl_timer mConf;

	public DailyFactoryImpl(DailyContext context, Ctrl_timer conf) {
		this.mContext = context;
		this.mConf = conf;
	}

	@Override
	public DailyController newController() {
		DailyModel model = this.newModel();
		DailyController ctrl = new DailyControllerImpl(model);
		ctrl.load();
		return ctrl;
	}

	@Override
	public DailyModel newModel() {
		DailyModel model = new DailyModelImpl(this.mContext,
				this._newTasks(this.mConf));
		return model;
	}

	private List<DailyTask> _newTasks(Ctrl_timer conf) {
		List<DailyTask> list = new ArrayList<DailyTask>();
		List<Ctrl_task> tasks = conf.getTasks();
		for (Ctrl_task task : tasks) {
			String name = task.getName();
			long tq = task.getTimeQuota();
			list.add(new DailyTaskImpl(name, tq));
		}
		return list;
	}
}
