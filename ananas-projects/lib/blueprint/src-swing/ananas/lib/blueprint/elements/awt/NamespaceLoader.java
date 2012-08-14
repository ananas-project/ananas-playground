package ananas.lib.blueprint.elements.awt;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuComponent;
import java.awt.MenuItem;
import java.awt.Window;

import ananas.lib.blueprint.IImplementation;
import ananas.lib.blueprint.INamespace;
import ananas.lib.blueprint.INamespaceLoader;

public class NamespaceLoader implements INamespaceLoader {

	public NamespaceLoader() {
	}

	@Override
	public INamespace load(IImplementation impl) {

		String nsURI = "xmlns:ananas:blueprint:awt";
		String defaultPrefix = "awt";
		INamespace ns = impl.newNamespace(nsURI, defaultPrefix);

		reg(ns, "Object", IEObject.Wrapper.class, Object.class);
		reg(ns, "Component", IEComponent.Wrapper.class, Component.class);
		reg(ns, "Container", IEContainer.Wrapper.class, Container.class);
		reg(ns, "Window", IEWindow.Wrapper.class, Window.class);
		reg(ns, "Frame", IEFrame.Wrapper.class, Frame.class);

		reg(ns, "MenuComponent", IEMenuComponent.Wrapper.class,
				MenuComponent.class);
		reg(ns, "MenuBar", IEMenuBar.Wrapper.class, MenuBar.class);
		reg(ns, "MenuItem", IEMenuItem.Wrapper.class, MenuItem.class);
		reg(ns, "Menu", IEMenu.Wrapper.class, Menu.class);

		reg(ns, "position", IE_position.Wrapper.class, IE_position.Core.class);
		reg(ns, "invoke", IE_invoke.Wrapper.class, IE_invoke.Core.class);

		reg(ns, "BorderLayout", IEBorderLayout.Wrapper.class,
				BorderLayout.class);
		reg(ns, "FlowLayout", IEFlowLayout.Wrapper.class, FlowLayout.class);
		reg(ns, "GridLayout", IEGridLayout.Wrapper.class, GridLayout.class);

		return ns;

	}

	private void reg(INamespace ns, String localName, Class<?> elementClass,
			Class<?> targetClass) {

		ns.registerClass(localName, elementClass, targetClass);
	}

}
