package mk.ukim.finki.lab2.movable;

import java.util.Objects;

public class MovingPoint implements Movable {
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    public MovingPoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if(y+ySpeed>MovablesCollection.max_y)
        {
            throw new ObjectCanNotBeMovedException(this,x,y+ySpeed);
        }
        y+=ySpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if(y-ySpeed<0)
        {
            throw new ObjectCanNotBeMovedException(this,x,y-ySpeed);
        }
        y-=ySpeed;

    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if(x-xSpeed<0)
        {
            throw new ObjectCanNotBeMovedException(this,x-xSpeed,y);
        }
        x-=xSpeed;

    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if(x+xSpeed>MovablesCollection.max_x)
        {
            throw new ObjectCanNotBeMovedException(this,x+xSpeed,y);
        }
        x+=xSpeed;

    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }
    @Override
    public TYPE getType()
    {
        return TYPE.POINT;
    }

    @Override
    public String toString() {
        return "Movable point with coordinates ("+x+","+y+")\n";
    }
}
