package ananas.objecthub.core;

import java.io.OutputStream;

public interface IObjectPutting extends IObject {

	OutputStream openOutputStream();

}
