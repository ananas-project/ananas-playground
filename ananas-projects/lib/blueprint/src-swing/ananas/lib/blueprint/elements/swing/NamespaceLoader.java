package ananas.lib.blueprint.elements.swing;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import ananas.lib.blueprint.IImplementation;
import ananas.lib.blueprint.INamespace;
import ananas.lib.blueprint.INamespaceLoader;
import ananas.lib.blueprint.elements.awt.IE_invoke;
import ananas.lib.blueprint.elements.awt.IE_position;

public class NamespaceLoader implements INamespaceLoader {

	public NamespaceLoader() {
	}

	@Override
	public INamespace load(IImplementation impl) {

		String nsURI = "xmlns:ananas:blueprint:swing";
		String defaultPrefix = "swing";
		INamespace ns = impl.newNamespace(nsURI, defaultPrefix);

		// begin {

		reg(ns, "position", IE_position.Wrapper.class, IE_position.Core.class);
		reg(ns, "invoke", IE_invoke.Wrapper.class, IE_invoke.Core.class);

		ns.registerClass("JFrame", IEJFrame.Wrapper.class, JFrame.class);
		ns.registerClass("JPanel", IEJPanel.Wrapper.class, JPanel.class);

		ns.registerClass("JButton", IEJButton.Wrapper.class, JButton.class);
		ns.registerClass("JLabel", IEJLabel.Wrapper.class, JLabel.class);

		ns.registerClass("JTextComponent", IEJTextComponent.Wrapper.class,
				JTextComponent.class);
		ns.registerClass("JTextField", IEJTextField.Wrapper.class,
				JTextField.class);
		ns.registerClass("JPasswordField", IEJPasswordField.Wrapper.class,
				JPasswordField.class);
		ns.registerClass("JTextArea", IEJTextArea.Wrapper.class,
				JTextArea.class);
		ns.registerClass("JScrollPane", IEJScrollPane.Wrapper.class,
				JScrollPane.class);
		ns.registerClass("JDesktopPane", IEJDesktopPane.Wrapper.class,
				JDesktopPane.class);
		reg(ns, "JInternalFrame", IEJInternalFrame.Wrapper.class,
				JInternalFrame.class);

		reg(ns, "JMenuBar", IEJMenuBar.Wrapper.class, JMenuBar.class);
		reg(ns, "JMenuItem", IEJMenuItem.Wrapper.class, JMenuItem.class);
		reg(ns, "JMenu", IEJMenu.Wrapper.class, JMenu.class);
		reg(ns, "JPopupMenu", IEJPopupMenu.Wrapper.class, JPopupMenu.class);
		reg(ns, "JSeparator", IEJSeparator.Wrapper.class, JSeparator.class);

		// } end

		return ns;

	}

	private void reg(INamespace ns, String localName, Class<?> elementClass,
			Class<?> targetClass) {
		ns.registerClass(localName, elementClass, targetClass);
	}

}
