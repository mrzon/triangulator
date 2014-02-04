//********************************************************************
//  Vertex.java
//
//  Represents vertex of the polygon, it contains position, its inciden edge, etc
//  modified on April 2, 2011  by Emerson Chan Simbolon (0806334773).
//********************************************************************
package triangulation;
import java.util.*;

public class Vertex implements Comparable
{
	public int label;
	public MyPoint position = null;
	public HalfEdge incidentEdge = null;
        public HashMap<Face,HalfEdge> iEdge = new HashMap<Face,HalfEdge>();
	public HalfEdge incidentEdgeOriginal = null;
	public static final int REGULARVERTEX = 0x10111;
	public static final int MERGEVERTEX = 0x11111;
	public static final int SPLITVERTEX = 0x11511;
	public static final int STARTVERTEX = 0x13111;
	public static final int ENDVERTEX = 0x11121;
        public int adjacentEdge;
	public int type = REGULARVERTEX;
	public Vertex(int a)
	{
		label = a;
	}


        public void changeFace(Face newF, Face oldF)
        {
            iEdge.put(newF, iEdge.remove(oldF));
        }



	/**
	*
	* @return
	*/
	public HalfEdge getIncidentEdge(Face f)
	{
		return iEdge.get(f);
	}


        /**
	*
	* @return
	*/
	public void setIncidentEdge(Face f, HalfEdge i)
	{
		iEdge.put(f,i);
	}

	/**
	* @param a
	* @param p
	*
	* @param i
	* @return
	*/
	public Vertex(int a, MyPoint p, HalfEdge i)
	{
		label = a; position = p; incidentEdge = i;
                incidentEdgeOriginal = i;
	}


	/**
	*
	* @return
	*/
	public int getLabel()
	{
		return label;
	}


	/**
	*
	* @return
	*/
	public MyPoint getPosition()
	{
		return position;
	}


	/**
	*
	* @return
	*/
	public HalfEdge getIncidentEdge()
	{
		return incidentEdge;
	}


	public String toString()
	{
		String t="";
		if(type == MERGEVERTEX)
			t="MERGEVERTEX";
		else if(type == SPLITVERTEX)
			t="SPLITVERTEX";
		else if(type == STARTVERTEX)
			t="STARTVERTEX";
		else if(type == ENDVERTEX)
			t="ENDVERTEX";
		else if(type == REGULARVERTEX)
			t="REGULARVERTEX";

		return "v"+label+" "+ position+ " "+t;
	}


	public int compareTo(Object o)
	{
		if(o instanceof Vertex)
		{
			Vertex v = (Vertex)o;
			return v.position.getY() > position.getY()  ? -1 : (v.position.getY() < position.getY() ? 1 : (position.getX() < v.position.getX() ? -1 : 1));
		}

		else return 1/0;
	}


	public boolean equals(Object o)
	{
		if(o instanceof Vertex)
		{
			Vertex v = (Vertex)o;
			return v.position.equals(position);
		}
		return false;
	}
}
