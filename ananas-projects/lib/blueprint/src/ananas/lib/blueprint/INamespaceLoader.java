package ananas.lib.blueprint;

/**
 * this class MUST has a Default() constructor
 * */

public interface INamespaceLoader {

	INamespace load(IImplementation impl);

}
