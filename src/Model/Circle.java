package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Circle<E> {
    private int x;
    private int y;
    private double radius;
    private Color color;
    //variable to draw the text inside

    public Circle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Circle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
    public Circle(double x, double y, double radius) {
        this.x = (int)x;
        this.y = (int)y;
        this.radius = radius;
    }

    public Circle(int x, int y, int radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }
    public Circle(double x, double y, double radius, Color color) {
        this.x = (int) x;
        this.y = (int) y;
        this.radius = radius;
        this.color = color;
    }

    public int getX() { return x; }

    public void setX(int x) {
        this.x = x;
    }
    public void setX(double x) {
        this.x = (int) x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public void setY(double y) {
        this.y = (int) y;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


}
