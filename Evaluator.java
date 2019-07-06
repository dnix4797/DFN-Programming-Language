/*
 *	Daniel Nix
 *	Evaluator.Java
 * 	CS 403 Dr. Lusth Spring 2019
 *
 *
*/

import java.util.ArrayList;

public class Evaluator implements Types {

    private static Lexeme glob;    //global lexeme
    private static Environment e;  // we need an environment

    public Evaluator(){
        e = new Environment();
        glob = e.create();
    }

    /*
    *
    *   Time to evaluate this thing
    *
    */
    public static void main(String[] args) throws Exception {
        String fileName = args[0];
        System.out.println(fileName);
        Parser temp = new Parser();
        Lexeme tree = temp.Parse(fileName);
        Evaluator run = new Evaluator();
        Lexeme env = run.glob;
        run.eval(tree, env);
        System.out.println();
        return;
    }
    private Lexeme eval(Lexeme tree, Lexeme env){
        
        if(tree == null){
            return null;
        }
        //System.out.println(tree.type);
        //  case statements:       
        switch(tree.type){

            case STRING: {
                return tree;
            }
            case BOOLEAN: {
                return tree;
            }
            case SEMICOLON: {
                return tree;
            }
            case VARIABLE: {
                return e.lookup(tree, env);
            }
            case INTEGER: {
                return tree;
            }
            case REAL: {
                return tree;
            }
            case FUNCCALL: {
                return evalFuncCall(tree, env);
            }
            /*case OPAREN: {
                return evalParen(tree, env);
            }*/
            //  operations
            case EQUIVALENT: {
                return evalOp(tree, env);
            }
            case LESSTHAN: {
                return evalOp(tree, env);
            }
            case GREATERTHAN: {
                return evalOp(tree, env);
            }
            case LTE: {
                return evalOp(tree, env);
            }
            case GTE: {
                return evalOp(tree, env);
            }
            case PLUS: {
                return evalOp(tree, env);
            }
            case MINUS: {
                return evalOp(tree, env);
            }
            case TIMES: {
                return evalOp(tree, env);
            }
            case DIVIDE: {
                return evalOp(tree, env);
            }
            case MOD: {
                return evalOp(tree, env);
            }
            case SQUARED: {
                return evalShortOp(tree, env);
            }
            case ASSIGN: {
                return evalOp(tree, env);
            }

            case PLUSPLUS: {
                return evalShortOp(tree, env);             
            }
            case MINMIN: {
                return evalShortOp(tree, env);
            }

            case AND: {
                return evalOpShortCircuit(tree, env);
            }
            case OR: {
                return evalOpShortCircuit(tree, env);
            }
            
            //  Variables and Function Definitions
            case PRINTLN: {
                return evalPrint(tree, env);
            }
            case PRINT: {
                return evalPrint(tree, env);
            }
            case PROGRAM: {
                return evalProgram(tree, env);
            }
            case FUNCTIONDEF: {
                return evalFuncDef(tree, env);
            }     
            case NEWDEF: {
                return evalNewDef(tree, env);
            }
            case IMPORTDEF: {
                return evalImportDef(tree, env);
            }
            case VARDEF: {
                return evalVarDef(tree, env);
            }
            case LAMBDADEF: {
                return evalLambda(tree, env);         
            }
            case ARRAY: {
                return null;
            }
            case EXPR: {
                return evalExpr(tree, env);
            }
            case EXPRLIST: {
                return evalExprList(tree, env);
            }
            case BLOCK: {
                return evalBlock(tree, env);
            }
            case IFSTATEMENT: {
                return evalIfStatement(tree, env);
            }
            case ELSE: {//issues with else ifs
                return evalOptElse(tree, env);
            }
            case WHILE: {
                return evalWhile(tree, env);
            }
            case STATEMENT: {
                return evalStatement(tree, env);
            }
            case STATEMENTS: {
                return evalStatements(tree, env);
            }
            case OPTINIT: {
                return evalOptInit(tree, env);
            }
            case PARAMLIST: {
                return evalParamList(tree, env);
            }
            case RETURN: {
                return evalReturn(tree, env);
            }
            case CLASSDEF: {
                return evalClassDef(tree, env);
            }

            default:
                System.err.printf("\nFatal error in Evaluator.Java: bad expression %s\n", tree.type);
                System.exit(1);
                return null;
        }

    }

    /*
    *
    *   High-end Evaluations
    *
    */

    private Lexeme evalProgram(Lexeme t, Lexeme env) {
        while(t != null) {
            eval(t.left, env);
            t = t.right;
        }
        return null;
    }

    private Lexeme evalDef(Lexeme t, Lexeme env) {
        switch(t.type){
            case FUNCTIONDEF:
                return evalFuncDef(t, env);
            case VARDEF:
                return evalVarDef(t, env);
            case CLASSDEF:
                return evalClassDef(t, env);
            case IMPORTDEF:
                return evalImportDef(t, env);
            case METHODDEF:
                return evalMethodDef(t, env);
            case LAMBDA:
                return evalLambda(t, env);

            default:
                System.out.println("Definition error: " + t.type + " cannot be defined.");
                System.exit(1);
                return null;
        }
    }

    /*
    *
    *   Evaluators for all parse trees!
    *
    */

    private Lexeme evalLambda(Lexeme t, Lexeme env) {

        while(t != null){
            eval(t.left, env);
            t = t.right;
        }
        return null;

    }

    private Lexeme evalImportDef(Lexeme t, Lexeme env) {
        return new Lexeme(GLUE, t.right, null);
    }

    private Lexeme evalNewDef(Lexeme t, Lexeme env) {
        Lexeme closure = new Lexeme("CLOSURE", env, t);
        e.insert(t.right, closure, env);
        return null;
    }

    private Lexeme evalClassDef(Lexeme t, Lexeme env) {
        Lexeme classID = eval(t.left, env);
        Lexeme classExprs = evalExprList(t.right.left, env);
        Lexeme classBlock = evalBlock(t.right.right, env);
        return new Lexeme(GLUE, classID, new Lexeme(GLUE, classExprs, classBlock));
    }

    private Lexeme evalShortOp(Lexeme t, Lexeme env){
        //System.out.println(t.type);
        switch(t.type){
            case PLUSPLUS: return evalPlusPlus(t, env);
            case MINMIN: return evalMinMin(t, env);
            case SQUARED: return evalSquared(t, env);
            default:
                return null;
        }
    }

    private Lexeme evalOpShortCircuit(Lexeme t, Lexeme env) {
        
        switch(t.type){
            case AND: 
                return evalAnd(t, env);
            case OR:
                return evalOr(t, env);
            default:
                return null;
        }

    }

    private Lexeme evalAnd(Lexeme t, Lexeme env) {
        if(eval(t.left, env).boolVal && eval(t.right, env).boolVal){
            return new Lexeme("BOOLEAN", true);
        }
        else{
            return new Lexeme("BOOLEAN", false);
        }
    }

    private Lexeme evalOr(Lexeme t, Lexeme env) {
        if(eval(t.left, env).boolVal || eval(t.right, env).boolVal){
            return new Lexeme("BOOLEAN", true);
        }
        else{
            return new Lexeme("BOOLEAN", false);
        }
    }

    private Lexeme evalOp(Lexeme tree, Lexeme env) {

        switch(tree.type){
            case EQUIVALENT: 
                return evalEQ(tree, env);
            case LESSTHAN: 
                return evalLT(tree, env);
            case GREATERTHAN: 
                return evalGT(tree, env);
            case LTE: 
                return evalLTE(tree, env);
            case GTE: 
                return evalGTE(tree, env);
            case PLUS: 
                return evalPlus(tree, env);
            case MINUS: 
                return evalMin(tree, env);
            case TIMES: 
                return evalTimes(tree, env);
            case DIVIDE: 
                return evalDiv(tree, env);
            case MOD: 
                return evalMod(tree, env);
            case SQUARED: 
                return evalSquared(tree, env);
            case ASSIGN: 
                return evalAss(tree, env);
            case PLUSPLUS: 
                return evalPlusPlus(tree, env);
            case MINMIN: 
                return evalMinMin(tree, env);
            
            default:
                return null;

        }

    }

    /*
    *
    *   evalOp helper functions!
    *
    */

    private Lexeme evalEQ(Lexeme t, Lexeme env) {
        Lexeme left = eval(t.left, env);
        Lexeme right = eval(t.right, env);

        if(left.type.equals(INTEGER) && right.type.equals(INTEGER)){
            return new Lexeme(BOOLEAN, left.valInt == right.valInt);
        }
        else if(left.type.equals(INTEGER) && right.type.equals(REAL)){
            return new Lexeme(BOOLEAN, left.valInt == right.valDouble);
        }
        else if(left.type.equals(REAL) && right.type.equals(INTEGER)){
            return new Lexeme(BOOLEAN, left.valDouble == right.valInt);
        }
        else if(left.type.equals(REAL) && right.type.equals(REAL)){
            return new Lexeme(BOOLEAN, left.valDouble == right.valDouble);
        }
        else if(left.type.equals(STRING) && right.type.equals(STRING)){
            return new Lexeme(BOOLEAN, left.valString.equals(right.valString));
        }
        else if(left.type.equals(BOOLEAN) && right.type.equals(BOOLEAN)){
            return new Lexeme(BOOLEAN, left.boolVal == right.boolVal);
        }
        else if(left.type.equals(EMPTY) && right.type.equals(EMPTY)){
            return new Lexeme(BOOLEAN, true);
        }
        else if(left.type.equals(EMPTY) && !right.type.equals(EMPTY)){
            return new Lexeme(BOOLEAN, false);
        }
        else if(!left.type.equals(EMPTY) && right.type.equals(EMPTY)){
            return new Lexeme(BOOLEAN, false);
        }
        else{
            System.out.println("\nFatal error in Evaluator.java: type mismatch. You cannot compare " + left.type + " with " + right.type + ".\n");
            System.exit(1);
            return null;
        }
    }

    private Lexeme evalLT(Lexeme t, Lexeme env) {
        Lexeme left = eval(t.left, env);
        Lexeme right = eval(t.right, env);

        if(left.type.equals(INTEGER) && right.type.equals(INTEGER)){
            return new Lexeme(BOOLEAN, left.valInt < right.valInt);
        }
        else if(left.type.equals(INTEGER) && right.type.equals(REAL)){
            return new Lexeme(BOOLEAN, left.valInt < right.valDouble);
        }
        else if(left.type.equals(REAL) && right.type.equals(INTEGER)){
            return new Lexeme(BOOLEAN, left.valDouble < right.valInt);
        }
        else if(left.type.equals(REAL) && right.type.equals(REAL)){
            return new Lexeme(BOOLEAN, left.valDouble < right.valDouble);
        }
        else{
            System.out.println("\nFatal error in Evaluator.java: type mismatch. You cannot compare " + left.type + " with " + right.type + ".\n");
            System.exit(1);
            return null;
        }
    }

    private Lexeme evalGT(Lexeme t, Lexeme env) {
        Lexeme left = eval(t.left, env);
        Lexeme right = eval(t.right, env);

        if(left.type.equals(INTEGER) && right.type.equals(INTEGER)){
            return new Lexeme(BOOLEAN, left.valInt > right.valInt);
        }
        else if(left.type.equals(INTEGER) && right.type.equals(REAL)){
            return new Lexeme(BOOLEAN, left.valInt > right.valDouble);
        }
        else if(left.type.equals(REAL) && right.type.equals(INTEGER)){
            return new Lexeme(BOOLEAN, left.valDouble > right.valInt);
        }
        else if(left.type.equals(REAL) && right.type.equals(REAL)){
            return new Lexeme(BOOLEAN, left.valDouble > right.valDouble);
        }
        else{
            System.out.println("\nFatal error in Evaluator.java: type mismatch. You cannot compare " + left.type + " with " + right.type + ".\n");
            System.exit(1);
            return null;
        }
    }

    private Lexeme evalLTE(Lexeme t, Lexeme env) {
        Lexeme left = eval(t.left, env);
        Lexeme right = eval(t.right, env);

        if(left.type.equals(INTEGER) && right.type.equals(INTEGER)){
            return new Lexeme(BOOLEAN, left.valInt <= right.valInt);
        }
        else if(left.type.equals(INTEGER) && right.type.equals(REAL)){
            return new Lexeme(BOOLEAN, left.valInt <= right.valDouble);
        }
        else if(left.type.equals(REAL) && right.type.equals(INTEGER)){
            return new Lexeme(BOOLEAN, left.valDouble <= right.valInt);
        }
        else if(left.type.equals(REAL) && right.type.equals(REAL)){
            return new Lexeme(BOOLEAN, left.valDouble <= right.valDouble);
        }
        else{
            System.out.println("\nFatal error in Evaluator.java: type mismatch. You cannot compare " + left.type + " with " + right.type + ".\n");
            System.exit(1);
            return null;
        }
    }

    private Lexeme evalGTE(Lexeme t, Lexeme env) {
        Lexeme left = eval(t.left, env);
        Lexeme right = eval(t.right, env);

        if(left.type.equals(INTEGER) && right.type.equals(INTEGER)){
            return new Lexeme(BOOLEAN, left.valInt >= right.valInt);
        }
        else if(left.type.equals(INTEGER) && right.type.equals(REAL)){
            return new Lexeme(BOOLEAN, left.valInt >= right.valDouble);
        }
        else if(left.type.equals(REAL) && right.type.equals(INTEGER)){
            return new Lexeme(BOOLEAN, left.valDouble >= right.valInt);
        }
        else if(left.type.equals(REAL) && right.type.equals(REAL)){
            return new Lexeme(BOOLEAN, left.valDouble >= right.valDouble);
        }
        else{
            System.out.println("\nFatal error in Evaluator.java: type mismatch. You cannot compare " + left.type + " with " + right.type + ".\n");
            System.exit(1);
            return null;
        }
    }

    private Lexeme evalPlus(Lexeme t, Lexeme env) {
        Lexeme left = eval(t.left, env);
        Lexeme right = eval(t.right, env);
        //  adding INTEGERs and reals 4 test cases
        if(left.type.equals(INTEGER) && right.equals(INTEGER)) {
            return new Lexeme(INTEGER, left.valInt + right.valInt);
        }
        else if(left.type.equals(INTEGER) && right.equals(REAL)) {
            return new Lexeme(REAL, left.valInt + right.valDouble);
        }
        else if(left.type.equals(REAL) && right.equals(INTEGER)) {
            return new Lexeme(REAL, left.valDouble + right.valInt);
        }
        else{
            return new Lexeme(REAL, left.valDouble + right.valDouble);
        }

    }

    private Lexeme evalMin(Lexeme t, Lexeme env) {
        Lexeme left = eval(t.left, env);
        Lexeme right = eval(t.right, env);
        //  adding INTEGERs and reals 4 test cases
        if(left.type.equals(INTEGER) && right.equals(INTEGER)) {
            return new Lexeme(INTEGER, left.valInt - right.valInt);
        }
        else if(left.type.equals(INTEGER) && right.equals(REAL)) {
            return new Lexeme(REAL, left.valInt - right.valDouble);
        }
        else if(left.type.equals(REAL) && right.equals(INTEGER)) {
            return new Lexeme(REAL, left.valDouble - right.valInt);
        }
        else{
            return new Lexeme(REAL, left.valDouble - right.valDouble);
        }
    }

    private Lexeme evalTimes(Lexeme t, Lexeme env) {
        Lexeme left = eval(t.left, env);
        Lexeme right = eval(t.right, env);
        //  adding INTEGERs and reals 4 test cases
        if(left.type.equals(INTEGER) && right.equals(INTEGER)) {
            return new Lexeme(INTEGER, left.valInt * right.valInt);
        }
        else if(left.type.equals(INTEGER) && right.equals(REAL)) {
            return new Lexeme(REAL, left.valInt * right.valDouble);
        }
        else if(left.type.equals(REAL) && right.equals(INTEGER)) {
            return new Lexeme(REAL, left.valDouble * right.valInt);
        }
        else{
            return new Lexeme(REAL, left.valDouble * right.valDouble);
        }
    }

    private Lexeme evalDiv(Lexeme t, Lexeme env) {
        Lexeme left = eval(t.left, env);
        Lexeme right = eval(t.right, env);
        //  adding INTEGERs and reals 4 test cases
        if(left.type.equals(INTEGER) && right.equals(INTEGER)) {
            return new Lexeme(INTEGER, left.valInt / right.valInt);
        }
        else if(left.type.equals(INTEGER) && right.equals(REAL)) {
            return new Lexeme(REAL, left.valInt / right.valDouble);
        }
        else if(left.type.equals(REAL) && right.equals(INTEGER)) {
            return new Lexeme(REAL, left.valDouble / right.valInt);
        }
        else{
            return new Lexeme(REAL, left.valDouble / right.valDouble);
        }
    }

    private Lexeme evalMod(Lexeme t, Lexeme env) {
        Lexeme left = t.left;
        Lexeme right = t.right;
        return new Lexeme(INTEGER, left.valInt % right.valInt);
    }

    private Lexeme evalSquared(Lexeme t, Lexeme env) {
        Lexeme num = eval(t.left, env);
        if(num.type == INTEGER){
            Lexeme val = new Lexeme(INTEGER, num.valInt * num.valInt);
            return e.update(t.left, val, env);
        }
        else{
            Lexeme val = new Lexeme(REAL, num.valDouble * num.valDouble);
            return e.update(t.left, val, env);
        }
    }

    private Lexeme evalAss(Lexeme t, Lexeme env) {
        Lexeme val = eval(t.right, env);
        return e.update(t.left, val, env);
    }

    private Lexeme evalPlusPlus(Lexeme t, Lexeme env) {
        Lexeme num = eval(t.left, env);
        if(num.type == INTEGER){
            Lexeme val = new Lexeme(INTEGER, num.valInt + 1);
            return e.update(t.left, val, env);
        }
        else{
            Lexeme val = new Lexeme(REAL, num.valDouble + 1);
            return e.update(t.left, val, env);
        }

    }

    private Lexeme evalMinMin(Lexeme t, Lexeme env) {
        //System.out.println("t.type is " + t.type);
        Lexeme num = eval(t.left, env);
        //System.out.println("num.type is " + num.type);
        if(num.type == INTEGER){
            //System.out.println("t.left.type = " + t.left.type);
            Lexeme val = new Lexeme(INTEGER, num.valInt - 1);
            return e.update(t.left, val, env);
        }
        else{
            //System.out.println("t.left.type = " + t.left.type);
            Lexeme val = new Lexeme(REAL, num.valDouble - 1);
            return e.update(t.left, val, env);
        }
    }



    private Lexeme evalFuncDef(Lexeme t, Lexeme env) {
        return e.insert(t.left, new Lexeme(CLOSURE, env, t), env);
    }

    private Lexeme evalFuncCall(Lexeme t, Lexeme env) {
        Lexeme closure = eval(t.left, env);
        Lexeme args = evalOptExprList(t.right, env);
        Lexeme staticEnv = closure.left;
        Lexeme params = closure.left.left.right.right;
        Lexeme localEnv = e.extend(params, args, staticEnv);
        Lexeme body = closure.right.right.right;
        //funcDef
        Lexeme result = eval(body, env);
        if(result.type.equals(RETURN)){
            return result.left;
        }
        else{
            return result;
        }
    }

    private Lexeme evalMethodDef(Lexeme t, Lexeme env) {
        return null;
    }

    // private Lexeme evalNewArray(Lexeme t, Lexeme env){
    //     int size = t.left.valInt;
    //     Lexeme a = new LexemeArray(size);
    // }

    // private Lexeme evalSetArray(Lexeme t, Lexeme env){
    //     Lexeme argc = t;
    //     int count = 0;
    //     Lexeme a = t.left;
    //     Lexeme i = t.right.left;
    //     Lexeme val = t.right.right.left;
    // }

    // private Lexeme evalGetArray(Lexeme t, Lexeme env){
    //     return null;
    // }

    private Lexeme evalExpr(Lexeme t, Lexeme env) {
        //if only a unary
        if(t.right == null){
            return eval(t.left, env);
        }
        else {
            //operation
            if(t.right.type == GLUE){
                Lexeme oper = t.right.left;
                oper.left = t.left;
                oper.right = t.right.right;
                return evalOp(t.right.left, env);
            }
            else{
                //System.out.println("t.right = " + t.right.type);
                Lexeme sop = t.right;
                sop.left = t.left;
                return evalShortOp(sop, env);
            }
        }
    }

    private Lexeme evalExprList(Lexeme t, Lexeme env) {
        //if more than one argument or expression
        if(t.right != null){
            return new Lexeme(GLUE, eval(t.left, env), evalExprList(t.right, env));
        }
        else{
            return new Lexeme(GLUE, eval(t.left, env), null);
        }
    }

    private Lexeme evalOptExprList(Lexeme t, Lexeme env) {
        if(t == null){
            return null;
        }
        else{
            return evalExprList(t, env);
        }
    }

    private Lexeme evalStatement(Lexeme t, Lexeme env) {
        return eval(t.left, env);
    }

    private Lexeme evalStatements(Lexeme t, Lexeme env) {
        Lexeme result = null;
        while(t != null){
            result = eval(t.left, env);
            if(t.left.type == RETURN){
                return eval(result.left, env);
            }
            t = t.right;
        }
        return result;
    }

    private Lexeme evalVarDef(Lexeme t, Lexeme env) {
        //if variable already defined, we need to update it
        return e.insert(t.left, eval(t.right, env), env);
    }

    private Lexeme evalOptInit(Lexeme t, Lexeme env) {
        if(t.right != null){
            return eval(t.right, env);
        }
        else{
            return null;
        }
    }

    private Lexeme evalBlock(Lexeme t, Lexeme env) {
        Lexeme result = null;
        while(t != null){
            result = eval(t.left, env);
            t = t.right;
        }
        return result;
    }

    private Lexeme evalIfStatement(Lexeme t, Lexeme env) {
        Lexeme ifEnv = e.extend(env, null, null);
        Lexeme tp = eval(t.left, env);
        if(tp.boolVal == true){
            return eval(t.right.left, env);
        }
        //System.out.println("t.right.right is " + t.right.right.type);
        return eval(t.right.right, env);
    }

    private Lexeme evalOptElse(Lexeme t, Lexeme env) {
        if(t.right.type == BLOCK){
            Lexeme elseEnv = e.extend(null, null, env);
            return evalBlock(t.right, elseEnv);
        }
        else{
            return evalIfStatement(t.right, env);
        }
    }

    private Lexeme evalWhile(Lexeme t, Lexeme env) {
        Lexeme whileEnv = e.extend(null, null, env);
        //condition is null for some reason!
        Lexeme condition = evalExpr(t.left, whileEnv);
        Lexeme whileDone;
        while(condition.boolVal == true){
            whileDone = evalBlock(t.right, whileEnv);
            condition = eval(t.left, whileEnv);
        }
        return condition;
    }

    private Lexeme evalParamList(Lexeme t, Lexeme env) {
        if (t.left != null) {
            Lexeme unaryParams = eval(t.left, env);
            if(t.right != null){
                Lexeme listParams = eval(t.right, env);
                return new Lexeme(GLUE, unaryParams, listParams);
            }
            else{
                return new Lexeme(GLUE, unaryParams, null);
            }
        }
        else{
            return null;
        }
    }

    private Lexeme evalReturn(Lexeme t, Lexeme env){
        return new Lexeme(RETURN, eval(t.left, env), null);
    }

    private Lexeme evalPrint(Lexeme t, Lexeme env) {
        Lexeme printArg = evalOptExprList(t.right, env);
        //while there is more to the expression
        //should I print the unary automatically?
        while(printArg != null){
            printArg.left.displayValue();
            //if not a short operation
            //else means it is a short operation ( ++, --, or $)
            printArg = printArg.right;
        }
        if(t.type == PRINTLN){
            System.out.println();
        }
        return null;
    }

}