package ananas.tools.dtt.dom;

import java.util.List;
import java.util.Vector;

public class Ctrl_timer extends AbstractDttObject {

	private final List<Ctrl_task> mTaskList;

	public Ctrl_timer() {
		this.mTaskList = new Vector<Ctrl_task>();
	}

	public boolean append_child_task(Ctrl_task task) {
		this.mTaskList.add(task);
		return true;
	}

	public List<Ctrl_task> getTasks() {
		return this.mTaskList;
	}

}
