package mk.ukim.finki.lab2.movable;

public interface Movable {
    void moveUp() throws ObjectCanNotBeMovedException;
    void moveDown() throws ObjectCanNotBeMovedException;
    void moveLeft() throws ObjectCanNotBeMovedException;
    void moveRight() throws ObjectCanNotBeMovedException ;
    int getCurrentXPosition();
    int getCurrentYPosition();
    TYPE getType();

}
