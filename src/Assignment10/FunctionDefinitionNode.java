package Assignment10;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

public class FunctionDefinitionNode extends Node {
    private String functionName;
    private LinkedList<Token> tokens = new LinkedList<Token>();
    private TokenHandler tokenHandler = new TokenHandler(tokens);
    private LinkedList<StatementNode> statementNodes;
    private LinkedList<String> parameters;
    public FunctionDefinitionNode(String functionName, LinkedList<String> parameters){
        this.functionName = functionName;
        this.parameters = parameters;
        statementNodes = new LinkedList<StatementNode>();
    }
    public FunctionDefinitionNode(){

    }
    public LinkedList<String> getParameters() {

        return parameters;
    }
    public String getName(){

        return functionName;
    }

    public LinkedList<StatementNode> getStatementNodes() {

        return statementNodes;
    }

    public void addtoStatement(LinkedList<StatementNode> statements){

        statementNodes.addAll(statements);
    }
    @Override
    public String toString(){
        StringBuilder parametersAdded = new StringBuilder("");
        Optional<Token> currentToken = tokenHandler.Peek(0);
        if(currentToken.isPresent() && currentToken.get().getType() == TokenType.COMMA && parameters.size() > 0) {
            parametersAdded.append(", ");
            parametersAdded.append(parameters);
            return TokenType.FUNCTION + " " + functionName + "(" + parametersAdded + ") {"+ statementNodes.toString() + "}";
        }
        return TokenType.FUNCTION + " " + functionName + "(" + parameters + ") {"+ statementNodes.toString() + "}";
    }
}
