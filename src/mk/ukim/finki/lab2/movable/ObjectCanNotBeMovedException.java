package mk.ukim.finki.lab2.movable;

public class ObjectCanNotBeMovedException extends Exception{
    private final Movable m;
    private int badX;
    private int badY;

    public ObjectCanNotBeMovedException(Movable m,int badX,int badY) {
        this.m = m;
        this.badX = badX;
        this.badY = badY;
    }

    public String getMessage(){
        if(m instanceof MovingCircle)
            return "Circle with center (" + m.getCurrentXPosition() + "," + m.getCurrentYPosition() +
                    ") and radius " + ((MovingCircle) m).getRadius() + " is out of bounds";
        return "Point (" + badX + "," + badY +
                ") is out of bounds";
    }
}
