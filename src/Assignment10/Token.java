package Assignment10;
enum TokenType {
    WORD, NUMBER, WHILE, IF, DO, FOR, BREAK, CONTINUE, ELSE, RETURN, BEGIN, END, PRINT, PRINTF, NEXT, IN, DELETE, GETLINE, EXIT,
    NEXTFILE, FUNCTION, STRINGLITERAL, PATTERN, GREATEREQUAL, DOUBLEPLUS, DOUBLEMINUS, LESSEQUAL, DOUBLEEQUALS, NOTEQUAL,
    EXPONENTEQUAL, PERCENTEQUALS, MULTIPLYEQUALS, DIVIDEEQUALS, PLUSEQUALS, MINUSEQUALS, NOTMATCH, DOUBLEAND, APPENDS, OR,
    OPENANGLEBRACKET, CLOSEDANGLEBRACKET, OPENBRACKET, CLOSEDBRACKET, OPENPARENTHESIS, CLOSEDPARENTHESIS, DOLLAR, MATCH, EQUALS,
    GREATERTHAN, SEPERATOR, LESSTHAN, NOT, PLUS, MINUS, EXPONENT, QUESTION, COLON, MULTIPLY, DIVIDE, MODULUS, BAR, COMMA
}
public class Token {
    private TokenType type;
    private String value;
    private int LineNumber;
    private int CharacterPosition;

    /**
     * First constructor that does not hold a volue param, this makes it possible to create a SEPERATOR token as it does
     * not hold any sort of value.
     * @param type
     * @param LineNumber
     * @param CharacterPosition
     */
    public Token(TokenType type, int LineNumber, int CharacterPosition) {
        this.type = type;
        this.LineNumber = LineNumber;
        this.CharacterPosition = CharacterPosition;
    }
    /**
     * Value token made for the other two types of tokens, WORD and NUMBER
     * @param type
     * @param value
     * @param LineNumber
     * @param CharacterPosition
     */
    public Token(TokenType type, String value, int LineNumber, int CharacterPosition) {
        this.type = type;
        this.LineNumber = LineNumber;
        this.CharacterPosition = CharacterPosition;
        this.value = value;
    }
    public String getValue(){
        return value;
    }
    public TokenType getType(){
        return type;
    }
    public String toString() {
        if(value!=null) {
            if(CharacterPosition<=0) {
                return type + "(" + value + ")";
            }else{
                return " "+ type + "(" + value + ")";
            }
        }else {
            if(LineNumber<2) {
                return " " + type.toString();
            }else{
                return " " + type.toString();
            }
        }
    }
}
