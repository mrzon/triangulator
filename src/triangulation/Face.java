//********************************************************************
//  Face.java
//
//  Represents face of the polygon, it contains inner halfedge, outer, etc
//  modified on April 2, 2011  by Emerson Chan Simbolon (0806334773).
//********************************************************************
package triangulation;

public class Face
{
	public int label;
	public HalfEdge outerComponent;
	public HalfEdge innerComponent;

	public Face(int a)
	{
		label = a;
	}


	public Face(int a, HalfEdge o, HalfEdge i)
	{
		label = a; outerComponent = o; innerComponent = i;
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
	public HalfEdge getOuterComponent()
	{
		return outerComponent;
	}


	/**
	*
	* @return
	*/
	public HalfEdge getInnerComponent()
	{
		return innerComponent;
	}

    @Override
        public String toString()
        {
            return "Face "+this.label;
        }

    @Override
        public int hashCode()
        {
            return toString().hashCode();
        }
}








