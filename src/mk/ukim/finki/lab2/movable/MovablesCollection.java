package mk.ukim.finki.lab2.movable;

import java.util.Arrays;

public class MovablesCollection {
    private Movable [] movables = new Movable[0];
    static int max_x;
    static int max_y;

    public MovablesCollection(int x,int y) {
        MovablesCollection.max_x=x;
        MovablesCollection.max_y=y;
    }

    public static void setxMax(int max_x) {
        MovablesCollection.max_x = max_x;
    }

    public static void setyMax(int max_y) {
        MovablesCollection.max_y = max_y;
    }
    public void addMovableObject(Movable m) throws MovableObjectNotFittableException
    {
        if(m instanceof MovingPoint){
            if(m.getCurrentYPosition()<0 || m.getCurrentYPosition()>MovablesCollection.max_y
                    || m.getCurrentXPosition()<0 || m.getCurrentXPosition()>MovablesCollection.max_x)
                throw new MovableObjectNotFittableException(m);
        }
        else if(m instanceof MovingCircle){
            if (m.getCurrentYPosition() < 0 || m.getCurrentYPosition() > MovablesCollection.max_y
                    || m.getCurrentXPosition() < 0 || m.getCurrentXPosition() > MovablesCollection.max_x ||
                    ((MovingCircle) m).getRadius()>m.getCurrentXPosition() || ((MovingCircle) m).getRadius()>m.getCurrentYPosition())
                throw new MovableObjectNotFittableException(m);
        }
        movables= Arrays.copyOf(movables,movables.length+1);
        movables[movables.length-1]=m;
    }
    void moveObjectsFromTypeWithDirection (TYPE type, DIRECTION direction)
    {
        for (int i=0;i< movables.length;i++) {
            if (movables[i] != null && movables[i].getType() == type ){
                if (direction == DIRECTION.DOWN){
                    try {
                        movables[i].moveDown();
                    } catch (ObjectCanNotBeMovedException e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (direction == DIRECTION.UP){
                    try {
                        movables[i].moveUp();
                    } catch (ObjectCanNotBeMovedException e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (direction == DIRECTION.LEFT){
                    try {
                        movables[i].moveLeft();
                    } catch (ObjectCanNotBeMovedException e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (direction == DIRECTION.RIGHT){
                    try {
                        movables[i].moveRight();
                    } catch (ObjectCanNotBeMovedException e) {
                        System.out.println(e.getMessage());
                    }
                }

            }

        }
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Collection of movable objects with size ");
        stringBuilder.append(movables.length);
        stringBuilder.append(":\n");

        for (Movable value : movables) {
            if (value != null)
                stringBuilder.append(value.toString());
        }


        return stringBuilder.toString();
    }

}
