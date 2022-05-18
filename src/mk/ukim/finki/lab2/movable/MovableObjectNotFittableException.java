package mk.ukim.finki.lab2.movable;

class MovableObjectNotFittableException extends Exception {
    private final Movable m;

    public MovableObjectNotFittableException(Movable m) {
        this.m = m;
    }

    public String getMessage() {
        if (m instanceof MovingCircle)
            return "Movable circle with center (" + m.getCurrentXPosition() + "," + m.getCurrentYPosition() +
                    ") and radius " + ((MovingCircle) m).getRadius() + " can not be fitted into the collection";
        return "Movable point with coordinates (" + m.getCurrentXPosition() + "," + m.getCurrentYPosition() +
                ") can not be fitted into the collection";
    }
}

