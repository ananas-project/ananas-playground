package test.ananas.objecthub;

import java.io.InputStream;

public class TestPut {

	private final String _url;

	public TestPut() {
		_url = "http://localhost:8980/objecthub/Object";
	}

	public static void main(String[] arg) {
		try {
			(new TestPut()).put("test-1-content.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void put(String name) {

		InputStream in = this.getClass().getResourceAsStream(name);

	}
}
