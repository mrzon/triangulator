/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package triangulation;

/*
* Tugas 2 Computational Geometry:
* implementasi "Triangulasi"
*
*  modified on March 10, 2011 by Emerson Chan Simbolon (0806334773).
*	- penyesuaian lain
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class TugasCG02 extends JFrame {
	// This application relies on the PointsPanel component
	PointsPanel pane;

	public static void main(String[] args) {

		TugasCG02 app = new TugasCG02();
		BinarySearchTree<Integer> i = new BinarySearchTree<Integer>();

		app.addWindowListener(new WindowAdapter() {
                                @Override
				public void windowClosing(WindowEvent e) { System.exit(0); }
			});

		app.pack();
		app.setVisible(true);
	}

	/*
	* This constructor creates the GUI for this application.
	*/
	public TugasCG02() {
		super("Tugas CG 02");

		// All content of a JFrame (except for the menubar) goes in the
		// Frame's internal "content pane", not in the frame itself.

		Container contentPane = this.getContentPane();

		// Specify a layout manager for the content pane
		contentPane.setLayout(new BorderLayout());

		// Create the main component, give it a border, and
		// a background color, and add it to the content pane
		pane = new PointsPanel();
		pane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		contentPane.add(pane, BorderLayout.CENTER);

		// Create a menubar and add it to this window.
		JMenuBar menubar = new JMenuBar();  // Create a menubar
		this.setJMenuBar(menubar);          // Display it in the JFrame

		// Create menus and add to the menubar
		JMenu filemenu = new JMenu("File");
		menubar.add(filemenu);

		// Create some Action objects for use in the menus and toolbars.
		// An Action combines a menu title and/or icon with an ActionListener.
		// These Action classes are defined as inner classes below.
		Action clear = new ClearAction();
		Action quit = new QuitAction();
		Action drawPolygon = new DrawPolygonAction();
		Action triPolygon = new TriangulateAction();
		Action save = new SaveAction();
		Action load = new LoadAction();

		// Populate the menus using Action objects
		filemenu.add(clear);
		filemenu.add(quit);
		filemenu.add(drawPolygon);
		filemenu.add(triPolygon);
		filemenu.add(save);
		filemenu.add(load);

		// Now create a toolbar, add actions to it, and add it to the
		// top of the frame (where it appears underneath the menubar)
		JToolBar toolbar = new JToolBar();
		toolbar.add(clear);
		toolbar.add(quit);
		toolbar.add(drawPolygon);
		toolbar.add(triPolygon);
		toolbar.add(save);
		toolbar.add(load);
		contentPane.add(toolbar, BorderLayout.NORTH);
	}

	/* This inner class defines the "clear" action */
	class ClearAction extends AbstractAction {
		public ClearAction() {
			super("Clear");  // Specify the name of the action
		}
		public void actionPerformed(ActionEvent e) { /* merespon terhadap action clear */
			pane.reset();
		}
	}
	/* This inner class defines the "draw" action */
	class DrawPolygonAction extends AbstractAction {
		public DrawPolygonAction() {
			super("Draw");  // Specify the name of the action
		}
		public void actionPerformed(ActionEvent e) { /* merespon terhadap action drawPolygon */
			pane.drawPolygon();
			repaint();
		}
	}

	/* This inner class defines the "quit" action */
	class QuitAction extends AbstractAction {
		public QuitAction() { super("Quit"); }
		public void actionPerformed(ActionEvent e) {
			// Use JOptionPane to confirm that the user really wants to quit
			int response =
			JOptionPane.showConfirmDialog(TugasCG02.this, "Benar mau quit?");
			if (response == JOptionPane.YES_OPTION) System.exit(0);
		}
	}
	/* This inner class defines the "quit" action */
	class TriangulateAction extends AbstractAction {
		public TriangulateAction() { super("Triangulate"); }
		public void actionPerformed(ActionEvent e) {
			// Use JOptionPane to confirm that the user really wants to quit
			pane.setTriangulateMonoton(true);
		//	pane.triangulate();
		}
	}
	class SaveAction extends AbstractAction {
		public SaveAction() { super("Save"); }
		public void actionPerformed(ActionEvent e) {
			// Use JOptionPane to confirm that the user really wants to quit
			pane.save();
		//	pane.triangulate();
		}
	}
	class LoadAction extends AbstractAction {
		public LoadAction() { super("Load"); }
		public void actionPerformed(ActionEvent e) {
			// Use JOptionPane to confirm that the user really wants to quit
			pane.load();
		//	pane.triangulate();
		}
	}
}
