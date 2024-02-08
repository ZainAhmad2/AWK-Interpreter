package Assignment10;
import java.util.HashMap;
public class InterpreterDataType {
    private String value;

    public InterpreterDataType(String value) {

        this.value = value;
    }
    public InterpreterDataType() {

        this.value = null;
    }
    public String getValue(){

        return value;
    }

    public void setValue(String value) {

        this.value = value;
    }

    public String toString(){

        return value;
    }

}
class InterpreterArrayDataType extends  InterpreterDataType{
    public HashMap<String, InterpreterDataType> arrayData;
    public InterpreterArrayDataType(String index) {

        arrayData = new HashMap<>();
        arrayData.put(index, new InterpreterDataType(String.valueOf(index)));
    }
    public HashMap<String, InterpreterDataType> getArrayData() {

        return arrayData;
    }
}
