package sebestaScanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class sebestaScanner{

//Character classes
	static final int LETTER = 0;
	static final int DIGIT 	 = 1;
	static final int UNKNOWN = 99;
	static final int  EOF 	 = -1;

// Token codes
	static final int INT_LIT 	 = 10;
	static final int IDENT		 = 11;
	static final int ASSIGN_OP	 = 20;
	static final int ADD_OP 	 = 21;
	static final int SUB_OP 	 = 22;
	static final int MULT_OP 	 = 23;
	static final int DIV_OP 	 = 24;
	static final int LEFT_PAREN  = 25;
	static final int RIGHT_PAREN = 26;

//Global variables	
	char[] lexeme = new char[100];
	int lexLen;
	int LastToken;
	char currentChar;
	int  currentClass;

//Reading file
	private InputStream reader = null;

	public sebestaScanner(String filename){
		try {
			reader = new FileInputStream(new File(filename));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

//a function to get character of input and determine its character class
	public int getChar() throws IOException{

		char token = (char) reader.read();
		
		int charClass =UNKNOWN;
		
		while(Character.isWhitespace(token)){
			token = (char) reader.read();
		}
		
		if(token == (char)-1){
			charClass = EOF;
		}else
		if(Character.isAlphabetic(token))
		{
			charClass= LETTER;
		}
		else if(Character.isDigit(token))
		{
			charClass = DIGIT;
		}
		
		currentChar = token;
		currentClass = charClass;
		return charClass;
	}

//Process the contents of input file
	public void scan(){

		int charClass = UNKNOWN;
		int nextToken = EOF;
		try {
			do{
				
				if(nextToken != IDENT && nextToken !=INT_LIT){
					charClass = getChar();
				}else{
					charClass = currentClass;
				}
				nextToken = lex(charClass);
			}while(nextToken !=EOF );
		}
		catch (IOException e) {

			e.printStackTrace();
		}

	}
	
// Function to add nextChar to lexeme
	void addChar(char token)
	{
		if (lexLen <= 98) {
			lexeme[lexLen++] = token;
			lexeme[lexLen] = 0;
		}
		else
			System.out.println("Error - lexeme is too long \n");
	}

// Function to check operators and parentheses and return the token
	int lookup(char ch)
	{
		int nextToken = EOF;
		switch (ch) {
		case '(':
			nextToken = LEFT_PAREN;
			break;
		case ')':
			nextToken = RIGHT_PAREN;
			break;
		case '+':
			nextToken = ADD_OP;
			break;
		case '-':
			nextToken = SUB_OP;
			break; 
		case '*':
			nextToken = MULT_OP;
			break;
		case '/':
			nextToken = DIV_OP;
			break;
		default:
			nextToken = EOF;
			break;
		}
		addChar(ch);
		return nextToken;
	}

// Lexical analyzer for arithmetic expressions 
	int lex(int charClass) throws IOException {
		lexLen = 0;
		int nextToken = EOF;
		
		switch (charClass)
		{
		//Parse Identifiers
		case LETTER:
			
			addChar(currentChar);
			charClass = getChar();
			
			while (charClass == LETTER || charClass == DIGIT) 
			{
				addChar(currentChar);
				charClass = getChar();
				
			}
			nextToken = IDENT;
			break;

		case DIGIT:
			addChar(currentChar);
			charClass = getChar();
			
			while (charClass == DIGIT)
			{
				addChar(currentChar);
				charClass = getChar();
			}
			nextToken = INT_LIT;
			break;

		case UNKNOWN:
			nextToken=lookup(currentChar);
			break;

		case EOF:
			nextToken = EOF;
			lexeme[0] = 'E';
			lexeme[1] = 'O';
			lexeme[2] = 'F';
			lexeme[3] = 0;
			break;
		}
		System.out.println("next token is "+nextToken+" "+"next lexeme is "+new String(lexeme));

		return nextToken;
	}
}
