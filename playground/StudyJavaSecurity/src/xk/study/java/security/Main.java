package xk.study.java.security;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Main {

	public static void main(String[] args) {

		// prepare
		boolean skipOuts = false;
		if (skipOuts) {
			System.setOut(new PrintStream(new OutputStream() {

				@Override
				public void write(int b) throws IOException {
				}
			}));
		}

		// testings
		(new Test001ProviderProperties(args)).run();
	}

}
