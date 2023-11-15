package Assignment10;

public class TernaryNode extends Node{
    private Node expression;
    private Node trueCase;
    private Node falseCase;

    public TernaryNode(Node expression, Node trueCase, Node falseCase){
        this.expression = expression;
        this.trueCase = trueCase;
        this.falseCase = falseCase;
    }
    public Node getExpression(){
        return expression;
    }
    public Node getTrueCase(){

        return trueCase;
    }
    public Node getFalseCase(){

        return falseCase;
    }
    @Override
    public String toString(){
        return expression.toString() + " ? " + trueCase.toString() + " : " + falseCase.toString();
    }

}
