package models;

public class Item implements Comparable<Item> {
    private double distance;
    private Shape value;

    public Item(Shape v, double d) {
        value = v;
        distance = d;
    }

    public Shape getItem() { return value; }
    public double getDistance() { return distance; }

    @Override
    public int compareTo(Item o) {
        if(o.distance == distance)
            return 0;
        else if(distance > o.distance)
            return 1;
        else
            return -1;
    }

    @Override
    public String toString() {
        return String.format("%s %s %.02fpx from click\n", value.getClass(), value.toString(), distance);
    }
}
