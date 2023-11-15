package Assignment10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Optional;

/**
 * Collects and reads the file(Filetest.txt) and then after the other three classes have stored types of SEPERATOR, WORD, and
 * NUMBER into the tokens, it prints it out by calling the toString method located in the Token class
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String filename = args[0];
        //String input = GetAllBytes("Filetest.txt");
        LinkedList<String> parameters = new LinkedList<>();
        Path myPath = Paths.get(filename);
        String input = new String(Files.readAllBytes(myPath));
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.Lex();
        Parser parser = new Parser(tokens);
        ProgramNode programNode = parser.Parser();
        parser.ParseOperation();
        parser.ParseBottomLevel();
        parser.ParseLValue();
        for (Token token : tokens) {
            System.out.print(token.toString());
        }
    }
}
//Dumping ground for code that I tested through the file, can be ignored :)
//System.out.println(programNode.toString());
       /*for (FunctionDefinitionNode functionNode : programNode.getFunctions()){
                System.out.println("this the function name man: "+functionNode.getName());
                System.out.println("param checked dawg: "+functionNode.getParameters());
                System.out.println("orignal form of the whole function def: "+functionNode.toString());
                for (FunctionDefinitionNode functionNode : programNode.getFunctions()){
            System.out.println("this the function name man: "+functionNode.getName());
            System.out.println("param checked dawg: "+functionNode.getParameters());
            System.out.println("orignal form of the whole function def: "+functionNode.toString());
            System.out.println(parser.AcceptSeperators());
        }
                }*/
      /*BlockNode blockNode = new BlockNode();
        blockNode.toString();*/
//TokenHandler newTokenHandler = new TokenHandler(tokens);
        /*if(peekedToken.isPresent()){
            System.out.println("Fail!!");
        }else{
            System.out.println("Success");
        }*/
    /*private static String GetAllBytes(String filename) throws IOException {
        Path myPath = Paths.get(filename);
        String content = new String(Files.readAllBytes(myPath));
        return content;
    }*/