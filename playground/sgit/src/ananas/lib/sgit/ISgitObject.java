package ananas.lib.sgit;

import java.io.File;

public interface ISgitObject {

	IHash getHash();

	IRepository getRepository();

	File getFile();

}
