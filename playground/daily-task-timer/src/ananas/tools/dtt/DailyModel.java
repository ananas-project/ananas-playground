package ananas.tools.dtt;

import java.util.List;

public interface DailyModel extends DailyRecorder {

	DailyContext getContext();

	List<DailyTask> getTasks();

	DailyTask getCurrentTask();

	DailyTask getTask(String taskName);

}
