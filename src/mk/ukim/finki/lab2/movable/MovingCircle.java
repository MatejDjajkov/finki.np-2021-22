package mk.ukim.finki.lab2.movable;

public class MovingCircle implements Movable {
    private int radius;
    private MovingPoint movingPoint;

    public MovingCircle(int radius, MovingPoint movingPoint) {
        this.radius = radius;
        this.movingPoint = movingPoint;
    }
    public int getRadius()
    {
        return radius;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if(getCurrentYPosition()>MovablesCollection.max_y || getCurrentYPosition()+radius>MovablesCollection.max_y)
        {
            throw new ObjectCanNotBeMovedException(this,getCurrentXPosition(),getCurrentYPosition());
        }
        movingPoint.moveUp();

    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if(getCurrentYPosition()<0 || getCurrentYPosition()-radius<0)
        {
            throw new ObjectCanNotBeMovedException(this,getCurrentXPosition(),getCurrentYPosition());
        }
        movingPoint.moveDown();

    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if (getCurrentXPosition() < 0 || (radius > getCurrentXPosition())) {
            throw new ObjectCanNotBeMovedException(this,getCurrentXPosition(),getCurrentYPosition());
        }
        movingPoint.moveLeft();

    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if (getCurrentXPosition() > MovablesCollection.max_x || (radius + getCurrentXPosition()) > MovablesCollection.max_x) {
            throw new ObjectCanNotBeMovedException(this,getCurrentXPosition(),getCurrentYPosition());
        }
        movingPoint.moveRight();

    }

    @Override
    public int getCurrentXPosition() {
        return movingPoint.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return movingPoint.getCurrentYPosition();
    }

    @Override
    public TYPE getType() {
        return TYPE.CIRCLE;
    }

    @Override
    public String toString() {
        return "Movable circle with center coordinates ("+movingPoint.getCurrentXPosition()+","+movingPoint.getCurrentYPosition()+") and radius " + radius + "\n";
    }
}
