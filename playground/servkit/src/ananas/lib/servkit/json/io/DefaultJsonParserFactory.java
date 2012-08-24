package ananas.lib.servkit.json.io;

public class DefaultJsonParserFactory implements IJsonParserFactory {

	@Override
	public IJsonParser newParser() {
		return new Impl_JsonParser() ;
	}

}
