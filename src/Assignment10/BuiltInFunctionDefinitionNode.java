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
// Failed attempts at getting my parameter to read HAHA
   /* public LinkedList<String> getParameters(LinkedList<String> parameters) {
        LinkedList<String> parameterList = new LinkedList<>();
        for (String parameterName : parametersMap.keySet()) {
            parameterList.add(parameterName);
        }
        return parameterList;
        public BuiltInFunctionDefinitionNode(HashMap<String, InterpreterDataType> parametersMap){
        this.parametersMap = parametersMap;
    }
    }
    //import java.util.Arrays;
    //import java.util.LinkedList;
    //import java.util.function.Function;
    //import java.util.regex.Matcher;
    //import java.util.regex.Pattern;*/