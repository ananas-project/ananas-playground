package example.ananas.rjr;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
			Properties sp = System.getProperties();
			sp.putAll(prop);
			prop = sp;

			List<String> keylist = new ArrayList<String>();
			Set<Object> keyset = prop.keySet();
			for (Object k : keyset) {
				keylist.add(k.toString());
			}
			Collections.sort(keylist);
			for (String key : keylist) {
				String value = prop.getProperty(key);
				System.out.println(key + "=" + value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
