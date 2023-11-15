package Assignment10;

import java.util.Optional;
enum OperationTypes{
    EQ, NE, LT, LE, GT, GE, AND, OR, NOT, MATCH, NOTMATCH, DOLLAR, PREINC, POSTINC, PREDEC, POSTDEC, UNARYPOS, UNARYNEG, IN,
    EXPONENT, ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO, CONCATENATION
}
public class OperationNode extends Node {
    private Node left;
    private Optional<Node> right;
    private OperationTypes operationTypes;

    public OperationNode(Node left, Optional<Node> right, OperationTypes operationTypes){
        this.left = left;
        this.right = right;
        this.operationTypes = operationTypes;
    }
    public OperationNode(Node left, OperationTypes operationTypes){
        this.left = left;
        this.right = Optional.empty();
        this.operationTypes = operationTypes;
    }
    public OperationNode(OperationTypes operationTypes){

        this.operationTypes = operationTypes;
    }
    public Node getLeft(){
        return left;
    }
    public Optional<Node> getRight(){
        return right;
    }

    public OperationTypes getOperationTypes() {
        return operationTypes;
    }

    @Override
    public String toString(){
        if(right.isPresent()){
            return left.toString() +" "+ operationTypes.toString()+ " " + right.get().toString();
        }
        if(left!=null) {
            return left.toString() + " " + operationTypes.toString();
        }
        return operationTypes.toString();
    }

}
