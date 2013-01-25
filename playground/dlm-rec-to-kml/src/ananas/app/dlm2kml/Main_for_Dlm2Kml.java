package ananas.app.dlm2kml;

import java.io.File;

public class Main_for_Dlm2Kml {

	public static void main(String arg[]) {

		try {
			File src, dest;
			src = new File(arg[0]);
			dest = new File(src.getAbsolutePath() + ".kml");
			System.out.println("convert");
			System.out.println("    source dlm : " + src);
			System.out.println("        to kml : " + dest);
			System.out.println("begin ...");
			int cnt = 0;
			LocationInput in = new DlmReader(src);
			LocationOutput out = new KmlWriter(dest);
			for (Location loc = in.read(); loc != null; loc = in.read()) {
				out.write(loc);
				if ((cnt++) > (3600 * 24 * 10)) {
					throw new RuntimeException(
							"the number of item is too large(" + cnt + ")");
				}
				if (cnt == 100000) {
					System.out.println();
				}

			}
			in.close();
			out.close();
			System.out.println("success");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println();
			System.out.println("error");
		}
	}
}
