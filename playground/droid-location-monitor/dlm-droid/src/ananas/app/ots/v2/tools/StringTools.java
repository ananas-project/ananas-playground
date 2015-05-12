package ananas.app.ots.v2.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class StringTools {

	public static void saveString(String string, String enc, File file,
			boolean mkdirs) throws IOException {
		Writer wtr = null;
		OutputStream out = null;
		try {
			if (enc == null) {
				enc = "UTF-8";
			}
			if (mkdirs) {
				File parent = file.getParentFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}
			}
			out = new FileOutputStream(file);
			wtr = new OutputStreamWriter(out, enc);
			wtr.write(string);
			wtr.flush();
		} finally {
			IOTools.close(wtr);
			IOTools.close(out);
		}
	}

}
