package ananas.app.sharehub;

import java.io.File;

public class Const {

	public static File getImageFile() {
		File file = android.os.Environment.getExternalStorageDirectory();
		return new File(file, "./.ananas/clip_image.png");
	}

}
