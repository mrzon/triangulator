//********************************************************************
//  BinarySearchTree.java
//
//  Data Structures that used too create a tree of a node which implements Comparable so the data always sorted
//  modified on April 2, 2011  by Emerson Chan Simbolon (0806334773).
//********************************************************************
package triangulation;

class Node<T extends Comparable>
{
	T data = null;
	Node<T> left = null;
	Node<T> right = null;

	public Node(T d)
	{
		data = d;
	}


	public T getData()
	{
		return data;
	}
}


public class BinarySearchTree<T extends Comparable>
{
	Node<T> root = null;
	int size = 0;

	/**
	*
	*/
	public BinarySearchTree()
	{
	}


	/**
	*
	*/
	public BinarySearchTree(Node <T> r)
	{
		root = r;
	}


	public void add(T t)
	{
		if(root!=null)
			insert(t, root);
		else
			root = new Node<T>(t);
		size++;
	}


	public void delete(T t)
	{
		root = delete(t, root);
		size--;
	}


	public Node<T> delete(T t, Node<T> r)
	{
		if(r == null)
		{
			return null;
		}

		if(r.getData().equals(t)){
			if(r.right == null && r.left == null)
			{
				return null;
			}
			if(r.right == null)
			{
				return r.left;
			}
			if(r.left == null)
			{
				return r.right;
			}
			Node<T> n = getTheMostLeftNode(r.right);
			n.right = delete(n.getData(), r.right);
			n.left = r.left;
			return n;

		}
		else
		{
			if(t.compareTo(r.getData()) > 0)
			{
				r.right = delete(t, r.right);
			}
			else
				r.left = delete(t, r.left);
		}
		return r;
	}


	public Node<T> getTheMostLeftNode(Node<T> r)
	{
		if(r.left != null)
		{
			return getTheMostLeftNode(r.left);
		}
		else
			return r;
	}


	public void insert(T t, Node<T> r)
	{
		if(r.getData().compareTo(t) < 0)
		{
			if(r.right != null)
				insert(t, r.right);
			else{
				r.right = new Node<T>(t);
			}

		}
		else if(r.getData().compareTo(t) > 0)
		{
			if(r.left != null)
				insert(t, r.left);
			else{
				r.left = new Node<T>(t);
			}

		}
	}


	public Node<T> getNode(T t)
	{
		if(root != null)
			return null;
		return getNode(t, root);
	}


	public Node<T> getNode(T t, Node<T> r)
	{
		if(t.compareTo(r.getData()) > 0)
		{
			if(r.right != null)
				return getNode(t, r.right);
			return null;
		}
		else if(t.compareTo(r.getData()) < 0)
		{
			if(r.left != null)
				return getNode(t, r.left);
			return null;
		}
		return r;
	}


	public Node<T> getRoot()
	{
		return root;
	}

	public String toString()
	{
		return root == null ? "null":print(root);
	}

	public String print(Node<T> n)
	{
		return n.data+ " (" + (n.left != null? print(n.left) : "null") + ","+ (n.right != null? print(n.right) : "null")+")";
	}


        /**
         * method getTheMostRight akan mencari node yang paling kanan dari sebuah subtree
         * @param r
         * @return
         */
        public Node<T> getTheMostRight(Node<T> r)
	{
		if(r.right != null)
		{
			return getTheMostRight(r.right);
		}
		return r;
	}
}
