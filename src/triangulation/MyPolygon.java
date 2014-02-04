//********************************************************************
//  MyPolygon.java
//
//  Represents polygon by the set of points, it process the direction of the polygon too
//  modified on April 2, 2011  by Emerson Chan Simbolon (0806334773).
//********************************************************************
package triangulation;

import java.util.*;
import java.io.*;
import java.awt.*;
/**
*
*/
public class MyPolygon implements Serializable
{
	int x[];
	int y[];
	int count = 0;
	int highest = 0;
	int righest = 0;
	ArrayList<MyPoint> array = new ArrayList<MyPoint>();
	private HashSet<MyPoint> setPoint;


	/**
	*
	* @return
	*/
	public MyPolygon()
	{
		x = new int[50];
		y = new int[50];
		setPoint = new HashSet<MyPoint>();
	}


	/**
	* @param x[]
	* @param y[]
	*
	* @param c
	* @return
	*/
	public MyPolygon(int x[], int y[], int c)
	{
		this.x = x;
		this.y = y;
		count = c;
	}


	/**
	*
	* @return
	*/
	public Polygon getPolygon()
	{
		return new Polygon(x,y,count);
	}


	/**
	* @param x
	*
	* @param y
	* @return
	*/
	public void addPoint(int x, int y)
	{
		MyPoint my = new MyPoint(x,y);
		if(!setPoint.contains(my)){
			if(this.x.length < count+2)
			{
				doubling();
			}
			this.x[count] = x;
			this.y[count] = y;
			if(-this.y[highest] < -y)
			{
				highest = count;
			}

			else if(this.y[highest]==y)
			{
				if(this.x[highest] > x)
				{
					highest = count;
				}
			}

			if(this.x[righest] < x)
			{
				righest = count;
			}
			else if(this.x[righest]==x)
			{
				if(-this.y[highest] > -y)
				{
					righest = count;
				}
			}

			array.add(my);
			count++;
			setPoint.add(my);
		}
	}


	/**
	*
	* @param p
	* @return
	*/
	public void addPoint(MyPoint p)
	{
		addPoint(p.getX(), p.getY());
	}


	/**
	*
	* @return
	*/
	private void doubling()
	{
		int x1[] = new int[x.length*2];
		int y1[] = new int[y.length*2];

		for(int i = 0; i < x.length; i++)
		{
			x1[i] = x[i];
			y1[i] = y[i];
		}

		x = x1;
		y = y1;
	}


	/**
	*
	* @return
	*/
	public MyPoint getHighestPoint()
	{
		return new MyPoint(x[highest],y[highest]);
	}


	/**
	*
	* @param i
	* @return
	*/
	public int rightIndex(int i)
	{
		return (i+1)%count;
	}


	/**
	*
	* @param i
	* @return
	*/
	public int leftIndex(int i)
	{
		return (i-1) < 0 ? count-1:i-1;
	}


	/**
	* @param i
	*
	* @param j
	* @return
	*/
	public int mostRight(int i, int j)
	{
		return x[j] > x[i] ? j : i;
	}
	public int mostTop(int i, int j)
	{
		return -y[j] > -y[i] ? j : i;
	}


	/**
	*
	* @return
	*/
	public ArrayList<MyPoint> getPointRepresentation()
	{
		return array;
	}


	/**
	* method getVertexList akan mengembalikan representasi polygon dalam vertex yang akan searah dengan jarum jam
	*
	* @return
	*/
	public ArrayList<Vertex> getVertexList()
	{
		ArrayList<Vertex> a = new ArrayList<Vertex>();
		int l = leftIndex(highest);		//yang ada diindex kiri node yang paling atas
		int r = rightIndex(highest);   		//yang ada diindex kanan node yang paling atas
		int mr = mostRight(highest, l);		//kananan mana antara titik tertinggi dengan yang sebelahnya
		int jj = 0;
		if(mr == highest)	//kalau yang lebih kanan titik yang paling tinggi
		{
			int ml = mostRight(highest, r); //bandingin kananan mana ama yang indexnya dikanannya

			if(ml == highest)	//kalo yang paling kanan masih yang tertinggi, pasti 2 2nya ada di kiri
			{
				jj = ((double)Math.abs(x[r]-x[highest])/((double)Math.abs(y[r]-y[highest])+1)) >
				((double)Math.abs(x[l]-x[highest])/((double)Math.abs(y[l]-y[highest])+1)) ? l : r;
			}
			else
				jj = r;
		}
		else			//kalau yang lebih kanan index kiri
		{
			int ml = mostRight(highest, r);	//bandingin, kananan mana ama yang indexnya dikanannya
			if(ml == highest)			//kalo yang lebih kanan index tertinggi, berarti yang paling kanan si index kiri
			{
				jj = l;
			}
			else		//dua-duanya di kanan
			{
				jj = ((double)Math.abs(x[r]-x[highest])/((double)Math.abs(y[r]-y[highest])+1)) >
				((double)Math.abs(x[l]-x[highest])/((double)Math.abs(y[l]-y[highest])+1)) ? r : l;
			}

		}

		if(jj == rightIndex(highest))
		{
			int i = righest;
			int j = 0;
			while(true)
			{
				Vertex v = new Vertex(j, new MyPoint(x[i],y[i]), null);
				a.add(v);
				i = (i-1) < 0 ? count - 1: i - 1;
				j++;
				if(i == righest)
					break;
			}
		}
		else
		{
			int i = righest;
			int j = 0;
			while(true)
			{
				Vertex v = new Vertex(j, new MyPoint(x[i],y[i]), null);
				a.add(v);
				i = (i+1)%count;
				if(i == righest)
					break;
				j++;
			}
		}
		return a;
	}
}

