package ananas.lib.sgit;

public interface IRepositoryFactory {

	IRepository newRepository(IConfiguration conf);

	IRepository openRepository(IConfiguration conf);

}
