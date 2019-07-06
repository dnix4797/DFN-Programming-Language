/*
 *	Daniel Nix
 *	Lexer.Java
 * 	CS 403 Dr. Lusth Spring 2019
 *
 *
*/

import java.io.*;
import java.lang.*;

public class Lexer implements Types {
	//Variable needed for Lexer
	String fileName;
	PushbackReader input;
    char ch;
    int p;          //passthrough from input.read();
	int lineNum;

	//Constructor
	public Lexer(String f) throws Exception {
		fileName = f;
		input = new PushbackReader(new BufferedReader(new FileReader(fileName)));
		lineNum = 1;
	}

	//We need to be able to ignore White Space because this isn't Python
	public void skipWhiteSpace() throws Exception {

        
        p = input.read();
        ch = (char) p;

		while( (Character.isWhitespace(ch) || ch == '^') && p != -1) {

			//keep track of the line we are on
			if(ch == '\n') {
				lineNum++;
			}
            
            if (ch == '^') {
                p = input.read();
                ch = (char) p;
    
                //Block Comments
                if (ch == '~') {
    
                    p = input.read();
                    ch = (char) p;
                    while (ch != '~') {
                        if (ch == '\n') {
                            lineNum++;
                        }
                        p = input.read();
                        ch = (char) p;
                    }
    
                    p = input.read();
                    ch = (char) p;
    
                    if (ch != '^') {
                        System.err.printf("Error! Line %d: Single ~ character.", lineNum);
                        System.err.println("Were you trying to make a block comment with ^~?");
                        System.exit(1);
                    }
    
                    p = input.read();
                    ch = (char) p;
                    skipWhiteSpace();
                }
                else if(ch == '^'){
                    while (ch != '\n') {
                        p = input.read();
                        ch = (char) p;
                    }
                    lineNum++;
                }
                else{
                    input.unread(p);
    
                }
            }
            p = input.read();
            ch = (char) p;
		}
        input.unread(p);
	}

	public Lexeme lex() throws Exception { 

        skipWhiteSpace();  
        p = input.read();
        ch = (char) p;
        //System.out.println(p + " " + ch); 
        if (p == -1 || p == 65535) {
            return new Lexeme(ENDOFFILE); 
        }
        switch(ch) { 
            //character tokens 

            case '(': return new Lexeme(OPAREN); 
            case ')': return new Lexeme(CPAREN); 
            case '{': return new Lexeme(OBRACKET);
            case '}': return new Lexeme(CBRACKET);
            case '[': return new Lexeme(OBRACE);
            case ']': return new Lexeme(CBRACE);
            case ',': return new Lexeme(COMMA); 
            //Checking for ++
            case '+':    
                p = input.read();
                ch = (char) p;
                if(ch == '+') {
                    return new Lexeme(PLUSPLUS);
                }
                else {
                    input.unread(p);
                    return new Lexeme(PLUS); 
                }
            case '*': return new Lexeme(TIMES); 
            //Checking for --
            case '-': 
                p = input.read();
                ch = (char) p;
                if(ch == '-') {
                    return new Lexeme(MINMIN);
                }
                else {
                    input.unread(p);
                    return new Lexeme(MINUS); 
                }
            //fix divides
            case '/': return new Lexeme(DIVIDE);
                
            //Checking <=
            case '<': 
                p = input.read();
                ch = (char) p;
                if(ch == '=') {
                    return new Lexeme(LTE);
                }
                else {
                    input.unread(p);
                    return new Lexeme(LESSTHAN); 
                }
            //Checking >=
            case '>': 
                p = input.read();
                ch = (char) p;
                if(ch == '=') {
                    return new Lexeme(GTE);
                }
                else {
                    input.unread(p);
                    return new Lexeme(GREATERTHAN); 
                }
            //Checking ==
            case '=': 
                p = input.read();
                ch = (char) p;
                if(ch == '=') {
                    return new Lexeme(EQUIVALENT);
                }
                else {
                    input.unread(p);
                    return new Lexeme(ASSIGN); 
                }
            case ';': return new Lexeme(SEMICOLON); 
            //New Operator because I'm tired and petty
            case '$': return new Lexeme(SQUARED);
            case '%': return new Lexeme(MOD);
            //Bitwise and Logical "AND"
            case '&':  
                p = input.read();
                ch = (char) p;
                if(ch == '&') {
                    return new Lexeme(AND);
                }
                else {
                    input.unread(p);
                    return new Lexeme(BWAND); 
                }
            //Bitwise and Logical "OR"
            case '|': 
                p = input.read();
                ch = (char) p;
                if(ch == '|') {
                    return new Lexeme(OR);
                }
                else {
                    input.unread(p);
                    return new Lexeme(BWOR); 
                }

            default: {
                // multi-character tokens (only numbers, 
                // variables/keywords, and strings) 
                if (Character.isDigit(ch)) { 
                    input.unread(p); 
                    return lexNumber(); 
                } 
                else if (Character.isLetter(ch)) { 
                    input.unread(p); 
                    return lexVariableOrKeyword();
                } 
                else if (ch == '\"') { 
                    return lexString(); 
                } 
                else{
                    return new Lexeme(UNKNOWN, ch, lineNum); 
                }
                
            }
        } 
    }
    

    public Lexeme lexString() throws Exception {

        String buffer = "";

        p = input.read();
        ch = (char) p;

        while (ch != '\"') {

            //check for double quotes within string
            if (ch == '\\') {
                p = input.read();
                ch = (char) p;
            }
            buffer += ch;
            p = input.read();
            ch = (char) p;
        }
        return new Lexeme(STRING, buffer);
    }


    public Lexeme lexVariableOrKeyword() throws Exception {
        String token = "";
    
        p = input.read();
        ch = (char) p;
        while (Character.isLetter(ch) || Character.isDigit(ch)) {

            token = token + ch; //grow the token string
            p = input.read();
            ch = (char) p;

        }

    //push back the character that got us out of the loop
    //it may be some kind of punctuation

        input.unread(p);

    //token holds either a variable or a keyword, so figure it out

        if ( token.equals("if")) return new Lexeme(IF);
        else if ( token.equals("else")) return new Lexeme(ELSE);
        else if ( token.equals("while")) return new Lexeme(WHILE);
        else if ( token.equals("function")) return new Lexeme(FUNCTIONDEF);
        else if ( token.equals("lambda")) return new Lexeme(LAMBDA);
        else if ( token.equals("private")) return new Lexeme(PRIVACY);
        else if ( token.equals("public")) return new Lexeme(PRIVACY);
        else if ( token.equals("print")) return new Lexeme(PRINT);
        else if ( token.equals("println")) return new Lexeme(PRINTLN);
        else if ( token.equals("new")) return new Lexeme(NEWDEF);
        else if ( token.equals("var")) return new Lexeme(VARDEF);
        else if ( token.equals("class")) return new Lexeme(CLASSDEF);
        else if ( token.equals("method")) return new Lexeme(METHODDEF);
        else if ( token.equals("return")) return new Lexeme(RETURN);

        //more keyword testing here
        else {//must be a variable!
            return new Lexeme(VARIABLE,token);
        }
    }
    
    public Lexeme lexNumber() throws Exception {
        
        boolean isReal = false;
        String buffer = "";

        p = input.read();
        ch = (char) p;
        
        //Is ch a digit or a "." then it is a number!
        while (Character.isDigit(ch) || ch == '.') {
            //check if we have a real
            if (ch == '.') {
                isReal = true;
            }
            buffer += ch;
            p = input.read();
            ch = (char) p;
        }

        input.unread(p);

        if (isReal) {
            return new Lexeme(REAL, Double.parseDouble(buffer));
        }
        else {
            return new Lexeme(INTEGER, Integer.parseInt(buffer));
        }
    }
}