package ananas.lib.servkit.json.parser;

public class DefaultJsonParserFactory implements IJsonParserFactory {

	@Override
	public IJsonParser newParser() {
		return new Impl_JsonParser() ;
	}

}
