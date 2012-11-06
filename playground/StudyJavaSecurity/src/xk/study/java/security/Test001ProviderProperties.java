package xk.study.java.security;

import java.security.Provider;
import java.util.Properties;

public class Test001ProviderProperties implements Runnable {

	public Test001ProviderProperties(String[] args) {
	}

	@Override
	public void run() {
		Provider[] ps = java.security.Security.getProviders();
		for (Provider p : ps) {
			System.out
					.println("=========================================================");
			System.out.println(p);
			p.list(System.out);

		}

		try {
			System.out.println();
			System.out
					.println("=========================================================");
			System.out.println("a test of " + Properties.class);
			Properties ppt = new Properties();
			ppt.setProperty("hello", "world");
			ppt.list(System.out);
			ppt.store(System.out, "[some comments]");
			ppt.storeToXML(System.out, "[some comments]", "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
