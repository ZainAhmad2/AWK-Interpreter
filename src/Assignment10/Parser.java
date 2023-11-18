package Assignment10;


import java.util.LinkedList;
import java.util.Optional;

public class Parser {
    private final TokenHandler tokenHandler;
    private LinkedList<Token> tokens = new LinkedList<Token>();

    public Parser(LinkedList<Token> tokens) {
        this.tokens = tokens;
        tokenHandler = new TokenHandler(tokens);
    }

    /**
     * This method works by checking first, if there are more tokens in the entire list, then by peeking to the next token, and then
     * checking if that particular tokenType is the same one as the SEPERATOR token we are attempting to get rid of. If it does end up
     * being a SEPERATOR token, we call the MatchAndRemove method from the TokenHandler and get rid of it and return true.
     * If it isn't we just end up breaking the program.
     *
     * @return true
     */
    boolean AcceptSeperators() {
        while (tokenHandler.MoreTokens()) {
            Optional<Token> currentToken = tokenHandler.Peek(0);
            if (currentToken.get().getType() == TokenType.SEPERATOR) {
                tokenHandler.MatchAndRemove(TokenType.SEPERATOR);
            } else {
                break;
            }
        }
        return true;
    }

    /**
     * This method just checks to see if ParseFunction and ParseAction return false or true, if they both return false then it throws an
     * exception. If it returns true then it runs without any errors and returns programNode
     *
     * @return programNode
     */
    public ProgramNode Parser() {
        ProgramNode programNode = new ProgramNode();
        if (programNode != null) {
            if (!ParseFunction(programNode) && !ParseAction(programNode)) {
                throw new RuntimeException("Both ended up being false!");
            } else {
                return programNode;
            }
        }
        return programNode;
    }

    /**
     * This method goes through the LinkedList of tokens, making sure that it is in the correct order of a function in AWK, once it verifies
     * it up until it sees the TokenType closing bracket, it returns true to the programNode, creates the FunctionDefinitionNode, populates it
     * with functionName and the parameters, and adds it to the programNode LinkedList of functions. If it is not in the correct order than
     * the program ends up returning false to the programNode.
     *
     * @param programNode
     * @return true, false
     */
    boolean ParseFunction(ProgramNode programNode) {
        LinkedList<String> parameters = new LinkedList<String>();
        LinkedList<StatementNode> statements = new LinkedList<StatementNode>();
        String functionName = "";
        Optional<Token> currentToken = tokenHandler.Peek(0);
        if (currentToken.get().getType() == TokenType.FUNCTION) {
            tokenHandler.MatchAndRemove(TokenType.FUNCTION);
            currentToken = tokenHandler.Peek(0);
            //System.out.println("Function Success");
            if (currentToken.get().getType() == TokenType.WORD) {
                functionName = currentToken.get().getValue();
                tokenHandler.MatchAndRemove(TokenType.WORD);
                currentToken = tokenHandler.Peek(0);
                //System.out.println("First Word Success");
                if (currentToken.get().getType() == TokenType.OPENPARENTHESIS) {
                    tokenHandler.MatchAndRemove(TokenType.OPENPARENTHESIS);
                    //System.out.println("Open Parenthesis Success");
                    while (true) {
                        currentToken = tokenHandler.Peek(0);
                        if (currentToken.get().getType() == TokenType.CLOSEDPARENTHESIS) {
                            tokenHandler.MatchAndRemove(TokenType.CLOSEDPARENTHESIS);
                            //System.out.println("Closed Parenthesis Success");
                            break;
                        }
                        if (currentToken.get().getType() == TokenType.WORD) {
                            parameters.add(currentToken.get().getValue());
                            tokenHandler.MatchAndRemove(TokenType.WORD);
                            currentToken = tokenHandler.Peek(0);
                            //System.out.println("Second Word Success");
                            if (currentToken.get().getType() == TokenType.COMMA) {
                                tokenHandler.MatchAndRemove(TokenType.COMMA);
                                //System.out.println("Comma Success");
                            }
                        } else {
                            return false;
                        }
                    }
                    AcceptSeperators();
                    currentToken = tokenHandler.Peek(0);
                    if (currentToken.get().getType() == TokenType.OPENANGLEBRACKET) {
                        tokenHandler.MatchAndRemove(TokenType.OPENANGLEBRACKET);
                        AcceptSeperators();
                        currentToken = tokenHandler.Peek(0);
                        //System.out.println("Open Angle Bracket Success");
                        if (currentToken.get().getType() == TokenType.CLOSEDANGLEBRACKET) {
                            tokenHandler.MatchAndRemove(TokenType.CLOSEDANGLEBRACKET);
                            AcceptSeperators();
                            //System.out.println("Closed Angle Bracket Success");
                        } else {
                            //System.out.println("Fail");
                            return false;
                        }
                    }
                    FunctionDefinitionNode functionNode = new FunctionDefinitionNode(functionName, parameters);
                    BlockNode blockNode = ParseBlock();
                    functionNode.addtoStatement(blockNode.getStatements());
                    programNode.addFunctionToList(functionNode);
                    return true;
                }
            }
        }
        //System.out.println("fail");
        return false;
    }

    /**
     * This method checks for the TokenType BEGIN or END, if it is either of those it creates a new BlockNode through the ParseBlock() method
     * and adds it to that particular LinkedList in ProgramNode. If it doesn't end up being either of these, it returns an empty instance
     * of Optional and then calls ParseBlock().
     *
     * @param programNode
     * @return true, false
     */
    boolean ParseAction(ProgramNode programNode) {
        Optional<Token> currentToken = tokenHandler.Peek(0);
        AcceptSeperators();
        if (currentToken.isPresent()) {
            //currentToken = tokenHandler.Peek(0);
            AcceptSeperators();
            if (currentToken.get().getType() == TokenType.BEGIN) {
                tokenHandler.MatchAndRemove(TokenType.BEGIN);
                AcceptSeperators();
                BlockNode blockNode = ParseBlock();
                programNode.addtoBegin(blockNode);
                //System.out.println("Begin Worked!");
                return true;
            } else if (currentToken.get().getType() == TokenType.END) {
                tokenHandler.MatchAndRemove(TokenType.END);
                AcceptSeperators();
                BlockNode blockNode = ParseBlock();
                programNode.addtoEnd(blockNode);
                //System.out.println("END Worked!");
                return true;
            } else {
                AcceptSeperators();
                Optional<Node> conditional = ParseOperation();
                if (conditional.isPresent()) {
                    BlockNode blockNode = ParseBlock();
                    blockNode.setConditional(conditional);
                    programNode.otherBlocks(blockNode);
                    return true;
                } else {
                    BlockNode blockNode = ParseBlock();
                    programNode.otherBlocks(blockNode);
                    return true;
                }
                //System.out.println("Other Worked!");
            }
        }
        return false;
    }

    /**
     * After checking for a specific TokenType, we then match and remove that specific token and return the instance of Optional with the specified parameter from the word doc.
     * For StringLiterals and Numbers, we put the instance of ConstantNode into the parameter. If we detect a Pattern TokenType, we do something similar except with PatternNode
     * and return that instance of in the Optional. For the TokenType OPENPARENTHESIS, we then call the ParseOperation() and then make sure that the CLOSEDPARENTHESIS exists,
     * return the instance of Optional with the instance of ParseOperation(). For the next few, we check for the particular TokenType and then create an instance of
     * OperationNode with the left side and the OperationType token that we return in the Optional. If none of them are found, we check through ParseLValue, but, if nothing
     * is found there we return an empty instance of Optional.
     *
     * @return Optional.of(), Optional.Empty(), ParseLValue()
     */
    public Optional<Node> ParseBottomLevel() {
        Optional<Token> currentToken = tokenHandler.Peek(0);
        Optional<Token> nextToken = tokenHandler.Peek(1);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.STRINGLITERAL) {
                String value = currentToken.get().getValue();
                tokenHandler.MatchAndRemove(TokenType.STRINGLITERAL);
                ConstantNode constantNode = new ConstantNode(value);
                return Optional.of(constantNode);
            } else if (currentToken.get().getType() == TokenType.NUMBER) {
                String value = currentToken.get().getValue();
                tokenHandler.MatchAndRemove(TokenType.NUMBER);
                ConstantNode constantNode = new ConstantNode(value);
                System.out.println(constantNode);
                return Optional.of(constantNode);
            } else if (currentToken.get().getType() == TokenType.PATTERN) {
                String value = currentToken.get().getValue();
                tokenHandler.MatchAndRemove(TokenType.PATTERN);
                PatternNode patternNode = new PatternNode(value);
                //System.out.println(patternNode.toString());
                return Optional.of(patternNode);
            } else if (currentToken.get().getType() == TokenType.OPENPARENTHESIS) {
                tokenHandler.MatchAndRemove(TokenType.OPENPARENTHESIS);
                Optional<Node> mathExpression = ParseOperation();
                tokenHandler.MatchAndRemove(TokenType.CLOSEDPARENTHESIS);
                //System.out.println(mathExpression.toString());
                return mathExpression;
            } else if (currentToken.get().getType() == TokenType.NOT) {
                tokenHandler.MatchAndRemove(TokenType.NOT);
                Optional<Node> mathExpression = ParseOperation();
                OperationNode operationNode = new OperationNode(mathExpression.get(), OperationTypes.NOT);
                //System.out.println(operationNode.toString());
                return Optional.of(operationNode);
            } else if (currentToken.get().getType() == TokenType.MINUS) {
                tokenHandler.MatchAndRemove(TokenType.MINUS);
                Optional<Node> mathExpression = ParseOperation();
                OperationNode operationNode = new OperationNode(mathExpression.get(), OperationTypes.UNARYNEG);
                //System.out.println(operationNode.toString());
                return Optional.of(operationNode);
            } else if (currentToken.get().getType() == TokenType.PLUS) {
                tokenHandler.MatchAndRemove(TokenType.PLUS);
                Optional<Node> mathExpression = ParseOperation();
                OperationNode operationNode = new OperationNode(mathExpression.get(), OperationTypes.UNARYPOS);
                //System.out.println(operationNode.toString());
                return Optional.of(operationNode);
            } else if (currentToken.get().getType() == TokenType.DOUBLEPLUS) {
                tokenHandler.MatchAndRemove(TokenType.DOUBLEPLUS);
                Optional<Node> mathExpression = ParseOperation();
                OperationNode operationNode = new OperationNode(mathExpression.get(), OperationTypes.PREINC);
                //System.out.println(operationNode.toString());
                //System.out.println("Made it here");
                return Optional.of(operationNode);
            } else if (currentToken.get().getType() == TokenType.DOUBLEMINUS) {
                tokenHandler.MatchAndRemove(TokenType.DOUBLEMINUS);
                Optional<Node> mathExpression = ParseOperation();
                OperationNode operationNode = new OperationNode(mathExpression.get(), OperationTypes.PREDEC);
                //System.out.println(operationNode.toString());
                return Optional.of(operationNode);
            } else {
                Optional<Node> functionCallResult = ParseFunctionCall();
                if (functionCallResult.isPresent()) {
                    return functionCallResult;
                } else
                    return ParseLValue();
            }
        }
        return Optional.empty();
    }

    /**
     * After being called by the ParseBottomLevel(), we check for the particular TokenType in a similar fashion to the way we did it in the above method. The difference
     * being that after we find the TokenType.WORD, instead of making an instance of OperationNode we make an instance of VariableReferenceNode that depending on the existence of
     * ParseOperation, returns one with the variable name and the actual expression or one with only the variable name. If it doesn't end up finding either type, we return an
     * instance of Optional that is empty.
     *
     * @return Optional.of(), Optional.Empty
     */
    public Optional<Node> ParseLValue() {
        Optional<Token> currentToken = tokenHandler.Peek(0);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.DOLLAR) {
                tokenHandler.MatchAndRemove(TokenType.DOLLAR);
                Optional<Node> ParseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(ParseBottomLevel.get(), OperationTypes.DOLLAR);
                //System.out.println("This should work");
                //System.out.println(operationNode.toString());
                return Optional.of(operationNode);
            } else if (currentToken.get().getType() == TokenType.WORD) {
                String varname = currentToken.get().getValue();
                tokenHandler.MatchAndRemove(TokenType.WORD);
                currentToken = tokenHandler.Peek(0);
                if (currentToken.get().getType() == TokenType.OPENBRACKET) {
                    tokenHandler.MatchAndRemove(TokenType.OPENBRACKET);
                    Optional<Node> expressionIndex = ParseOperation();
                    tokenHandler.MatchAndRemove(TokenType.CLOSEDBRACKET);
                    if (expressionIndex.isPresent()) {
                        VariableReferenceNode variableReferenceNode = new VariableReferenceNode(varname, expressionIndex);
                        //System.out.print(variableReferenceNode.toString());
                        return Optional.of(variableReferenceNode);
                    }
                } else {
                    VariableReferenceNode variableReferenceNode = new VariableReferenceNode(varname);
                    //System.out.println(variableReferenceNode.toString());
                    return Optional.of(variableReferenceNode);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Calls the other methods that we made below and prints them out properly as long as there is more tokens ahead. I also implemented expression, term, factor recursively
     * in order to be able to collect and parse multiple operations at a time instead of just doing them one by one.
     *
     * @return the specific operation we are looking for.
     */
    public Optional<Node> ParseOperation() {
        //Node x = Expression();
        //System.out.println(x);
        Optional<Node> assignmentNode = AssignmentNodeStuff();
        if (assignmentNode.isPresent()) {
            Expression();
            return assignmentNode;
        }
        Optional<Node> ternaryNode = Ternary();
        if (ternaryNode.isPresent()) {
            Expression();
            return ternaryNode;
        }
        Optional<Node> orAndNode = ORAND();
        if (orAndNode.isPresent()) {
            Expression();
            return orAndNode;
        }
        Optional<Node> arrayIndexesNode = ArrayIndexes();
        if (arrayIndexesNode.isPresent()) {
            Expression();
            return arrayIndexesNode;
        }
        Optional<Node> matchOrNotNode = MatchorNot();
        if (matchOrNotNode.isPresent()) {
            Expression();
            return matchOrNotNode;
        }
        Optional<Node> comparisonsNode = Comparisons();
        if (comparisonsNode.isPresent()) {
            Expression();
            return comparisonsNode;
        }
        Optional<Node> stringConcatNode = StringCon();
        if (stringConcatNode.isPresent()) {
            Expression();
            return stringConcatNode;
        }
        Optional<Node> restofOppsNode = RestofOpps();
        if (restofOppsNode.isPresent()) {
            return restofOppsNode;
        }
        Optional<Node> postNode = Post();
        if (postNode.isPresent()) {
            Expression();
            return postNode;
        }
        if (assignmentNode.isEmpty() && ternaryNode.isEmpty() && orAndNode.isEmpty() && arrayIndexesNode.isEmpty() && matchOrNotNode.isEmpty()
                && comparisonsNode.isEmpty() && stringConcatNode.isEmpty() && restofOppsNode.isEmpty() && postNode.isEmpty()) {
            return ParseBottomLevel();
        }
        return Optional.empty();
    }

    public Node Factor() {
        Optional<Token> num = tokenHandler.MatchAndRemove(TokenType.NUMBER);
        Optional<Token> variables = tokenHandler.MatchAndRemove(TokenType.WORD);
        if (num.isPresent()) {
            ConstantNode constantNode = new ConstantNode(num.get().getValue());
            return constantNode;
        } else if (variables.isPresent()) {
            VariableReferenceNode variableReferenceNode = new VariableReferenceNode(variables.get().getValue());
            return variableReferenceNode;
        }
        if (tokenHandler.MatchAndRemove(TokenType.OPENPARENTHESIS).isPresent()) {
            Node expression = Expression();
            return expression;
        }
        return null;
    }

    public Node Term() {
        Node left = Factor();
        OperationTypes operationTypes;
        do {
            Optional<Token> operation = tokenHandler.MatchAndRemove(TokenType.EXPONENT);
            operationTypes = OperationTypes.EXPONENT;
            if (operation.isEmpty()) {
                operation = tokenHandler.MatchAndRemove(TokenType.MULTIPLY);
                operationTypes = OperationTypes.MULTIPLY;
                if (operation.isEmpty()) {
                    operation = tokenHandler.MatchAndRemove(TokenType.DIVIDE);
                    operationTypes = OperationTypes.DIVIDE;
                    if (operation.isEmpty()) {
                        operation = tokenHandler.MatchAndRemove(TokenType.MODULUS);
                        operationTypes = OperationTypes.MODULO;
                    }
                }
            }
            if (operation.isEmpty()) {
                return left;
            }
            Node right = Factor();
            left = new OperationNode(left, Optional.of(right), operationTypes);
        } while (true);
    }

    public Node Expression() {
        Node left = Term();
        OperationTypes operationTypes;
        do {
            Optional<Token> operation = tokenHandler.MatchAndRemove(TokenType.PLUS);
            operationTypes = OperationTypes.ADD;
            if (operation.isEmpty()) {
                operation = tokenHandler.MatchAndRemove(TokenType.MINUS);
                operationTypes = OperationTypes.SUBTRACT;
            }
            if (operation.isEmpty()) {
                return left;
            }
            Node right = Term();
            left = new OperationNode(left, Optional.of(right), operationTypes);
        } while (true);
    }

    /**
     * Rather than have everything in the ParseOperation method, I decided to split each level of the chart into different methods and call them in ParseOperation. For this
     * particular method, I go through the two character if else-if statement and return the assignment node that lets me return it in the form of the target of the operation
     * along with the operation.
     *
     * @return Optional.of(assignmentNode) or Optional.Empty() is nothing is there
     */
    public Optional<Node> AssignmentNodeStuff() {
        Optional<Token> currentToken = tokenHandler.Peek(1);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.EXPONENTEQUAL) {
                Optional<Node> parseLValue = ParseLValue();
                tokenHandler.MatchAndRemove(TokenType.EXPONENTEQUAL);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(parseLValue.get(), parseBottomLevel, OperationTypes.EXPONENT);
                AssignmentNode assignmentNode = new AssignmentNode(parseLValue.get(), operationNode);
                return Optional.of(assignmentNode);
            } else if (currentToken.get().getType() == TokenType.PERCENTEQUALS) {
                Optional<Node> parseLValue = ParseLValue();
                tokenHandler.MatchAndRemove(TokenType.PERCENTEQUALS);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(parseLValue.get(), parseBottomLevel, OperationTypes.MODULO);
                AssignmentNode assignmentNode = new AssignmentNode(parseLValue.get(), operationNode);
                return Optional.of(assignmentNode);
            } else if (currentToken.get().getType() == TokenType.MULTIPLYEQUALS) {
                Optional<Node> parseLValue = ParseLValue();
                tokenHandler.MatchAndRemove(TokenType.MULTIPLYEQUALS);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(parseLValue.get(), parseBottomLevel, OperationTypes.MULTIPLY);
                AssignmentNode assignmentNode = new AssignmentNode(parseLValue.get(), operationNode);
                return Optional.of(assignmentNode);
            } else if (currentToken.get().getType() == TokenType.DIVIDEEQUALS) {
                Optional<Node> parseLValue = ParseLValue();
                tokenHandler.MatchAndRemove(TokenType.DIVIDEEQUALS);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(parseLValue.get(), parseBottomLevel, OperationTypes.DIVIDE);
                AssignmentNode assignmentNode = new AssignmentNode(parseLValue.get(), operationNode);
                return Optional.of(assignmentNode);
            } else if (currentToken.get().getType() == TokenType.PLUSEQUALS) {
                Optional<Node> parseLValue = ParseLValue();
                tokenHandler.MatchAndRemove(TokenType.PLUSEQUALS);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(parseLValue.get(), parseBottomLevel, OperationTypes.ADD);
                AssignmentNode assignmentNode = new AssignmentNode(parseLValue.get(), operationNode);
                return Optional.of(assignmentNode);
            } else if (currentToken.get().getType() == TokenType.MINUSEQUALS) {
                Optional<Node> parseLValue = ParseLValue();
                tokenHandler.MatchAndRemove(TokenType.MINUSEQUALS);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(parseLValue.get(), parseBottomLevel, OperationTypes.SUBTRACT);
                AssignmentNode assignmentNode = new AssignmentNode(parseLValue.get(), operationNode);
                return Optional.of(assignmentNode);
            } else if (currentToken.get().getType() == TokenType.EQUALS) {
                Optional<Node> parseLValue = ParseLValue();
                tokenHandler.MatchAndRemove(TokenType.EQUALS);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(parseLValue.get(), parseBottomLevel, OperationTypes.EQ);
                AssignmentNode assignmentNode = new AssignmentNode(parseLValue.get(), operationNode);
                return Optional.of(assignmentNode);
            }
        }
        return Optional.empty();
    }

    /**
     * This method checks for the tokens related to the Ternary operator, if found we return a ternary operator with the three nodes as the original condition, the true case,
     * and the false case.
     *
     * @return Optional.of(ternaryNode) or Optional.Empty() is nothing is there
     */
    public Optional<Node> Ternary() {
        Optional<Token> currentToken = tokenHandler.Peek(1);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.QUESTION) {
                Optional<Node> parseCondition = ParseBottomLevel();
                tokenHandler.MatchAndRemove(TokenType.QUESTION);
                Optional<Node> trueCase = ParseBottomLevel();
                tokenHandler.MatchAndRemove(TokenType.COLON);
                Optional<Node> falseCase = ParseBottomLevel();
                //Optional<Node> parseOperation = ParseOperation();
                TernaryNode ternaryNode = new TernaryNode(parseCondition.get(), trueCase.get(), falseCase.get());
                return Optional.of(ternaryNode);
            }
        }
        return Optional.empty();
    }

    /**
     * This method checks for if the tokens is either an OR or an AND. If we find either we return the operationNode with the left Node and the operation type
     *
     * @return Optional.of(operationNode) or Optional.Empty() is nothing is there
     */
    public Optional<Node> ORAND() {
        Optional<Token> currentToken = tokenHandler.Peek(1);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.OR) {
                tokenHandler.MatchAndRemove(TokenType.OR);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                //Optional<Node> parseOperation = ParseOperation();
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(), OperationTypes.OR);
                return Optional.of(operationNode);
            } else if (currentToken.get().getType() == TokenType.DOUBLEAND) {
                tokenHandler.MatchAndRemove(TokenType.DOUBLEAND);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                //Optional<Node> parseOperation = ParseOperation();
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(), OperationTypes.AND);
                return Optional.of(operationNode);
            }
        }
        return Optional.empty();
    }

    /**
     * For this method we check if we have a single dimensional array or if we have a multidimensional array and then return the operationNode with a left, right Node, and the
     * Operation type for multi and only the left with the Operation type for a single array.
     *
     * @return Optional.of(operationNode) or Optional.Empty() is nothing is there
     */
    public Optional<Node> ArrayIndexes() {
        Optional<Token> currentToken = tokenHandler.Peek(1);
        Optional<Token> next = tokenHandler.Peek(3);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.OPENBRACKET) {
                tokenHandler.MatchAndRemove(TokenType.OPENBRACKET);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                LinkedList<Node> arrayValues = new LinkedList<>();
                if (currentToken.isPresent()) {
                    if (next.isPresent() && next.get().getType() == TokenType.COMMA) {
                        tokenHandler.MatchAndRemove(TokenType.COMMA);
                        Optional<Node> parseIndex = ParseBottomLevel();
                        arrayValues.add(parseBottomLevel.get());
                        arrayValues.add(parseIndex.get());
                        tokenHandler.MatchAndRemove(TokenType.CLOSEDBRACKET);
                        OperationNode operationNode = new OperationNode(arrayValues.get(1), parseBottomLevel, OperationTypes.IN);
                        return Optional.of(operationNode);
                    }
                    tokenHandler.MatchAndRemove(TokenType.CLOSEDBRACKET);
                    OperationNode operationNode = new OperationNode(parseBottomLevel.get(), OperationTypes.IN);
                    return Optional.of(operationNode);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * For this method we are checking for the ~ sign and then seeing if there is an exclamation mark in front of it for the notmatch token type.
     *
     * @return Optional.of(operationNode) or Optional.Empty() is nothing is there
     */
    public Optional<Node> MatchorNot() {
        Optional<Token> currentToken = tokenHandler.Peek(1);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.NOTMATCH) {
                tokenHandler.MatchAndRemove(TokenType.NOTMATCH);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                //Optional<Node> parseOperation = ParseOperation();
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(), OperationTypes.NOTMATCH);
                return Optional.of(operationNode);
            } else if (currentToken.get().getType() == TokenType.MATCH) {
                tokenHandler.MatchAndRemove(TokenType.MATCH);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                //Optional<Node> parseOperation = ParseOperation();
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(), OperationTypes.MATCH);
                return Optional.of(operationNode);
            }
        }
        return Optional.empty();
    }

    /**
     * We are looking for the comparison token types in this method and are returning a left node along with that particular token type
     *
     * @return Optional.of(operationNode) or Optional.Empty() is nothing is there
     */
    public Optional<Node> Comparisons() {
        Optional<Token> currentToken = tokenHandler.Peek(1);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.LESSTHAN) {
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                tokenHandler.MatchAndRemove(TokenType.LESSTHAN);
                //Optional<Node> parseOperation = ParseOperation();
                Optional<Node> SecondparseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(),SecondparseBottomLevel, OperationTypes.LT);
                return Optional.of(operationNode);
            } else if (currentToken.get().getType() == TokenType.LESSEQUAL) {
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                tokenHandler.MatchAndRemove(TokenType.LESSEQUAL);
                //Optional<Node> parseOperation = ParseOperation();
                Optional<Node> SecondparseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(),SecondparseBottomLevel, OperationTypes.LE);
                return Optional.of(operationNode);
            } else if (currentToken.get().getType() == TokenType.NOTEQUAL) {
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                tokenHandler.MatchAndRemove(TokenType.NOTEQUAL);
                //Optional<Node> parseOperation = ParseOperation();
                Optional<Node> SecondparseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(),SecondparseBottomLevel,  OperationTypes.NE);
                return Optional.of(operationNode);
            } else if (currentToken.get().getType() == TokenType.DOUBLEEQUALS) {
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                tokenHandler.MatchAndRemove(TokenType.DOUBLEEQUALS);
                //Optional<Node> parseOperation = ParseOperation();
                Optional<Node> SecondparseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(),SecondparseBottomLevel, OperationTypes.EQ);
                return Optional.of(operationNode);
            } else if (currentToken.get().getType() == TokenType.GREATERTHAN) {
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                tokenHandler.MatchAndRemove(TokenType.GREATERTHAN);
                //Optional<Node> parseOperation = ParseOperation();
                Optional<Node> SecondparseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(),SecondparseBottomLevel, OperationTypes.GT);
                return Optional.of(operationNode);
            } else if (currentToken.get().getType() == TokenType.GREATEREQUAL) {
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                tokenHandler.MatchAndRemove(TokenType.GREATEREQUAL);
                //Optional<Node> parseOperation = ParseOperation();
                Optional<Node> SecondparseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(),SecondparseBottomLevel, OperationTypes.GE);
                return Optional.of(operationNode);
            }
        }
        return Optional.empty();
    }

    /**
     * For this method we check for if two expressions are next to each other and return an operationNode with a left node for the first expression, a right node for the second,
     * and then the operationType.
     *
     * @return Optional.of(operationNode) or Optional.Empty() is nothing is there
     */
    public Optional<Node> StringCon() {
        Optional<Token> currentToken = tokenHandler.Peek(1);
        Optional<Token> previous = tokenHandler.Peek(0);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.WORD) {
                if (previous.get().getType() == TokenType.WORD) {
                    if (previous.get().getType() != TokenType.OPENBRACKET || previous.get().getType() != TokenType.OPENPARENTHESIS) {
                        Optional<Node> parseBottomLevel = ParseBottomLevel();
                        if (currentToken.isPresent() && currentToken.get().getType() == TokenType.WORD && parseBottomLevel.isPresent()) {
                            tokenHandler.MatchAndRemove(TokenType.WORD);
                            OperationNode operationNode = new OperationNode(parseBottomLevel.get(), OperationTypes.CONCATENATION);
                            return Optional.of(operationNode);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }
    //This project is one of my Opps

    /**
     * Since the operations that I check for here have been successfully created in Expression(), instead of checking for and removing the tokens in this method, I just check
     * if that particular tokenType exists and return that particular expression.
     *
     * @return Optional.of(operationNode) or Optional.Empty() is nothing is there
     */
    public Optional<Node> RestofOpps() {
        Optional<Token> currentToken = tokenHandler.Peek(1);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.PLUS) {
                return Optional.of(Expression());
            } else if (currentToken.get().getType() == TokenType.MINUS) {
                return Optional.of(Expression());
            } else if (currentToken.get().getType() == TokenType.MULTIPLY) {
                return Optional.of(Expression());
            } else if (currentToken.get().getType() == TokenType.DIVIDE) {
                return Optional.of(Expression());
            } else if (currentToken.get().getType() == TokenType.MODULUS) {
                return Optional.of(Expression());
            } else if (currentToken.get().getType() == TokenType.EXPONENT) {
                tokenHandler.MatchAndRemove(TokenType.EXPONENT);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                Optional<Node> parseOperation = ParseOperation();
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(), parseOperation, OperationTypes.EXPONENT);
                return Optional.of(Expression());
            }
        }
        return Optional.empty();
    }

    /**
     * This is for the post increment and post decrement of an expression, where we return the left node along with the OperationType
     *
     * @return Optional.of(operationNode) or Optional.Empty() is nothing is there
     */
    public Optional<Node> Post() {
        Optional<Token> currentToken = tokenHandler.Peek(1);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.DOUBLEPLUS) {
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                tokenHandler.MatchAndRemove(TokenType.DOUBLEPLUS);
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(), OperationTypes.POSTINC);
                return Optional.of(operationNode);
            } else if (currentToken.get().getType() == TokenType.DOUBLEMINUS) {
                tokenHandler.MatchAndRemove(TokenType.DOUBLEMINUS);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(), OperationTypes.POSTDEC);
                return Optional.of(operationNode);
            }
        }
        return Optional.empty();
    }

    /**
     * This method first checks for the opening angled bracket and then checks for adds either single line or multi line statements, adding them to the linked list of statements
     * we made in the BlockNode class. After there are no more statements to add, we match and remove the closing angled bracket and return the blockNode.
     *
     * @return blockNode
     */
    private BlockNode ParseBlock() {
        BlockNode blockNode = new BlockNode();
        Optional<Token> currentToken = tokenHandler.Peek(0);
        if (currentToken.get().getType() == TokenType.OPENANGLEBRACKET) {
            tokenHandler.MatchAndRemove(TokenType.OPENANGLEBRACKET);
            Optional<StatementNode> statementNode = ParseStatement();
            if (statementNode.isPresent()) {
                blockNode.addtoStatments(statementNode.get());
            }
            currentToken = tokenHandler.Peek(0);
            while (currentToken.get().getType() != TokenType.CLOSEDANGLEBRACKET) {
                statementNode = ParseStatement();
                blockNode.addtoStatments(statementNode.get());
                AcceptSeperators();
                currentToken = tokenHandler.Peek(0);
            }
            tokenHandler.MatchAndRemove(TokenType.CLOSEDANGLEBRACKET);
        } else {
            Optional<StatementNode> statementNode = ParseStatement();
            blockNode.addtoStatments(statementNode.get());
            AcceptSeperators();
        }

        return blockNode;
    }

    /**
     * This method looks through the different types of possible statements that we parse below and checks if they exist. If it does, we return that particular statement and
     * if it doesn't we return an Optional.Empty() that the method should've returned to us. Finally, if none of the statements end up existing, we check for the creation of a
     * ParseOperation that can hold a specific type of statement. If it does end up being present, we call the Assignmentnode to be able to return that particular type of
     * operation for us.
     *
     * @return Specific Statement Parse or AssignmentNode with the Operation we found.
     */
    public Optional<StatementNode> ParseStatement() {
        Optional<Token> currentToken = tokenHandler.Peek(0);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.CONTINUE) {
                tokenHandler.MatchAndRemove(TokenType.CONTINUE);
                StatementNode continueStatement = new ContinueNode();
                return Optional.of(continueStatement);
            } else if (currentToken.get().getType() == TokenType.BREAK) {
                tokenHandler.MatchAndRemove(TokenType.BREAK);
                StatementNode breakStatement = new BreakNode();
                return Optional.of(breakStatement);
            } else if (currentToken.get().getType() == TokenType.IF) {
                tokenHandler.MatchAndRemove(TokenType.IF);
                return ParseIfStatement();
            } else if (currentToken.get().getType() == TokenType.FOR) {
                tokenHandler.MatchAndRemove(TokenType.FOR);
                return ParseForStatement();
            } else if (currentToken.get().getType() == TokenType.DELETE) {
                tokenHandler.MatchAndRemove(TokenType.DELETE);
                return ParseDeleteStatement();
            } else if (currentToken.get().getType() == TokenType.WHILE) {
                tokenHandler.MatchAndRemove(TokenType.WHILE);
                return ParseWhileStatement();
            } else if (currentToken.get().getType() == TokenType.DO) {
                tokenHandler.MatchAndRemove(TokenType.DO);
                return ParseDoWhileStatement();
            } else if (currentToken.get().getType() == TokenType.RETURN) {
                tokenHandler.MatchAndRemove(TokenType.RETURN);
                return ParseReturnStatement();
            } else {
                Optional<Node> parseOperation = ParseOperation();
                if (parseOperation.isPresent()) {
                    if(parseOperation.get() instanceof AssignmentNode){
                        BlockNode blockNode = new BlockNode();
                        return Optional.of((AssignmentNode) parseOperation.get());
                    }if (parseOperation.get() instanceof FunctionCallNode) {
                        return Optional.of((FunctionCallNode) parseOperation.get());
                    }
                }

            }
        }
        return Optional.empty();
    }

    /**
     * This method checks for the tokens needed for return. There is no block in this method so we only return the parse operation that follows the return statement
     *
     * @return
     */
    private Optional<StatementNode> ParseReturnStatement() {
        Optional<Token> currentToken = tokenHandler.Peek(0);
        if (currentToken.isPresent()) {
            Optional<Node> parseOperation = ParseOperation();
            AcceptSeperators();
            ReturnNode returnNode = new ReturnNode(parseOperation);
            return Optional.of(returnNode);
        }
        return Optional.empty();
    }

    /**
     * Parses the necessary stuff for a do-while loop, where after the do is parsed in the ParseStatement method, we check for the stuff within the brackets through parseBlock,
     * the while after those brackets, and the operation that goes between the parenthesis. Afterwards, we return the DoWhileNode that holds the parseOperation and the block
     * of code in between.
     *
     * @return Optional.of(DowhileNode) or Optional.empty() if nothing is found
     */
    private Optional<StatementNode> ParseDoWhileStatement() {
        Optional<Token> currentToken = tokenHandler.Peek(0);
        if (currentToken.isPresent()) {
            BlockNode parseBlock = ParseBlock();
            tokenHandler.MatchAndRemove(TokenType.WHILE);
            tokenHandler.MatchAndRemove(TokenType.OPENPARENTHESIS);
            Optional<Node> parseOperation = ParseOperation();
            parseBlock.setConditional(parseOperation);
            tokenHandler.MatchAndRemove(TokenType.CLOSEDPARENTHESIS);
            AcceptSeperators();
            DoWhileNode DowhileNode = new DoWhileNode(parseOperation, parseBlock);
            return Optional.of(DowhileNode);
        }
        return Optional.empty();
    }

    /**
     * Similar to previous where instead we are looking for the stuff for a while loop, putting the operation in between the parenthesis, and then looking for the block of code
     * that is created after that point.
     *
     * @return Optional.of(whileNode) or Optional.empty() if nothing is found
     */
    private Optional<StatementNode> ParseWhileStatement() {
        Optional<Token> currentToken = tokenHandler.Peek(0);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.OPENPARENTHESIS) {
                tokenHandler.MatchAndRemove(TokenType.OPENPARENTHESIS);
                Optional<Node> parseOperation = ParseOperation();
                tokenHandler.MatchAndRemove(TokenType.CLOSEDPARENTHESIS);
                BlockNode parseBlock = ParseBlock();
                parseBlock.setConditional(parseOperation);
                WhileNode whileNode = new WhileNode(parseOperation, parseBlock);
                return Optional.of(whileNode);
            }
        }
        return Optional.empty();
    }

    /**
     * Similar to return where we check for only an operation after the initial match and removal and no block of code afterwards. The difference with this one being that delete
     * only gets rid of array stuff. So in order to make sure that is what is being deleted we call arrayIndexes and have that in place of the regular ParseOperation from before.
     *
     * @return Optional.of(deleteNode) or Optional.empty() if nothing is found
     */
    private Optional<StatementNode> ParseDeleteStatement() {
        Optional<Token> currentToken = tokenHandler.Peek(0);
        if (currentToken.isPresent()) {
            Optional<Node> arrayIndexes = ArrayIndexes();
            DeleteNode deleteNode = new DeleteNode(arrayIndexes);
            return Optional.of(deleteNode);
        }
        return Optional.empty();
    }

    /**
     * In this for loop parse, we first look for an open parenthesis and then the first conditional, as it ends up being the same for a regular for loop and a for each loop,
     * after that, we check for the existence of a colon, something that will indicate to us what type of loop we are dealing with. After this is done, if it ends up finding
     * a colon we check for the second conditional and a closed parenthesis, and then the block of code afterwards. After that, we return the foreachNode with the two
     * conditionals and the block of code afterwards. If it doesnt end up finding a conditional, we check for the second conditional, the third conditional, and then
     * the closing parenthesis after that with the block of code. We then return the forNode with the three conditionals and the blockNode. For both cases, we are accepting
     * seperators that will get rid of the semicolons that come after such statements.
     *
     * @return Optional.of(forEachNode); or return Optional.of(forNode); or Optional.empty();
     */
    private Optional<StatementNode> ParseForStatement() {
        Optional<Token> currentToken = tokenHandler.Peek(0);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.OPENPARENTHESIS) {
                tokenHandler.MatchAndRemove(TokenType.OPENPARENTHESIS);
                Optional<Node> firstConditional = ParseOperation();
                AcceptSeperators();
                currentToken = tokenHandler.Peek(0);
                if (currentToken.get().getType() == TokenType.COLON) {
                    tokenHandler.MatchAndRemove(TokenType.COLON);
                    Optional<Node> secondConditional = ParseOperation();
                    tokenHandler.MatchAndRemove(TokenType.CLOSEDPARENTHESIS);
                    BlockNode parseBlock = ParseBlock();
                    parseBlock.setConditional(secondConditional);
                    ForEachNode forEachNode = new ForEachNode(firstConditional, secondConditional, parseBlock);
                    return Optional.of(forEachNode);
                } else {
                    AcceptSeperators();
                    Optional<Node> secondConditional = ParseOperation();
                    AcceptSeperators();
                    Optional<Node> thirdConditional = ParseOperation();
                    tokenHandler.MatchAndRemove(TokenType.CLOSEDPARENTHESIS);
                    BlockNode parseBlock = ParseBlock();
                    parseBlock.setConditional(thirdConditional);
                    ForNode forNode = new ForNode(firstConditional, secondConditional, thirdConditional, parseBlock);
                    return Optional.of(forNode);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * For the if statement, we first check if the first token is an if Statement in the top part of the ParseStatement, after that, we check for the operation between the
     * parenthesis, the block of code that comes after that point, and then we save that if statement to the IFNode we made before, we then check if there is an else token
     * along with an if token as the next one. If its just an else statement we move on and skip the if block and go straight to else block of code that saves an empty for the
     * operation type and the block of parsed code after that. We then save that into the else IFNode list of statements we made in the IFNode class. If there is an if token after
     * the else token, we check for the parse operation and the block of code again and then return that elseIFNode stuff into the IFNode class again moving stuff over to the
     * next item in the list.
     *
     * @return return Optional.of(ifNode); or Optional.empty() if nothing is found
     */
    private Optional<StatementNode> ParseIfStatement() {
        Optional<Token> currentToken = tokenHandler.Peek(0);
        Optional<Token> nextToken = tokenHandler.Peek(1);
        if (currentToken.isPresent() && currentToken.get().getType() == TokenType.OPENPARENTHESIS) {
            tokenHandler.MatchAndRemove(TokenType.OPENPARENTHESIS);
            Optional<Node> parseOperation = ParseOperation();
            tokenHandler.MatchAndRemove(TokenType.CLOSEDPARENTHESIS);
            BlockNode blockNode = ParseBlock();
            blockNode.setConditional(parseOperation);
            IFNode ifNode = new IFNode(parseOperation, blockNode);
            IFNode currentIFNode = ifNode;
            currentToken = tokenHandler.Peek(0);
            nextToken = tokenHandler.Peek(1);
            while (currentToken.isPresent() && currentToken.get().getType() == TokenType.ELSE) {
                tokenHandler.MatchAndRemove(TokenType.ELSE);
                if (currentToken.isPresent() && nextToken.get().getType() == TokenType.IF) {
                    tokenHandler.MatchAndRemove(TokenType.IF);
                    tokenHandler.MatchAndRemove(TokenType.OPENPARENTHESIS);
                    parseOperation = ParseOperation();
                    tokenHandler.MatchAndRemove(TokenType.CLOSEDPARENTHESIS);
                    blockNode = ParseBlock();
                    blockNode.setConditional(parseOperation);
                    IFNode elseIFNode = new IFNode(parseOperation, blockNode);
                    currentIFNode.setElseIf(elseIFNode);
                    currentIFNode = elseIFNode;
                } else {
                    blockNode = ParseBlock();
                    IFNode elseNode = new IFNode(Optional.empty(), blockNode);
                    currentIFNode.setElse(elseNode);
                    currentIFNode = elseNode;
                    break;
                }
                currentToken = tokenHandler.Peek(0);
            }
            return Optional.of(ifNode);
        }
        return Optional.empty();
    }

    /**
     * Method that looks for and parses functionCalls. It looks for the name of the function, parses that, and then after that looks in the parenthesis of the function call
     * and adds those particular values to the linked list of parameters until there is no more left. It also parses and removes the commas that come between the parameters.
     * After there is no parameters to find, we return the FunctionCallNode that holds the name along with the list or we return an empty optional if nothing matching this
     * has been found.
     *
     * POST INTERPRETER:
     * In the new ParseFunctionCall, we look for token types listed in the document and then add their parameters along with a comma
     * between them with them. We then toString them each which will give us the function that we were looking for along with the
     * parameters that are going to be used by the user.
     * @return Optional.of(functionCallNode); or Optional.empty();
     */
    private Optional<Node> ParseFunctionCall() {
        Optional<Token> currentToken = tokenHandler.Peek(0);
        if (currentToken.isPresent()) {
            Optional<Token> nextToken = tokenHandler.Peek(1);
            if (nextToken.get().getType() == TokenType.OPENPARENTHESIS) {
                tokenHandler.MatchAndRemove(TokenType.OPENPARENTHESIS);
                if (currentToken.get().getType() == TokenType.WORD) {
                    String functionCallName = String.valueOf(currentToken.get().getValue());
                    tokenHandler.MatchAndRemove(TokenType.WORD);
                    LinkedList<Node> parametersLIST = new LinkedList<>();
                    while (tokenHandler.MoreTokens()) {
                        Optional<Node> parameter = ParseOperation();
                        if (parameter.isPresent()) {
                            parametersLIST.add(parameter.get());
                        }
                        currentToken = tokenHandler.Peek(0);
                        if (currentToken.isPresent() && currentToken.get().getType() == TokenType.COMMA) {
                            tokenHandler.MatchAndRemove(TokenType.COMMA);
                        } else {
                            break;
                        }
                    }
                    if (currentToken.get().getType() == TokenType.CLOSEDPARENTHESIS) {
                        tokenHandler.MatchAndRemove(TokenType.CLOSEDPARENTHESIS);
                        AcceptSeperators();
                        FunctionCallNode functionCallNode = new FunctionCallNode(functionCallName, parametersLIST);
                        return Optional.of(functionCallNode);
                    }
                }
            } else if (currentToken.get().getType() == TokenType.GETLINE) {
                String functionCallName = currentToken.get().getType().toString();
                tokenHandler.MatchAndRemove(TokenType.GETLINE);
                currentToken = tokenHandler.Peek(0);
                if (currentToken.get().getType() == TokenType.WORD) {
                    LinkedList<Node> parametersLIST = new LinkedList<>();
                    while (tokenHandler.MoreTokens()) {
                        Optional<Node> parameter = ParseOperation();
                        if (parameter.isPresent()) {
                            parametersLIST.add(parameter.get());
                            tokenHandler.MatchAndRemove(TokenType.WORD);
                        }
                        currentToken = tokenHandler.Peek(0);
                        if (currentToken.isPresent() && currentToken.get().getType() == TokenType.COMMA) {
                            tokenHandler.MatchAndRemove(TokenType.COMMA);
                        } else {
                            break;
                        }
                    }
                    AcceptSeperators();
                    FunctionCallNode functionCallNode = new FunctionCallNode(functionCallName, parametersLIST);
                    return Optional.of(functionCallNode);
                }
            } else if (currentToken.get().getType() == TokenType.PRINT) {
                String functionCallName = currentToken.get().getType().toString();
                tokenHandler.MatchAndRemove(TokenType.PRINT);
                currentToken = tokenHandler.Peek(0);
                if (currentToken.get().getType() == TokenType.WORD) {
                    LinkedList<Node> parametersLIST = new LinkedList<>();
                    while (tokenHandler.MoreTokens()) {
                        Optional<Node> parameter = ParseOperation();
                        if (parameter.isPresent()) {
                            parametersLIST.add(parameter.get());
                            tokenHandler.MatchAndRemove(TokenType.WORD);
                        }
                        currentToken = tokenHandler.Peek(0);
                        if (currentToken.isPresent() && currentToken.get().getType() == TokenType.COMMA) {
                            tokenHandler.MatchAndRemove(TokenType.COMMA);
                        } else {
                            break;
                        }
                    }
                    AcceptSeperators();
                    FunctionCallNode functionCallNode = new FunctionCallNode(functionCallName, parametersLIST);
                    return Optional.of(functionCallNode);
                }
            } else if (currentToken.get().getType() == TokenType.PRINTF) {
                String functionCallName = currentToken.get().getType().toString();
                tokenHandler.MatchAndRemove(TokenType.PRINTF);
                currentToken = tokenHandler.Peek(0);
                if (currentToken.get().getType() == TokenType.WORD) {
                    LinkedList<Node> parametersLIST = new LinkedList<>();
                    while (tokenHandler.MoreTokens()) {
                        Optional<Node> parameter = ParseOperation();
                        if (parameter.isPresent()) {
                            parametersLIST.add(parameter.get());
                            tokenHandler.MatchAndRemove(TokenType.WORD);
                        }
                        currentToken = tokenHandler.Peek(0);
                        if (currentToken.isPresent() && currentToken.get().getType() == TokenType.COMMA) {
                            tokenHandler.MatchAndRemove(TokenType.COMMA);
                        } else {
                            break;
                        }
                    }
                    AcceptSeperators();
                    FunctionCallNode functionCallNode = new FunctionCallNode(functionCallName, parametersLIST);
                    return Optional.of(functionCallNode);
                }
            } else if (currentToken.get().getType() == TokenType.EXIT) {
                String functionCallName = currentToken.get().getType().toString();
                tokenHandler.MatchAndRemove(TokenType.EXIT);
                currentToken = tokenHandler.Peek(0);
                if (currentToken.get().getType() == TokenType.WORD) {
                    LinkedList<Node> parametersLIST = new LinkedList<>();
                    while (tokenHandler.MoreTokens()) {
                        Optional<Node> parameter = ParseOperation();
                        if (parameter.isPresent()) {
                            parametersLIST.add(parameter.get());
                            tokenHandler.MatchAndRemove(TokenType.WORD);
                        }
                        currentToken = tokenHandler.Peek(0);
                        if (currentToken.isPresent() && currentToken.get().getType() == TokenType.COMMA) {
                            tokenHandler.MatchAndRemove(TokenType.COMMA);
                        } else {
                            break;
                        }
                    }
                    AcceptSeperators();
                    FunctionCallNode functionCallNode = new FunctionCallNode(functionCallName, parametersLIST);
                    return Optional.of(functionCallNode);
                }
            } else if (currentToken.get().getType() == TokenType.NEXTFILE) {
                String functionCallName = currentToken.get().getType().toString();
                tokenHandler.MatchAndRemove(TokenType.NEXTFILE);
                currentToken = tokenHandler.Peek(0);
                if (currentToken.get().getType() == TokenType.WORD) {
                    LinkedList<Node> parametersLIST = new LinkedList<>();
                    while (tokenHandler.MoreTokens()) {
                        Optional<Node> parameter = ParseOperation();
                        if (parameter.isPresent()) {
                            parametersLIST.add(parameter.get());
                            tokenHandler.MatchAndRemove(TokenType.WORD);
                        }
                        currentToken = tokenHandler.Peek(0);
                        if (currentToken.isPresent() && currentToken.get().getType() == TokenType.COMMA) {
                            tokenHandler.MatchAndRemove(TokenType.COMMA);
                        } else {
                            break;
                        }
                    }
                    AcceptSeperators();
                    FunctionCallNode functionCallNode = new FunctionCallNode(functionCallName, parametersLIST);
                    return Optional.of(functionCallNode);
                }
            } else if (currentToken.get().getType() == TokenType.NEXT) {
                String functionCallName = currentToken.get().getType().toString();
                tokenHandler.MatchAndRemove(TokenType.NEXT);
                currentToken = tokenHandler.Peek(0);
                if (currentToken.get().getType() == TokenType.WORD) {
                    LinkedList<Node> parametersLIST = new LinkedList<>();
                    while (tokenHandler.MoreTokens()) {
                        Optional<Node> parameter = ParseOperation();
                        if (parameter.isPresent()) {
                            parametersLIST.add(parameter.get());
                            tokenHandler.MatchAndRemove(TokenType.WORD);
                        }
                        currentToken = tokenHandler.Peek(0);
                        if (currentToken.isPresent() && currentToken.get().getType() == TokenType.COMMA) {
                            tokenHandler.MatchAndRemove(TokenType.COMMA);
                        } else {
                            break;
                        }
                    }
                    AcceptSeperators();
                    FunctionCallNode functionCallNode = new FunctionCallNode(functionCallName, parametersLIST);
                    return Optional.of(functionCallNode);
                }
            }
        }
        return Optional.empty();
    }
}
//Old code that didn't work/got lost in the sauce -> Better to just restart + Any other coding problems :)
    /*boolean ParseFunction(ProgramNode programNode) {
        LinkedList<String> parameters = new LinkedList<String>();
        LinkedList<StatementNode> statements = new LinkedList<StatementNode>();
        String functionName = "";
        Optional<Token> currentToken = tokenHandler.Peek(0);
            if (currentToken.isPresent() && currentToken.equals(TokenType.FUNCTION)) {
                tokenHandler.MatchAndRemove(TokenType.FUNCTION);
                currentToken = tokenHandler.Peek(0);
                if (currentToken.isPresent() && currentToken.equals(TokenType.WORD)) {
                    tokenHandler.MatchAndRemove(TokenType.WORD);
                    functionName = currentToken.get().getValue();
                    currentToken = tokenHandler.Peek(0);
                    if (currentToken.isPresent() && currentToken.equals(TokenType.OPENPARENTHESIS)) {
                        tokenHandler.MatchAndRemove(TokenType.OPENPARENTHESIS);
                        currentToken = tokenHandler.Peek(0);
                        if (currentToken.isPresent() && currentToken.equals(TokenType.CLOSEDPARENTHESIS)) {
                            tokenHandler.MatchAndRemove(TokenType.CLOSEDPARENTHESIS);
                            currentToken = tokenHandler.Peek(0);
                            /*AcceptSeperators();
                            FunctionNode functionNode = new FunctionNode(functionName, parameters);
                            BlockNode blockNode = ParseBlock();
                            functionNode.addtoStatement(blockNode.getStatements());
                            programNode.addFunctionToList(functionNode);
                            return true;
                            if (currentToken.isPresent() && currentToken.equals(TokenType.OPENANGLEBRACKET)) {
                                tokenHandler.MatchAndRemove(TokenType.OPENANGLEBRACKET);
                                //currentToken = tokenHandler.Peek(0);
                                AcceptSeperators();
                            }*/
                            /*if (currentToken.isPresent() && currentToken.equals(TokenType.CLOSEDANGLEBRACKET)) {
                                tokenHandler.MatchAndRemove(TokenType.CLOSEDANGLEBRACKET);
                                //currentToken = tokenHandler.Peek(0);
                                AcceptSeperators();
                                FunctionNode functionNode = new FunctionNode(functionName, parameters);
                                BlockNode blockNode = ParseBlock();
                                functionNode.addtoStatement(blockNode.getStatements());
                                programNode.addFunctionToList(functionNode);
                                return true;
                            }
                        }
                        while (currentToken.isPresent() && currentToken.equals(TokenType.WORD)) {
                            tokenHandler.MatchAndRemove(TokenType.WORD);
                            parameters.add(currentToken.get().getValue());
                            currentToken = tokenHandler.Peek(0);
                            /*FunctionDefinitionNode functionDefinitionNode = new FunctionDefinitionNode(functionName, parameters);
                            FunctionNode functionNode = new FunctionNode(functionName, parameters);
                            programNode.addFunctionToList(functionNode);
                            if (currentToken.isPresent() && currentToken.equals(TokenType.COMMA)) {
                                tokenHandler.MatchAndRemove(TokenType.COMMA);
                            } else {
                                break;
                            }
                            AcceptSeperators();*/
                        /*}
                        /*if (currentToken.isPresent() && currentToken.equals(TokenType.CLOSEDPARENTHESIS)) {
                            tokenHandler.MatchAndRemove(TokenType.CLOSEDPARENTHESIS);
                            AcceptSeperators();
                            FunctionNode functionNode = new FunctionNode(functionName, parameters);
                            BlockNode blockNode = ParseBlock();
                            functionNode.addtoStatement(blockNode.getStatements());
                            programNode.addFunctionToList(functionNode);
                            return true;
                        }
                    }
                }
                if (currentToken.isPresent() && currentToken.equals(TokenType.CLOSEDANGLEBRACKET)) {
                    tokenHandler.MatchAndRemove(TokenType.CLOSEDANGLEBRACKET);
                    //currentToken = tokenHandler.Peek(0);
                    AcceptSeperators();
                    FunctionNode functionNode = new FunctionNode(functionName, parameters);
                    BlockNode blockNode = ParseBlock();
                    functionNode.addtoStatement(blockNode.getStatements());
                    programNode.addFunctionToList(functionNode);
                    return true;
                }
            }
        return false;
    }
    FROM PARSE OPERATION DAWG!
    //Parser parser = new Parser(tokens);
        //System.out.println("Am here");
   Other stuff
        /*String functionName = "";
        LinkedList<String> parameters = new LinkedList<>();
        if (tokenHandler.MatchAndRemove(TokenType.FUNCTION).isPresent()) {
            FunctionDefinitionNode functionDefinitionNode = new FunctionDefinitionNode(functionName, parameters);
            FunctionNode newFunctionNode = new FunctionNode(functionName, parameters);
            BlockNode blockNode = ParseBlock();
            programNode.AddingtoFunctionNode(newFunctionNode);
        return true;*/

//Fixing and moving of stuff with no association:
/*
Optional<Token> currentToken = tokenHandler.Peek(1);
        Optional<Token> previous = tokenHandler.Peek(0);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.DOUBLEPLUS) {
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                tokenHandler.MatchAndRemove(TokenType.DOUBLEPLUS);
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(), OperationTypes.POSTINC);
                return Optional.of(operationNode);
            } else if (currentToken.get().getType() == TokenType.DOUBLEMINUS) {
                tokenHandler.MatchAndRemove(TokenType.DOUBLEMINUS);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(), OperationTypes.POSTDEC);
                return Optional.of(operationNode);
            } else if (currentToken.get().getType() == TokenType.EXPONENT) {
                tokenHandler.MatchAndRemove(TokenType.EXPONENT);
                Optional<Node> parseBottomLevel = ParseBottomLevel();
                Optional<Node> parseOperation = ParseOperation();
                OperationNode operationNode = new OperationNode(parseBottomLevel.get(), parseOperation, OperationTypes.EXPONENT);
                return Optional.of(operationNode);
            } else{
                    return Optional.empty();
                }
            } else {
                return ParseBottomLevel();
            }
        }
        return Optional.empty();
    }
    /*
    If Statement Block:
    Optional<Token> currentToken = tokenHandler.Peek(0);
        if (currentToken.isPresent()) {
            if (currentToken.get().getType() == TokenType.OPENPARENTHESIS) {
                tokenHandler.MatchAndRemove(TokenType.OPENPARENTHESIS);
                Optional<Node> parseOperation = ParseOperation();
                tokenHandler.MatchAndRemove(TokenType.CLOSEDPARENTHESIS);
                tokenHandler.MatchAndRemove(TokenType.OPENANGLEBRACKET);
                BlockNode blockNode = new BlockNode();
                tokenHandler.MatchAndRemove(TokenType.CLOSEDANGLEBRACKET);
            }
            if (currentToken.get().getType() == TokenType.ELSE) {
                if (currentToken.get().getType() == TokenType.IF) {
                    tokenHandler.MatchAndRemove(TokenType.OPENPARENTHESIS);
                    Optional<Node> parseOperation = ParseOperation();
                    tokenHandler.MatchAndRemove(TokenType.CLOSEDPARENTHESIS);

                }
            } else if (currentToken.get().getType() == TokenType.OPENANGLEBRACKET) {
                tokenHandler.MatchAndRemove(TokenType.OPENANGLEBRACKET);
                BlockNode blockNodeInElse = new BlockNode();
                tokenHandler.MatchAndRemove(TokenType.CLOSEDANGLEBRACKET);
                 ADD STATEMENT ADDITION HERE
                return null;
            }
            return null;
        }
        return Optional.empty();
    }
 */