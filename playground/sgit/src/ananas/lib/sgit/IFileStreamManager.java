package ananas.lib.sgit;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public interface IFileStreamManager {

	InputStream openInputStream(File file);

	OutputStream openOutputStream(File file);
}
