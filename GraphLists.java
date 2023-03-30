// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;
import java.util.Queue;
import java.util.LinkedList;

enum C { White, Gray, Black };

class Heap
{
    private int[] a;	   // heap array
    private int[] hPos;	   // hPos[h[k]] == k
    private int[] dist;    // dist[v] = priority of v

    private int N;         // heap size
   
    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) 
    {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }


    public boolean isEmpty() 
    {
        return N == 0;
    }


    public void siftUp( int k) 
    {
        int v = a[k];

        // code yourself
        // must use hPos[] and dist[] arrays

        dist[0] = Integer.MIN_VALUE; // set the first element to the max value
        while(dist[v] < dist[a[k/2]]) // while the parent is less than the child
        {
            a[k] = a[k/2]; // move the parent down the heap
            hPos[a[k]] = k; // update the position of the node in the heap, which is now at k
            k = k/2; // move up the heap

            
        }
        a[k] = v; // move the child up the heap
        hPos[v] = k; // update the position of the node in the heap, which is now at k
    }


    public void siftDown( int k) 
    {
        int v, j;
       
        v = a[k];  
        
        // code yourself 
        // must use hPos[] and dist[] arrays
        j = 2*k;

        while(j <= N)
        {
            if(j < N && dist[a[j+1]] < dist[a[j]]) // if the right child is greater than the left child
            {
                j++;
            }
            if(dist[v] < dist[a[j]]) // if the parent is greater than the greater child
            {
                break;
            }
            a[k] = a[j]; // move the greater child up the heap
            hPos[a[k]] = k; // update the position of the node in the heap, which is now at k
            k = j; // move down the heap
            //update the position of the node in the heap, which is now at k
            
            j = 2*k; // move down the heap
        }
        a[k] = v; // move the parent down the heap
        hPos[v] = k; // update the position of the node in the heap, which is now at k


    }


    public void insert( int x) 
    {
        a[++N] = x;
        siftUp( N);
    }


    public int remove() 
    {   
        int v = a[1];
        hPos[v] = 0; // v is no longer in heap
        a[N+1] = 0;  // put null node into empty spot
        
        a[1] = a[N--];
        siftDown(1);
        
        return v;
    }

}

class Graph {
    class Node {
        public int vert;
        public int wgt;
        public Node next;
    }
    
    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;
    
    // used for traversing graph
    private int[] visited;
    private int id;
    private C[] colour;
    private int[] parent, d,f;
    private int time;
    
    // default constructor
    public Graph(String graphFile)  throws IOException
    {
        int u, v;
        int e, wgt;
        Node t;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        // create sentinel node
        z = new Node(); 
        z.next = z;

        // initialise parent, colour, d, f arrays
        parent = new int[V+1];
        colour = new C[V+1];
        d = new int[V+1];
        f = new int[V+1];
        
        // create adjacency lists, initialised to sentinel node z       
        adj = new Node[V+1];        
        for(v = 1; v <= V; ++v)
            adj[v] = z;               
        
       // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            wgt = Integer.parseInt(parts[2]);
            
            System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));   

           
            
            // write code to put edge into adjacency matrix
            
            Node n = new Node(); //node 1
            n.vert = u; //set the ver to the vertex
            n.wgt = wgt; // set the weight to the weight
            n.next = adj[v]; // set the next node to the next node in the adjacency list
            adj[v] = n; // set the adjacency list to the new node(1st node)

            Node n2 = new Node(); //node 2
            n2.vert = v; //set the ver to the vertex
            n2.wgt = wgt;
            n2.next = adj[u]; 
            adj[u] = n2; // set the adjacency list to the new node (2nd node)
 
        }	       
    }
   
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
    
    // method to display the graph representation
    public void display() {
        int v;
        Node n;
        
        for(v=1; v<=V; ++v){
            System.out.print("\nadj[" + toChar(v) + "] ->" );
            for(n = adj[v]; n != z; n = n.next) 
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");    
        }
        System.out.println("");
    }

        //cormen's depth first search algorithm
        public void DF(int s)
        {
            int v;
    
            for(v = 1; v <= V; ++v)
            {
                colour[v] = C.White;
                parent[v] = 0;
                f[v] = 0;
            }
            time = 0;
            DFS_Visit(s);
        }
    
        public void DFS_Visit(int u)
        {
            Node t;
            int v;
    
            colour[u] = C.Gray;
            d[u] = ++time; //time increments when a node is visited
    
            System.out.println("\n DF Visiting vertex " + toChar(u) + " along edge " + toChar(parent[u]) + "--" + toChar(u));
    
            for(t = adj[u]; t != z; t = t.next)
            {
                v = t.vert;
                if(colour[v] == C.White) // if the node is white, it has not been visited
                {
                    parent[v] = u;
                    DFS_Visit(v);
                }
            }
            colour[u] = C.Black;
            f[u] = ++time; // time increments when a node is finished
            
        }
    // cormen's breadth first search algorithm
    public void BFS(int s)
    {
        int u, v;
        Node t;
        Queue q = new Queue();

        for (v = 1; v <=V; v++)
        {
            colour[v] = C.White;
            parent[v] = 0;
        }

        colour[s] = C.Gray;
        parent[s] = 0;
        q.enqueue(s);

        while(!q.isEmpty())
        {
            u = q.dequeue();
            System.out.println("\n BFS Visiting vertex " + toChar(u) + " along edge " + toChar(parent[u]) + "--" + toChar(u));

            for(t = adj[u]; t !=z; t = t.next)
            {
                v = t.vert;
                if(colour[v] == C.White)
                {
                    colour[v] = C.Gray;
                    parent[v] = u;
                    q.enqueue(v);
                }
            }
            colour[u] = C.Black;
        }

    }
    
    
    
    
    public void breadthFirst(int s)
    {

    }

    
	public void MST_Prim(int s)
	{
        int v, u;
        int wgt, wgt_sum = 0;
        int[]  dist, parent, hPos;
        Node t;

        dist = new int[V+1];
        hPos = new int[V+1];
        parent = new int[V+1];

        // initialise arrays
        for(int i = 0; i<V+1; i++)
        {
            dist[i] = Integer.MAX_VALUE;
            hPos[i] = 0;
            parent[i] = 0;
        }
        
        dist[s] = 0;
        
        Heap h =  new Heap(V, dist, hPos);
        h.insert(s);
        
        while(!h.isEmpty())
        {
            v = h.remove(); // get the vertex with the smallest weight(remove it from the heap)
            wgt_sum += dist[v]; // add the weight to the weight sum
            
            dist[v] = -dist[v]; // make the weight negative so we know it has been visited
            
            for(t = adj[v] ; t != z; t = t.next) //each neighbour of v
            {
                u = t.vert;
                wgt = t.wgt;

                if(wgt < dist[u]) // if the weight of the neighbour is less than the weight of the current vertex
                {
                    dist[u] = wgt;
                    parent[u] = v;
                    
                    if(hPos[u] == 0) // if the vertex is not in the heap
                    {
                        h.insert(u);
                    }
                    else
                    { 
                        h.siftUp(hPos[u]);
                    }
                }
            }

        }

        System.out.print("\n\nWeight of MST = " + wgt_sum + "\n");
        mst = parent;         		
	}
    
    public void showMST()
    {
            System.out.print("\n\nMinimum Spanning tree parent array is:\n");
            for(int v = 1; v <= V; ++v)
                System.out.println(toChar(v) + " -> " + toChar(mst[v]));
            System.out.println("");
    }

    public void SPT_Dijkstra(int s)
    {
    

    }

}

public class GraphLists {
    public static void main(String[] args) throws IOException
    {
        int s = 12;
        String fname = "wGraph3.txt";               

        Graph g = new Graph(fname);
       
        g.display();

        g.DF(s);
        //g.breadthFirst(s);
        g.MST_Prim(s); 
        g.showMST();  
       //g.SPT_Dijkstra(s);               
    }
}
