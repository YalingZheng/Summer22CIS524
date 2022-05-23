import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/*
 * Simple Compiler 
 * <Program1> -> ( <VarDefinition> | <AssignmentStatement> ) {[ <VarDefinition> | <AssignmentStatement> ] }

<varDefinition> -> var <varName>;
<varName> -> (a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w){0|1|a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w}

<AssignmentStatement> -> <varname> = <ExpressionStatement>;
<ExpressionStatement> -> [<varname1> | <constant>] { + [<varname2> | <constant>] };

 * 
 * */

public class simpleCompiler {

	// <Program1> -> (<VarDefinition> | <Assignment> | <ForStatment>) {(<VarDefinition> | <AssignmentStatement> | <ForStatment>) }
	// NEED TO TRANSLATE THIS EBNF to BNF (non left-recursive)
	// <Program1> -> (<VarDefinition> | <AssignmentStatement>) <Program2>
	// <Program2> -> epsilon | (<VarDefinition> | <AssignmentStatement>) <Program2>
	
	public static String linecode = "";
	public static int lineIndex = 0;
	public static int columnIndex = 0;
	
	public static boolean Program1() {
		System.out.println("Enter <Program1> -> (<VarDefinition> | <AssignmentStatement>) <Program2>");
		if (varDefinition() || (AssignmentStatement())) {
			 
		} else {
			System.out.println("Exit <Program1> -> (<VarDefinition> | <AssignmentStatement>)  <Program2>  FAIL");
			return false;
		}
		boolean isProgram2 = Program2();
		if (isProgram2) {
			System.out.println("Exit <Program1> -> (<VarDefinition> | <AssignmentStatement>)  <Program2>  SUCCESS");
			System.out.println("Compilation succeeded!");
			return true;
		} else {
			System.out.println("Exit <Program1> -> (<VarDefinition> | <AssignmentStatement>)  <Program2>  FAIL");
			System.out.println("Compilation failed");
			return false;
		}
	}
	
	public static boolean Program2() {
		System.out.println("Enter <Program2> -> epsilon | (<VarDefinition> | <AssignmentStatement>) <Program2> ");
		String nextSymbol = getNextSymbol();
		if ((nextSymbol == null) || (nextSymbol.trim().equals(""))) {
			System.out.println("EXIT <Program2> -> epsilon | (<VarDefinition> | <AssignmentStatement>)  <Program2> SUCCESS");
			return true;
		} else {
			if (varDefinition() || (AssignmentStatement())) {
				 
			} else {
				return false;
			}
			return Program2();
		}
	}
	
	// <varDefinition> -> var <varName>;	
	public static boolean varDefinition() {
		// scan the next symbol
		// if it is a variable, good
		// otherwise, back the cursor
		System.out.println("Enter <varDefinition> -> var <varName>;");
		int startIndex = columnIndex;
		String nextSymbol = getNextSymbol();
		if (nextSymbol!=null) {
			if (nextSymbol.equals("var")) {
				// good, get next Symbol
				nextSymbol = getNextSymbol();
				boolean isVarNameSemicolon = varNameSemicolon(nextSymbol);
				if (isVarNameSemicolon) {
					System.out.println("Exit <varDefinition> -> var <varName>;  SUCCESS");
					return true;
				} else {
					columnIndex = startIndex;
					System.out.println("Exit <varDefinition> -> var <varName>;  FAIL");
					return false;
				}
			} else {
				columnIndex = startIndex;
				System.out.println("Exit <varDefinition> -> var <varName>;  FAIL");
				return false;
			}
		} else {
			System.out.println("Exit <varDefinition> -> var <varName>;  FAIL");
			return false;
		}
	}
	
	// <varName> -> [a-z]{[a-z01]};
	public static boolean varNameSemicolon(String variable) {
		System.out.println("Enter <varName> -> (a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w){0|1|a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w}; ");
		int lastindex = variable.length()-1;
		int variableIndex = 0;
		if ((variable.charAt(variableIndex) >= 'a') && (variable.charAt(variableIndex) <= 'z')) {
			// continue 
			variableIndex++;
			// check the net 
			while ( (variableIndex <= lastindex) &&
					(((variable.charAt(variableIndex) >= 'a') && (variable.charAt(variableIndex) <= 'z')) 
					|| 	(variable.charAt(variableIndex) == ';')
					|| 	(variable.charAt(variableIndex) == '0')
					||  (variable.charAt(variableIndex) == '1'))) {
				variableIndex++;
				if (variable.charAt(variableIndex) == ';') {
					if (variableIndex == lastindex) {
						// GOOD, RETURN SUCCESS
						System.out.println("EXIT <varName> -> [a-z]{[a-z01]};  SUCCESS");
						return true;
					} else {
						int currentColumn = columnIndex - variable.length() + variableIndex;
						System.out.println("Error at line " + lineIndex + " column " + (currentColumn+1));
						return false;
					}
				}
			}
			if (variableIndex <= lastindex) {
				int currentColumn = columnIndex - variable.length() + variableIndex;
				System.out.println("Error at line " + lineIndex + " column " + (currentColumn + 1));
				return false;
			}
		} else {
			int currentColumn = columnIndex - variable.length() + variableIndex;
			System.out.println("Error at line " + lineIndex + " column " + (currentColumn + 1));
			return false;
		}		
		return true;
	}
	
	public static String getNextSymbol() {
		System.out.println("Enter GetNextSymbol " + linecode + " " + columnIndex);
		while (linecode.charAt(columnIndex) == ' '){
			columnIndex++;
		}
		if (linecode.charAt(columnIndex) == '\n') {
			System.out.println("No more symbol.");
			return null;
		}
		String curSymbol = "" + linecode.charAt(columnIndex);
		columnIndex++;
		while ((linecode.charAt(columnIndex) != '\n') && (linecode.charAt(columnIndex) != ' ')) {
			curSymbol += linecode.charAt(columnIndex);
			columnIndex++;
		}
		System.out.println("Exit GetNextSymbol " + curSymbol);
		return curSymbol;
	}
	
    // <AssignmentStatement> -> <varname> = <ExpressionStatement>;
	public static boolean AssignmentStatement() {
		System.out.println("<AssignmentStatement> -> <varname> = <ExpressionStatement>;");
		int startIndex = columnIndex;
		
		String nextSymbol = getNextSymbol();
		String variable = nextSymbol;
		//TODO: Implement this BNF
		int currentColumn = columnIndex - variable.length();
		System.out.println("Error at line " + lineIndex + " column " + (currentColumn + 1));
		columnIndex = startIndex;
		return false;
	}
	
	public static void main(String[] args) {
		// read content from a text file 
		BufferedReader reader;
		try (Scanner sc = new Scanner(System.in)){
			  // Create a Scanner object
			System.out.println("Enter your file name");	
			reader = new BufferedReader(new FileReader(sc.next()));
			linecode = reader.readLine();
			// NOTE THAT THE FOLLOWING SOLUTION ASSUMES that EACH LINE 
			// FOLLOWS THE RULE
			// This is NOT the best solution
			// This solution cannot recognize the following program as correct
			// var myCount; var
			// secondCount;
			
			while (linecode != null) {
				//System.out.println(line);
				// read next line				
				lineIndex++;
				// print out this line
				linecode += '\n';
				System.out.println("Read: " + linecode);
				// handle each line to see whether it is a correct statement
				boolean isCorrectLineCode = Program1();
				if (!isCorrectLineCode) {
					System.out.println("EXIT ... Compilation failed");
					break;
				}
				linecode = reader.readLine();
				columnIndex = 0;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
