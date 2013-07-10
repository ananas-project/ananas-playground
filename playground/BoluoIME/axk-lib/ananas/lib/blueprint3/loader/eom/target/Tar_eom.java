package ananas.lib.blueprint3.loader.eom.target;

import java.util.ArrayList;
import java.util.List;

public class Tar_eom extends TargetBase {

	private final List<Tar_namespace> mNsList;

	public Tar_eom() {
		this.mNsList = new ArrayList<Tar_namespace>();
	}

	public void addNamespace(Tar_namespace ns) {
		this.mNsList.add(ns);
	}

	public List<Tar_namespace> listNamespaces() {
		return this.mNsList;
	}

	@Override
	public ITargetNode[] getChildren() {
		List<Tar_namespace> list = this.mNsList;
		return list.toArray(new Tar_namespace[list.size()]);
	}

	@Override
	public void setParent(ITargetNode node) {
	}

	@Override
	public ITargetNode getParent() {
		return null;
	}

}
