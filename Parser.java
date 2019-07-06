/*
*
*   Daniel Nix 
*   CS 403 Parser modified from my Recognizer
*
*
*/

public class Parser implements Types{

    /*
    *   Global Variables
    */

    Lexer l;
    Lexeme currentLexeme;
    Lexeme output;

    /*
    *
    *   MAIN Function
    *
    */
    
    public static void main(String [] args) throws Exception{

        String fileName = args[0];
        System.out.println(fileName);
        Parser run = new Parser();
        run.Parse(fileName);
        System.out.println("legal");
        return;

    }

    /*
    *
    *   Recognize Function
    *
    */

    public Lexeme Parse(String file) throws Exception{

        l = new Lexer(file);
        currentLexeme = l.lex();
        Lexeme start = program();
        match(ENDOFFILE);
        return start;

    }

    /*
    *   Run through Functions
    */

    public boolean check(String type){
        return currentLexeme.type.equals(type);
    }

    public Lexeme advance() throws Exception {
        Lexeme old = currentLexeme;
        currentLexeme = l.lex();

        return old;
    }

    public Lexeme match(String type) throws Exception {
        //System.out.println(currentLexeme.type + " ");
        //System.out.println(currentLexeme.valInt);
        
        matchNoAdvance(type);
        if(currentLexeme.type.equals(UNKNOWN)){
            System.out.println("Illegal: Syntax error " + currentLexeme.lineNumbah);
        }
        return advance();
    }

    public void matchNoAdvance(String type){
        if(!check(type)){
            System.out.println("Illegal: Syntax error. " + currentLexeme.type + " " + type + " " + l.lineNum);
            System.exit(0);
        }

    }

    /*
    *
    *   Pending functions
    *
    */

    public boolean programPending(){
        return statementPending();
    }

    public boolean definitionPending(){
        return functionDefPending() || varDefPending() || classDefPending() || importDefPending() || methodDefPending() || lambdaDefPending();
    }

    public boolean lambdaDefPending(){
        return check("LAMBDA");
    }

    public boolean importDefPending(){
        return check("IMPORT");
    }

    public boolean dotMethodPending(){
        return check("METHOD");
    }

    public boolean newDefPending(){
        return check("NEWDEF");
    }

    public boolean classDefPending(){
        return check("CLASSDEF");
    }

    public boolean variablePending(){
        return check("VARIABLE");
    }

    public boolean unaryPending(){
        return variablePending() || check("INT") || check("REAL") || check("TRUE") || check("CHAR") || check("STRING") || check("UMINUS") || arrayPending() || check("RETURN");
    }

    public boolean arrayPending(){
        return check("VARIABLE");
    }

    public boolean optUnaryPending(){
        return unaryPending();
    }

    public boolean shortOpPending(){
        return check("MINMIN") || check("PLUSPLUS") || check("SQUARED");
    }
    public boolean operationPending(){
        return check("EQUALS") || check("LESSTHAN") || check("GREATERTHAN") || check("LTE") || check("GTE") || check("PLUS") || check("MINUS") || check("TIMES") || check("DIVIDE") || check("MOD")  || check("ASSIGN") || check("AND") || check("OR");
    }

    public boolean functionDefPending(){
        return check("FUNCTIONDEF");
    }

    public boolean methodDefPending(){
        return check("PRIVACY");
    }

    public boolean exprPending(){
        return unaryPending();
    }

    public boolean exprListPending(){
        return exprPending();
    }

    public boolean optExprListPending(){
        return exprListPending();
    }

    public boolean statementPending(){
        return exprPending() || ifStatementPending() || whileLoopPending() || definitionPending() || printStatementPending();
    }

    public boolean statementsPending(){
        return statementPending();
    }

    public boolean optStatementsPending(){
        return statementsPending();
    }

    public boolean varDefPending(){
        return check("VARDEF");
    }

    public boolean optInitPending(){
        return check("ASSIGN");
    }

    public boolean blockPending(){
        return check("OBRACKET");
    }

    public boolean optBlockPending(){
        return blockPending();
    }

    public boolean ifStatementPending(){
        return check("IF");
    }

    public boolean optElsePending(){
        return check("ELSE");
    }

    public boolean whileLoopPending(){
        return check("WHILE");
    }
    public boolean paramListPending(){
        return unaryPending();
    }

    public boolean printStatementPending(){
        return check("PRINT") || check("PRINTLN");
    }

    /*
    *
    *   Grammar implementation functions!
    *
    */

    public Lexeme program() throws Exception{
        Lexeme a = statement();
        Lexeme b = null;
        if(programPending()){
            b = program();
        }

        return new Lexeme("PROGRAM", a, b);

    }

    public Lexeme definition() throws Exception{
        if(functionDefPending()){
            return functionDef();
        }
        else if(varDefPending()){
            return varDef();
        }
        else if(classDefPending()){
            return classDef();
        }
        else if(importDefPending()){
            return importDef();
        }
        else if(methodDefPending()){
            return methodDef();
        }
        else{
            return lambdaDef();
        }

    }

    public Lexeme lambdaDef() throws Exception{
        match("LAMBDA");
        match("OPAREN");
        Lexeme a = exprList();
        match("CPAREN");
        Lexeme b = block();
        return new Lexeme("LAMBDADEF", a, b);
    }

    public Lexeme importDef() throws Exception{

        Lexeme a = match("IMPORT");
        Lexeme b = match("STRING");
        match("SEMICOLON");
        return new Lexeme ("IMPORTDEF", a, b);     //glues everything together
    }

    public Lexeme newDef() throws Exception{
        Lexeme a = match("NEWDEF");
        Lexeme b = definition();
        return new Lexeme("NEWDEF", a, b);
    }

    public Lexeme classDef() throws Exception{
        match("CLASSDEF");
        Lexeme a = match("VARIABLE");
        match("OPAREN");
        Lexeme b = optExprList();
        match("CPAREN");
        Lexeme c = block();
        return new Lexeme("CLASSDEF", a, new Lexeme("GLUE", b, c));
    }

    public Lexeme variable() throws Exception{
        Lexeme a = match("VARIABLE");
        if(check("OPAREN")){
            match("OPAREN");
            Lexeme b = optExprList();
            match("CPAREN");
            return new Lexeme("FUNCCALL", a, b);
        }
        else if(check("OBRACE")){
            match("OBRACE");
            Lexeme b = expr();
            match("CBRACE");
            return new Lexeme("ARRAY", a, b);
        }
        return a;

    }

    public Lexeme unary() throws Exception{
        Lexeme tree;
        if(variablePending()){
            tree = variable();
        }
        else if(check("INTEGER")){
            tree = match("INTEGER");
        }
        else if(check("REAL")){
            tree = match("REAL");
        }
        else if(check("BOOLEAN")){
            tree = match("BOOLEAN");
        }
        else if(check("CHAR")){
            tree = match("CHAR");
        }
        else if(check("STRING")){
            tree = match("STRING");
        }
        else if(check("UMINUS")){
            tree = match("UMINUS");
            tree.right = unary();
        }
        else{
            tree = match("RETURN");
            tree.right = optUnary();
        }
        return tree;
    }

    public Lexeme optUnary() throws Exception{
        Lexeme a = null;
        if(unaryPending()){
            a = unary();
        }
        return a;
    }

    public Lexeme shortOp() throws Exception{
        Lexeme tree;
        if(check("MINMIN")){
            tree = match("MINMIN");
        }
        else if(check("PLUSPLUS")){
            tree = match("PLUSPLUS");
        }
        else{
            tree = match("SQUARED");
        }
        return tree;
    }

    public Lexeme operation() throws Exception{
        Lexeme tree;
        if(check("EQUIVALENT")){
           tree = match("EQUIVALENT");
        }
        else if(check("LESSTHAN")){
            tree = match("LESSTHAN");
        }
        else if(check("GREATERTHAN")){
            tree = match("GREATERTHAN");
        }
        else if(check("LTE")){
            tree = match("LTE");
        }
        else if(check("GTE")){
            tree = match("GTE");
        }
        else if(check("PLUS")){
            tree = match("PLUS");
        }
        else if(check("MINUS")){
            tree = match("MINUS");
        }
        else if(check("TIMES")){
            tree = match("TIMES");
        }
        else if(check("DIVIDE")){
            tree = match("DIVIDE");
        }
        else if(check("MOD")){
            tree = match("MOD");
        }
        else if(check("ASSIGN")){
            tree = match("ASSIGN");
        }
        else if(check("AND")){
            tree = match("AND");
        }
        else{
            tree = match("OR");
        }
        return tree;
    }

    public Lexeme functionDef() throws Exception{
        match("FUNCTIONDEF");
        Lexeme a = match("VARIABLE");
        match("OPAREN");
        Lexeme b = paramList();
        match("CPAREN");
        Lexeme c = block();
        return new Lexeme("FUNCTIONDEF", a,new Lexeme("GLUE", b, c));
    }
    
    public Lexeme methodDef() throws Exception{
        Lexeme a = match("PRIVACY");
        Lexeme b = match("VARIABLE");
        match("OPAREN");
        Lexeme c = optExprList();
        match("CPAREN");
        Lexeme d = block();
        return new Lexeme("METHODDEF", a, new Lexeme("GLUE", b, new Lexeme("GLUE", c, d)));
    }

    public Lexeme expr() throws Exception{
        Lexeme a = unary();    
        if(operationPending()){
            Lexeme b = operation();
            Lexeme c = expr();
            return new Lexeme("EXPR", a, new Lexeme("GLUE", b, c));
        }
        else if(shortOpPending()) {
            Lexeme b = shortOp();
            return new Lexeme("EXPR", a, b);
        }
        return new Lexeme("EXPR", a, null);
    }

    public Lexeme exprList() throws Exception{
        Lexeme a = expr();
        if(check(COMMA)){
            match("COMMA");
            Lexeme b = exprList();
            return new Lexeme("EXPRLIST", a, b);
        }
        return new Lexeme("EXPR", a, null);
    }

    public Lexeme optExprList() throws Exception{
        Lexeme a = null;
        if(exprListPending()){
            a = exprList();
        }
        return a;
    }

    public Lexeme statement() throws Exception{
        Lexeme a = null;
        if(exprPending()){
            a = expr();
            match("SEMICOLON");
        }
        else if(ifStatementPending()){
            a = ifStatement();
        }
        else if(whileLoopPending()){
            a = whileLoop();
        }
        else if(printStatementPending()){
            a = printStatement();
        }
        else{
            a = definition();
        }
        return new Lexeme("STATEMENT", a, null);
    }

    public Lexeme statements() throws Exception{
        Lexeme a = statement();
        if(statementsPending()){
            Lexeme b = statements();
            return new Lexeme("STATEMENTS", a, b);
        }
        return new Lexeme("STATEMENTS", a, null);
    }

    public Lexeme optStatements() throws Exception{
        Lexeme a = null;
        if(optStatementsPending()){
            a = statements();
        }
        return a;
    }

    public Lexeme varDef() throws Exception{
        match("VARDEF");
        Lexeme a = variable();
        Lexeme b = optInit();
        match("SEMICOLON");
        return new Lexeme("VARDEF", a, b);
    }

    public Lexeme optInit() throws Exception{
        Lexeme a = null;
        Lexeme b = null;
        if(check("ASSIGN")){
            a = match("ASSIGN");
            b = expr();
            return new Lexeme("OPTINIT", a, b);
        }
        return a;
    }

    public Lexeme block() throws Exception{
        match("OBRACKET");
        Lexeme a = optStatements();
        match("CBRACKET");
        return new Lexeme("BLOCK", a, null);
    }

    public Lexeme optBlock() throws Exception{
        Lexeme a = null;
        if(blockPending()){
            a = block();
            return new Lexeme("BLOCK", a, null);
        }
        return a;
    }

    public Lexeme ifStatement() throws Exception{
        match("IF");
        match("OPAREN");
        Lexeme a = exprList();
        match("CPAREN");
        Lexeme b = block();
        Lexeme c = optElse();
        return new Lexeme("IFSTATEMENT", a, new Lexeme("GLUE", b, c));
    }

    public Lexeme optElse() throws Exception{
        Lexeme a = null;
        if(check("ELSE")){
            a = match("ELSE");
            Lexeme b = null;
            if(optBlockPending()){
                b = optBlock();
            }
            else{
                b = ifStatement();
            }
            return new Lexeme("ELSE", a, b);
        }
        return a;
    }

    public Lexeme whileLoop() throws Exception{
        match("WHILE");
        match("OPAREN");
        Lexeme a = expr();
        match("CPAREN");
        Lexeme b = block();
        return new Lexeme("WHILE", a, b);
    }

    public Lexeme paramList() throws Exception{
        Lexeme a = null;
        if(unaryPending()){
            a = unary();
            if(check("COMMA")){
                match("COMMA");
                Lexeme b = paramList();
                return new Lexeme("PARAMLIST", a, b);
            }
            else {
                return new Lexeme("PARAMLIST", a, null);
            }
        }
        return a;
    }

    public Lexeme printStatement() throws Exception{
        Lexeme a,b;
        if(check("PRINT")) {
            a = match("PRINT");
            match("OPAREN");
            b = optExprList();
            match("CPAREN");
            match("SEMICOLON");
            return new Lexeme("PRINT", a, b);
        }
        else {
            a = match("PRINTLN");
            match("OPAREN");
            b = optExprList();
            match("CPAREN");
            match("SEMICOLON");
            return new Lexeme("PRINTLN", a, b);
        }
    }

}