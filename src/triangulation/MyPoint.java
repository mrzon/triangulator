//********************************************************************
//  MyPoint.java
//
//  represents point by its coordinate x,y, the differences between
//  the same class on java API are this class implements Comparable,
//  so it can be compared with another instance of point, etc
//  modified on April 2, 2011  by Emerson Chan Simbolon (0806334773).
//********************************************************************
package triangulation;


import java.io.*;
import java.awt.*;

/**
* Class MyPoint merepresentasikan objek titik
* Class ini mengimplementasi Interface comparable. hal ini disebabkan kita  membutuhkan
* objek titik yang bisa disort.
*/
public class MyPoint implements Comparable, Serializable
{
	private Point p; //objek Titik yang disimpan
	//-----------------------------------------------------------------
	//  Constructor: Membuat objek titik yang baru berdasarkan object "Point"
	//-----------------------------------------------------------------
	MyPoint(Point p1)
	{
		p = p1;
	}


	//-----------------------------------------------------------------
	//  Constructor: Membuat objek titik yang baru berdasarkan koordinat
	// 		 x dan y
	//-----------------------------------------------------------------
	MyPoint(int x, int y)
	{
		p = new Point(x,y);
	}


	/**
	* method ini merupakan implementasi method compareTo pada interface Comparable
	* dengan adanya method ini, objek MyPoint bisa dibandingkan dengan objek MyPoint yang lain
	*
	* @param o objek yang akan dibandingkan
	* @return -1 jika objek ini lebih kecil dari objek o
	* 	  0 jika objek ini sama dengan objek o
	* 	  1 jika objek ini lebih besar dari objek o
	*/
	public int compareTo(Object o)
	{
		Point p1 = ((MyPoint)o).getPoint();
		if (p.getY() != p1.getY())
			return p.getY()>p1.getY()?1:-1;
		if(p.getX() != p1.getX())
		{
			return p.getX()>p1.getX()?1:-1;
		}
		return 0;
	}


	/**
	* method getPoint akan memberikan instance Point,
	* @return p instance Point
	*/
	public Point getPoint()
	{
		return p;
	}


	/**
	*
	* @return
	*/
	public boolean equals(Object o)
	{
		if(o instanceof MyPoint)
		{
			MyPoint p = (MyPoint)o;
			return p.getX() == getX() && p.getY() == getY();
		}
		return false;
	}


	/**
	*
	* @return
	*/
	public int getX()
	{
		return (int)p.getX();
	}


	/**
	*
	* @return
	*/
	public int getY()
	{
		return (int)p.getY();
	}


	/**
	* method areaSign merupakan method yang melakukan perhitungan luas yang dihasilkan dari 3 titik.
	*
	* @param a titik pertama
	* @param b titik kedua
	* @param c titik ketiga
	* @return -1 jika luas dari area yang dihasilkan oleh ke tiga titik negatif
	* 	  0 jika luas dari area yang dihasilkan oleh ke tiga titik nol
	* 	  1 jika luas dari area yang dihasilkan oleh ke tiga titik positif
	*/
	private static int areaSign( Point a, Point b, Point c )
	{
		double area2;
		area2 = ( b.getX()-a.getX()) * (( c.getY()-a.getY())) -
		( c.getX()-a.getX()) * ( b.getY()-a.getY());
		/* The area should be an integer. */
		if ( area2 > 0.5 ) return 1;
		else if ( area2 < -0.5 ) return -1;
		else return 0;
	}


	/**
	* method left akan menentukan apakah suatu titik c berada pada kiri garis ab atau tidak
	* @param a titik pertama
	* @param b titik kedua
	* @param c titik ketiga
	* @return true jika titik c berada di kiri garis ab
	* 	 false jika titik c berada di kanan atau tepat pada garis ab
	*/
	public static boolean left( Point a, Point b, Point c )
	{
		return areaSign( a, b, c ) > 0;
	}

	public static boolean left( MyPoint a, MyPoint b, MyPoint c )
	{
		return left( a.p, b.p, c.p );
	}
	/**
	*
	*/
	public String toString()
	{
		return "("+getX()+", "+getY()+")";
	}


	/**
	*
	*/
	public int hashCode()
	{
		return toString().hashCode();
	}
}
