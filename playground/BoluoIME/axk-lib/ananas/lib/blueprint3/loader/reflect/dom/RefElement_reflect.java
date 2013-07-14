package ananas.lib.blueprint3.loader.reflect.dom;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import ananas.lib.blueprint3.lang.BPEnvironment;

public class RefElement_reflect extends RefElement {

	private final List<RefElement_namespace> mNSList = new ArrayList<RefElement_namespace>();

	public RefElement_reflect(RefDocument ownerDoc) {
		super(ownerDoc);
	}

	@Override
	public boolean setAttribute(String attrURI, String attrLName,
			String attrValue) {

		return false;
	}

	@Override
	public boolean appendChild(RefNode child) {

		if (child instanceof RefElement_namespace) {
			RefElement_namespace ns = (RefElement_namespace) child;
			this.mNSList.add(ns);

		} else {
			return false;
		}
		return true;
	}

	@Override
	public void printSelf(PrintStream out) {
		out.println("<reflect>");
		for (RefElement_namespace item : this.mNSList) {
			item.printSelf(out);
		}
		out.println("</reflect>");
	}

	public void regNamespaces(BPEnvironment envi) {

		for (RefElement_namespace ns : this.mNSList) {
			ns.regNamespace( envi ) ;
		}

	}

}
