package ananas.lib.blueprint.elements.swing;

import javax.swing.JFrame;

import ananas.lib.blueprint.IElement;
import ananas.lib.blueprint.elements.awt.IEContainer;
import ananas.lib.blueprint.elements.awt.IEFrame;
import ananas.lib.blueprint.elements.awt.IE_invoke;

public interface IEJFrame extends IEFrame {

	JFrame toJFrame();

	public static class Wrapper extends IEFrame.Wrapper implements IEJFrame {

		@Override
		public Object createTarget() {
			return new JFrame();
		}

		@Override
		public void tagEnd() {

			super.tagEnd();

			// Frame frame = (Frame) this.getTarget(true);

		}

		@Override
		public JFrame toJFrame() {
			return (JFrame) this.getTarget(true);
		}

		@Override
		public boolean appendChild(IElement element) {
			if (element == null) {
				return false;
			} else {
				return super.appendChild(element);
			}
			// return true;
		}

		@Override
		public boolean appendInvoke(IE_invoke invoke) {
			String method = invoke.getMethod();
			if (method == null) {
				return false;
			} else if (method.equals("setContentPane")) {
				IEContainer p0 = (IEContainer) invoke.getParameterList().get(0);
				this.toJFrame().setContentPane(p0.toContainer());
			} else {
				return super.appendInvoke(invoke);
			}
			return true;
		}

	}
}
