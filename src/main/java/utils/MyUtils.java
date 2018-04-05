package utils;

import java.util.Scanner;

/**
 * Created by OsipovMS on 22.03.2018.
 */
public class MyUtils {

    public static int getConsoleNom (String quest) {
        Scanner inn = new Scanner(System.in);
        int i = -1;

        System.out.print(quest);
        try{
            i = inn.nextInt();
            return i;
        }
        catch (Exception e){

        }

        return -1;
    }


    /**
     * temporary = for hand test
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(getConsoleNom("nom pls : "));
    }
}
