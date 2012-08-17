package ananas.lib.blueprint.elements.awt;

import java.awt.MenuBar;
import java.util.EventObject;

import ananas.lib.blueprint.IElement;
import ananas.lib.blueprint.elements.awt.util.DefaultEventChainNode;
import ananas.lib.blueprint.elements.awt.util.IEventChainNode;

public interface IEMenuBar extends IEMenuComponent {

	MenuBar toMenuBar();

	IEventChainNode getEventChainNode();

	public static class Wrapper extends IEMenuComponent.Wrapper implements
			IEMenuBar {

		@Override
		public MenuBar toMenuBar() {
			return (MenuBar) this.getTarget(true);
		}

		@Override
		public Object createTarget() {
			return new MenuBar();
		}

		@Override
		public boolean appendChild(IElement element) {

			if (element == null) {
				return false;

			} else if (element instanceof IEMenu) {

				this._addMenu((IEMenu) element);

			} else {
				return super.appendChild(element);

			}
			return true;
		}

		private void _addMenu(IEMenu menu) {
			this.toMenuBar().add(menu.toMenu());
			IEventChainNode cNode = menu.getEventChainNode();
			IEventChainNode pNode = this.getEventChainNode();
			cNode.setNextNode(pNode);
		}

		private final IEventChainNode mEventChainNode = new DefaultEventChainNode() {

			@Override
			protected EventObject onEvent(EventObject event) {
				System.out.println(this + ".onEvent():" + event.getSource());
				return super.onEvent(event);
			}

		};

		@Override
		public IEventChainNode getEventChainNode() {
			return this.mEventChainNode;
		}

	}

}
