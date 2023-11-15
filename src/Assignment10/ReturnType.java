package Assignment10;
enum ReturnTypes {
    BREAK, CONTINUE, RETURN, NONE
}
public class ReturnType {
    private ReturnTypes returnTypes;
    private String value;
    public ReturnType(ReturnTypes returnTypes, String value){
        this.returnTypes = returnTypes;
        this.value = value;
    }
    public ReturnType(ReturnTypes returnTypes){
        this.returnTypes = returnTypes;
        this.value = "";
    }
    public ReturnTypes getReturnTypes(){

        return returnTypes;
    }
    public String getValue() {

        return value;
    }
    public String toString(){

        return value + "(" + returnTypes + ")";
    }
}
