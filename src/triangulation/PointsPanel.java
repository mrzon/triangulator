//********************************************************************
//  PointsPanel.java
//
//  Represents the primary panel for user to enter points.
//  modified on April 2, 2011  by Emerson Chan Simbolon (0806334773).
//	- penyesuaian dengan tugas2
//********************************************************************
package triangulation;

import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;



/**
* Class PointsPanel merupakan instance dari canvas
*/
public class PointsPanel extends JPanel {
	private MyPolygon poly;	//struktur data yang menyimpan list dari MyPoint
	private boolean isPolygon = false;	//boolean yang memberikan flag, apakah convex hull sudah tergambar apa belum
	//private Polygon poly;			//representasi polygon dari convex hull
	private boolean tri = true;
	private boolean triM = false;
	private Polygon poly1;
	private Polygon poly3;
	private Polygon poly2 = new Polygon();
	private boolean lepas;
	private boolean done;
	int xpoints[] = new int[100];
	int ypoints[] = new int[100];

	int count = 0;
	int dcount = 0;
	DoublyConnectedEdgeList dcel;

	ArrayList<Polygon> listP = new ArrayList<Polygon>();

	private ArrayList<Polygon> pList = new ArrayList<Polygon>();
	private ArrayList<Polygon> pList1 = new ArrayList<Polygon>();
	private ArrayList<HalfEdge> heList = new ArrayList<HalfEdge>();
	//-----------------------------------------------------------------
	//  Constructor: Sets up this panel to listen for mouse events.
	//-----------------------------------------------------------------
        /**
         *
         */
        public PointsPanel() {
		poly = new MyPolygon();
		poly1 = new Polygon();
		poly3 = new Polygon();
		lepas = true;
		done = false;

		addMouseListener (new PointsListener());
		addMouseMotionListener (new PointsListener());

		setBackground (Color.black);
		setPreferredSize (new Dimension(300, 200));
		dcel = new DoublyConnectedEdgeList();
	}


	/**
	* method reset merupakan methode yang akan melakukan pembersihan layar canvas
	* yaitu mengassign setiap variable ke nilai awal.
	*/
	public void reset(){
		isPolygon = false;
		poly = new MyPolygon();
		lepas = true;
		done = false;
		xpoints = new int[100];
		ypoints = new int[100];
		count = 0;
		poly1 = new Polygon();
		poly2 = new Polygon();
		poly3 = new Polygon();
		repaint();
		pList = new ArrayList<Polygon>();
		pList1 = new ArrayList<Polygon>();
		dcel = new DoublyConnectedEdgeList();
		dcount = 0;
	}


	/**
	* method paintComponent akan menggambar setiap titik yang ada pada list pointList,
	* dan menggambar polygon, jika atribut isPolygon bernilai true
	* @param page instance Graphics
	*/
        @Override
	public void paintComponent (Graphics page) {
		super.paintComponent(page);

		page.setColor (Color.green);

		for (int i = 0; i < poly.count; i++){
			page.fillOval (poly.x[i]-3, poly.y[i]-3, 7, 7);
			page.drawString ((char)('A'+i)+"("+poly.x[i]+","+poly.y[i]+")", poly.x[i], poly.y[i]);
		}
		if(isPolygon)
		{
			page.drawPolygon(poly3);
		}
		if(!lepas && !done)
		{
			page.drawPolygon(poly2);
		}

		for(int i = 0; i < pList1.size(); i++)
		{
			page.setColor(Color.BLUE);
			page.drawPolygon(pList1.get(i));
		}
		for(int i = 0; i < pList.size(); i++)
		{
			page.setColor(Color.YELLOW);
			page.drawPolygon(pList.get(i));
		}
		if(lepas && done)
		{
			page.setColor(Color.GREEN);
			page.drawPolygon(poly1);
		}

		page.drawString ("Count: " + poly.count, 5, 15);
	}


	/**
	* method drawPolygon akan melakukan perhitungan convex hull dari titik-titik yang ada
	* pada list pointList, algoritma yang digunakan adalah berupa incremental. method ini memiliki kompleksitas O(N (log N))
	* dengan N adalah jumlah titik, kompleksitas ini dihasilkan karena proses sorting dari titik yang memiliki kompleksitas O(N (log N))
	* dan proses pembuatan convex hull yang memiliki kompleksitas O(N), sehingga kompleksitas yang diambil adalah yang paling dominan, yaitu O(N (log N))
	*/
	public void drawPolygon()
	{
		ArrayList <MyPoint> arr = poly.getPointRepresentation();


		if(!isPolygon && arr.size() > 1)
		{
			isPolygon = true;
			Collections.sort(arr);		//melakukan sorting terhadap titik-titik yang ada


			ArrayList <Point> list = new ArrayList<Point>();
			list.add(arr.get(0).getPoint());
			list.add(arr.get(1).getPoint());

			for(int i = 2; i < arr.size(); i++)
			{
				list.add(arr.get(i).getPoint());
				while(list.size()>2)
				{
					Point c = list.get(list.size()-1);
					Point b = list.get(list.size()-2);
					Point a = list.get(list.size()-3);
					if(!MyPoint.left(a,b,c)){	//jika titik c tidak berada di kiri garis ab
						list.remove(list.size()-2);	//titik yang berada di tengah dibuang
					}
					else
						break;
				}
			}
			for(int i = 0; i < list.size(); i++)
			{
				poly3.addPoint((int)list.get(i).getX(), (int)list.get(i).getY());
			}

			ArrayList <Point> list1 = new ArrayList<Point>();
			list1.add(arr.get(arr.size()-1).getPoint());
			list1.add(arr.get(arr.size()-2).getPoint());
			for(int i = arr.size()-3; i >= 0; i--)
			{
				list1.add(arr.get(i).getPoint());
				while(list1.size()>2)
				{
					Point c = list1.get(list1.size()-1);
					Point b = list1.get(list1.size()-2);
					Point a = list1.get(list1.size()-3);
					if(!MyPoint.left(a,b,c)){
						list1.remove(list1.size()-2);
					}
					else break;
				}
			}
			for(int i = 0; i < list1.size(); i++)
			{
				poly3.addPoint((int)list1.get(i).getX(), (int)list1.get(i).getY());
			}

		}
		repaint();
	}


	//*****************************************************************
	//  Represents the listener for mouse events.
	//*****************************************************************
	private class PointsListener extends MouseMotionAdapter implements MouseListener {
		//--------------------------------------------------------------
		//  Adds the current point to the list of points and redraws
		//  the panel whenever the mouse button is pressed.
		//--------------------------------------------------------------
		public void mousePressed (MouseEvent event) {
			if(!isPolygon){
				poly.addPoint(new MyPoint(event.getPoint()));
				repaint();
			}
			if(tri){
				if(lepas && !done)
				{
					lepas = false;
				}

				if(!lepas){
					int c = event.getClickCount();
					//JOptionPane.showMessageDialog(PointsPanel.this,c);
					if(c>1)
					{
						lepas = true;
						done = true;
						poly1 = new Polygon(xpoints, ypoints, count);
						repaint();
						ArrayList<Vertex> a = poly.getVertexList();
						ArrayList<MyPoint> b = poly.getPointRepresentation();
						dcel.build(a);
						dcel.processPolygon(pList);
						repaint();
						//System.out.println(a);


					}
					else
					{

						xpoints[count] = event.getX();
						ypoints[count] = event.getY();
						count++;
					}
				}
			}

		}

		//--------------------------------------------------------------
		//  Provide empty definitions for unused event methods.
		//--------------------------------------------------------------
		public void mouseClicked (MouseEvent event) {
			/* 	int c = event.getClickCount();
			JOptionPane.showMessageDialog(PointsPanel.this,c);
			if(c>1)
			{
			lepas = true;
			} */
		}
		public void mouseReleased (MouseEvent event) {}
		public void mouseEntered (MouseEvent event) {}
		public void mouseExited (MouseEvent event) {}
                @Override
		public void mouseMoved(MouseEvent event){
			if(!lepas && !done){
				poly2 = new Polygon(xpoints,ypoints,count);
				poly2.addPoint(event.getX(),event.getY());
				repaint();
			}
		}
                @Override
		public void mouseDragged(MouseEvent event){
		}
	}


	/**
	*
         *
         * @param i
         */
	public void setTriangulateValue(boolean i)
	{
		tri = i;
	}


	/**
	*
         *
         * @param i
         */
	public void setTriangulateMonoton(boolean i)
	{
		triM = i;
		pList1 = new ArrayList<Polygon>();
		dcel.triangulateMonoton(pList1);
		System.out.println("ada "+pList1.size()+" segitiga");
		repaint();
	}

        /**
         *
         */
        public void save()
	{
		try{

			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("out.txt"));
			out.writeObject(poly);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

        /**
         *
         */
        public void load()
	{
		try{
			reset();
			ObjectInputStream out = new ObjectInputStream(new FileInputStream("out.txt"));
			poly = (MyPolygon)out.readObject();

			dcel.build(poly.getVertexList());
			dcel.processPolygon(pList);
			repaint();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}



}
