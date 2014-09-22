package ananas.playground.square_counter;

/**
 * Hello world!
 *
 */
public class SquareCounterApp {

	public static void main(String[] args) {

		System.out.println("Hello World!");

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				MainFrame mf = new MainFrame();
				mf.setVisible(true);
			}
		});

	}
}
