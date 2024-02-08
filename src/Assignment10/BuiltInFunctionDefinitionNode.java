package Assignment10;

import java.util.function.Function;
import java.util.HashMap;

public class BuiltInFunctionDefinitionNode extends FunctionDefinitionNode {
    private boolean variadic;
    public Function<HashMap<String, InterpreterDataType>, String> execute;
    private Interpreter.LineHandler lineHandler;
    private String functionName;

    public BuiltInFunctionDefinitionNode(String functionName, boolean variadic, Function<HashMap<String, InterpreterDataType>, String> execute) {
        this.functionName = functionName;
        this.execute = execute;
        this.variadic = variadic;
    }

    public String execute(HashMap<String, InterpreterDataType> parameters) {

        return execute.apply(parameters);
    }
}
