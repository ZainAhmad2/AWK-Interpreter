package Assignment10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {
    HashMap<String, InterpreterDataType> globalVariables;
    HashMap<String, FunctionDefinitionNode> functionDefinitionNodeHashMap;
    LineHandler lineHandler;
    private boolean variadic = true;

    /**
     * This method first works by making the two hashmaps and then the processing of the file input. The first hashmap is used to store global variables detailed in the doc.
     * Then we make each of the builtIns and the inner LineHandler class that holds counters for the files we input and pertinent information to the actual string values in
     * the document.
     *
     * @param programNode
     * @param input
     * @throws IOException
     */
    public Interpreter(ProgramNode programNode, String input) throws IOException {
        this.globalVariables = new HashMap<>();
        this.functionDefinitionNodeHashMap = new HashMap<>();
        if (input != null) {
            List<String> lines = Files.readAllLines(Paths.get(input));
            lineHandler = new LineHandler(lines);
        } else {
            lineHandler = new LineHandler(new ArrayList<>());
        }
        globalVariables.put("FILENAME", new InterpreterDataType(input));

        globalVariables.put("FS", new InterpreterDataType(" "));
        globalVariables.put("OFMT", new InterpreterDataType("%.6g"));
        globalVariables.put("OFS", new InterpreterDataType(" "));
        globalVariables.put("ORS", new InterpreterDataType("\n"));
        /**
         * The printImplementation works through lambda functionality that takes in an array of parameters and then returns a string value. It then extracts the array through
         * the IADT, goes through all of the elements, and then puts them all together for the string return statement. Finally, it adds them to the hashmap of functions
         * and to the programNode of function blocknodes.
         */
        BuiltInFunctionDefinitionNode printImplementation = new BuiltInFunctionDefinitionNode("print", true, execute -> {
            String printFunction = "";
            InterpreterArrayDataType interpreterArrayDataType = (InterpreterArrayDataType) execute.get("0");
            for (int i = 0; i < interpreterArrayDataType.getArrayData().size(); i++) {
                InterpreterDataType interpreterDataType = interpreterArrayDataType.getArrayData().get(String.valueOf(i));
                printFunction += interpreterDataType;
            }
            return printFunction;
        });
        //printImplementation.execute = printImplementation::execute;
        functionDefinitionNodeHashMap.put("print", printImplementation);
        programNode.addFunctionToList(printImplementation);
        /**
         *The printfImplementation works by first checking if the first value is a formatted type of string. Then, it loops through the rest of the items in the array like the
         * other print statement. It then uses Java built in functionality to be able to print out the functions properly replacing the formatter stuff with the new string items
         * Finally, it adds them to the hashmap of functions and to the programNode of function blocknodes.
         */
        BuiltInFunctionDefinitionNode printfImplementation = new BuiltInFunctionDefinitionNode("printf", true, execute -> {
            InterpreterArrayDataType interpreterArrayDataType = (InterpreterArrayDataType) execute.get("0");
            String formatString = interpreterArrayDataType.getArrayData().get("0").toString();
            List<Object> argumentList = new ArrayList<>();
            for (int i = 1; i < interpreterArrayDataType.getArrayData().size(); i++) {
                InterpreterDataType interpreterDataType = interpreterArrayDataType.getArrayData().get(String.valueOf(i));
                argumentList.add(interpreterDataType.toString());
            }
            return String.format(formatString, argumentList.toArray());
        });
        //printfImplementation.execute = printfImplementation::execute;
        functionDefinitionNodeHashMap.put("printf", printfImplementation);
        programNode.addFunctionToList(printfImplementation);

        variadic = false;
        /**
         * Calls SplitAndAssign as detailed by the document. It then adds them to the hashmap of functions and to the programNode of function blocknodes.
         */
        BuiltInFunctionDefinitionNode getlineImplementation = new BuiltInFunctionDefinitionNode("getline", false, execute -> {
            lineHandler.fieldSeperator = "\n";
            if (lineHandler.SplitAndAssign() == true) {
                return "1";
            } else {
                return "0";
            }
        });
        //getlineImplementation.execute = getlineImplementation::execute;
        functionDefinitionNodeHashMap.put(getlineImplementation.getName(), getlineImplementation);
        programNode.addFunctionToList(getlineImplementation);
        /**
         * Calls SplitAndAssign as detailed by the document. It then adds them to the hashmap of functions and to the programNode of function blocknodes.
         */
        BuiltInFunctionDefinitionNode nextImplementation = new BuiltInFunctionDefinitionNode("next", false, execute -> {
            lineHandler.fieldSeperator = "\t";
            if (lineHandler.SplitAndAssign() == true) {
                return "1";
            } else {
                return "0";
            }
        });
        //nextImplementation.execute = nextImplementation::execute;
        functionDefinitionNodeHashMap.put("next", nextImplementation);
        programNode.addFunctionToList(nextImplementation);
        /**
         *In this function, we take three String inputs, the actual input, the regular expression, and then the string we want to replace it with. It then uses the Java built
         * in functionality to use the regular expression to find and replace the matching strings in the input string. It then returns the new output. Finally, it adds them
         * to the hashmap of functions and to the programNode of function blocknodes.
         */
        BuiltInFunctionDefinitionNode gsubImplementation = new BuiltInFunctionDefinitionNode("gsub", false, execute -> {
            String gsubinput = String.valueOf(execute.get("0"));
            String regex = String.valueOf(execute.get("1"));
            String replacement = String.valueOf(execute.get("2"));
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(gsubinput);
            String output = matcher.replaceAll(replacement);
            return output;
        });
        //gsubImplementation.execute = gsubImplementation::execute;
        functionDefinitionNodeHashMap.put("gsub", gsubImplementation);
        programNode.addFunctionToList(gsubImplementation);
        /**
         * The indexImplementation works by taking two parameters, "0" and "1" before converting them to Strings and then searching for the index of the second string in the
         * first string input. This also uses Java's builtIn functionality. Finally, it adds them to the hashmap of functions and to the programNode of function blocknodes.
         */
        BuiltInFunctionDefinitionNode indexImplementation = new BuiltInFunctionDefinitionNode("index", false, execute -> {
            String indexinput = String.valueOf(execute.get("0"));
            String substring = String.valueOf(execute.get("1"));
            int index = indexinput.indexOf(substring);
            return String.valueOf(index);
        });
        //indexImplementation.execute = indexImplementation::execute;
        functionDefinitionNodeHashMap.put("index", indexImplementation);
        programNode.addFunctionToList(indexImplementation);
        /**
         * LengthImplementation takes in a String input and then determines the length of the string using Java's built in functionality. Finally, it adds them to the hashmap
         * of functions and to the programNode of function blocknodes.
         */
        BuiltInFunctionDefinitionNode lengthImplementation = new BuiltInFunctionDefinitionNode("length", false, execute -> {
            //BuiltInFunctionDefinitionNode builtInFunctionDefinitionNode = new BuiltInFunctionDefinitionNode(parametersMap);
            String lengthinput = String.valueOf(execute.get("0"));
            int length = lengthinput.length();
            return String.valueOf(length);
        });
        //lengthImplementation.execute = lengthImplementation::execute;
        functionDefinitionNodeHashMap.put("length", lengthImplementation);
        programNode.addFunctionToList(lengthImplementation);
        /**
         * The matchImplementation takes in two string inputs. It uses Java's built in functionality to then check if the first String input matches the second string input
         * and then returns it as a boolean true or false. I then convert it to string to be able to print it out properly when it is called later. Finally, it adds them to
         * the hashmap of functions and to the programNode of function blocknodes.
         */
        BuiltInFunctionDefinitionNode matchImplementation = new BuiltInFunctionDefinitionNode("match", false, execute -> {
            //BuiltInFunctionDefinitionNode builtInFunctionDefinitionNode = new BuiltInFunctionDefinitionNode(parametersMap);
            String matchinput = String.valueOf(execute.get("0"));
            String regex = String.valueOf(execute.get("1"));
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(matchinput);
            boolean didItMatch = matchinput.contains(regex);
            return String.valueOf(didItMatch);
        });
        //matchImplementation.execute = matchImplementation::execute;
        functionDefinitionNodeHashMap.put("match", matchImplementation);
        programNode.addFunctionToList(matchImplementation);
        /**
         * The splitImplementation takes in two input arguments the string that we want to split and the thing that we want to split it by. Using java's built in functionality
         * we can then split the input string with the delimiter and covert the array that results from it into a String that can be returned.  Finally, it adds them to the
         * hashmap of functions and to the programNode of function blocknodes.
         */
        BuiltInFunctionDefinitionNode splitImplementation = new BuiltInFunctionDefinitionNode("split", false, execute -> {
            //BuiltInFunctionDefinitionNode builtInFunctionDefinitionNode = new BuiltInFunctionDefinitionNode(parametersMap);
            String splitinput = String.valueOf(execute.get("0"));
            String delimiter = String.valueOf(execute.get("1"));
            String[] splitString = splitinput.split(delimiter);
            return Arrays.toString(splitString);
        });
        //splitImplementation.execute = splitImplementation::execute;
        functionDefinitionNodeHashMap.put("split", splitImplementation);
        programNode.addFunctionToList(splitImplementation);
        /**
         * The sprintf implementation works by taking in all of the possible arguments that the user wants and puts them together into a single string output. It is then added
         * to the functionDefNode hashmap the programNode that holds all the other functions to the list.
         */
        BuiltInFunctionDefinitionNode sprintfImplementation = new BuiltInFunctionDefinitionNode("sprintf", false, execute -> {
            String sprintf = "";
            LinkedList<Object> ArgumentList = new LinkedList<>();
            for (Object value : execute.values()) {
                ArgumentList.add(value);
            }
            Object[] args = ArgumentList.toArray();
            for (Object arg : args) {
                sprintf += arg;
            }
            return sprintf;
        });
        //sprintfImplementation.execute = sprintfImplementation::execute;
        functionDefinitionNodeHashMap.put("sprintf", sprintfImplementation);
        programNode.addFunctionToList(sprintfImplementation);
        /**
         * Almost exactly like the gsub except, instead of replacing every instance where the strings match...this only replaces the first one it sees. It then adds them to the
         * hashmap of functions and to the programNode of function blocknodes.
         */
        BuiltInFunctionDefinitionNode subImplementation = new BuiltInFunctionDefinitionNode("sub", false, execute -> {
            //BuiltInFunctionDefinitionNode builtInFunctionDefinitionNode = new BuiltInFunctionDefinitionNode(parametersMap);
            String subinput = String.valueOf(execute.get("0"));
            String regex = String.valueOf(execute.get("1"));
            String replacement = String.valueOf(execute.get("2"));
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(subinput);
            String output = matcher.replaceFirst(replacement);
            return output;
        });
        //subImplementation.execute = subImplementation::execute;
        functionDefinitionNodeHashMap.put("sub", subImplementation);
        programNode.addFunctionToList(subImplementation);
        /**
         *The substr implementation works by taking in a string input and two integer parameters. Then, by using javas built in functionality to be able to print from the very
         * start of whatever the user asks us up until the end of wherever in the string the user asks us to. We then return the new string that was made from the start to end
         * index. Finally, it adds them to the hashmap of functions and to the programNode of function blocknodes.
         */
        BuiltInFunctionDefinitionNode substrImplementation = new BuiltInFunctionDefinitionNode("substr", false, execute -> {
            //BuiltInFunctionDefinitionNode builtInFunctionDefinitionNode = new BuiltInFunctionDefinitionNode(parametersMap);
            String substrinput = String.valueOf(execute.get("0"));
            int startIndex = Integer.parseInt(String.valueOf(execute.get("1")));
            int endIndex = Integer.parseInt(String.valueOf(execute.get("2")));
            String substring = substrinput.substring(startIndex, endIndex);
            return substring;
        });
        //substrImplementation.execute = substrImplementation::execute;
        functionDefinitionNodeHashMap.put("substr", substrImplementation);
        programNode.addFunctionToList(substrImplementation);
        /**
         * Takes in a single string input and then convert it to lower case by using Java built in functionality. It then adds them to the hashmap of functions and to the
         * programNode of function blocknodes.
         */
        BuiltInFunctionDefinitionNode tolowerImplementation = new BuiltInFunctionDefinitionNode("tolower", false, execute -> {
            //BuiltInFunctionDefinitionNode builtInFunctionDefinitionNode = new BuiltInFunctionDefinitionNode(parametersMap);
            String lowerinput = String.valueOf(execute.get("0"));
            String toLower = lowerinput.toLowerCase();
            return toLower;
        });
        //tolowerImplementation.execute = tolowerImplementation::execute;
        functionDefinitionNodeHashMap.put("tolower", tolowerImplementation);
        programNode.addFunctionToList(tolowerImplementation);
        /**
         * Takes in a single string input and then convert it to upper case by using Java built in functionality. It then adds them to the hashmap of functions and to the
         * programNode of function blocknodes.
         */
        BuiltInFunctionDefinitionNode toupperImplementation = new BuiltInFunctionDefinitionNode("toupper", false, execute -> {
            //BuiltInFunctionDefinitionNode builtInFunctionDefinitionNode = new BuiltInFunctionDefinitionNode(parametersMap);
            String upperinput = String.valueOf(execute.get("0"));
            String toUpper = upperinput.toUpperCase();
            return toUpper;
        });
        //toupperImplementation.execute = toupperImplementation::execute;
        functionDefinitionNodeHashMap.put("toupper", toupperImplementation);
        programNode.addFunctionToList(toupperImplementation);
    }

    public InterpreterDataType getIDT(Node node, HashMap<String, InterpreterDataType> localVariables) throws Exception {
        /**
         * The assignmentNode instanceof first makes sure the left(the target) is an instance of a VariableReferenceNode or an OperationNode with an OperationType of DOLLAR
         * attatched to it. Once this part is done, we use recursion to be able to get the specific tokens associated with the right side of the assignment and put that value
         * into the hashmap of local variables. Finally, we get that value and end up returning that as the result, successfully interpreting the AssignmentNode instance of.
         */
        if (node instanceof AssignmentNode) {
            AssignmentNode assignmentNode = (AssignmentNode) node;
            Node target = assignmentNode.target;
            if (target instanceof VariableReferenceNode || target instanceof OperationNode && ((OperationNode) target).getOperationTypes().equals(OperationTypes.DOLLAR)) {
                InterpreterDataType rightSide = getIDT(assignmentNode.target, localVariables);
                localVariables.put(assignmentNode.expression.toString(), rightSide);
                return rightSide;
            }
            /**
             * We just return a new IDT with the value set to the actual constant nodes value while also putting that set value into the hashmap of local variables.
             */
        } else if (node instanceof ConstantNode) {
            ConstantNode constantNode = (ConstantNode) node;
            // localVariables.put(constantNode.toString(), new InterpreterDataType(constantNode.toString()));
            return new InterpreterDataType(constantNode.getConstantValue());
            /**
             * Takes in the functioncallNode and the local variables into the runFunctionCall method. For right now, it just returns an empty string.
             */
        } else if (node instanceof FunctionCallNode) {
            FunctionCallNode functionCallNode = (FunctionCallNode) node;
            String functionCallResult = runFunctionCall(functionCallNode, localVariables);
            return new InterpreterDataType(functionCallResult);
            /**
             * When a PatternNode instance of is found in my program, it throws a runtime exception as the user is not allowed to pass a pattern to a function or assignment.
             */
        } else if (node instanceof PatternNode) {
            throw new RuntimeException("You cannot try to pass a pattern to a function or assignment!");
            /**
             * We first determine what is being held in the boolean conditional, calling getIDT to figure out what is being held and what we are trying to compare. Next, we
             * call getIDT, and determine the true and false case, determining if the values there result in the conditional returning either the true case that we interpreted
             * or the false case that we interpreted depending on what the condtional ended up becoming.
             */
        } else if (node instanceof TernaryNode) {
            TernaryNode ternaryNode = (TernaryNode) node;
            InterpreterDataType expression = getIDT(ternaryNode.getExpression(), localVariables);
            if (expression != null) {
                InterpreterDataType truuCase = getIDT(ternaryNode.getTrueCase(), localVariables);
                return truuCase;
            } else {
                InterpreterDataType falseCase = getIDT(ternaryNode.getFalseCase(), localVariables);
                return falseCase;
            }
            /**
             * For the VariableReferenceNode, when the variable is not an array reference, we intialize the variable and put it into the local variable hashmap and then look
             * for the value in the localvariable or globalvariable hashmaps. If it does end up being an array reference, we look for the index of the array, set the value of
             * it to IADT to be able to collect the array data, collect the resolved index by turning it into a string and then looking up that index in the variable's hashmap.
             * Also, if it doesn't end up being able to become an IADT, we throw an exception stating that so. If it does end up being correct, we return the array with the
             * data and the proper index we need.
             */
        } else if (node instanceof VariableReferenceNode) {
            VariableReferenceNode variableReferenceNode = (VariableReferenceNode) node;
            String variableName = variableReferenceNode.getName();
            Optional<Node> expressionIndex = variableReferenceNode.getExpressionIndex();
            if (expressionIndex != null) {
                localVariables.put(variableName, new InterpreterArrayDataType(String.valueOf(expressionIndex)));
            } else {
                localVariables.put(variableName, new InterpreterDataType("0"));
            }
            InterpreterDataType localVarCheck = localVariables.get(variableName);
            if (expressionIndex != null) {
                if (localVarCheck instanceof InterpreterArrayDataType) {
                    InterpreterArrayDataType array = (InterpreterArrayDataType) localVarCheck;
                    InterpreterDataType indexIDT = getIDT(expressionIndex.get(), localVariables);
                    String resolvedIndex = indexIDT.toString();
                    if (array.getArrayData().containsValue(resolvedIndex)) {
                        return array.getArrayData().get(resolvedIndex);
                    }
                } else {
                    throw new RuntimeException("Variable is not of type IADT!");
                }
            } else {
                if (localVarCheck != null) {
                    return localVarCheck;
                } else if (globalVariables.containsKey(variableName)) {
                    return globalVariables.get(variableName);
                }
            }
            /**
             * For OperationNode, we first do the comparisons, setting it up as a try-catch statement, where it determines whether the string value that we are passing can be
             * converted to a float properly. If it can, we compare the float value of the left side along with the float value of the right side. If both sides cannot be
             * converted we throw a NumberFormatException and compare the string values of the left and right side and return true or false depending on the type of
             * comparison we are looking for.
             *
             * Next, for the boolean operations since there is only the left side of the expression we are looking for, we do an else if with if the right side is present, and
             * then do another try catch with the NumberFormatExpression. If it can be converted, we then make sure that the value is not equal to 0.0 and return true if it
             * doesnt. If it is caught, we just return false for the passed in tokens.
             *
             * For match and notmatch, we use Java's builtin regex functions to determine if the left and the right side match with each other. If it doesnt, we return true or
             * false determining on the specific values passed through and the specific operationType we are using to check.
             *
             * For the dollar assignment in my operationNode, I figure out the left side of the operation using getValue and then add the dollar to that left side. This
             * returns the operationNode with the dollar attatched to in the new IDT.
             *
             * For the six math based operations, I collect the left value, declare the variable in VariableReferenceNode if needed and then use that converted value to actually
             * perform the operation that we need. If there ends up being a right side for the calculation we then add or subtract the left and right side together.
             *
             * Concatenation I get the value of the left and right side of the operation and then combine the two values that I got together. This results in a proper string
             * concatenation.
             *
             * Finally, for the index of the array in OperatioNode, I check first if the right hand operation is a VariableReferenceNode and has the array available, if it doesnt
             * end up existing, I throw a RuntimeException saying the right hand side is not an array. I then check for the left value thats being held in the array and determine
             * if that value actually exists in the local or global variables declared each time VariableReferenceNode is called upon using getIDT. In the end, it should properly
             * get an array if it exists and throw an error if it doesnt exist.
             */
        } else if (node instanceof OperationNode) {
            OperationNode operationNode = (OperationNode) node;
            InterpreterDataType leftOperation = getIDT(operationNode.getLeft(), localVariables);
            if (operationNode.getRight().isPresent()) {
                InterpreterDataType rightOperation = getIDT(operationNode.getRight().get(), localVariables);
                try {
                    float leftFloat = Float.parseFloat(leftOperation.getValue());
                    float rightFloat = Float.parseFloat(rightOperation.getValue());
                    if (operationNode.getOperationTypes() == OperationTypes.EQ) {
                        return new InterpreterDataType(String.valueOf(leftFloat == rightFloat));
                    } else if (operationNode.getOperationTypes() == OperationTypes.NE) {
                        return new InterpreterDataType(String.valueOf(leftFloat != rightFloat));
                    } else if (operationNode.getOperationTypes() == OperationTypes.LT) {
                        return new InterpreterDataType(String.valueOf(leftFloat < rightFloat));
                    } else if (operationNode.getOperationTypes() == OperationTypes.LE) {
                        return new InterpreterDataType(String.valueOf(leftFloat <= rightFloat));
                    } else if (operationNode.getOperationTypes() == OperationTypes.GT) {
                        return new InterpreterDataType(String.valueOf(leftFloat > rightFloat));
                    } else if (operationNode.getOperationTypes() == OperationTypes.GE) {
                        return new InterpreterDataType(String.valueOf(leftFloat >= rightFloat));
                    } else if (operationNode.getOperationTypes() == OperationTypes.UNARYPOS) {
                        float result = +leftFloat;
                        return new InterpreterDataType(String.valueOf(result));
                    } else if (operationNode.getOperationTypes() == OperationTypes.UNARYNEG) {
                        float result = -leftFloat;
                        return new InterpreterDataType(String.valueOf(result));
                    } else if (operationNode.getOperationTypes() == OperationTypes.ADD) {
                        float result = leftFloat + rightFloat;
                        return new InterpreterDataType(String.valueOf(result));
                    } else if (operationNode.getOperationTypes() == OperationTypes.SUBTRACT) {
                        float result = leftFloat - rightFloat;
                        return new InterpreterDataType(String.valueOf(result));
                    } else if (operationNode.getOperationTypes() == OperationTypes.DIVIDE) {
                        if (rightFloat != 0) {
                            float result = leftFloat / rightFloat;
                            return new InterpreterDataType(String.valueOf(result));
                        } else {
                            throw new RuntimeException("Division By Zero!");
                        }
                    } else if (operationNode.getOperationTypes() == OperationTypes.MULTIPLY) {
                        float result = leftFloat * rightFloat;
                        return new InterpreterDataType(String.valueOf(result));
                    } else if (operationNode.getOperationTypes() == OperationTypes.MODULO) {
                        float result = leftFloat % rightFloat;
                        return new InterpreterDataType(String.valueOf(result));
                    } else if (operationNode.getOperationTypes() == OperationTypes.EXPONENT) {
                        float result = (float) Math.pow(leftFloat, rightFloat);
                        return new InterpreterDataType(String.valueOf(result));
                    } else if (operationNode.getOperationTypes() == OperationTypes.IN) {
                        if (operationNode.getRight().get() instanceof VariableReferenceNode) {
                            VariableReferenceNode rightVariable = (VariableReferenceNode) operationNode.getRight().get();
                            String RightArray = rightVariable.getName();
                            String variableName = leftOperation.getValue();
                            if (localVariables.containsValue(variableName)) {
                                return new InterpreterDataType(variableName);
                            } else if (globalVariables.containsKey(variableName)) {
                                return new InterpreterDataType(variableName);
                            } else {
                                throw new RuntimeException("The left side was not found!");
                            }
                        } else {
                            throw new RuntimeException("The right side was not a variable reference node!");
                        }
                    } else if (operationNode.getOperationTypes() == OperationTypes.CONCATENATION) {
                        String leftValue = operationNode.getLeft().toString();
                        String rightValue = String.valueOf(operationNode.getRight().get().toString());
                        String result = leftValue + rightValue;
                        return new InterpreterDataType(result);
                    }
                } catch (NumberFormatException exception) {
                    if (operationNode.getOperationTypes() == OperationTypes.EQ) {
                        return new InterpreterDataType(String.valueOf(leftOperation.getValue().equals(rightOperation.getValue())));
                    } else if (operationNode.getOperationTypes() == OperationTypes.NE) {
                        return new InterpreterDataType(String.valueOf(!leftOperation.getValue().equals(rightOperation.getValue())));
                    } else if (operationNode.getOperationTypes() == OperationTypes.LT) {
                        return new InterpreterDataType(String.valueOf(leftOperation.getValue().compareTo(rightOperation.getValue()) < 0));
                    } else if (operationNode.getOperationTypes() == OperationTypes.LE) {
                        return new InterpreterDataType(String.valueOf(leftOperation.getValue().compareTo(rightOperation.getValue()) <= 0));
                    } else if (operationNode.getOperationTypes() == OperationTypes.GT) {
                        return new InterpreterDataType(String.valueOf(leftOperation.getValue().compareTo(rightOperation.getValue()) > 0));
                    } else if (operationNode.getOperationTypes() == OperationTypes.GE) {
                        return new InterpreterDataType(String.valueOf(leftOperation.getValue().compareTo(rightOperation.getValue()) >= 0));
                    }
                }
            } else if (operationNode.getOperationTypes() == OperationTypes.AND) {
                try {
                    float leftValue = Float.parseFloat(leftOperation.getValue());
                    boolean checked = leftValue != 0.0;
                    return new InterpreterDataType(String.valueOf(checked));
                } catch (NumberFormatException exception) {
                    return new InterpreterDataType(String.valueOf(false));
                }
            } else if (operationNode.getOperationTypes() == OperationTypes.OR) {
                try {
                    float leftValue = Float.parseFloat(leftOperation.getValue());
                    boolean checked = leftValue != 0.0;
                    return new InterpreterDataType(String.valueOf(checked));
                } catch (NumberFormatException exception) {
                    return new InterpreterDataType(String.valueOf(false));
                }
            } else if (operationNode.getOperationTypes() == OperationTypes.NOT) {
                try {
                    float leftValue = Float.parseFloat(leftOperation.getValue());
                    boolean checked = leftValue != 0.0;
                    return new InterpreterDataType(String.valueOf(checked));
                } catch (NumberFormatException exception) {
                    return new InterpreterDataType(String.valueOf(false));
                }
            } else if (operationNode.getOperationTypes() == OperationTypes.MATCH) {
                Pattern pattern = Pattern.compile(operationNode.getLeft().toString());
                Matcher matcher = pattern.matcher(leftOperation.getValue().toString());
                boolean matchCheck = matcher.find();
                return new InterpreterDataType(String.valueOf(matchCheck));
            } else if (operationNode.getOperationTypes() == OperationTypes.NOTMATCH) {
                Pattern pattern = Pattern.compile(operationNode.getLeft().toString());
                Matcher matcher = pattern.matcher(leftOperation.getValue().toString());
                boolean matchCheck = !matcher.find();
                return new InterpreterDataType(String.valueOf(matchCheck));
            } else if (operationNode.getOperationTypes() == OperationTypes.DOLLAR) {
                String variable = "$" + leftOperation.getValue();
                return new InterpreterDataType(variable);
            } else if (operationNode.getOperationTypes() == OperationTypes.POSTINC) {
                float leftFloat = Float.parseFloat(new InterpreterDataType(leftOperation.getValue()).toString());
                leftFloat++;
                return new InterpreterDataType(String.valueOf(leftFloat));
            } else if (operationNode.getOperationTypes() == OperationTypes.POSTDEC) {
                float leftFloat = Float.parseFloat(leftOperation.getValue());
                leftFloat--;
                return new InterpreterDataType(String.valueOf(leftFloat));
            } else if (operationNode.getOperationTypes() == OperationTypes.PREINC) {
                float leftFloat = Float.parseFloat(leftOperation.getValue());
                ++leftFloat;
                return new InterpreterDataType(String.valueOf(leftFloat));
            } else if (operationNode.getOperationTypes() == OperationTypes.PREDEC) {
                float leftFloat = Float.parseFloat(leftOperation.getValue());
                --leftFloat;
                return new InterpreterDataType(String.valueOf(leftFloat));
            }
        }
        return null;
    }

    public ReturnType ProcessStatement(HashMap<String, InterpreterDataType> locals, StatementNode stmt) throws Exception {
        /**
         * Checks for the statement that is of type break and returns a break statement
         */
        if (stmt instanceof BreakNode) {
            return new ReturnType(ReturnTypes.BREAK);
            /**
             * This else if checks for the statement that is of type continue and returns the continue statement when it is found
             **/
        } else if (stmt instanceof ContinueNode) {
            return new ReturnType(ReturnTypes.CONTINUE);
            /**
             * First, I figure out the conditional in the aforementioned array, if the index does actually exist I make sure it
             * is an instance of a VariableReferenceNode. If it is a VRN we know that it is an array that can be deleted. Then,
             * we can resolve the index and can find out if the value actually exists in either the global or local variables. If
             * it is found in either we delete that specific index or, we delete the entire array. If it is not a VRN, we throw
             * an exception and the item cannot be deleted.
             */
        } else if (stmt instanceof DeleteNode) {
            DeleteNode deleteNode = (DeleteNode) stmt;
            Optional<Node> conditional = deleteNode.getConditional();
            if (conditional.isPresent()) {
                Node condition = conditional.get();
                if (condition instanceof VariableReferenceNode) {
                    String arrayName = String.valueOf(((VariableReferenceNode) condition).getName());
                    if (locals.containsKey(arrayName)) {
                        InterpreterArrayDataType array = (InterpreterArrayDataType) locals.get(arrayName);
                        InterpreterDataType indexIDT = getIDT(((VariableReferenceNode) condition).getExpressionIndex().get(), locals);
                        array.arrayData.remove(indexIDT.getValue());
                        return new ReturnType(ReturnTypes.NONE, arrayName);
                    } else if (globalVariables.containsKey(arrayName)) {
                        InterpreterArrayDataType array = (InterpreterArrayDataType) locals.get(arrayName);
                        InterpreterDataType indexIDT = getIDT(((VariableReferenceNode) condition).getExpressionIndex().get(), locals);
                        array.arrayData.remove(indexIDT.getValue());
                        return new ReturnType(ReturnTypes.NONE, arrayName);
                    } else {
                        InterpreterArrayDataType array = (InterpreterArrayDataType) locals.get(arrayName);
                        array.arrayData.clear();
                    }
                } else {
                    throw new Exception("Delete doesn't have a conditional for the array!");
                }
            }
            /**
             * First, we interpret the list of all the statements given in the do-while loop. While the conditional we got is true
             * through getIDT, we loop through the statements until we either find a statement that asks to break, which we then do,
             * and then once the condition becomes false, we return the condition along with the return type of none denoting the
             * value we want to return.
             */
        } else if (stmt instanceof DoWhileNode) {
            DoWhileNode doWhileNode = (DoWhileNode) stmt;
            InterpreterDataType conditional = new InterpreterDataType();
            do {
                ReturnType listofStatements = InterpretListOfStatements(doWhileNode.getStatements().getStatements(), locals);
                if (listofStatements.getReturnTypes() == ReturnTypes.BREAK) {
                    break;
                }
                conditional = getIDT(doWhileNode.getCondition().get(), locals);
            } while (conditional.getValue().equals("true"));
            return new ReturnType(ReturnTypes.NONE, conditional.getValue());
            /**
             * If there was an initial conditional, we called process statement on it. Then, we collected the second conditional to
             * determine the while loop for the conditional. This while loop worked while it returned true and looped through all
             * the possible iterations using the thrid conditional. When we either got to a break or, if the while loop returned false
             * we broke out of the loop and either returned a break statement or just returned the normal return statement with the
             * new value.
             */
        } else if (stmt instanceof ForNode) {
            ForNode forNode = (ForNode) stmt;
            if (forNode.getFirstCondition() != null) {
                ProcessStatement(locals, (StatementNode) forNode.getFirstCondition().get());
            }
            InterpreterDataType SecondConditional = getIDT(forNode.getSecondCondition().get(), locals);
            while (SecondConditional.getValue().equals("true")) {
                ReturnType statementsFor = InterpretListOfStatements(forNode.getStatements().getStatements(), locals);
                if (statementsFor.getReturnTypes() == ReturnTypes.BREAK) {
                    break;
                }
                ProcessStatement(locals, (StatementNode) forNode.getThirdCondition().get());
                if (SecondConditional.getValue().equals("false")) {
                    return new ReturnType(ReturnTypes.NONE, SecondConditional.getValue());
                }
            }
            /**
             * Similar to the way the do-while loop has been constructed, where we are looking for the fist condition and checking if
             * it is a VariableReferenceNode and therefore, also considered an array. If it is an instance of VariableReferenceNode we
             * then look through the indexes of the array through a java for-each loop. We then put each of the looped in values into
             * the locals hashmap then we loop through the remaining statements in the specified for-each loop. Finally, we either break
             * when a statement gives us a break or we return the specific indexed value if we find a statement that asks to return.
             */
        } else if (stmt instanceof ForEachNode) {
            ForEachNode forEachNode = (ForEachNode) stmt;
            Optional<Node> conditional = forEachNode.getFirstCondition();
            if (conditional.isPresent()) {
                Node condition = conditional.get();
                String arrayName = String.valueOf(((VariableReferenceNode) condition).getName());
                if (condition instanceof VariableReferenceNode) {
                    InterpreterArrayDataType array = (InterpreterArrayDataType) locals.get(arrayName);
                    InterpreterDataType indexIDT = getIDT(((VariableReferenceNode) condition).getExpressionIndex().get(), locals);
                    for (String key : array.arrayData.keySet()) {
                        locals.put(indexIDT.getValue(), new InterpreterDataType(key));
                        ReturnType ListOfStatements = InterpretListOfStatements(forEachNode.getStatements().getStatements(), locals);
                        if (ListOfStatements.getReturnTypes() == ReturnTypes.BREAK) {
                            break;
                        } else if (ListOfStatements.getReturnTypes() == ReturnTypes.RETURN) {
                            return new ReturnType(ReturnTypes.NONE, indexIDT.getValue());
                        }
                    }
                }
            }
            /**
             * In this else-if, I am first looking to make sure it is of type IFNode then, I walk through the linked list, looking for a
             * node where the condition either is empty or if the statement returns true. When this is found in the first IFNode,
             * we interpret the list of statements in that particular IFNode. If the return type is not none, we return back up to
             * the caller. Since this is a linked list and the other types of if's are saved differently, I check through the
             * else block and the else if block to see if they exist and do something similar as I said before where I check if
             * it is fist not null and then if the return type is not none.
             */
        } else if (stmt instanceof IFNode) {
            IFNode ifNode = (IFNode) stmt;
            InterpreterDataType returnValue = getIDT(ifNode.getParseOperation().get(), locals);
            if (ifNode.getParseOperation().isEmpty() || returnValue.getValue().equals("true")) {
                ReturnType IFreturnType = InterpretListOfStatements(ifNode.getBlockNode().getStatements(), locals);
                if (IFreturnType.getReturnTypes() != ReturnTypes.NONE) {
                    return IFreturnType;
                } else if (ifNode.getElseIfNode() != null) {
                    IFNode elseifNode = ifNode.getElseIfNode();
                    ReturnType ElseIfReturnType = ProcessStatement(locals, elseifNode);
                    if (ElseIfReturnType.getReturnTypes() != ReturnTypes.NONE) {
                        return ElseIfReturnType;
                    } else if (ifNode.getElseNode() != null) {
                        IFNode elseNode = ifNode.getElseNode();
                        ReturnType elseReturnType = ProcessStatement(locals, elseNode);
                        if (elseReturnType.getReturnTypes() != ReturnTypes.NONE) {
                            return elseReturnType;
                        }
                    }
                }
            }
            /**
             * For this node, we are checking if the return statement has a condition associated with it. If it does, we return the
             * condition along with that or if it doesn't exist we return just the enum with the RETURN type.
             */
        } else if (stmt instanceof ReturnNode) {
            ReturnNode returnNode = (ReturnNode) stmt;
            if (returnNode.getCondition().isPresent()) {
                InterpreterDataType returnValue = getIDT(returnNode.getCondition().get(), locals);
                return new ReturnType(ReturnTypes.RETURN, returnValue.toString());
            } else {
                return new ReturnType(ReturnTypes.RETURN);
            }
            /**
             * Like the do-while loop except with the use of a while loop.
             */
        } else if (stmt instanceof WhileNode) {
            WhileNode whileNode = (WhileNode) stmt;
            while (true) {
                InterpreterDataType conditional = getIDT(whileNode.getCondition().get(), locals);
                if (conditional.getValue().equals("false")) {
                    return new ReturnType(ReturnTypes.NONE, conditional.getValue());
                }
                ReturnType listofStatements = InterpretListOfStatements(whileNode.getStatements().getStatements(), locals);
                if (listofStatements.getReturnTypes() == ReturnTypes.BREAK) {
                    break;
                }
            }
            /**
             * If none of the nodes we checked for is found, we use getIDT on the statement and the locals and just check if that
             * returns null or not.
             */
        } else {
            InterpreterDataType getIDT = getIDT(stmt, locals);
            if (getIDT == null) {
                throw new Exception("The node you were looking for does not exist!");
            } else {
                return new ReturnType(ReturnTypes.NONE, getIDT.toString());
            }
        }
        return null;
    }

    public ReturnType InterpretListOfStatements(LinkedList<StatementNode> statements, HashMap<String, InterpreterDataType> locals) throws Exception {
        for (StatementNode statementsLoop : statements) {
            ReturnType statementsReturned = ProcessStatement(locals, statementsLoop);
            if (statementsReturned.getReturnTypes() != ReturnTypes.NONE) {
                return statementsReturned;
            }
        }
        return new ReturnType(ReturnTypes.NONE);
    }

    private String runFunctionCall(FunctionCallNode functionCallNode, HashMap<String, InterpreterDataType> localVariables) throws Exception {
        if (localVariables.containsKey(functionDefinitionNodeHashMap)) {
            FunctionDefinitionNode functionDefinitionNode = new FunctionDefinitionNode();
            HashMap<String, InterpreterDataType> parameters = new HashMap<>();
            if (variadic == false) {
                if (functionDefinitionNode instanceof BuiltInFunctionDefinitionNode) {
                    return ((BuiltInFunctionDefinitionNode) functionDefinitionNode).execute(parameters);
                } else {
                    if (!functionDefinitionNode.getParameters().equals(functionCallNode.getParameters())) {
                        throw new RuntimeException("The number of parameters don't match!");
                    }
                    for (String parameterName : functionDefinitionNode.getParameters()) {
                        InterpreterDataType parameterValue = getIDT(functionCallNode, localVariables);
                        parameters.put(parameterName, parameterValue);
                    }
                    return String.valueOf(InterpretListOfStatements(functionDefinitionNode.getStatementNodes(), parameters));
                }
            } else {
                if (functionDefinitionNode instanceof BuiltInFunctionDefinitionNode) {
                    return ((BuiltInFunctionDefinitionNode) functionDefinitionNode).execute(parameters);
                } else {
                    if (!functionDefinitionNode.getParameters().equals(functionCallNode.getParameters())) {
                        throw new RuntimeException("The number of parameters don't match!");
                    }
                    LinkedList<String> parameterNamesList = functionDefinitionNode.getParameters();
                    for (int i = 0; i < parameterNamesList.size() - 1; i++) {
                        String parameterNames = parameterNamesList.get(i);
                        InterpreterDataType parameterValue = getIDT(functionCallNode, localVariables);
                        parameters.put(parameterNames, parameterValue);
                    }
                    InterpreterArrayDataType variadicArray = new InterpreterArrayDataType(parameterNamesList.get(parameterNamesList.size() - 1));
                    for (int i = parameters.size(); i < functionCallNode.getParameters().size(); i++) {
                        InterpreterDataType variadicArgument = getIDT(functionCallNode, localVariables);
                        variadicArray.getArrayData().put(String.valueOf(i - parameters.size()), variadicArgument);
                    }
                    parameters.put(parameterNamesList.get(parameterNamesList.size() - 1), variadicArray);
                }
                return String.valueOf(InterpretListOfStatements(functionDefinitionNode.getStatementNodes(), parameters));
            }
        }
        return null;
    }


    public void InterpretProgram(ProgramNode programNode) throws Exception {
        HashMap<String, InterpreterDataType> localVariables = new HashMap<>();
        for (BlockNode BEGINblockNode : programNode.getBegin()) {
            InterpretBlock(BEGINblockNode, localVariables);
        }
        while (lineHandler.SplitAndAssign()) {
            for (BlockNode OTHERblockNode : programNode.getOtherblockNodes()) {
                InterpretBlock(OTHERblockNode, localVariables);
            }
        }
        for (BlockNode ENDblockNode : programNode.getEnd()) {
            InterpretBlock(ENDblockNode, localVariables);
        }
    }

    public List<ReturnType> InterpretBlock(BlockNode blockNode, HashMap<String, InterpreterDataType> localVariables) throws Exception {
        List<ReturnType> returnTypeList = new ArrayList<>();
        if (blockNode!=null) {
            if (getIDT(blockNode.getConditional().get(), localVariables).equals("true") || getIDT(blockNode.getConditional().get(), localVariables).equals("1")) {
                for (StatementNode statementNode : blockNode.getStatements()) {
                    returnTypeList.add(ProcessStatement(localVariables, statementNode));
                }
                return returnTypeList;
            }
        } else {
            for (StatementNode statementNode : blockNode.getStatements()) {
                returnTypeList.add(ProcessStatement(localVariables, statementNode));
            }
            return returnTypeList;
        }
        return returnTypeList;
    }

    /*public boolean isVariadic() {
        return variadic;
    }*/
    public class LineHandler {
        List<String> lines;
        int currentLine;
        private int NR;
        private int FNR;
        private String fieldSeperator;

        public LineHandler(List<String> lines) {
            this.lines = lines;
            this.currentLine = 0;
            this.NR = 0;
            this.FNR = 0;
            this.fieldSeperator = " ";
        }

        /**
         * The SplitandAssign method works by splitting the current line using the specified field seperator and assigning these values to the neccassary global variables. The
         * method then the NF, the number of fields, and the FNR, the number of records in the file while also keeping track of the current line number and the total number of
         * lines processed.
         *
         * @return true or false.
         */
        public boolean SplitAndAssign() {
            if (currentLine >= lines.size()) {
                return false;
            }
            String currentLineIndex = lines.get(currentLine);
            String[] fields = currentLineIndex.split(fieldSeperator);
            for (int i = 0; i < fields.length + 1; i++) {
                if (i == 0) {
                    globalVariables.put("$0", new InterpreterDataType(currentLineIndex));

                } else {
                    String variableName = "$" + i;
                    globalVariables.put(variableName, new InterpreterDataType(fields[i - 1]));
                }
            }
            globalVariables.put("NF", new InterpreterDataType(String.valueOf(fields.length)));
            NR++;
            FNR++;
            currentLine++;
            return true;
        }
    }
}
/*
varReferenceOGVersion
else if(node instanceof VariableReferenceNode){
            VariableReferenceNode variableReferenceNode = (VariableReferenceNode) node;
            String variableName = variableReferenceNode.getName();
            Optional<Node> expressionIndex = variableReferenceNode.getExpressionIndex();
            if(expressionIndex.isPresent()){
                InterpreterDataType indexIDT = getIDT(expressionIndex.get(), localVariables);
                String index = indexIDT.toString();
                InterpreterDataType LocalVarCheck = localVariables.get(variableName);
                if(LocalVarCheck instanceof InterpreterArrayDataType){

                }
            }
            if (ifNode.getParseOperation().isEmpty() || returnValue.getValue().equals("true")) {
                ReturnType IFreturnType = InterpretListOfStatements(ifNode.getBlockNode().getStatements(), locals);
                if (IFreturnType.getReturnTypes() != ReturnTypes.NONE) {
                    return IFreturnType;
                }
            }
            if (ifNode.getElseIfNode() != null) {
                IFNode elseIfNode = ifNode.getElseIfNode();
                ReturnType ElseIfReturnType = ProcessStatement(locals, elseIfNode);
                if (ElseIfReturnType.getReturnTypes() != ReturnTypes.NONE) {
                    return ElseIfReturnType;
                }
            }
            if (ifNode.getElseNode() != null) {
                IFNode elseNode = ifNode.getElseNode();
                ReturnType elseReturnType = ProcessStatement(locals, elseNode);
                if (elseReturnType.getReturnTypes() != ReturnTypes.NONE) {
                    return elseReturnType;
                }
            }
 */