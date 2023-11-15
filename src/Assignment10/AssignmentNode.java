package Assignment10;

public class AssignmentNode extends StatementNode {
    Node target;
    Node expression;
    public AssignmentNode(Node target, Node expression){
        this.target = target;
        this.expression = expression;
    }

    public Node getExpression() {

        return expression;
    }

    public Node getTarget() {

        return target;
    }

    public String toString(){

        return target + " " +  expression;
    }

}
