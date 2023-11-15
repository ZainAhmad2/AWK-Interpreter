package Assignment10;
import java.io.IOException;
import java.util.LinkedList;
import java.util.HashMap;
public class Lexer {
    private StringHandler stringhandler;
    private int LineNumber=1;
    private int CharacterPosition=0;
    private LinkedList<Token> tokens = new LinkedList<Token>();
    private HashMap<String, TokenType> keywordHash = new HashMap<String, TokenType>();
    private HashMap<String, TokenType> twoCharacterHash = new HashMap<String, TokenType>();
    private HashMap<String, TokenType> oneCharacterHash = new HashMap<String, TokenType>();
    /**
     * Constructor for the Lexer class that initializes the StringHandler
     * @param inputs
     * @throws IOException
     */
    public Lexer(String inputs) throws IOException {
        stringhandler = new StringHandler(inputs);
        KeyWordHashmap();
        TwoCharacterHashmap();
        OneCharacterHashmap();
    }
    public void KeyWordHashmap(){
        keywordHash.put("while", TokenType.WHILE);
        keywordHash.put("if", TokenType.IF);
        keywordHash.put("do", TokenType.DO);
        keywordHash.put("for", TokenType.FOR);
        keywordHash.put("break", TokenType.BREAK);
        keywordHash.put("continue", TokenType.CONTINUE);
        keywordHash.put("else", TokenType.ELSE);
        keywordHash.put("return", TokenType.RETURN);
        keywordHash.put("BEGIN", TokenType.BEGIN);
        keywordHash.put("END", TokenType.END);
        keywordHash.put("print", TokenType.PRINT);
        keywordHash.put("printf", TokenType.PRINTF);
        keywordHash.put("next", TokenType.NEXT);
        keywordHash.put("in", TokenType.IN);
        keywordHash.put("delete", TokenType.DELETE);
        keywordHash.put("getline", TokenType.GETLINE);
        keywordHash.put("exit", TokenType.EXIT);
        keywordHash.put("nextfile", TokenType.NEXTFILE);
        keywordHash.put("function", TokenType.FUNCTION);
    }
    public void TwoCharacterHashmap(){
        twoCharacterHash.put(">=", TokenType.GREATEREQUAL);
        twoCharacterHash.put("++", TokenType.DOUBLEPLUS);
        twoCharacterHash.put("--", TokenType.DOUBLEMINUS);
        twoCharacterHash.put("<=", TokenType.LESSEQUAL);
        twoCharacterHash.put("==", TokenType.DOUBLEEQUALS);
        twoCharacterHash.put("!=", TokenType.NOTEQUAL);
        twoCharacterHash.put("^=", TokenType.EXPONENTEQUAL);
        twoCharacterHash.put("%=", TokenType.PERCENTEQUALS);
        twoCharacterHash.put("*=", TokenType.MULTIPLYEQUALS);
        twoCharacterHash.put("/=", TokenType.DIVIDEEQUALS);
        twoCharacterHash.put("+=", TokenType.PLUSEQUALS);
        twoCharacterHash.put("-=", TokenType.MINUSEQUALS);
        twoCharacterHash.put("!~", TokenType.NOTMATCH);
        twoCharacterHash.put("&&", TokenType.DOUBLEAND);
        twoCharacterHash.put(">>", TokenType.APPENDS);
        twoCharacterHash.put("||", TokenType.OR);
    }
    public void OneCharacterHashmap(){
        oneCharacterHash.put("{",TokenType.OPENANGLEBRACKET);
        oneCharacterHash.put("}",TokenType.CLOSEDANGLEBRACKET);
        oneCharacterHash.put("[",TokenType.OPENBRACKET);
        oneCharacterHash.put("]",TokenType.CLOSEDBRACKET);
        oneCharacterHash.put("(",TokenType.OPENPARENTHESIS);
        oneCharacterHash.put(")",TokenType.CLOSEDPARENTHESIS);
        oneCharacterHash.put("$",TokenType.DOLLAR);
        oneCharacterHash.put("~",TokenType.MATCH);
        oneCharacterHash.put("=",TokenType.EQUALS);
        oneCharacterHash.put(">",TokenType.GREATERTHAN);
        oneCharacterHash.put("<",TokenType.LESSTHAN);
        oneCharacterHash.put("!",TokenType.NOT);
        oneCharacterHash.put("+",TokenType.PLUS);
        oneCharacterHash.put("^",TokenType.EXPONENT);
        oneCharacterHash.put("-",TokenType.MINUS);
        oneCharacterHash.put("?",TokenType.QUESTION);
        oneCharacterHash.put(":",TokenType.COLON);
        oneCharacterHash.put("*",TokenType.MULTIPLY);
        oneCharacterHash.put("/",TokenType.DIVIDE);
        oneCharacterHash.put("%",TokenType.MODULUS);
        //oneCharacterHash.put(";",TokenType.SEPERATOR);
        //oneCharacterHash.put("\n",TokenType.SEPERATOR);
        oneCharacterHash.put("|",TokenType.BAR);
        oneCharacterHash.put(",",TokenType.COMMA);
    }
    /**
     * Collects the inputs as the Peek() method from StringHandler goes through each letter. It checks for spaces, tabs, empty
     * lines(\n), carriage returns(\r), letters and finally, digits. If there is a character that is not one of those things or
     * if thereis a second decimal point where there shouldn't be, it will throw an IllegalArgumentException and break.
     * @return
     */
    public LinkedList<Token> Lex(){
        while(!stringhandler.IsDone()) {
            char currentCharacter = stringhandler.Peek(0);
            if (currentCharacter == ' ' || currentCharacter == '\t') {
                stringhandler.GetChar();
                CharacterPosition++;
            } else if (currentCharacter == '\n' || currentCharacter == ';') {
                stringhandler.GetChar();
                LineNumber++;
                //CharacterPosition=0;
                //CharacterPosition=1;
                CharacterPosition++;
                tokens.add(new Token(TokenType.SEPERATOR, LineNumber, CharacterPosition));
            } else if (currentCharacter == '\r') {
                stringhandler.GetChar();
                //CharacterPosition=0;
                CharacterPosition++;
            } else if (Character.isLetter(currentCharacter)) {
                Token wordProcessor = ProcessWord();
                tokens.add(wordProcessor);
            } else if (Character.isDigit(currentCharacter)) {
                Token numberProcessor = ProcessNumber();
                tokens.add(numberProcessor);
            } else if (currentCharacter == '#') {
                while (stringhandler.Peek(0) != '\n') {
                    stringhandler.GetChar();
                    CharacterPosition++;
                }
            } else if (currentCharacter == '"') {
                Token StringLiteral = HandleStringLiterals();
                tokens.add(StringLiteral);
            } else if (currentCharacter == '`') {
                Token patternHandler = HandlePattern();
                tokens.add(patternHandler);
            } else {
                Token OneTwoSymbols = ProcessSymbols();
                if (OneTwoSymbols != null) {
                    tokens.add(OneTwoSymbols);
                } else {
                    throw new IllegalArgumentException("UNRECOGNIZED CHARACTER: " + currentCharacter);
                }
            }
        }
        tokens.add(new Token(TokenType.SEPERATOR, LineNumber, CharacterPosition));
        return tokens;
    }
    /**
     * When a NUMBER is found in the above method, this method is called through that if statement where it checks for digits
     * and the possible use of a decimal. If a decimal is found, it sets the booolean value to true and lets it through the while
     * loop while increasing the position of the line by one. When the process is done, we calculate the length of the substring
     * and are able to get it to print properly by having the getSubstring helper method created in the StringHandler class
     * @return
     */
    private Token ProcessNumber() {
        int startPosition = CharacterPosition;
        boolean decimalFound = false;
        while(Character.isDigit(stringhandler.Peek(0)) || (!decimalFound && stringhandler.Peek(0) == '.')) {
            char currentCharacter  = stringhandler.GetChar();
            CharacterPosition++;
            if(currentCharacter == '.') {
                decimalFound = true;
            }
        }
        int length = CharacterPosition - startPosition;
        String value = stringhandler.getSubstring(startPosition, length);
        //String value = stringhandler.PeekString(CharacterPosition);
        return new Token(TokenType.NUMBER, value, LineNumber, startPosition);
    }
    /**
     * Similar to the previous method minus the inclusion of the decimalfound boolean. Also, when this method is called, we allow
     * for the use of digits when it is not the first character in the String. If it is found in the middle of the wrd, it
     * should still end up working and outputing the Token as a WORD
     * @return
     */
    private Token ProcessWord() {
        int startPosition = CharacterPosition;
        TokenType tokenType;
        while(Character.isLetterOrDigit(stringhandler.Peek(0)) || stringhandler.Peek(0) == '_') {
            stringhandler.GetChar();
            CharacterPosition++;
        }
        int length = CharacterPosition - startPosition;
        String value = stringhandler.getSubstring(startPosition, length);
        if(keywordHash.containsKey(value)) {
            tokenType = keywordHash.get(value);
            return new Token(tokenType, LineNumber, startPosition);
        }
        //String value = stringhandler.PeekString(CharacterPosition);
        return new Token(TokenType.WORD, value, LineNumber, startPosition);
    }
    /**
     *When a quotation mark is detected in the above if else-if block, the program calls this method to be able to put it into a
     * STRINGLITERAL token. The token is made by scanning through the entire input via each character, identifying the boolean escape
     * sequences until it reaches the end of the double quotes. Afterwards, when printing it out, we replace the escape \ with nothing
     * to be able to print it out in the correct format.
     * @return
     */
    private Token HandleStringLiterals(){
        int startPosition = CharacterPosition+1;
        boolean escaped = false;
        stringhandler.GetChar();
        CharacterPosition++;
        while(stringhandler.Peek(0)!= '"' || escaped) {
            char currentCharacter = stringhandler.GetChar();
            CharacterPosition++;
            if(escaped){
                escaped = false;
            }else if(currentCharacter == '\\'){
                escaped =true;
            }
        }
        stringhandler.GetChar();
        CharacterPosition++;
        int length = CharacterPosition - startPosition;
        String value = stringhandler.getSubstring(startPosition, length-1);
        //String escapeRemover = value.replace('\\', '\0');
        String escapeRemover = value.replace("\\", "");
        //String quoteRemover = escapeRemover.replace("\"", "");
        return new Token(TokenType.STRINGLITERAL, escapeRemover, LineNumber, startPosition);
    }
    /**
     * Similar to the previous method except, this works with the backtick symbol instead and does not require an escape character.
     * @return
     */
    private Token HandlePattern(){
        int startPosition = CharacterPosition;
        stringhandler.GetChar();
        CharacterPosition++;
        while(stringhandler.Peek(0)!= '`') {
            char currentCharacter = stringhandler.GetChar();
            CharacterPosition++;
        }
        stringhandler.GetChar();
        CharacterPosition++;
        int length = CharacterPosition - startPosition;
        String value = stringhandler.getSubstring(startPosition, length);
        String backtickRemover = value.replace("`", "");
        return new Token(TokenType.PATTERN, backtickRemover, LineNumber, startPosition);
    }
    /**
     * The first if statement works by looking at the next two string elements found through PeekString. By looking through the two symbol
     * hashmap, we can then determine if there is a token that already exists for it and we can return that value as that set token. As
     * always, the CharacterPosition is added to by the number of characters we looked at. This is a similar process to the one
     * character if statement except we are only using PeekString for the singular symbol.
     * @return new Token depending on one or two character symbol, null if not an allowed character
     */
    public Token ProcessSymbols(){
        String twoCharacterSymbols = stringhandler.PeekString(2);
        String oneCharacterSymbols = stringhandler.PeekString(1);
        if(twoCharacterHash.containsKey(twoCharacterSymbols)){
            stringhandler.GetChar();
            stringhandler.GetChar();
            CharacterPosition+=2;
            return new Token(twoCharacterHash.get(twoCharacterSymbols), LineNumber, CharacterPosition);
        }
        if(oneCharacterHash.containsKey(oneCharacterSymbols)){
            stringhandler.GetChar();
            CharacterPosition++;
            return new Token(oneCharacterHash.get(oneCharacterSymbols), LineNumber, CharacterPosition);
        }
        return null;
    }
}
