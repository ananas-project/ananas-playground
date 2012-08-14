package ananas.lib.blueprint.elements.awt;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.util.HashMap;

public interface IEBorderLayout extends IEObject, IELayoutManager {

	BorderLayout toBorderLayout();

	public static class Wrapper extends IEObject.Wrapper implements
			IEBorderLayout {

		@Override
		public LayoutManager toLayoutManager() {
			return (LayoutManager) this.getTarget(true);
		}

		@Override
		public void addComponentToContainer(IEComponent component,
				IEContainer container, IE_position position) {

			String posStr = null;
			if (position != null) {
				posStr = this.mapPosStr(position.getValue());
			}
			if (posStr == null) {
				posStr = BorderLayout.CENTER;
			}

			final Container cont = container.toContainer();
			final Component comp = component.toComponent();
			cont.add(comp, posStr);
		}

		private static HashMap<String, String> sPosStrMapper;

		private String mapPosStr(String value) {

			if (value == null)
				return BorderLayout.CENTER;
			value = value.toLowerCase();
			HashMap<String, String> mapper = sPosStrMapper;
			if (mapper == null) {
				mapper = new HashMap<String, String>();
				{
					mapper.put("center", BorderLayout.CENTER);

					mapper.put("west", BorderLayout.WEST);
					mapper.put("east", BorderLayout.EAST);
					mapper.put("south", BorderLayout.SOUTH);
					mapper.put("north", BorderLayout.NORTH);

					mapper.put("line_end", BorderLayout.LINE_END);
					mapper.put("line_start", BorderLayout.LINE_START);
					mapper.put("page_end", BorderLayout.PAGE_END);
					mapper.put("page_start", BorderLayout.PAGE_START);
				}
				sPosStrMapper = mapper;
			}
			String rlt = mapper.get(value);
			if (rlt == null)
				return BorderLayout.CENTER;
			return rlt;
		}

		@Override
		public BorderLayout toBorderLayout() {
			return (BorderLayout) this.getTarget(true);
		}

		@Override
		public Object createTarget() {
			return super.createTarget();
		}
	}
}
