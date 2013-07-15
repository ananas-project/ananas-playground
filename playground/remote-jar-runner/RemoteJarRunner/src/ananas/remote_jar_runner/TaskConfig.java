package ananas.remote_jar_runner;

import java.util.Properties;

public class TaskConfig {

	public static final String key_jar_url = "jar.url";
	public static final String key_jar_sha1 = "jar.sha1";
	public static final String key_task_class = "task.class";

	private final Properties _prop;

	public TaskConfig(Properties prop) {
		this._prop = prop;
	}

	public String getTaskClassName() {
		return _prop.getProperty(key_task_class);
	}

	public String getJarURL() {
		return _prop.getProperty(key_jar_url);
	}

	public String getJarSha1() {
		return _prop.getProperty(key_jar_sha1);
	}

}
