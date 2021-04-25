import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Graph<T, K>{

    private static class Vertex<T, K>{
        private int nDegree = 0;
        private boolean visited = false;

        private T data = null;
        private K color = null;

        private Vertex<T, K> parent = null;
        private ArrayList<Vertex<T, K>> edges = new ArrayList<>();

        Vertex(){
        }
        Vertex(T d, K c){
            data = d;
            color = c;
        }

        public void addEdge(Vertex<T, K> v){
            //if the weight is 0 that means there should be no link between vertices
            v.nDegree++;
            edges.add(v);
        }

        public String toString(){
            return data.toString();
        }

        public void printVertex(){
            System.out.print(toString()+" ");
        }
        public void printWithAdj(){
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            int c = 0;
            for( Vertex<T, K> v : edges){
                if(c < edges.size()-1) {
                    String values = v.data + ", ";
                    sb.append(values);
                }else if(c == edges.size()-1){
                    sb.append(v.data);
                }
                c++;
            }
            sb.append("}");
            System.out.println(data.toString()+": "+sb.toString());
        }
        public void printWithNDegrees(){
            System.out.println(data.toString()+": "+nDegree);
        }
        public void printWithColor(){
            System.out.println(data.toString()+": "+color);
        }
    }

    private class Edge{
        Vertex<T, K> v1;
        Vertex<T, K> v2;
        Edge(Vertex<T,K> v, Vertex<T, K> w){
            v1 = v;
            v2 = w;
        }

        public String toString(){
            return v1.toString()+"--"+ v2.toString();
        }

        public void printEdge(){
            System.out.println(toString());
        }
    }

    private final int[][] adjMatrix;
    private Vertex<T, K>[] vertices;

    public Graph(T[] vertexData){
        //if given a list of vertices without anything about their weights and edges then assume it's a disconnected graph
        instantiateVertices(vertexData);
        adjMatrix = new int[vertexData.length][vertexData.length];
    }

    public Graph(T[] vertexData, int[][] adj){
        //if given a list of vertex and the adjMatrix then update the weights and edges of each vertex according to the
        //adjMatrix
        instantiateVertices(vertexData);
        adjMatrix = adj;
        for(int i = 0; i < adjMatrix.length; i++){
            for(int j = 0; j < adjMatrix[i].length; j++){
                if(adjMatrix[i][j] != 0){
                    vertices[i].edges.add(vertices[j]);
                }
            }
        }

        //go through the adjMatrix again
    }

    private void instantiateVertices(T[] vertexData){
        vertices = new Vertex[vertexData.length];
        for(int i = 0; i < vertexData.length; i++){
            vertices[i] = new Vertex<>(vertexData[i], null);
        }
    }

    public void printMatrix(){
        System.out.print(" : ");
        for(Vertex<T, K> v : vertices){
            System.out.print(v.data+"  ");
        }
        System.out.println();

        int c = 0;
        for(int[] row : adjMatrix){
            System.out.print(vertices[c]+": ");
            for(int i : row){
                System.out.print(i+"  ");
            }
            System.out.println();
            c++;
        }
    }
    public void printVertices(){
        for(Vertex<T, K> v : vertices){
            v.printVertex();
        }
        System.out.println();
    }
    public void printAdj(){
        for(Vertex<T, K> v : vertices){
            v.printWithAdj();
        }
        System.out.println();
    }
    public void printVerticesWithColor(){
        for(Vertex<T, K> v : vertices){
            v.printWithColor();
        }
        System.out.println();
    }
    public void printVerticesWithDegrees(){
        for(Vertex<T, K> v: vertices){
            v.printWithNDegrees();
        }
        System.out.println();
    }
    public void printMatrixCodeForm(){
        System.out.print("{");
        for(int i = 0; i < adjMatrix.length; i++){
            System.out.print("{");
            for(int j = 0; j < adjMatrix[i].length; j++){
                if(j < adjMatrix[i].length-1) {
                    System.out.print(adjMatrix[i][j] + ", ");
                }else{
                    System.out.print(adjMatrix[i][j]);
                }
            }

            if(i < adjMatrix.length-1) {
                System.out.print("}, ");
            }else{
                System.out.print("}");
            }

        }
        System.out.println("}");
    }

    public void randomUndirectedGraph(){
        Random r = new Random();
        for(int i = 0; i < adjMatrix.length; i++){
            for(int j = i+1; j < adjMatrix.length; j++){

                //get a random weight
                int weight = r.nextInt(2);

                //update the weight. The addEdge() method will not add to adjList of a vertex if weight is 0
                adjMatrix[i][j] = weight;
                adjMatrix[j][i] = weight;

                if(weight == 1) {
                    vertices[i].addEdge(vertices[j]);
                    vertices[j].addEdge(vertices[i]);
                }
            }
        }
    }
    public void randomDirectedGraph(){
        Random r = new Random();
        for(int i = 0; i < adjMatrix.length; i++){
            for(int j = 0; j < adjMatrix[i].length; j++){
                int weight = r.nextInt(2);
                adjMatrix[i][j] = weight;
                if(weight == 1) {
                    vertices[i].addEdge(vertices[j]);
                }
            }
        }
    }
    public void randomDirectedAcyclicGraph(){//this produces an upperTriangular Matrix
        Random r = new Random();
        for(int i = 0; i < adjMatrix.length; i++){
            for(int j = i+1; j < adjMatrix.length; j++){
                int weight = r.nextInt(2);
                adjMatrix[i][j] = weight;
                vertices[i].addEdge(vertices[j]);
            }
        }
    }

    public void connectTo(T vData, T wData){
        //what if v is already pointing to w? That gets taken care of later

        //find indexes of the vertices in question
        int vIndex = 0;
        int wIndex = 0;
        for(int i = 0; i < vertices.length; i++){
            if(vertices[i].data.equals(vData)){
                vIndex = i;
            }
            if(vertices[i].data.equals(wData)){
                wIndex = i;
            }
        }

        //check the vertices v is already pointing to so that we know if there is already an Edge
        for(Vertex<T, K> wl : vertices[vIndex].edges){
            if(wl.data.equals(wData)){//if there is such an edge then update the weight and exit the method
                adjMatrix[vIndex][wIndex] = 1;
                wl.nDegree++;
                return;
            }
        }

        //let v point to w
        vertices[vIndex].addEdge(vertices[wIndex]);

        //update the adjMatrix to reflect this connection
        adjMatrix[vIndex][wIndex] = 1;
    }

    ArrayList<Edge> edgeList = new ArrayList<>();
    public void makeEdgeList(){
        for(int i = 0; i < adjMatrix.length; i++){
            for(int j = i+1; j < adjMatrix[i].length; j++){
                if(adjMatrix[i][j] != 0){
                    edgeList.add(new Edge(vertices[i], vertices[j]));
                }
            }
        }
    }
    HashMap<Vertex<T, K>, ArrayList<Vertex<T, K>>> nonAdjMap = new HashMap<>();
    public HashMap<Vertex<T, K>, ArrayList<Vertex<T, K>>> nonAdj(){
        for(Vertex<T, K> v : vertices) {
            ArrayList<Vertex<T, K>> nonAdjList = new ArrayList<>();
            for (Vertex<T, K> w : vertices) {
                //is v adjacent to w?
                if (!v.edges.contains(w) && !w.edges.contains(v)) {
                    nonAdjList.add(w);
                }
            }
            nonAdjMap.put(v, nonAdjList);
        }
        return nonAdjMap;
    }
    public void printNonAdj(){
        if(nonAdjMap.size() == 0){
            nonAdj();
        }

        for(Vertex<T,K> v : vertices){
            System.out.print(v+": (");
            int c = 0;
            ArrayList<Vertex<T, K>> nam = nonAdjMap.get(v);
            for(Vertex<T,K> w : nam){
                if(c < nam.size()-1) {
                    System.out.print(w + " ");
                }else{
                    System.out.print(w);
                }
                c++;
            }

            System.out.println(")");
        }
    }
    public void colorBFS(K[] colors){
        for(Vertex<T,K> v : vertices){
            v.color = colors[0];
        }

        MyQueue<Vertex<T,K>> q = new MyQueue<>();
        q.enqueue(vertices[0]);

        while(!q.isEmpty()){
            Vertex<T,K> v = q.dequeue();
            v.visited = true;
            int c = 0;

            for(Vertex<T,K> w : v.edges){

                if(v.color.equals(w.color)){
                    System.out.println(v+", "+w+", "+c);
                    c++;
                    w.color = colors[c];
                }

                if(!w.visited){
                    q.enqueue(w);
                }
            }
        }
    }

    public Vertex<T, K> BFS(T s, T e){
        //go through each vertex to see if start & end exist in the graph
        Vertex<T, K> start = new Vertex<>();
        Vertex<T, K> end = new Vertex<>();
        for(Vertex<T, K> v : vertices){
            if(s.equals(v.data)){
                start = v;
            }
            if(e.equals(v.data)){
                end = v;
            }
        }

        //if either one doesn't exist in the graph exit the method
        if(start.data == null || end.data == null){
            return start;
        }

        //if they do exist the do BFS
        MyQueue<Vertex<T, K>> q = new MyQueue<>();
        q.enqueue(start);
        while(!q.isEmpty()){
            Vertex<T, K> v = q.dequeue();
            v.visited = true;
            if(v.data.equals(end.data)){
                return v;
            }
            for(Vertex<T, K> w : v.edges){
                if(!w.visited){
                    w.parent = v;
                    q.enqueue(w);
                }
            }
        }

        unVisitAll();
        return start;
    }
    public MyQueue<Vertex<T, K>> pathTo(T s, T e){
        Vertex<T, K> end = BFS(s, e);
        MyQueue<Vertex<T, K>> path = new MyQueue<>();
        Vertex<T, K> current = end;
        while(current != null){
            path.enqueue(current);
            current = current.parent;
        }
        return path;
    }
    public String pathString(T s, T e){
        return pathTo(s, e).toString();
    }
    private void unVisitAll(){
        //since we already have the vertex we're looking for set each visited boolean back to false
        for(Vertex<T, K> v : vertices){//if pathTo() is called again all the vertices have to start off at false
            v.visited = false;
        }
    }

    public static void main(String[] args){
        Character[] C = {'A', 'B', 'C', 'D', 'E', 'F', 'G', /*'H', 'I', 'J', 'K'*/};
        Character[] I = {'1', '2', '3', '4', '5'};
        String[] colors = {"BLUE", "RED", "YELLOW", "PURPLE", "ORANGE", "GREEN", "MAGENTA", "CYAN", "BLACK"};

        int[][] adj1 = {{0,0,1,1,1,0,0}, {0,0,0,0,0,1,0}, {1,0,0,0,1,1,1}, {1,0,0,0,0,1,0}, {1,0,1,0,0,1,0}, {0,1,1,1,1,0,1}, {0,0,1,0,0,1,0}};
        int[][] adj2 = {{0,0,0,1,1}, {0,0,1,0,1}, {0,1,0,1,0}, {1,0,1,0,0}, {1,1,0,0,0}};

        for(int i = 0; i < 1; i++) {
            Graph<Character, String> G = new Graph<>(C, adj1);
            //G.randomUndirectedGraph();

            G.printMatrix();
            G.printAdj();
            //G.printNonAdj();

            G.colorBFS(colors);
            G.printVerticesWithColor();

            System.out.println("-------------\n");
        }



    }
}
