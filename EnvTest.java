/*
*
*   Daniel Nix 
*   CS 403 EnvTest
*   This class is used to test Environment 
*
*/


public class EnvTest{
    static Environment env;

    public static void main(String [] args){
        System.out.println("Creating a new environment...");
        //  creating
        Lexeme created = env.create();
        System.out.println("The environment is: Created (currently empty)");
        System.out.println("Adding variable x with value 3");
        //  Adding
        Lexeme x = new Lexeme();
        x.right = new Lexeme("XVAL", 3);
        env.insert(x, x.right, created);
        System.out.println("The environment is: x with value" + x.valInt);
        //   extending
        Lexeme y = new Lexeme();
        y.right = new Lexeme("YVAL", 4);
        Lexeme z = new Lexeme();
        z.right = new Lexeme("ZVAL", "hello");
        System.out.println("Extending the environment with y:4 and z:\"hello\"");
        env.extend(y, y.right, created);
        env.extend(z, z.right, created);
    }
}