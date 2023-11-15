package Assignment10;

import java.util.LinkedList;
import java.util.Optional;

public abstract class Node {
    public String toString() {

        return null;
    }

}

class BlockNode extends Node {
    private LinkedList<StatementNode> statements;
    private Optional<Node> conditional;

    public BlockNode() {
        statements = new LinkedList<StatementNode>();
    }

    public LinkedList<StatementNode> getStatements() {

        return statements;
    }

    public void setConditional(Optional<Node> conditional) {

        this.conditional = conditional;
    }

    public Optional<Node> getConditional() {

        return conditional;
    }

    public void addtoStatments(StatementNode statementNode) {

        statements.add(statementNode);
    }

    @Override
    public String toString() {

        return "{ " + statements.toString() + " }";
    }
}

class ProgramNode extends Node {
    private LinkedList<BlockNode> BEGINblockNodes;
    private LinkedList<BlockNode> ENDblockNodes;
    private LinkedList<BlockNode> OtherblockNodes;
    private LinkedList<FunctionDefinitionNode> functionNodes;

    public ProgramNode() {
        BEGINblockNodes = new LinkedList<BlockNode>();
        ENDblockNodes = new LinkedList<BlockNode>();
        OtherblockNodes = new LinkedList<BlockNode>();
        functionNodes = new LinkedList<FunctionDefinitionNode>();
    }

    public void addFunctionToList(FunctionDefinitionNode functionNode) {

        functionNodes.add(functionNode);
    }

    public void addtoBegin(BlockNode blockNode) {

        BEGINblockNodes.add(blockNode);
    }

    public void addtoEnd(BlockNode blockNode) {

        ENDblockNodes.add(blockNode);
    }

    public void otherBlocks(BlockNode blockNode) {

        OtherblockNodes.add(blockNode);
    }

    public LinkedList<FunctionDefinitionNode> getFunctions() {

        return functionNodes;
    }

    public LinkedList<BlockNode> getBegin() {

        return BEGINblockNodes;
    }

    public LinkedList<BlockNode> getEnd() {

        return ENDblockNodes;
    }

    public LinkedList<BlockNode> getOtherblockNodes() {

        return OtherblockNodes;
    }

    @Override
    public String toString() {
        StringBuilder blockPrinters = new StringBuilder("");
        blockPrinters.append("Begin BlockNodes: ");
        for (BlockNode blockNode : BEGINblockNodes) {
            blockPrinters.append(blockNode.toString());
            blockPrinters.append(", ");
        }
        blockPrinters.append("\nEnd BlockNodes: ");
        for (BlockNode blockNode : ENDblockNodes) {
            blockPrinters.append(blockNode.toString());
            blockPrinters.append(", ");
        }
        blockPrinters.append("\nOther BlockNodes: ");
        for (BlockNode blockNode : OtherblockNodes) {
            blockPrinters.append(blockNode.toString());
            blockPrinters.append(", ");
        }
        blockPrinters.append("\nFunctionDefinitionNode: ");
        for (FunctionDefinitionNode functionDefinitionNode : functionNodes) {
            blockPrinters.append(functionDefinitionNode.toString());
            blockPrinters.append(", ");
        }
        return blockPrinters.toString();
    }
}

class ConstantNode extends Node {
    private String value;

    public ConstantNode(String value) {

        this.value = value;
    }

    public String getConstantValue() {

        return value;
    }

    public String toString() {

        return value;
    }
}

class PatternNode extends Node {
    private String value;

    public PatternNode(String value) {

        this.value = value;
    }

    public String getPatternValue() {

        return value;
    }

    public String toString() {

        return value;
    }
}

abstract class StatementNode extends Node {

}

class ContinueNode extends StatementNode {
    public String toString() {

        return "continue";
    }
}

class BreakNode extends StatementNode {
    public String toString() {

        return "break";
    }
}

class IFNode extends StatementNode {
    private Optional<Node> parseOperation;
    private BlockNode blockNode;
    private IFNode elseIfNode;
    private IFNode elseNode;


    public IFNode(Optional<Node> parseOperation, BlockNode blockNode) {
        this.parseOperation = parseOperation;
        this.blockNode = blockNode;
        this.elseIfNode = null;
        this.elseNode = null;
    }

    public void setElseIf(IFNode elseIfNode) {

        this.elseIfNode = elseIfNode;
    }

    public void setElse(IFNode elseNode) {

        this.elseNode = elseNode;
    }

    public Optional<Node> getParseOperation() {

        return parseOperation;
    }

    public IFNode getElseIfNode() {

        return elseIfNode;
    }

    public IFNode getElseNode() {

        return elseNode;
    }

    public BlockNode getBlockNode() {

        return blockNode;
    }

    @Override
    public String toString() {
        String ifPrint = "if(" + parseOperation.toString() + ") {\n" + blockNode.getStatements() + "}";
        if (elseIfNode != null && elseNode==null) {
            ifPrint += "else if(" + parseOperation.toString() + ") {\n" + blockNode.getStatements() + "}";
        } else if (elseNode != null) {
            ifPrint += "else {" + blockNode.getStatements() + "}";
        }
        return ifPrint;
    }
}
class ForNode extends StatementNode {
    private Optional<Node> firstCondition;
    private Optional<Node> secondCondition;
    private Optional<Node> thirdCondition;
    private BlockNode statements;

    public ForNode(Optional<Node> firstCondition, Optional<Node> secondCondition, Optional<Node> thirdCondition, BlockNode statements) {
        this.firstCondition = firstCondition;
        this.secondCondition = secondCondition;
        this.thirdCondition = thirdCondition;
        this.statements = statements;
    }

    public Optional<Node> getFirstCondition() {

        return firstCondition;
    }

    public Optional<Node> getSecondCondition() {

        return secondCondition;
    }

    public BlockNode getStatements() {

        return statements;
    }

    public Optional<Node> getThirdCondition() {

        return thirdCondition;
    }

    public String toString() {
        String forLoop = "for(" + firstCondition.toString() + "; " + secondCondition.toString() + "; " + thirdCondition.toString() + ") {\n" + statements.getStatements() + "}";
        return forLoop;
    }
}

class ForEachNode extends StatementNode {
    private Optional<Node> firstCondition;
    private Optional<Node> secondCondition;
    private BlockNode statements;

    public ForEachNode(Optional<Node> firstCondition, Optional<Node> secondCondition, BlockNode statements) {
        this.firstCondition = firstCondition;
        this.secondCondition = secondCondition;
        this.statements = statements;
    }

    public Optional<Node> getFirstCondition() {

        return firstCondition;
    }

    public Optional<Node> getSecondCondition() {

        return secondCondition;
    }

    public BlockNode getStatements() {

        return statements;
    }

    public String toString() {
        String forEachLoop = "for(" + firstCondition.toString() + " : " + secondCondition.toString() + ") {\n" + statements.getStatements() + "}";
        return forEachLoop;
    }
}

class DeleteNode extends StatementNode {
    private Optional<Node> conditional;

    public DeleteNode(Optional<Node> conditional) {

        this.conditional = conditional;
    }

    public Optional<Node> getConditional() {

        return conditional;
    }

    public String toString() {
        String delete = "delete " + conditional.toString();
        return delete;
    }
}

class WhileNode extends StatementNode {
    private Optional<Node> condition;
    private BlockNode statements;

    public WhileNode(Optional<Node> condition, BlockNode statements) {
        this.condition = condition;
        this.statements = statements;
    }

    public Optional<Node> getCondition() {

        return condition;
    }

    public BlockNode getStatements() {

        return statements;
    }

    public String toString() {
        String whileLoop = "while(" + condition.toString() + ") {\n" + statements.getStatements() + "}";
        return whileLoop;
    }
}

class DoWhileNode extends StatementNode {
    private Optional<Node> condition;
    private BlockNode statements;

    public DoWhileNode(Optional<Node> condition, BlockNode statements) {
        this.condition = condition;
        this.statements = statements;
    }

    public Optional<Node> getCondition() {

        return condition;
    }

    public BlockNode getStatements() {

        return statements;
    }

    public String toString() {
        String DowhileLoop = "do{\n" + statements.getStatements() + "} while(" + condition.toString() + ");";
        return DowhileLoop;
    }
}

class ReturnNode extends StatementNode {
    private Optional<Node> condition;

    public ReturnNode(Optional<Node> condition) {

        this.condition = condition;
    }

    public Optional<Node> getCondition() {

        return condition;
    }

    public String toString() {
        String returnPrint = "return " + condition.toString() + ";";
        return returnPrint;
    }
}

class FunctionCallNode extends StatementNode {
    private String functionName;
    private Optional<Node> tokenType;
    private LinkedList<Node> parameters;

    public FunctionCallNode(String functionName, LinkedList<Node> parameters) {
        this.functionName = functionName;
        this.parameters = parameters;
    }

    public LinkedList<Node> getParameters() {

        return parameters;
    }

    public String toString(){
        String functionCallPrint = functionName+"(";
        for(int i =0; i< parameters.size(); i++){
            functionCallPrint += parameters.get(i).toString();
            if (i< parameters.size()-1){
                functionCallPrint +=", ";
            }
        }
        functionCallPrint+=")";
        return functionCallPrint;
    }
}
/*class FunctionNode extends Node{
    private String name;
    private LinkedList<String> parameters;
    private LinkedList<StatementNode> statements;
    //BlockNode blockNode;
    public FunctionNode(String name, LinkedList<String> parameters){
        this.name = name;
        this.parameters = parameters;
    }
    public LinkedList<String> getParameters() {
        return parameters;
    }
    /*public void addtoStatement(LinkedList<StatementNode> statements){
        this.statements = statements;
    }*/
    /*public void setBlock(BlockNode blockNode){
        this.blockNode = blockNode;
    }
    public String getName(){
        return name;
    }
    public String toString() {
        StringBuffer parametersAdded = new StringBuffer("");
        while(parameters.size() >= 0){
            parametersAdded.append(", "+parameters);
        }
        return TokenType.FUNCTION.toString() + " " + name + "(" + parametersAdded + ")";
    }
}*/