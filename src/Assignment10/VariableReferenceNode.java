package Assignment10;

import java.util.Optional;

public class VariableReferenceNode extends Node{
    private String name;
    private Optional<Node> expressionIndex;

    public VariableReferenceNode(String name, Optional<Node> expressionIndex){
        this.name = name;
        this.expressionIndex = expressionIndex;
    }
    public VariableReferenceNode(String name){

        this.name = name;
    }
    public String getName(){

        return name;
    }
    public Optional<Node> getExpressionIndex(){

        return expressionIndex;
    }
    @Override
    public String toString() {
        if (expressionIndex != null) {
            return name + "[" + expressionIndex.get().toString() + "]";
        } else {
            return name;
        }
    }
}
