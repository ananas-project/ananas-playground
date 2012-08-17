package ananas.lib.blueprint.elements.awt;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ananas.lib.blueprint.elements.awt.util.DefaultEventChainNode;
import ananas.lib.blueprint.elements.awt.util.IEventChainNode;

public interface IEMenuItem extends IEMenuComponent {

	MenuItem toMenuItem();

	IEventChainNode getEventChainNode();

	public static class Wrapper extends IEMenuComponent.Wrapper implements
			IEMenuItem {

		public static final String attr_label = "label";
		public static final String attr_action_command = "actionCommand";

		@Override
		public MenuItem toMenuItem() {
			return (MenuItem) this.getTarget(true);
		}

		@Override
		public Object createTarget() {

			MenuItem ret = new MenuItem();

			ret.addActionListener(this.mActionListener);

			return ret;
		}

		@Override
		public boolean setAttribute(String nsURI, String name, String value) {

			if (name == null) {
				return false;

			} else if (attr_action_command.equals(name)) {
				this.toMenuItem().setActionCommand(value);

			} else if (attr_label.equals(name)) {
				this.toMenuItem().setLabel(value);

			} else {
				return super.setAttribute(nsURI, name, value);

			}
			return true;
		}

		private final ActionListener mActionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				IEMenuItem.Wrapper.this.mEventChainNode.processEvent(event);
			}
		};

		private final IEventChainNode mEventChainNode = new DefaultEventChainNode();

		@Override
		public IEventChainNode getEventChainNode() {
			return this.mEventChainNode;
		}

	}

}
