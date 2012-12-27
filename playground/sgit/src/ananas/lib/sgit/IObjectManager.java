package ananas.lib.sgit;

public interface IObjectManager {

	ISgitObject getObject(IHash hash);

	ITreeObject getTreeObject(IHash hash);

	IBlobObject getBlobObject(IHash hash);

	ICommitObject getCommitObject(IHash hash);

	ITagObject getTagObject(IHash hash);

}
