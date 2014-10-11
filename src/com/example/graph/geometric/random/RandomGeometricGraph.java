package com.example.graph.geometric.random;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import javax.swing.JFrame;

// The class representing the random geometric graph

public class RandomGeometricGraph {
      
    private Hashtable<Vertex,List<Vertex>> adjacencyList;
    private Random rand;
    private double r;
    
    public RandomGeometricGraph(int numVertices, double r, boolean inOneHemisphere){
        adjacencyList = new Hashtable<Vertex,List<Vertex>>();
        rand = new Random();
        newGraph(numVertices, r, inOneHemisphere);
        this.r = r;
    }
    
    // Return the distance r
    public double getThresholdDistance(){
        return r;
    }
    
    // Create a new graph with given number of vertices and distance
    // r
    public void newGraph(int numVertices, double r, boolean inOneHemisphere){
        adjacencyList.clear();
        for(int i=0; i < numVertices; i++){
            Vertex v = createRandomVertex(inOneHemisphere);
            adjacencyList.put(v, new ArrayList<Vertex>());
        }
        
        Vertex[] vertices = getVertices();
        for(Vertex v: vertices){
            for(Vertex u: vertices){
                if(!v.equals(u) && v.distanceFrom(u) <= r){
                    adjacencyList.get(v).add(u);
                    adjacencyList.get(u).add(v);
                }
            }
        }
    }
  
    // Return all the vertices of this graph
    public Vertex[] getVertices(){
        Vertex[] vertices = new Vertex[adjacencyList.size()];
        int i=0;
        Iterator<Vertex> it = adjacencyList.keySet().iterator();
        while(it.hasNext()){
            Vertex v = it.next();            
            vertices[i++] = v;
        }
        return vertices;
    }
    
    // Return the number of vertices of this graph
    public int getNumOfVertices(){
        return adjacencyList.size();
    }
    
    // Display this graph
    public void display(){        
        Vertex[] vertices = getVertices();
        // display all vertices
        System.out.println("[Vertices (x,y,z)]:");
        for(Vertex v: vertices){
            System.out.printf("%s (%.3f,%.3f,%.3f)\n",v.getLabel(),v.getX(),v.getY(),v.getZ());
        }
        
        System.out.println("\n[Graph Edges as adjacency list]:");
        Iterator<Vertex> it = adjacencyList.keySet().iterator();
        while(it.hasNext()){
            Vertex v = it.next();
            List<Vertex> neighbors = adjacencyList.get(v);
            System.out.print(v.toString() + " -> ");
            for(Vertex u: neighbors){
                System.out.print(u.getLabel()+ " ");
            }
            System.out.println();
        }
    }
    
    // Compute and return the vertex with max degree
    public Vertex getVertexWithMaxDegree(){
        int maxDegree = 0;
        Vertex v=null;
        
        Iterator<Vertex> it = adjacencyList.keySet().iterator();
        while(it.hasNext()){
            Vertex u = it.next();
            if(maxDegree < adjacencyList.get(u).size()){
                maxDegree = adjacencyList.get(u).size();
                v = u;
            }
        }        
        return v;
    }
    
     // Compute and return the vertex with min degree
    public Vertex getVertexWithMinDegree(){
        int minDegree = 99999;
        Vertex v=null;
        
        Iterator<Vertex> it = adjacencyList.keySet().iterator();
        while(it.hasNext()){
            Vertex u = it.next();
            if(adjacencyList.get(u).size() > 0 && (minDegree > adjacencyList.get(u).size()) ){
                minDegree = adjacencyList.get(u).size();
                v = u;
            }
        }        
        return v;
    }
    
    // Compute and return the average degree
    public double getAverageDegree(){
        Iterator<Vertex> it = adjacencyList.keySet().iterator();
        int s = 0;
        while(it.hasNext()){
            Vertex u = it.next();
            s += adjacencyList.get(u).size();

        }        
        return s/adjacencyList.size();
    }
    
    // Return all vertices incident to a given vertex
    public List<Vertex> getAllNeighbors(Vertex v){
        return adjacencyList.get(v);
    }
    
    // Create a new vertex with random location in the space (0,0,0) - (1,1,1)
    private Vertex createRandomVertex(boolean inOneHemisphere){
        double z;
        if (inOneHemisphere) {
            z = Math.random();
        } else {
            z = -1 + 2 * Math.random();
        }
        
        double theta = Math.random() * 2*Math.PI;
        double x = Math.sqrt(1 - Math.pow(z, 2)) * Math.cos(theta);
        double y = Math.sqrt(1 - Math.pow(z, 2)) * Math.sin(theta);
        
        return new Vertex("v"+ adjacencyList.size(), x,y,z);
    }
    public void planeSweepMethod() {
        Queue<Vertex> E = new PriorityQueue<Vertex>(2000, new Comparator() {

            @Override
            public int compare(Object arg0, Object arg1) {
                Vertex v0 = (Vertex) arg0;
                Vertex v1 = (Vertex) arg1;
                double phi0 = Math.asin(v0.getZ());
                double theta0 = Math.atan(v0.getY()/v0.getX());
                double phi1 = Math.asin(v1.getZ());
                double theta1 = Math.atan(v1.getY()/v0.getX());
                if (phi0 > phi1) {
                    return -1;
                }
                if (phi0 < phi1) {
                    return 1;
                }
                return 0;
            }
        });
        Iterator<Vertex> it = adjacencyList.keySet().iterator();
        while(it.hasNext()) {
            Vertex u = it.next();
            E.add(u);
            
        }
        int n = 0;
        while (!E.isEmpty()) {
            Vertex F = E.poll();
            Queue S = new PriorityQueue<>(200, new Comparator() {
    
                @Override
                public int compare(Object arg0, Object arg1) {
                    Vertex v0 = (Vertex) arg0;
                    Vertex v1 = (Vertex) arg1;
                    double phi0 = Math.asin(v0.getZ());
                    double theta0 = Math.atan(v0.getY()/v0.getX());
                    double phi1 = Math.asin(v1.getZ());
                    double theta1 = Math.atan(v1.getY()/v0.getX());
                    if (theta0 > theta1) {
                        return -1;
                    }
                    if (theta0 < theta1) {
                        return 1;
                    }
                    return 0;
                }
            });

            S.add(F);
            Vertex s = E.peek();
            double phiF = Math.asin(F.getZ());
            double phi = Math.asin(s.getZ());
            //length of the cord : l = 2*r*SIN(alpha/2)
            while (2 * Math.sin((phi - phiF) / 2) <= r) {
                S.add(s);
                E.remove();
                s = E.peek();
                if (s == null) {
                    break;
                }
                phiF = Math.asin(F.getZ());
                phi = Math.asin(s.getZ());
                
            }
            Vertex cur = (Vertex) S.poll();
            while (!S.isEmpty() && cur != null) {
                Vertex next = (Vertex) S.peek();
                if (next !=null && cur.distanceFrom(next) <= r) {
                    S.remove();
                    n++;
                }
                cur = (Vertex) S.poll();
            }
        }
        System.out.println("Number of pairs checked for the distance bound edge verification : " + n);
        System.out.println("The number of all pairs : " + 
                            fact(adjacencyList.size()).divide(BigInteger.valueOf(2).multiply(fact(adjacencyList.size() - 2))));
        //return E;
    }
    public static BigInteger fact(int a)
    {
        BigInteger factorial = BigInteger.ONE;

        BigInteger factz = BigInteger.ONE;

        if(a == 1)
        {
            return factorial;
        }

        else
        {
            return factz.multiply(fact(a-1));
        }
    }
    public static void displayGraph(RandomGeometricGraph graph){
        String title = String.format("Random Geometric Graph (%d vertices, r = %.3f)",graph.getNumOfVertices(), graph.getThresholdDistance());
        JFrame frame = new JFrame(title);
        DisplayGraph display = new DisplayGraph(graph, false);
        display.setPreferredSize(new Dimension(450,450));
        frame.add(display, BorderLayout.CENTER);
        //frame.setPreferredSize(new Dimension(400, 400));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void displayGraphWithMinDegree(RandomGeometricGraph graph){
        String title = String.format("Random Geometric Graph (%d vertices, r = %.3f)",graph.getNumOfVertices(), graph.getThresholdDistance());
        JFrame frame = new JFrame(title);
        DisplayGraph display = new DisplayGraph(graph, true);
        display.setPreferredSize(new Dimension(450,450));
        frame.add(display, BorderLayout.CENTER);
        //frame.setPreferredSize(new Dimension(400, 400));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String args[]){
        
        // create a random geometric graph with 2000 vertices and r = 0.085        
        RandomGeometricGraph graph = new RandomGeometricGraph(2000, 0.085, false);     
        // display the graph
        System.out.printf("\nRGG (%d vertices, r = %.3f):\n\n",
                            graph.getNumOfVertices(),
                            graph.getThresholdDistance());
        graph.display();
        
        // Using the plane sweep method count how many pairs are checked for the distance bound 
        // edge verification and compare with the number of all pairs.
        graph.planeSweepMethod();
        
        // create a random geometric graph with 500 vertices and r = 0.094        
        RandomGeometricGraph graph2 = new RandomGeometricGraph(500, 0.094 ,true);     
        // display the graph
        System.out.printf("\nRGG (%d vertices, r = %.3f):\n\n",
                            graph2.getNumOfVertices(),
                            graph2.getThresholdDistance());
        graph2.display();
        displayGraphWithMinDegree(graph2);
        
        // get the vertex with max degree
        Vertex vMax = graph2.getVertexWithMaxDegree();
        // get all edges incident on the max degree vertex
        List<Vertex> neighbors = graph2.getAllNeighbors(vMax);
        System.out.println("Max Degree = " + neighbors.size());
        
        // get the vertex with max degree
        Vertex vMin = graph2.getVertexWithMinDegree();
        // get all edges incident on the max degree vertex
        neighbors = graph2.getAllNeighbors(vMin);
        System.out.println("Min Degree = " + neighbors.size());
        
        // get average degree
        System.out.println("Average Degree = " + graph2.getAverageDegree());
        
    }
}
