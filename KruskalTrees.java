// Kruskal's Minimum Spanning Tree Algorithm
// Union-find implemented using disjoint set trees without compression

// Heap and Graph classes are renamed to Heap1 and Graph1 to avoid conflict with the Heap and Graph classes in the GraphLists.java file

import java.io.*;

class Edge
{
    public int u, v, wgt;

    public Edge()
    {
        u = 0;
        v = 0;
        wgt = 0;
    }

    public Edge(int x, int y, int w)
    {
        u = x;
        v = y;
        wgt = w;
    }

    public void show()
    {
        System.out.print("Edge " + toChar(u) + "--" + wgt + "--" + toChar(v) + "\n");
    }

    // convert vertex into char for pretty printing
    private char toChar(int u)
    {
        return (char) (u + 64);
    }
}

class Heap1
{
    private int[] h;
    int N, Nmax;
    Edge[] edge;

    // Bottom up heap construc
    public Heap1(int _N, Edge[] _edge)
    {
        int i;
        Nmax = N = _N;
        h = new int[N + 1];
        edge = _edge;

        // initially just fill heap array with
        // indices of edge[] array.
        for (i = 0; i <= N; ++i)
            h[i] = i;

        // Then convert h[] into a heap
        // from the bottom up.
        for (i = N / 2; i > 0; --i)
            siftDown(i);

    }

    private void siftDown(int k)
    {
        int e, j;

        e = h[k];
        j = 2 * k;
        while (k <= N / 2)
        {
            if (j < N && edge[h[j]].wgt > edge[h[j + 1]].wgt)
                ++j;

            if (edge[e].wgt <= edge[h[j]].wgt)
            {
                break;

            }
            h[k] = h[j];
            k = j;
            j = 2 * k;
        }
        h[k] = e;
    }

    public int remove()
    {
        h[0] = h[1];
        h[1] = h[N--];
        siftDown(1);
        return h[0];
    }
}

/****************************************************
 * UnionFind partition to support union-find operations Implemented simply using
 * Discrete Set Trees
 *****************************************************/

class UnionFindSets
{
    private int[] treeParent;
    private int N;

    public UnionFindSets(int V)
    {
        N = V;
        treeParent = new int[V + 1];
        // initially each vertex is in its own set
        for (int i = 1; i <= N; ++i)
            treeParent[i] = 0;
        

    }

    public int findSet(int vertex)
    {
        if (treeParent[vertex] == 0)
            return vertex;
        else
        return 0;
    }

    public void union(int set1, int set2)
    {
        treeParent[set1] = set2;
    }

    public void showTrees()
    {
        int i;
        for (i = 1; i <= N; ++i)
            System.out.print(toChar(i) + "->" + toChar(treeParent[i]) + "  ");
        System.out.print("\n");
    }

    public void showSets()
    {
        int u, root;
        int[] shown = new int[N + 1];
        for (u = 1; u <= N; ++u)
        {
            root = findSet(u);
            if (shown[root] != 1)
            {
                showSet(root);
                shown[root] = 1;
            }
        }
        System.out.print("\n");
    }

    private void showSet(int root)
    {
        int v;
        System.out.print("Set{");
        for (v = 1; v <= N; ++v)
            if (findSet(v) == root)
                System.out.print(toChar(v) + " ");
        System.out.print("}  ");

    }

    private char toChar(int u)
    {
        return (char) (u + 64);
    }
}

class Graph1
{
    private int V, E;
    private Edge[] edge;
    private Edge[] mst;

    public Graph1(String graphFile) throws IOException
    {
        int u, v;
        int w, e;

        FileReader fr = new FileReader(graphFile);
        try (BufferedReader reader = new BufferedReader(fr))
        {
            String splits = " +"; // multiple whitespace as delimiter
            String line = reader.readLine();
            String[] parts = line.split(splits);
            System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
            
            V = Integer.parseInt(parts[0]);
            E = Integer.parseInt(parts[1]);
            
            // create edge array
            edge = new Edge[E + 1];
            
            // read the edges
            System.out.println("Reading edges from text file");
            for (e = 1; e <= E; ++e)
            {
                line = reader.readLine();
                parts = line.split(splits);
                u = Integer.parseInt(parts[0]);
                v = Integer.parseInt(parts[1]);
                w = Integer.parseInt(parts[2]);
                
                System.out.println("Edge " + toChar(u) + "--(" + w + ")--" + toChar(v));
                
                // create Edge object
                edge[e] = new Edge(u, v, w); // u(first vertex), v(second vertex), w(weight)
            }
        }
        catch (NumberFormatException e1)
        {
            e1.printStackTrace();
        }
    }
    
    /**********************************************************
     * Kruskal's minimum spanning tree algorithm
     **********************************************************/
    public Edge[] MST_Kruskal()
    {
        int ei, i = 0;
        Edge e;
        int uSet, vSet;
        UnionFindSets partition;

        // create edge array to store MST
        // Initially it has no edges.
        mst = new Edge[V - 1];
        for(i = 0; i < V - 1; ++i)
            mst[i] = new Edge(0, 0, 0);


        // priority queue for indices of array of edges
        Heap1 h = new Heap1(E, edge);

        // create partition of singleton sets for the vertices
        partition = new UnionFindSets(V);

        // repeat until MST has V-1 edges
        i = 0;
        while (i < V - 1)
        {
            // remove edge with minimum weight from heap
            ei = h.remove();
            e = edge[ei];

            // find the sets containing the two endpoints
            uSet = partition.findSet(e.u);
            vSet = partition.findSet(e.v);

            // if endpoints are in different sets
            // then add edge to MST and union the two sets
            if (uSet != vSet)
            {
                mst[i++] = e;
                partition.union(uSet, vSet);
            }
        }

        return mst;
    }

    // convert vertex into char for pretty printing
    private char toChar(int u)
    {
        return (char) (u + 64);
    }
    
    // show the total cost of the minimum spanning tree
    public void totalCost()
    {
        int total = 0;
        for (int i = 0; i < V - 1; ++i)
        {
            total += mst[i].wgt;
        }
        System.out.println("The total cost of the minimum spanning tree is: " + total);
    }

    public void showMST()
    {
        System.out.print("\nMinimum spanning tree build from following edges:\n");
        for (int e = 0; e < V - 1; ++e)
        {
            mst[e].show();
        }
        System.out.println();
        totalCost();

    }

} // end of Graph class

// test code
class KruskalTrees
{
    public static void main(String[] args) throws IOException
    {
        String fname = "wGraph3.txt";
        // System.out.print("\nInput name of file with graph definition: ");
        // fname = Console.ReadLine();

        Graph1 graph = new Graph1(fname);

        graph.MST_Kruskal();

        graph.showMST();

    }
}
