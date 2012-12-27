package ananas.lib.sgit;

import java.io.File;

public interface IRepository {

	IConfiguration getConfiguration();

	void close();

	/**
	 * the parent of .sgit|.git
	 * */
	File getBaseDirectory();

	/**
	 * .sgit|.git
	 * */
	File getRepositoryDirectory();

	IFileStreamManager getFileStreamManager();

	IHashProcessorFactory getHashProcessorFactory();

	IObjectManager getObjectManager();

}
