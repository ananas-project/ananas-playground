package ananas.remote_jar_runner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class Runner {

	private final String _url;
	private final MyRunnable _runnable;

	public Runner(String url) {
		this._url = url;
		this._runnable = new MyRunnable(this);
	}

	public static void main(String[] arg) {
		String url = null;
		if (arg != null) {
			if (arg.length > 0) {
				url = arg[0];
			}
		}
		if (url == null) {
			System.out
					.println("Use command with argument [url] to run this jar.");
		} else {
			Runner runner = new Runner(url);
			runner.startMonitor();
			runner.doUILoop();
			runner.stopMonitor();
		}
		System.out.println("This app is exited.");
	}

	private void doUILoop() {
		try {
			InputStream in = System.in;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			for (int b = in.read(); b >= 0; b = in.read()) {
				if (b == 0x0a || b == 0x0d) {
					String cmd = new String(out.toByteArray(), "UTF-8");
					out.reset();
					this.processCommand(cmd);
					if ("exit".equals(cmd)) {
						break;
					}
				} else {
					out.write(b);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processCommand(String cmd) {
		if ("exit".equals(cmd)) {
		} else {
			System.out.println("Bad command '" + cmd + "'");
		}
	}

	private void stopMonitor() {
		this._runnable.stop();
	}

	private void startMonitor() {
		this._runnable.start();
	}

	static class MyRunnable implements Runnable {

		private final Runner pthis;
		private boolean _needForStop;
		private Thread _thread;
		private TaskRunnable _curTask;

		public MyRunnable(Runner outer) {
			this.pthis = outer;
		}

		public void start() {
			Thread thd = new Thread(this);
			this._thread = thd;
			thd.start();
		}

		public void stop() {
			System.out.println("stopping the monitor ...");
			this._needForStop = true;
			try {
				this._thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			System.out.println("start monitor for the url : " + pthis._url);
			String currentSha1 = "";
			for (; !this._needForStop;) {
				byte[] data = TaskUtil.downloadFile(pthis._url);
				String sha1 = TaskUtil.calcSha1(data);
				if (!sha1.equals(currentSha1)) {
					// config changed
					System.out.println("load new config : " + sha1);
					if (this.loadNewConfig(data)) {
						currentSha1 = sha1;
						System.out.println("[success]");
					} else {
						System.out.println("[failed]");
					}
				}
				this.sleep(5000);
			}
			this.setCurrentTask(null);
		}

		private void setCurrentTask(TaskRunnable newTask) {
			TaskRunnable oldTask = this._curTask;
			this._curTask = newTask;
			if (oldTask != null) {
				oldTask.stop();
			}
			if (newTask != null) {
				newTask.start();
			}
		}

		private boolean loadNewConfig(byte[] data) {
			try {
				Properties prop = new Properties();
				prop.load(new ByteArrayInputStream(data));

				Set<Object> keys = prop.keySet();
				List<String> keyList = new ArrayList<String>();
				for (Object k : keys) {
					keyList.add(k.toString());
				}
				Collections.sort(keyList);
				for (String key : keyList) {
					String value = prop.getProperty(key);
					System.out.println("    " + key + "  =  " + value);
				}
				TaskConfig conf = new TaskConfig(prop);
				if (this.loadJar(conf)) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		private boolean loadJar(TaskConfig conf) {

			// download & check
			final String url = conf.getJarURL();
			final byte[] data = TaskUtil.downloadFile(url);
			final String sha1_1 = conf.getJarSha1().trim();
			final String sha1_2 = TaskUtil.calcSha1(data).trim();
			if (!sha1_1.equalsIgnoreCase(sha1_2)) {
				System.out.println("the sha1 of jar is not match!");
				return false;
			}

			// save to file
			String filename = sha1_1 + ".jar";
			File dir = this.getJarCacheDirectory();
			File jarfile = new File(dir, filename);
			this.saveJarDataToFile(data, jarfile);

			// load jar & run
			try {
				URL classURL = jarfile.toURI().toURL();
				URLClassLoader ldr = new URLClassLoader(new URL[] { classURL });
				InputStream in = ldr.getResourceAsStream("task.properties");
				Properties prop = new Properties();
				prop.load(in);
				in.close();
				String name = prop.getProperty(TaskConfig.key_task_class); // conf.getTaskClassName();
				Class<?> cls = ldr.loadClass(name);
				TaskRunnable task = (TaskRunnable) cls.newInstance();
				task = new MyTaskRunnableProxy(task, ldr);
				this.setCurrentTask(task);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;

		}

		private void saveJarDataToFile(byte[] data, File file) {
			try {
				System.out.println("save jar to file " + file);
				if (!file.exists()) {
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				FileOutputStream out = new FileOutputStream(file);
				out.write(data);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private File getJarCacheDirectory() {
			try {
				URL url = pthis.getClass().getProtectionDomain()
						.getCodeSource().getLocation();
				File file = new File(url.toURI());
				return new File(file.getParent(), "tmp/cache-jar");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			return new File("/tmp/cache-jar/");
		}

		private void sleep(int ms) {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	static class MyTaskRunnableProxy implements TaskRunnable {

		private final URLClassLoader _classLoader;
		private final TaskRunnable _task;

		public MyTaskRunnableProxy(TaskRunnable task, URLClassLoader ldr) {
			this._classLoader = ldr;
			this._task = task;
		}

		@Override
		public void start() {
			try {
				_task.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void stop() {
			try {
				_task.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				this._classLoader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
