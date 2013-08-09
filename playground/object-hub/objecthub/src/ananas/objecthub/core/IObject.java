package ananas.objecthub.core;

import java.io.File;
import java.io.InputStream;

public interface IObject {

	InputStream openInputStream();

	String getSha1();

	int getFileLength();

	File getFile();
}
