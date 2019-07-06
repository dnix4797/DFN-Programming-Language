/*
 *	Daniel Nix
 *	Lexeme.Java
 * 	CS 403 Dr. Lusth Spring 2019
 *
 *
*/

import java.util.ArrayList;
import java.util.Scanner;


public class Lexeme implements Types{
	//The values Lexeme could possibly have.
	String valString = null;
	int valInt;
	double valDouble;
	char valChar = '\0';
	boolean boolVal = false;
	ArrayList<Lexeme> arrayVal = (ArrayList<Lexeme>)null;
	int lineNumbah; 
	Scanner FileScanner;
	Lexeme lexArray[];

	String type;	//The type it is passed.
	Lexeme left = null;
	Lexeme right = null;


	//Constructors for each possibility of a Lexeme

	public Lexeme(){

	}

	public Lexeme(String t){
		type = t;
	}
	public Lexeme(String t, String str){
		type = t;
		valString = str;
	}
	public void LexemeArray(int size){
		type = ARRAY;
		lexArray = new Lexeme[size];
	}

	public void setArr(Lexeme a, Lexeme i, Lexeme val){
		lexArray[i.valInt] = val;
	}

	public Lexeme(String t, int i){
		type = t;
		valInt = i;

	}
	public Lexeme(String t, int i, int lineNum){
		type = t;
		valInt = i;
		lineNumbah = lineNum;

	}
	public Lexeme(String t, double dub){
		type = t;
		valDouble = dub;
	}
	public Lexeme(String t, char ch){
		type = t;
		valChar = ch;
	}
	public Lexeme(String t,boolean bool){
		type = t;
		boolVal = bool;
	}
	public Lexeme(String t, ArrayList<Lexeme> arr){
		type = t;
		arrayVal = arr;
	}
	public Lexeme(String t, Lexeme lft, Lexeme rght){
		type = t;
		left = lft;
		right = rght;
	}
	public void displayValue(){
		//take out ____ is 
		if(this.valString != null){
			System.out.print(this.valString);
		}
		else if(this.type.equals(INTEGER)){
			System.out.print(this.valInt);
		}
		else if(this.type.equals(REAL)){
			System.out.print(this.valDouble);
		}
		else if(this.type.equals(BOOLEAN)){
			System.out.print(this.boolVal);
		}
		else if(this.arrayVal != null){
			System.out.print(this.arrayVal);
		}
		else{
			System.out.print(this.type);
		}
	}
	
}