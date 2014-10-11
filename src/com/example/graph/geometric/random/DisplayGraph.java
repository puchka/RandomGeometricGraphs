package com.example.graph.geometric.random;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.JPanel;

// This class display a random geometric graph
public class DisplayGraph extends JPanel {

    private RandomGeometricGraph graph;
    private boolean withMinDegree = true;

    public DisplayGraph(RandomGeometricGraph graph, boolean withMinDegreeEdges) {
        this.graph = graph;
        this.withMinDegree = withMinDegreeEdges;
        //this.setSize(400, 400);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setStroke(new BasicStroke(0.5f));
        int WIDTH = this.getWidth();
        int HEIGHT = this.getHeight();
        if (graph != null) {
            if (withMinDegree) {
                Vertex minDegVertex = graph.getVertexWithMinDegree();
                int minDeg = graph.getAllNeighbors(minDegVertex).size();
                Vertex[] vertices = graph.getVertices();
                for (int i = 0; i < vertices.length; i++) {
                    Vertex v = vertices[i];
                    if(graph.getAllNeighbors(v).size() == minDeg){
                        g2d.setColor(Color.green);
                    }
                    else{
                        g2d.setColor(Color.red);
                    }                    
                    
                    int vx = WIDTH / 2 + (int) (v.getX() * WIDTH/2);
                    int vy = HEIGHT / 2 + (int) (v.getY() * HEIGHT/2);
                    g2d.fillOval(vx, vy, 5, 5);
                    List<Vertex> neighbors = graph.getAllNeighbors(v);
                    if (neighbors != null) {
                        for (int j = 0; j < neighbors.size(); j++) {
                            Vertex u = neighbors.get(j);
                            int ux = WIDTH / 2 + (int) (u.getX() * WIDTH/2);
                            int uy = HEIGHT / 2 + (int) (u.getY() * HEIGHT/2);
                            g2d.fillOval(ux, uy, 2, 2);
                            if(graph.getAllNeighbors(v).size() == minDeg 
                               || graph.getAllNeighbors(u).size() == minDeg) {
                                g2d.setColor(Color.blue);
                            }
                            else{
                                g.setColor(Color.black);
                            }
                            g2d.drawLine(vx, vy, ux, uy);

                        }
                    }
                }
            } else {
                Vertex[] vertices = graph.getVertices();
                for (int i = 0; i < vertices.length; i++) {
                    g2d.setColor(Color.red);
                    Vertex v = vertices[i];
                    int vx = WIDTH / 2 + (int) (v.getX() * WIDTH/2);
                    int vy = HEIGHT / 2 + (int) (v.getY() * HEIGHT/2);
                    g2d.fillOval(vx, vy, 5, 5);
                    List<Vertex> neighbors = graph.getAllNeighbors(v);
                    if (neighbors != null) {
                        for (int j = 0; j < neighbors.size(); j++) {
                            Vertex u = neighbors.get(j);
                            int ux = WIDTH / 2 + (int) (u.getX() * WIDTH/2);
                            int uy = HEIGHT / 2 + (int) (u.getY() * HEIGHT/2);
                            g2d.fillOval(ux, uy, 2, 2);
                            g.setColor(Color.black);
                            g2d.drawLine(vx, vy, ux, uy);
                        }
                    }
                }
            }

        }
        g2d.dispose();
    }

    private void draw() {
    }

    private void drawWithMaxDegree() {
    }
}
