package net.machina.photomanager.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by machina on 22.10.2015.
 */
public class CollageComponent implements Serializable {

    public CollageComponent() {}

    public CollageComponent(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    private void readObject(ObjectInputStream oInputStream) throws ClassNotFoundException, IOException {
        oInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream oOutputStream) throws IOException {
        oOutputStream.defaultWriteObject();
    }

    private static final long serialVersionUID = 0xdeadd00dL;

    private float x;
    private float y;
    private float width;
    private float height;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
