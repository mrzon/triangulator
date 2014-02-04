//********************************************************************
//  HalfEdge.java
//
//  Represents HalfEdge of the polygon, it contains origin, its next halfedge, its previous, etc
//  modified on April 2, 2011  by Emerson Chan Simbolon (0806334773).
//********************************************************************
package triangulation;

class HalfEdge implements Comparable
{
	public int label;
	public Vertex origin;
	public HalfEdge twin;
	public HalfEdge next;
	public HalfEdge nextOriginal;
	public HalfEdge prevOriginal;
	public HalfEdge prev;
	public Face incidentFace;
	public Vertex helper;
	public boolean yes = false;

	public HalfEdge(int a, Vertex v, Face f)
	{
		label = a; origin = v; incidentFace = f;
		helper = origin;
	}


	public void setPrev(HalfEdge a)
	{
		if(a.next != this)
		{
			a.next = this;
			prev = a;
		}
	}


	/**
	*
	*/
	public int compareTo(Object o)
	{
		HalfEdge he = (HalfEdge)o;

		if(he.getHighestPoint().compareTo(getHighestPoint()) < 0)
		{

			return -he.compareTo(this);
		}
                /*
		return MyPoint.left(getLowestPoint(), getHighestPoint(), he.getHighestPoint()) ? -1 : 1;*/
                double x,x1=getHighestPoint().getX(),y1=getHighestPoint().getY(),
                        x2=getLowestPoint().getX(),y2=getLowestPoint().getY(),
                        x3=he.getHighestPoint().getX(),y3=he.getHighestPoint().getY();
                if(x1 == x2)
                {
                       x = x1;
                }
                else if(y1==y2)
                {
                       x = x1;
                }
                else
                {
                   x = ((-y3 + y1)*(x2-x1)/(-y2+y1))+x1;
                }
                return x-x3 < 0? -1: 1;
	}


	/**
	*
	*/
	public boolean equals(Object o)
	{
		if(o instanceof HalfEdge)
		{
			HalfEdge he = (HalfEdge)o;
			return he.origin.equals(origin) && he.next.origin.equals(next.origin) && he.prev.origin.equals(prev.origin);
		}
		return false;
	}


        @Override
	public int hashCode()
	{
		return tostr().hashCode();
	}


        @Override
	public String toString()
	{
		return "e"+origin.label+" on "+incidentFace;
	}
	public String tostr()
	{
		return "e"+origin+" "+(next != null? next.origin:null)+" "+(prev != null? prev.origin:null)+';';
	}


	/**
	*
	*/
	public MyPoint getHighestPoint()
	{
		//System.out.println("next Original dari "+this+" adalah "+nextOriginal);
            //System.out.println("oieo" + origin.incidentEdgeOriginal);
		return origin.compareTo(origin.incidentEdgeOriginal.nextOriginal.origin) < 0 ? origin.position : origin.incidentEdgeOriginal.nextOriginal.origin.position;
	}


	public MyPoint getLowestPoint()
	{
		return getHighestPoint().equals(origin.position) ? origin.incidentEdgeOriginal.nextOriginal.origin.position: origin.position ;
	}


	public void setNext(HalfEdge a)
	{
		if(a.prev != this)
		{
			a.prev = this;
			next = a;
			helper = next.origin;
		}
	}


	public void setTwin(HalfEdge a)
	{
		if(a.twin != this)
		{
			twin = a;
			a.twin=this;
		}
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
	public Vertex getOrigin()
	{
		return origin;
	}


	/**
	*
	* @return
	*/
	public HalfEdge getTwin()
	{
		return twin;
	}


	/**
	*
	* @return
	*/
	public HalfEdge getNext()
	{
		return next;
	}


	/**
	*
	* @return
	*/
	public HalfEdge getPrev()
	{
		return prev;
	}


	/**
	*
	* @return
	*/
	public Face getIncidentFace()
	{
		return incidentFace;
	}


	public void setHelper(Vertex v)
	{
		helper = v;
	}


	public Vertex getHelper()
	{
		return helper;
	}
}
