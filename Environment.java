/*
*
*   Daniel Nix 
*   CS 403 Environment
*
*
*/

public class Environment implements Types{

    /*
    *
    *   Cons Functions
    *
    */

    public Lexeme car(Lexeme env){
        return env.left;
    }

    public Lexeme cdr(Lexeme env){
        return env.right;
    }

    public Lexeme cadr(Lexeme env){
        return env.right.left;
    }

    public Lexeme setCar(Lexeme env, Lexeme val){
        env.left = val;
        return env.left;
    }

    public Lexeme setCdr(Lexeme env, Lexeme val){
        env.right = val;
        return env.right;
    }

    public boolean sameVar(Lexeme a, Lexeme b) {
        return a.valString.equals(b.valString);
    }

    /*
    *
    *   Environment functions
    *
    */ 

    public Lexeme create(){
        return extend(null, new Lexeme(TABLE, null, null), null);
    }

    public Lexeme lookup(Lexeme variable, Lexeme env){
        
        while(env != null){
            Lexeme vars = car(car(env));
            Lexeme vals = cdr(car(env));
            
            while(vars != null){
                if(sameVar(variable, car(vars)) == true){ 
                    return car(vals);
                }
                //SSystem.out.println("vars not null");
                vars = cdr(vars);
                vals = cdr(vals);
            }
            env = cdr(env);
        }
        System.out.println("Variable " + variable.valString + " is undefined.");
        System.exit(1);
        return null;
    }

    public Lexeme update(Lexeme variable, Lexeme value, Lexeme env){

        while(env != null){
            Lexeme vars = car(car(env));
            Lexeme vals = cdr(car(env));

            while(vars != null){
                if(sameVar(variable, car(vars)) == true){ 
                    setCar(vals, value);
                    return value;
                }

                vars = cdr(vars);
                vals = cdr(vals);
            }
            env = cdr(env);
        }
        System.out.println("Variable " + variable.valString + " is undefined.");
        return null;
    }

    public Lexeme insert(Lexeme variable, Lexeme value, Lexeme env){
        
        setCar(car(env), new Lexeme("JOIN", variable, car(car(env))));
        setCdr(car(env), new Lexeme("JOIN", value, cdr(car(env))));
        return value;
    }

    public Lexeme extend(Lexeme variables, Lexeme values, Lexeme env){
        return new Lexeme("ENV", new Lexeme(TABLE, variables, values), env);
    }
}