package mk.ukim.finki.lab2.movable;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;


public class CirclesTest {

    public static void main(String[] args) throws MovableObjectNotFittableException{

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            try{
                if (Integer.parseInt(parts[0]) == 0) { //point
                    collection.addMovableObject(new MovingPoint(x, y, xSpeed, ySpeed));
                }
                else { //circle
                    int radius = Integer.parseInt(parts[5]);
                    collection.addMovableObject(new MovingCircle(radius, new MovingPoint(x, y, xSpeed, ySpeed)));
                }
            }
            catch (MovableObjectNotFittableException e){
                System.out.println(e.getMessage());
            }


        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());
        String name="Matej";



    }


}
