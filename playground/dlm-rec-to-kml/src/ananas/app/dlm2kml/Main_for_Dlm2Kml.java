package ananas.app.dlm2kml;

import java.io.File;

public class Main_for_Dlm2Kml {

	public static void main(String arg[]) {
		try {
			String srcPath = arg[0];
			File src, dest;
			src = new File(srcPath);
			dest = new File(src.getAbsolutePath() + ".kml");
			ConvertTask task = new ConvertTask(src, dest);
			task.run();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println();
			System.out.println("error");
			System.out.println("use command line 'this.jar [src_path]' to do.");
		}
	}
}
