package ananas.tools.dtt;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Properties;

public class Main {

	public static void main(String arg[]) {

		(new Main()).run();

	}

	private void run() {

		System.out.println("===============================================");
		System.out.println(this + ".appBegin");

		Properties prop = new Properties();
		prop.setProperty(Const.key_file_conf, getFilePath("config.xml"));
		prop.setProperty(Const.key_file_current, getFilePath("current.xml"));

		DailyContext context = DailyContext.Factory.getDefault(prop);
		DailyController ctrl = context.getFactory().newController();
		DailyModel model = ctrl.getModel();

		ctrl.reset();

		Collection<DailyTask> tasks = model.getTasks();
		for (DailyTask task : tasks) {
			this.printTask(task);
		}
		{
			for (DailyTask task : tasks) {
				this.costSomeTime(ctrl, task);
			}
		}

		// ctrl.selectTask("");

		for (DailyTask task : tasks) {
			this.printTask(task);
		}

		System.out.println(this + ".appEnd");

	}

	private void printTask(DailyTask task) {

		StringBuilder sb = new StringBuilder();

		sb.append("[task name:" + task.getName());
		sb.append(" span/quota:" + task.getTimeSpan());
		sb.append("/" + task.getTimeQuota());
		sb.append("]");

		System.out.println("    " + sb.toString());
	}

	private String getFilePath(String name) {
		URL url = this.getClass().getProtectionDomain().getCodeSource()
				.getLocation();
		File dir = new File(url.getPath());
		dir = new File(dir.getParentFile(), "doc");
		return (new File(dir, name)).getAbsolutePath();
	}

	private void costSomeTime(DailyController ctrl, DailyTask task) {

		long ms = System.currentTimeMillis() & 0x00ff;
		System.out.println("    set current as:" + task);
		// ctrl.addRecord(new DefaultRecord(ms, task.getName()));
		ctrl.selectTask(task.getName());

		ms *= 100;

		try {
			System.out.println("    sleep " + ms + " ms");
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
