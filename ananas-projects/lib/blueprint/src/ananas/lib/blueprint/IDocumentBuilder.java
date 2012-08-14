package ananas.lib.blueprint;

import java.io.File;
import java.io.InputStream;

public interface IDocumentBuilder {

	IDocument build(InputStream is, String docURI);

	IDocument build(File file);

	IDocument build(String docURI);

	IBlueprintContext getBlueprintContext();

}
