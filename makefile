#Daniel Nix makefile
all: clean Lexeme.class Lexer.class Parser.class Environment.class Pretty.class Evaluator.class
	chmod +x environment
	chmod +x parser
	chmod +x evaluator

dfn: Lexeme.class Lexer.class Parser.class Environment.class Pretty.class Evaluator.class
	chmod +x environment
	chmod +x parser
	chmod +x evaluator

#files to compile
Lexeme.class: Lexeme.java
	javac -classpath . Lexeme.java

Lexer.class: Lexer.java
	javac -classpath . Lexer.java 

Parser.class: Parser.java
	javac -classpath . Parser.java

Environment.class:
	javac -classpath . Environment.java

Evaluator.class:
	javac -classpath . Evaluator.java

Pretty.class:
	javac -classpath . Pretty.java

clean:
	rm -f *.class

run: run

error1: dfn
	cat error1.dfn
	java Pretty error1.dfn

error1x: dfn
	java Pretty Evaluator error1.dfn
error2: dfn
	cat error2.dfn
	java Pretty error2.dfn
error2x: dfn
	java Evaluator error2.dfn
	
error3: dfn
	cat error3.dfn
	java Pretty error3.dfn
error3x: dfn
	java Evaluator error3.dfn

error4: dfn
	cat error4.dfn
	java Pretty error4.dfn
error4x: dfn
	java Evaluator error4.dfn

error5: dfn
	cat error5.dfn
	java Pretty error5.dfn
error5x: dfn
	java Evaluator error5.dfn

arrays: dfn
	cat arrays.dfn
	java Pretty arrays.dfn
arraysx: dfn
	java Evaluator arrays.dfn

conditionals: dfn
	cat conditionals.dfn
	java Pretty conditionals.dfn
conditionalsx: dfn
	java Evaluator conditionals.dfn

recursion: dfn
	cat recursion.dfn
	java Pretty recursion.dfn
recursionx: dfn
	java Evaluator recursion.dfn

iteration: dfn
	cat iteration.dfn
	java Pretty iteration.dfn
iterationx: dfn
	java Evaluator iteration.dfn

functions: dfn
	cat functions.dfn
	java Pretty functions.dfn
functionsx: dfn
	java Evaluator functions.dfn

lambda: dfn
	cat lambda.dfn
	java Pretty lambda.dfn
lambdax: dfn
	java Evaluator lambda.dfn

objects:
	cat objects.dfn
	java Pretty objects.dfn
objectsx: dfn
	java Evaluator objects.dfn