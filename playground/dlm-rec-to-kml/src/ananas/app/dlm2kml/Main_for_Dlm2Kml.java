package ananas.app.dlm2kml;

import java.io.File;

public class Main_for_Dlm2Kml {

	public static void main(String arg[]) {
		String srcPath = arg[0];
		File src = new File(srcPath);
		(new Main_for_Dlm2Kml()).todo(src);
	}

	public void todo(File src) {
		try {
			File dir = src.getParentFile();
			File dest = new File(dir, src.getName() + ".kml");
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
