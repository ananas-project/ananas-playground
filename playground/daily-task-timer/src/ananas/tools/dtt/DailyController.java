package ananas.tools.dtt;

public interface DailyController extends DailyRecorder {

	DailyModel getModel();

	void load();

	DailyTask selectTask(String taskName);

}
