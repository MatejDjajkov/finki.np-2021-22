package mk.ukim.finki.lab6.superstring;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
class SuperString {
    private LinkedList<String> stringList;
    private LinkedList<String> stringOrder;
    SuperString ()
    {
        this.stringList= new LinkedList<>();
        this.stringOrder=new LinkedList<>();

    }

    public void append(String next) {
        stringList.add(next);
        stringOrder.addFirst(next);
    }

    public void insert(String next) {

        stringList.add(0,next);
        stringOrder.addFirst(next);
    }
    private String stringvalue()
    {
        StringBuilder stringBuilder=new StringBuilder();
        for(String s:stringList)
        {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }
    private String reversestring(String string)
    {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(string);
        return stringBuilder.reverse().toString();
    }


    public boolean contains(String next) {

        return stringvalue().contains(next);
    }

    public void reverse() {
        Collections.reverse(stringList);
        String str;
        for(int i=0;i<stringList.size();i++)
        {
            str=reversestring(stringList.get(i));
            stringList.remove(i);
            stringList.add(i,str);
        }
        for(int i=0;i<stringOrder.size();i++)
        {
            str=reversestring(stringOrder.get(i));
            stringOrder.remove(i);
            stringOrder.add(i,str);
        }

    }

    public void removeLast(int nextInt) {
        for(int i=0;i<nextInt;i++)
        {
            stringList.remove(stringOrder.get(0));
            stringOrder.remove(0);
        }


    }

    @Override
    public String toString() {

        return stringvalue();
    }
}

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}