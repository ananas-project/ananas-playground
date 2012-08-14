package ananas.lib.blueprint;

import java.io.InputStream;

public interface IBlueprint extends IBlueprintContext {

	IDocument loadDocument(String docURI);

	IDocument loadDocument(InputStream is, String docURI);

}
