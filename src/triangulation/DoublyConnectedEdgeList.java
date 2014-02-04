//********************************************************************
//  DoublyConnectedEdgeList.java
//
//  Represents the polygon by its vertices, edges, and facess
//  modified on April 2, 2011  by Emerson Chan Simbolon (0806334773).
//********************************************************************
package triangulation;

import java.util.*;
import java.awt.*;

/**
*
*/
public class DoublyConnectedEdgeList
{
	private ArrayList<Face> faceList;
	private ArrayList<HalfEdge> edgeList;
	private ArrayList<Vertex> vertexList;
	private ArrayList<MyPolygon> myPolygon;
	private ArrayList<Face> fList = new ArrayList<Face>();
	private BinarySearchTree<HalfEdge> bst = new BinarySearchTree<HalfEdge>();
	private int i = 100;
	private ArrayList<ArrayList<Vertex>> listOfPolygon = new ArrayList<ArrayList<Vertex>>();


        /**
         *
         */
        public DoublyConnectedEdgeList()
	{
		faceList = new ArrayList<Face>();
		edgeList = new ArrayList<HalfEdge>();
		vertexList = new ArrayList<Vertex>();
		myPolygon = new ArrayList<MyPolygon>();
	}


        /**
         *
         * @param f
         */
        public void addFace(Face f)
	{
		faceList.add(f);
	}


        /**
         *
         * @param v
         */
        public void addVertex(Vertex v)
	{
		vertexList.add(v);
	}


        /**
         * 
         * @param e
         */
        public void addEdge(HalfEdge e)
	{
		edgeList.add(e);
	}

        /**
         *
         * @param v
         * @return
         */
        public boolean onLeft(Vertex v)
	{
		return v.compareTo(v.incidentEdge.next.origin) < 0;
	}


        public Face getFace(int i)
        {
            return faceList.get(i);
        }


        /**
         * method build adalah method yang digunakan untuk membuat inisialisasi
         * doubly connected edge list dari kumpulan vertex
         * @param vertexList
         */
        public void build(ArrayList<Vertex> vertexList)
	{
		addFace(new Face(0, null, null));
		addFace(new Face(1, null, null));
		int vC = vertexList.size();

		HalfEdge prevH = new HalfEdge(0, vertexList.get(1), faceList.get(0));
		HalfEdge nextH = new HalfEdge(0, vertexList.get(0), faceList.get(1));
		
                getFace(0).innerComponent = prevH;
                getFace(1).outerComponent = nextH;
		prevH.twin=nextH;
		nextH.twin=prevH;
		
		vertexList.get(0).incidentEdge=nextH;
                vertexList.get(0).incidentEdgeOriginal=nextH;
                vertexList.get(0).iEdge.put(faceList.get(1), nextH);
		edgeList.add(prevH);
		edgeList.add(nextH);

		for(int j = 1,k = vC-1; j < vC-1; j++,k--)
		{
			HalfEdge he1 = new HalfEdge(j, vertexList.get(j+1), faceList.get(0));
			HalfEdge he2 = new HalfEdge(j, vertexList.get(j), faceList.get(1));
			he1.twin = he2;
			he2.twin = he1;
			prevH.prev = he1;
			prevH.prevOriginal = he1;
			he1.next = prevH;
			he1.nextOriginal = prevH;
			nextH.next=he2;
			nextH.nextOriginal=he2;

			he2.prev = nextH;
			he2.prevOriginal = nextH;
			vertexList.get(j).incidentEdge = he2;
                        vertexList.get(j).incidentEdgeOriginal = he2;
			edgeList.add(he1);
			edgeList.add(he2);
			prevH = he1;
			nextH = he2;
                        vertexList.get(j).iEdge.put(faceList.get(1), he2);
		}
		HalfEdge prevL = new HalfEdge(vC-1, vertexList.get(0), faceList.get(0));
		HalfEdge nextL = new HalfEdge(vC-1, vertexList.get(vC-1), faceList.get(1));
                vertexList.get(vC-1).iEdge.put(faceList.get(1), nextL);
		//
		prevL.twin = nextL;
		nextL.twin = prevL;
		//
		prevL.prev = edgeList.get(0);
		prevL.prevOriginal = edgeList.get(0);
		edgeList.get(0).next = prevL;
		edgeList.get(0).nextOriginal = prevL;
		//
		nextL.next = edgeList.get(1);
		nextL.nextOriginal = edgeList.get(1);
		edgeList.get(1).prev = nextL;
		edgeList.get(1).prevOriginal = nextL;
		//
		prevH.prev = prevL;
		prevH.prevOriginal = prevL;
		prevL.next = prevH;
		prevL.nextOriginal = prevH;
		//
		nextH.next = nextL;
		nextH.nextOriginal = nextL;
		nextL.prev = nextH;
		nextL.prevOriginal = nextH;
		//
		vertexList.get(vC-1).incidentEdge = nextL;
                vertexList.get(vC-1).incidentEdgeOriginal = nextL;
		faceList.get(0).innerComponent = edgeList.get(0);
		faceList.get(1).outerComponent = edgeList.get(1);
		edgeList.add(prevL);
		edgeList.add(nextL);
		this.vertexList = vertexList;
	}

	/**
	 * method processPolygon akan membangun poligon monoton dari poligon dcel
         *
         * @param pList list dari poligon monoton disimpan
         */
	public void processPolygon(ArrayList<Polygon> pList)
	{
		PriorityQueue<Vertex> q = new PriorityQueue<Vertex>(vertexList);
		while(!q.isEmpty())
		{
			Vertex v = q.poll();
			Vertex r = v.incidentEdge.next.origin;
			Vertex l = v.incidentEdge.prev.origin;
			if(v.compareTo(r) < 0 && v.compareTo(l) < 0)
			{
				if(MyPoint.left(r.position.getPoint(),v.position.getPoint(), l.position.getPoint()))
				{
					v.type = Vertex.STARTVERTEX;
					handleStartVertex(v);
				}
				else
				{
					v.type = Vertex.SPLITVERTEX;
					handleSplitVertex(v);
				}
			}
			else if(v.compareTo(r) > 0 && v.compareTo(l) > 0)
			{
				if(MyPoint.left(r.position.getPoint(),v.position.getPoint(), l.position.getPoint()))
				{
					v.type = Vertex.ENDVERTEX;
					handleEndVertex(v);
				}
				else
				{
					v.type = Vertex.MERGEVERTEX;
					handleMergeVertex(v);
				}
			}
			else
			{
				handleRegularVertex(v);
			}
		}
		for(int ii = 1; ii < faceList.size(); ii++)
                {
                    HalfEdge h0 = faceList.get(ii).outerComponent;
                    HalfEdge h1 = h0;
                    Polygon p = new Polygon();
                    ArrayList<Vertex> a = new ArrayList<Vertex>();
                    MyPolygon m = new MyPolygon();
                    while(true)
                    {
                        p.addPoint(h1.origin.getPosition().getX(), h1.origin.getPosition().getY());
                        m.addPoint(h1.origin.getPosition().getX(), h1.origin.getPosition().getY());
                        a.add(h1.origin);
                        h1 = h1.next;
                        if(h1 == h0)
                            break;
                    }

                    pList.add(p);
                    myPolygon.add(m);
                    listOfPolygon.add(a);
                }
	}


	/**
	* handle regular vertex
	*/
	private void handleRegularVertex(Vertex v)
	{
		if(v.compareTo(v.incidentEdgeOriginal.nextOriginal.origin) < 0)
		{
			Vertex helper = v.incidentEdgeOriginal.prevOriginal.helper;
			if(helper.type == Vertex.MERGEVERTEX)
			{
                            	createDiagonal(v.incidentEdge.incidentFace, v, helper);
			}
			bst.delete(v.incidentEdgeOriginal.prevOriginal);
			bst.add(v.incidentEdgeOriginal);
			v.incidentEdgeOriginal.helper = v;
		}
		else
		{
			bst.add(v.incidentEdgeOriginal.prevOriginal);
			Node<HalfEdge> h = getTheLeftEdge(v.incidentEdgeOriginal.prevOriginal, bst.getRoot(), bst.getRoot());
			bst.delete(v.incidentEdgeOriginal.prevOriginal);

			Vertex helper = h.getData().helper;
			if(helper.type == Vertex.MERGEVERTEX)
			{
                            	createDiagonal(v.incidentEdge.incidentFace, v, helper);
			}
			h.data.helper = v;

		}
                
	}


	/**
	* handle start vertex
	*/
	private void handleStartVertex(Vertex v)
	{
		HalfEdge c = v.incidentEdgeOriginal;
		bst.add(c);
		c.helper = v;
	}


	/**
	* handle end vertex
	*/
	private void handleEndVertex(Vertex v)
	{
		Vertex helper = v.incidentEdgeOriginal.prevOriginal.getHelper();
		if(helper.type == Vertex.MERGEVERTEX)
		{
			createDiagonal(v.incidentEdge.incidentFace, v, helper);
		}
		bst.delete(v.incidentEdgeOriginal.prevOriginal);
	}


	/**
	* handle merge vertex
	*/
	private void handleMergeVertex(Vertex v)
	{
		Vertex helper = v.incidentEdgeOriginal.prevOriginal.helper;
                if(helper.type == Vertex.MERGEVERTEX)
		{
                    createDiagonal(v.incidentEdge.incidentFace, v, helper);
		}
		Node<HalfEdge> h = getTheLeftEdge(v.incidentEdgeOriginal.prevOriginal, bst.getRoot(),bst.getRoot());
		bst.delete(v.incidentEdgeOriginal.prevOriginal);
		if(h.getData().helper.type == Vertex.MERGEVERTEX)
		{
                    createDiagonal(v.incidentEdge.incidentFace, v, h.getData().helper);
		}
		h.getData().helper = v;
	}


	/**
	* handle split vertex
	*/
	private void handleSplitVertex(Vertex v)
	{
		bst.add(v.incidentEdgeOriginal);
		Node<HalfEdge> h = getTheLeftEdge(v.incidentEdgeOriginal, bst.getRoot(), bst.getRoot());
		createDiagonal(v.incidentEdge.incidentFace, v, h.getData().helper);
		h.getData().helper = v;
		v.incidentEdgeOriginal.helper = v;
	}


        /**
         * method createDiagonal akan membuat diagonal antara 2 buah vertex
         * @param f face dimana akan dibuat diagonal
         * @param u vertex pertama
         * @param v vertex kedua
         */
        public void createDiagonal(Face f, Vertex u, Vertex v)
        {
            Face f1 = new Face(faceList.size(),null,null);
            HalfEdge he = new HalfEdge(i++,u,f);
            
            HalfEdge hf = new HalfEdge(i++,v,f1);
            f1.outerComponent = hf;
            f.outerComponent = he;
            addFace(f1);
            he.twin = hf;
            hf.twin = he;
            HalfEdge heN = v.iEdge.get(f);
            HalfEdge hfN = u.iEdge.get(f);
            HalfEdge heP = hfN.prev;
            HalfEdge hfP = heN.prev;
            he.next = heN;
            heN.prev = he;
            hf.next = hfN;
            hfN.prev = hf;
            he.prev = heP;
            heP.next = he;
            hf.prev = hfP;
            hfP.next = hf;
            u.iEdge.remove(f);
            u.iEdge.put(f, he);
            u.iEdge.put(f1, hfN);
            v.iEdge.put(f1, hf);
            hfN.incidentFace = f1;
            
            HalfEdge ff = hf.next.next;

            /**
             * update incidentEdge dengan face yang baru
             */
            while(true)
            {
                ff.origin.changeFace(f1, f);
                ff.incidentFace = f1;
               
                ff = ff.next;
                if(ff == hf)
                {
                    break;
                }
            }
        }


        /**
         *
         * @param u
         * @param v
         */
        public void createSimpleDiagonal(Vertex u, Vertex v)
	{
		HalfEdge he = new HalfEdge(i++,u,null);
		HalfEdge hf = new HalfEdge(i++,v,null);
                he.next = v.incidentEdge;
		hf.next = u.incidentEdge;
		he.prev = u.incidentEdge.prev;
		hf.prev = v.incidentEdge.prev;
		u.incidentEdge.prev.next = he;
		v.incidentEdge.prev.next = hf;
		hf.twin = he;
		he.twin = hf;
		u.incidentEdge.prev = hf;
		v.incidentEdge.prev = he;
	}


        /**
         * method triangulateMonoton adalah method yang berfungsi untuk melakukan triangulasi terhadap poligon monoton
         * yang menjadi parameternya, method ini memiliki kompleksitas O(n log n)
         * @param pList list dari polygon monoton
         */
        public void triangulateMonoton(ArrayList<Polygon> pList)
	{
                ArrayList<DoublyConnectedEdgeList> dcelList = new ArrayList<DoublyConnectedEdgeList>();
		for(int i = 0; i < myPolygon.size(); i++)
		{
                        DoublyConnectedEdgeList dcel = new DoublyConnectedEdgeList();
                        dcel.build(myPolygon.get(i).getVertexList());
                        dcelList.add(dcel);
			ArrayList<Vertex> arrV = dcel.vertexList;
                        Collections.sort(arrV);
			Stack<Vertex> tri = new Stack<Vertex> ();
			tri.push(arrV.get(0));
			tri.push(arrV.get(1));
			for(int j = 2; j < arrV.size()-1 ; j++)
			{
				if(isOnDifferentChains(tri.peek(), arrV.get(j)))
				{
					while(tri.size() > 1)
					{
						createSimpleDiagonal(tri.pop(), arrV.get(j));
					}
					tri.clear();
					tri.push(arrV.get(j-1));
					tri.push(arrV.get(j));
				}
				else
				{
					Vertex v = tri.pop();
					while(tri.size() > 0)
					{
						Vertex u = tri.peek();
						if(onLeft(arrV.get(j))){
							if(MyPoint.left(arrV.get(j).position, v.position, u.position))
							{
								createSimpleDiagonal(tri.pop(), arrV.get(j));
								v = u;
							}
							else
							{
								break;
							}
						}
						else
						{
							if(!MyPoint.left(arrV.get(j).position, v.position, u.position))
							{
								createSimpleDiagonal(tri.pop(), arrV.get(j));
								v = u;
							}
							else
							{
								break;
							}
						}
					}
					tri.push(v);
					tri.push(arrV.get(j));
				}
			}
                        if(tri.size()>0)
			{
				tri.remove(tri.size()-1);
				while(tri.size()>1)
				{
					Vertex v = tri.remove(tri.size()-1);
					createSimpleDiagonal(v, arrV.get(arrV.size()-1));
				}
			}
		}
		int countFace = 2;
		fList = new ArrayList<Face>();
		HashSet<HalfEdge> set = new HashSet<HalfEdge>();
		for(int i = 0; i < dcelList.size(); i++)
		{
			for(int j = 0; j < dcelList.get(i).vertexList.size(); j++)
			{
				HalfEdge h0 = dcelList.get(i).vertexList.get(j).incidentEdge;
				if(!set.contains(h0))
				{
					Face f = new Face(countFace, h0, null);
					HalfEdge h = h0;
					Polygon p = new Polygon();
					while(!set.contains(h))
					{
						set.add(h);
						h.incidentFace = f;
						h.yes = false;
						p.addPoint(h.origin.getPosition().getX(), h.origin.getPosition().getY());

						h = h.next;
					}
					pList.add(p);
					fList.add(f);
					countFace++;
				}
			}

		}
	}


        /**
         * method isOnDifferentChains akan memeriksa apakah 2 vertex berada pada sisi poligon monoton yang sama
         * @param u vertex 1
         * @param v vertex 1
         * @return true jika u dan v berada pada sisi poligon yang sama <br/>
         *          false jika u dan v berada pada sisi poligon yang berbeda
         */
        public boolean isOnDifferentChains(Vertex u, Vertex v)
	{
		return ((onLeft(u) && !onLeft(v)) || (!onLeft(u) && onLeft(v)));
	}


	/**
	 * method getTheLeftEdge ini digunakan untuk mencari edge yang berada tepat di sebelah kiri suatu halfedge pada tree
	 * @param he
	 *
	 * @param r
         * @param left
         * @return
	*/
	public Node<HalfEdge> getTheLeftEdge(HalfEdge he, Node<HalfEdge> r, Node<HalfEdge> left)
	{
		if(r!= null && r.right != null){
			if(r.right.getData().equals(he))
			{
				if(r.right.left == null)
					return r;
			}
		}
		if(he.equals(r.getData()))
		{
			return r.left==null? left : bst.getTheMostRight(r.left);
		}
		else if(r.getData().compareTo(he) < 0)
		{
			return getTheLeftEdge(he, r.right, r);
		}
		else
                {
			return getTheLeftEdge(he, r.left, left);
		}
	}
}

