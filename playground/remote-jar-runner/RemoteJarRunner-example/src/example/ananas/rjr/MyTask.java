package example.ananas.rjr;

import ananas.remote_jar_runner.TaskRunnable;

public class MyTask implements TaskRunnable {

	@Override
	public void start() {

		Example.main(null);

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
