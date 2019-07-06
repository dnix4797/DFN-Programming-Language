//Pretty.java

public class Pretty implements Types{

    public static void main(String [] args) throws Exception {

        String fileName = args[0];
        System.out.println(fileName);
        Parser temp = new Parser();
        Lexeme root = temp.Parse(fileName);
        prettyPrint(root);
        System.out.println();
        return;
    }

    public static void prettyPrint(Lexeme tree) throws Exception {
        if(tree == null) {
            return;
        }

        switch(tree.type) {

            case STRING: {
                System.out.print("\"" + tree.valString + "\"");
                break;
            }
            case BOOLEAN: {
                System.out.print(tree.boolVal);
                break;
            }
            case SEMICOLON: {
                System.out.print(";");
                break;
            }
            case VARIABLE: {
                System.out.print(tree.valString);
                break;
            }
            case INTEGER: {
                //System.out.println("here dumbass");
                System.out.print(tree.valInt);
                break;
            }
            case REAL: {
                System.out.print(tree.valDouble);
                break;
            }
            case FUNCCALL: {
                prettyPrint(tree.left);
                System.out.print("(");
                prettyPrint(tree.right);
                System.out.print(")");
                break;
            }
            case OPAREN: {
                System.out.print("(");
                prettyPrint(tree.right);
                System.out.print(")");
                break;
            }
            //  operations
            case EQUIVALENT: {
                prettyPrint(tree.left);
                System.out.print(" == ");
                prettyPrint(tree.right);
                break;
            }
            case LESSTHAN: {
                prettyPrint(tree.left);
                System.out.print(" < ");
                prettyPrint(tree.right);
                break;
            }
            case GREATERTHAN: {
                prettyPrint(tree.left);
                System.out.print(" > ");
                prettyPrint(tree.right);
                break;
            }
            case LTE: {
                prettyPrint(tree.left);
                System.out.print(" <= ");
                prettyPrint(tree.right);
                break;
            }
            case GTE: {
                prettyPrint(tree.left);
                System.out.print(" >= ");
                prettyPrint(tree.right);
                break;
            }
            case PLUS: {
                prettyPrint(tree.left);
                System.out.print(" + ");
                prettyPrint(tree.right);
                break;
            }
            case MINUS: {
                prettyPrint(tree.left);
                System.out.print(" - ");
                prettyPrint(tree.right);
                break;
            }
            case TIMES: {
                prettyPrint(tree.left);
                System.out.print(" * ");
                prettyPrint(tree.right);
                break;
            }
            case DIVIDE: {
                prettyPrint(tree.left);
                System.out.print(" / ");
                prettyPrint(tree.right);
                break;
            }
            case MOD: {
                prettyPrint(tree.left);
                System.out.print(" % ");
                prettyPrint(tree.right);
                break;
            }
            case SQUARED: {
                prettyPrint(tree.left);
                System.out.print("$");
                break;
            }
            case ASSIGN: {
                prettyPrint(tree.left);
                System.out.print(" = ");
                prettyPrint(tree.right);
                break;
            }

            case PLUSPLUS: {
                prettyPrint(tree.left);
                System.out.print("++");   
                break;             
            }
            case MINMIN: {
                prettyPrint(tree.left);
                System.out.print("--");
                break;
            }

            case AND: {
                prettyPrint(tree.left);
                System.out.print(" && ");
                prettyPrint(tree.right);
                break;
            }
            case OR: {
                prettyPrint(tree.left);
                System.out.print(" || ");
                prettyPrint(tree.right);
                break;
            }
            
            //  Variables and Function Definitions
            case PRINT: {
                System.out.print("print(");
                prettyPrint(tree.right);
                System.out.print(")");
                break;
            }
            case PRINTLN: {
                System.out.print("println(");
                prettyPrint(tree.right);
                System.out.println(")");
                break;
            }
            case PROGRAM: {
                while(tree != null) {
                    prettyPrint(tree.left); 
                    tree = tree.right;
                }
                break;
            }
            case FUNCTIONDEF: {
                System.out.print("function ");
                prettyPrint(tree.left);
                System.out.print("(");
                prettyPrint(tree.right.left); //fails here
                System.out.print(")");
                prettyPrint(tree.right.right);
                break;
            }     
            case NEWDEF: {
                System.out.print("new ");
                prettyPrint(tree.right);
                break;
            }
            case IMPORTDEF: {
                System.out.print("import ");
                prettyPrint(tree.right.left);
                break;
            }
            case VARDEF: {
                System.out.print("var ");
                prettyPrint(tree.left);
                prettyPrint(tree.right);
                break;
            }
            case LAMBDADEF:{
                System.out.print("lambda( ");
                prettyPrint(tree.left);
                System.out.print(")");
                prettyPrint(tree.right);   
                break;           
            }
            case ARRAY: {
                prettyPrint(tree.left);
                System.out.print("[");
                prettyPrint(tree.right);
                System.out.print("]");
                break;
            }
            case EXPR: {
                prettyPrint(tree.left);
                if(tree.right != null){ //if op or shortOp
                    //System.out.println("tree.right of expr not null");
                    if(tree.right.type == GLUE){//if operation, not shortOp
                        prettyPrint(tree.right.left);
                        prettyPrint(tree.right.right);
                        break;
                    }
                    else{   //shortOp
                        prettyPrint(tree.right);
                        break;
                    }
                }
                break;
            }
            case EXPRLIST: {
                prettyPrint(tree.left);
                tree = tree.right;
                while(tree != null){
                    System.out.print(",");
                    prettyPrint(tree.left);
                    tree = tree.right;
                }
                break;
            }
            case BLOCK: {
                System.out.print("{");
                prettyPrint(tree.left);
                System.out.print("}");
                break;
            }
            case IFSTATEMENT: {
                System.out.print("if(");
                prettyPrint(tree.left);
                System.out.print(")");
                prettyPrint(tree.right.left);
                prettyPrint(tree.right.right);
                break;
            }
            case ELSE: {//issues with else ifs
                System.out.print("else ");
                prettyPrint(tree.right);    
                break;
            }
            case WHILE: {
                System.out.print("while(");
                prettyPrint(tree.left);
                System.out.print(")");
                prettyPrint(tree.right);
                break;
            }
            case STATEMENT: {
                prettyPrint(tree.left);
                if(tree.left.type == VARDEF || tree.left.type == FUNCCALL || tree.left.type == EXPR || tree.left.type == PRINT || tree.left.type == PRINTLN){
                    System.out.print(";");
                }
                break;
            }
            case STATEMENTS: {
                prettyPrint(tree.left);
                prettyPrint(tree.right);
                break;
            }
            case OPTINIT: {
                //System.out.println("before tree.left");
                prettyPrint(tree.left);
                //System.out.println("before tree.right");
                prettyPrint(tree.right);
                //System.out.println("after tree.right");
                break;
            }
            case PARAMLIST: {
                prettyPrint(tree.left);
                if(tree.left != null){    //fails here
                    if(tree.right != null){
                        System.out.print(",");
                        prettyPrint(tree.right);
                    }
                }
                break;
            }
            case RETURN: {
                System.out.print("return");
                break;
            }
            case CLASSDEF: {
                System.out.print("class ");
                prettyPrint(tree.left);
                System.out.print("(");
                prettyPrint(tree.right.left);
                System.out.print(")");
                prettyPrint(tree.right.right);
                break;
            }


            default:
                System.err.printf("\nFatal error in Evaluator.Java: bad expression %s\n", tree.type);
                System.exit(1);
                return;
        }


    }





}