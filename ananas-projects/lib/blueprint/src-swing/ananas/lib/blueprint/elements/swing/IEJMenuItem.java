package ananas.lib.blueprint.elements.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JMenuItem;

import ananas.lib.blueprint.elements.awt.util.DefaultEventChainNode;
import ananas.lib.blueprint.elements.awt.util.IEventChainNode;

public interface IEJMenuItem extends IEAbstractButton {

	JMenuItem toJMenuItem();

	IEventChainNode getEventChainNode();

	public static class Wrapper extends IEAbstractButton.Wrapper implements
			IEJMenuItem {

		@Override
		public Object createTarget() {
			JMenuItem mi = (JMenuItem) super.createTarget();
			mi.addActionListener(this.mActionListener);
			mi.addItemListener(this.mItemListener);
			return mi;
		}

		@Override
		public JMenuItem toJMenuItem() {
			return (JMenuItem) this.getTarget(true);
		}

		private final ActionListener mActionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				IEJMenuItem.Wrapper.this._onActionPerformed(ae);
			}
		};

		private final ItemListener mItemListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
			}
		};

		private final IEventChainNode mEventChainNode = new DefaultEventChainNode();

		protected void _onActionPerformed(ActionEvent ae) {
			this.mEventChainNode.processEvent(ae);
		}

		@Override
		public IEventChainNode getEventChainNode() {
			return this.mEventChainNode;
		}

	}

}
