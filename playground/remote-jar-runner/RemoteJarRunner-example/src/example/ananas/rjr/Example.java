package example.ananas.rjr;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class Example {

	public static void main(String[] arg) {
		Example obj = new Example();
		System.out.println("hello, world! this is " + obj);

		try {
			InputStream in = obj.getClass()
					.getResourceAsStream("my.properties");
			Properties prop = new Properties();
			prop.load(in);
			in.close();
			Enumeration<Object> keys = prop.keys();
			for (; keys.hasMoreElements();) {
				String key = keys.nextElement().toString();
				String value = prop.getProperty(key);
				System.out.println(key + "=" + value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
