package Assignment10;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.util.*;

public class Assignment10JUnits {
    @Test
    public void Test1() throws Exception {
        String testString1 = "HARD TO CHOOSE ONE";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Assert.assertEquals(tokens.get(0).toString(), "WORD(HARD)");
        Assert.assertEquals(tokens.get(1).toString(), " WORD(TO)");
        Assert.assertEquals(tokens.get(2).toString(), " WORD(CHOOSE)");
        Assert.assertEquals(tokens.get(3).toString(), " WORD(ONE)");
    }
    @Test
    public void Test2() throws Exception {
        String testString2 = "55 ST1CK LIFESTYLE 2023";
        Lexer newLexer = new Lexer(testString2);
        LinkedList<Token> tokens = newLexer.Lex();
        Assert.assertEquals(tokens.get(0).toString(), "NUMBER(55)");
        Assert.assertEquals(tokens.get(1).toString(), " WORD(ST1CK)");
        Assert.assertEquals(tokens.get(2).toString(), " WORD(LIFESTYLE)");
        Assert.assertEquals(tokens.get(3).toString(), " NUMBER(2023)");
    }
    @Test
    public void Test3() throws Exception {
        String testString3 = "\n SPAC_ED OUT FUTURE SP1N";
        Lexer newLexer = new Lexer(testString3);
        LinkedList<Token> tokens = newLexer.Lex();
        Assert.assertEquals(tokens.get(0).toString(), " SEPERATOR");
        Assert.assertEquals(tokens.get(1).toString(), " WORD(SPAC_ED)");
        Assert.assertEquals(tokens.get(2).toString(), " WORD(OUT)");
        Assert.assertEquals(tokens.get(3).toString(), " WORD(FUTURE)");
        Assert.assertEquals(tokens.get(4).toString(), " WORD(SP1N)");
    }
    @Test
    public void Test4() throws Exception {
        String testString4 = "5.55 7.31 999.999";
        Lexer newLexer = new Lexer(testString4);
        LinkedList<Token> tokens = newLexer.Lex();
        Assert.assertEquals(tokens.get(0).toString(), "NUMBER(5.55)");
        Assert.assertEquals(tokens.get(1).toString(), " NUMBER(7.31)");
        Assert.assertEquals(tokens.get(2).toString(), " NUMBER(999.999)");
    }
    @Test(expected = IllegalArgumentException.class)
    public void Test5() throws Exception {
        String testString5 = "4..3";
        Lexer newLexer = new Lexer(testString5);
        LinkedList<Token> tokens = newLexer.Lex();
        String testString6 = "/43";
        Lexer newLexer2 = new Lexer(testString6);
        LinkedList<Token> tokens2 = newLexer2.Lex();
        String testString7 = "{4.3}";
        Lexer newLexer3 = new Lexer(testString7);
        LinkedList<Token> tokens3 = newLexer3.Lex();
    }
    @Test
    public void Test6() throws Exception {
        String testString1 = "$0 = tolower($0)";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Assert.assertEquals(tokens.get(0).toString(), " DOLLAR");
        Assert.assertEquals(tokens.get(1).toString(), " NUMBER(0)");
        Assert.assertEquals(tokens.get(2).toString(), " EQUALS");
        Assert.assertEquals(tokens.get(3).toString(), " WORD(tolower)");
        Assert.assertEquals(tokens.get(4).toString(), " OPENPARENTHESIS");
        Assert.assertEquals(tokens.get(5).toString(), " DOLLAR");
        Assert.assertEquals(tokens.get(6).toString(), " NUMBER(0)");
        Assert.assertEquals(tokens.get(7).toString(), " CLOSEDPARENTHESIS");
        //Assert.assertEquals(tokens.get(8).toString(), " SEPERATOR");
    }
    @Test
    public void Test7() throws Exception{
        String testString2 = "BEGIN {FS = \" \"} \n" +
                "{\n" +
                " total+=NF\n" +
                " }\n" +
                " END {\n" +
                " print total\n" +
                "}\n";
        Lexer newLexer = new Lexer(testString2);
        LinkedList<Token> tokens = newLexer.Lex();
        Assert.assertEquals(tokens.get(0).toString(), " BEGIN");
        Assert.assertEquals(tokens.get(1).toString(), " OPENANGLEBRACKET");
        Assert.assertEquals(tokens.get(2).toString(), " WORD(FS)");
        Assert.assertEquals(tokens.get(3).toString(), " EQUALS");
        Assert.assertEquals(tokens.get(4).toString(), " STRINGLITERAL( )");
        Assert.assertEquals(tokens.get(5).toString(), " CLOSEDANGLEBRACKET");
        Assert.assertEquals(tokens.get(6).toString(), " SEPERATOR");
        Assert.assertEquals(tokens.get(7).toString(), " OPENANGLEBRACKET");
        Assert.assertEquals(tokens.get(8).toString(), " SEPERATOR");
        Assert.assertEquals(tokens.get(9).toString(), " WORD(total)");
        Assert.assertEquals(tokens.get(10).toString(), " PLUSEQUALS");
        Assert.assertEquals(tokens.get(11).toString(), " WORD(NF)");
        Assert.assertEquals(tokens.get(12).toString(), " SEPERATOR");
        Assert.assertEquals(tokens.get(13).toString(), " CLOSEDANGLEBRACKET");
        Assert.assertEquals(tokens.get(14).toString(), " SEPERATOR");
        Assert.assertEquals(tokens.get(15).toString(), " END");
        Assert.assertEquals(tokens.get(16).toString(), " OPENANGLEBRACKET");
        Assert.assertEquals(tokens.get(17).toString(), " SEPERATOR");
        Assert.assertEquals(tokens.get(18).toString(), " PRINT");
        Assert.assertEquals(tokens.get(19).toString(), " WORD(total)");
        Assert.assertEquals(tokens.get(20).toString(), " SEPERATOR");
        Assert.assertEquals(tokens.get(21).toString(), " CLOSEDANGLEBRACKET");
        Assert.assertEquals(tokens.get(22).toString(), " SEPERATOR");
    }
    @Test
    public void Test8() throws Exception{
        String testString3 = "\"She said, \\\"Hello there\\\" and then she left.\"";
        Lexer newLexer = new Lexer(testString3);
        LinkedList<Token> tokens = newLexer.Lex();
        Assert.assertEquals(tokens.get(0).toString(), " STRINGLITERAL(She said, \"Hello there\" and then she left.)");
    }
    @Test
    public void Test9() throws Exception{
        String testString3 = "if while >= next +";
        Lexer newLexer = new Lexer(testString3);
        LinkedList<Token> tokens = newLexer.Lex();
        Assert.assertEquals(tokens.get(0).toString(), " IF");
        Assert.assertEquals(tokens.get(1).toString(), " WHILE");
        Assert.assertEquals(tokens.get(2).toString(), " GREATEREQUAL");
        Assert.assertEquals(tokens.get(3).toString(), " NEXT");
        Assert.assertEquals(tokens.get(4).toString(), " PLUS");
    }
    @Test
    public void Test10() throws Exception {
        String testString3 = "underated\n" + "testSequence\n" + "doneHere";
        Lexer newLexer = new Lexer(testString3);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        ProgramNode programNode = new ProgramNode();
        TokenHandler newTokenHandler = new TokenHandler(tokens);
        Optional<Token> peekedToken = newTokenHandler.Peek(0);

        Assert.assertTrue(newParser.AcceptSeperators());
    }
    @Test
    public void Test11() throws Exception {
        String testString3 = "underated test Sequence done Here\n";
        Lexer newLexer = new Lexer(testString3);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        ProgramNode programNode = new ProgramNode();
        TokenHandler newTokenHandler = new TokenHandler(tokens);
        Optional<Token> peekedToken = newTokenHandler.Peek(0);

        Assert.assertTrue(newParser.AcceptSeperators());
    }
    @Test
    public void Test12() throws Exception {
        String testString4 = "neccessary test";
        Lexer newLexer = new Lexer(testString4);
        LinkedList<Token> tokens = newLexer.Lex();
        TokenHandler newTokenHandler = new TokenHandler(tokens);

        Optional<Token> removedToken = newTokenHandler.MatchAndRemove(TokenType.WORD);
        Assert.assertTrue(removedToken.isPresent());
        Assert.assertEquals(TokenType.WORD, removedToken.get().getType());
        Assert.assertFalse(tokens.contains(removedToken.get()));
    }
    @Test
    public void Test13() throws Exception {
        String oldTestString = "$0 = tolower($0)";
        Lexer newLexer = new Lexer(oldTestString);
        LinkedList<Token> tokens = newLexer.Lex();
        TokenHandler newTokenHandler = new TokenHandler(tokens);

        Optional<Token> peekedToken = newTokenHandler.Peek(0);
        Assert.assertTrue(peekedToken.isPresent());
        Assert.assertEquals(TokenType.DOLLAR, peekedToken.get().getType());
    }
    @Test
    public void Test14() throws Exception {
        String testString1 = "++a";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseBottomLevel();

        Assert.assertEquals("Optional[a PREINC]", operationNode.toString());
    }
    @Test
    public void Test15() throws Exception {
        String testString1 = "++$b";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseBottomLevel();

        Assert.assertEquals("Optional[b DOLLAR PREINC]", operationNode.toString());
    }
    @Test
    public void Test16() throws Exception {
        String testString1 = "(++d)";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseBottomLevel();

        Assert.assertEquals("Optional[d PREINC]", operationNode.toString());
    }
    @Test
    public void Test17() throws Exception {
        String testString1 = "-5";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseBottomLevel();

        Assert.assertEquals("Optional[5 UNARYNEG]", operationNode.toString());
    }
    @Test
    public void Test18() throws Exception {
        String testString1 = "`[abc]`";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseBottomLevel();

        Assert.assertEquals("Optional[[abc]]", operationNode.toString());
    }
    @Test
    public void Test19() throws Exception {
        String testString1 = "e[++b]";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseBottomLevel();

        Assert.assertEquals("Optional[e[b PREINC]]", operationNode.toString());
    }
    @Test
    public void Test20() throws Exception {
        String testString1 = "$7";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseBottomLevel();

        Assert.assertEquals("Optional[7 DOLLAR]", operationNode.toString());
    }
    @Test
    public void Test21() throws Exception {
        String testString1 = "!!a";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseBottomLevel();

        Assert.assertEquals("Optional[a NOT NOT]", operationNode.toString());
    }
    @Test
    public void Test22() throws Exception {
        String testString1 = "++++a";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseBottomLevel();

        Assert.assertEquals("Optional[a PREINC PREINC]", operationNode.toString());
    }
    @Test
    public void Test23() throws Exception {
        String testString1 = "--++a";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseBottomLevel();

        Assert.assertEquals("Optional[a PREINC PREDEC]", operationNode.toString());
    }
    @Test
    public void Test24() throws Exception {
        String testString1 = "e[++a]";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseBottomLevel();

        Assert.assertEquals("Optional[e[a PREINC]]", operationNode.toString());
    }
    @Test
    public void Test25() throws Exception {
        String testString1 = "a++";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a POSTINC]", operationNode.toString());
    }

    @Test
    public void Test26() throws Exception {
        String testString1 = "a--";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a POSTDEC]", operationNode.toString());
    }

    @Test
    public void Test27() throws Exception {
        String testString1 = "a+b";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a ADD b]", operationNode.toString());
    }

    @Test
    public void Test28() throws Exception {
        String testString1 = "a-b";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a SUBTRACT b]", operationNode.toString());
    }

    @Test
    public void Test29() throws Exception {
        String testString1 = "a*b";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a MULTIPLY b]", operationNode.toString());
    }

    @Test
    public void Test30() throws Exception {
        String testString1 = "a/b";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a DIVIDE b]", operationNode.toString());
    }

    @Test
    public void Test31() throws Exception {
        String testString1 = "a%5";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a MODULO 5]", operationNode.toString());
    }

    @Test
    public void Test32() throws Exception {
        String testString1 = "word two";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[word CONCATENATION]", operationNode.toString());
    }

    @Test
    public void Test33() throws Exception {
        String testString1 = "a<";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a LT]", operationNode.toString());
    }

    @Test
    public void Test34() throws Exception {
        String testString1 = "a<=";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a LE]", operationNode.toString());
    }

    @Test
    public void Test35() throws Exception {
        String testString1 = "a!=";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a NE]", operationNode.toString());
    }

    @Test
    public void Test36() throws Exception {
        String testString1 = "a==";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a EQ]", operationNode.toString());
    }

    @Test
    public void Test37() throws Exception {
        String testString1 = "a>";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a GT]", operationNode.toString());
    }

    @Test
    public void Test38() throws Exception {
        String testString1 = "a>=";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a GE]", operationNode.toString());
    }

    @Test
    public void Test39() throws Exception {
        String testString1 = "a~";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a MATCH]", operationNode.toString());
    }

    @Test
    public void Test40() throws Exception {
        String testString1 = "a!~";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a NOTMATCH]", operationNode.toString());
    }

    @Test
    public void Test41() throws Exception {
        String testString1 = "a&&";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a AND]", operationNode.toString());
    }

    @Test
    public void Test42() throws Exception {
        String testString1 = "a||";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();
        Assert.assertEquals("Optional[a OR]", operationNode.toString());
    }

    @Test
    public void Test43() throws Exception {
        String testString1 = "a^=4";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a a EXPONENT 4]", operationNode.toString());
    }

    @Test
    public void Test44() throws Exception {
        String testString1 = "a%=5";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a a MODULO 5]", operationNode.toString());
    }

    @Test
    public void Test45() throws Exception {
        String testString1 = "a*=6";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a a MULTIPLY 6]", operationNode.toString());
    }

    @Test
    public void Test46() throws Exception {
        String testString1 = "a/=7";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a a DIVIDE 7]", operationNode.toString());
    }

    @Test
    public void Test47() throws Exception {
        String testString1 = "a+=8";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a a ADD 8]", operationNode.toString());
    }

    @Test
    public void Test48() throws Exception {
        String testString1 = "a-=9";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a a SUBTRACT 9]", operationNode.toString());
    }

    @Test
    public void Test49() throws Exception {
        String testString1 = "a=10";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a a EQ 10]", operationNode.toString());
    }

    @Test
    public void Test50() throws Exception {
        String testString1 = "condition ? truecase : falsecase";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[condition ? truecase : falsecase]", operationNode.toString());
    }

    @Test
    public void Test51() throws Exception {
        String testString1 = "a[x++]";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[a[x POSTINC] IN]", operationNode.toString());
    }

    @Test
    public void Test52() throws Exception {
        String testString1 = "5+3*4";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> operationNode = newParser.ParseOperation();

        Assert.assertEquals("Optional[5 ADD 3 MULTIPLY 4]", operationNode.toString());
    }
    @Test
    public void Test53() throws Exception {
        String testString1 = "continue";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<StatementNode> statementNode = newParser.ParseStatement();

        Assert.assertTrue(statementNode.isPresent());
        Assert.assertEquals("continue", statementNode.get().toString());
    }
    @Test
    public void Test54() throws Exception {
        String testString1 = "break";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<StatementNode> statementNode = newParser.ParseStatement();

        Assert.assertTrue(statementNode.isPresent());
        Assert.assertEquals("break", statementNode.get().toString());
    }
    @Test
    public void Test55() throws Exception {
        String testString1 = "for (i = 0; i < 10; ++i) { continue }";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<StatementNode> statementNode = newParser.ParseStatement();

        Assert.assertTrue(statementNode.isPresent());
        Assert.assertEquals("for(Optional[i i EQ 0]; Optional[i LT]; Optional[i PREINC]) {\n" + "[continue]}", statementNode.get().toString());
    }
    @Test
    public void Test56() throws Exception {
        String testString1 = "for(i: i++){ continue}";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<StatementNode> statementNode = newParser.ParseStatement();

        Assert.assertTrue(statementNode.isPresent());
        Assert.assertEquals("for(Optional[i] : Optional[i POSTINC]) {\n" + "[continue]}", statementNode.get().toString());
    }
    @Test
    public void Test57() throws Exception {
        String testString1 = "delete a[x++]";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<StatementNode> statementNode = newParser.ParseStatement();

        Assert.assertTrue(statementNode.isPresent());
        Assert.assertEquals("delete Optional[a[x POSTINC] IN]", statementNode.get().toString());
    }
    @Test
    public void Test58() throws Exception {
        String testString1 = "while (x < 10) { x++ }";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<StatementNode> statementNode = newParser.ParseStatement();

        Assert.assertTrue(statementNode.isPresent());
        Assert.assertEquals("while(Optional[x LT]) {\n" + "[{ [] } { [] }]}", statementNode.get().toString());
    }
    @Test
    public void Test59() throws Exception {
        String testString1 = "do{ continue }while(x>=4);";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<StatementNode> statementNode = newParser.ParseStatement();

        Assert.assertTrue(statementNode.isPresent());
        Assert.assertEquals("do{\n" + "[continue]} while(Optional[x GE]);", statementNode.get().toString());
    }
    @Test
    public void Test60() throws Exception {
        String testString1 = "return x++;";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<StatementNode> statementNode = newParser.ParseStatement();

        Assert.assertTrue(statementNode.isPresent());
        Assert.assertEquals("return Optional[x POSTINC];", statementNode.get().toString());
    }
    @Test
    public void Test61() throws Exception {
        String testString1 = "if(x>=4){ continue }";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<StatementNode> statementNode = newParser.ParseStatement();

        Assert.assertTrue(statementNode.isPresent());
        Assert.assertEquals("if(Optional[x GE]) {\n" + "[continue]}", statementNode.get().toString());
    }
    @Test
    public void Test62() throws Exception {
        String testString1 = "if(x>=4){ continue} else{ continue}";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<StatementNode> statementNode = newParser.ParseStatement();

        Assert.assertTrue(statementNode.isPresent());
        Assert.assertEquals("if(Optional[x GE]) {\n" + "[continue]}else {[continue]}", statementNode.get().toString());
    }
    @Test
    public void Test63() throws Exception {
        String testString1 = "if(x>=4){ continue} else if(x<2) {continue}";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<StatementNode> statementNode = newParser.ParseStatement();

        Assert.assertTrue(statementNode.isPresent());
        Assert.assertEquals("if(Optional[x GE]) {\n" + "[continue]}else if(Optional[x GE]) {\n" + "[continue]}", statementNode.get().toString());
    }
    @Test
    public void Test64() throws Exception {
        String testString1 = "functionName(one, two, three);";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        //ProgramNode programNode = new ProgramNode();
        Optional<Node> Node = newParser.ParseBottomLevel();

        Assert.assertEquals("functionName(one, two, three)", Node.get().toString());
    }
    @Test
    public void Test65() throws Exception {
        String testString1 = "x++";
        Lexer newLexer = new Lexer(testString1);
        LinkedList<Token> tokens = newLexer.Lex();
        Parser newParser = new Parser(tokens);
        Optional<Node> operationNode = newParser.ParseOperation();
        OperationNode convert = (OperationNode) operationNode.get();
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        hashMap.put("x", new InterpreterDataType(convert.toString()));
        InterpreterDataType getData = interpreter.getIDT(convert, hashMap);

        Assert.assertEquals("1.0", getData.toString());
    }
    @Test
    public void Test66() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode("3"),Optional.of(new ConstantNode("4")), OperationTypes.GE);
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        InterpreterDataType getData = interpreter.getIDT(node, hashMap);

        Assert.assertEquals("false", getData.toString());
    }
    @Test
    public void Test67() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode("4"),Optional.of(new ConstantNode("4")), OperationTypes.EQ);
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        InterpreterDataType getData = interpreter.getIDT(node, hashMap);

        Assert.assertEquals("true", getData.toString());
    }
    @Test
    public void Test68() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode("a"),Optional.of(new ConstantNode("b")), OperationTypes.LT);
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        InterpreterDataType getData = interpreter.getIDT(node, hashMap);

        Assert.assertEquals("true", getData.toString());
    }
    @Test
    public void Test69() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode("DOMINO"),Optional.of(new ConstantNode("THOUXANBANFAUNI")), OperationTypes.EQ);
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        InterpreterDataType getData = interpreter.getIDT(node, hashMap);

        Assert.assertEquals("false", getData.toString());
    }
    @Test
    public void Test70() throws Exception {
        PatternNode node = new PatternNode("`Pattern`");
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        //InterpreterDataType getData = interpreter.getIDT(node, hashMap);
        try{
            interpreter.getIDT(node, hashMap);
            Assert.fail();
        }catch(RuntimeException exception){
            Assert.assertEquals("You cannot try to pass a pattern to a function or assignment!", exception.getMessage());
        }
    }
    @Test
    public void Test71() throws Exception {
        AssignmentNode node = new AssignmentNode(new VariableReferenceNode("x"), new OperationNode(new VariableReferenceNode("x"), Optional.of(new ConstantNode("4")), OperationTypes.EXPONENT));
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        InterpreterDataType getData = interpreter.getIDT(node, hashMap);

        Assert.assertEquals("0", getData.toString());
    }
    @Test
    public void Test72() throws Exception {
        TernaryNode node = new TernaryNode(new ConstantNode("5"), new ConstantNode("4"), new ConstantNode("3"));
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        InterpreterDataType getData = interpreter.getIDT(node, hashMap);

        Assert.assertEquals("4", getData.toString());
    }
    @Test
    public void Test73() throws Exception {
        OperationNode node = new OperationNode(new VariableReferenceNode("a"), OperationTypes.AND);
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        InterpreterDataType getData = interpreter.getIDT(node, hashMap);

        Assert.assertEquals("false", getData.toString());
    }
    @Test
    public void Test74() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode("22"), OperationTypes.AND);
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        InterpreterDataType getData = interpreter.getIDT(node, hashMap);

        Assert.assertEquals("true", getData.toString());
    }
    @Test
    public void Test75() throws Exception {
        OperationNode node = new OperationNode(new VariableReferenceNode("a"), OperationTypes.NOTMATCH);
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        InterpreterDataType getData = interpreter.getIDT(node, hashMap);

        Assert.assertEquals("true", getData.toString());
    }
    @Test
    public void Test76() throws Exception {
        OperationNode node = new OperationNode(new VariableReferenceNode("a"), OperationTypes.MATCH);
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        InterpreterDataType getData = interpreter.getIDT(node, hashMap);

        Assert.assertEquals("false", getData.toString());
    }
    @Test
    public void Test77() throws Exception {
        OperationNode node = new OperationNode(new VariableReferenceNode("SUPER"), Optional.of(new VariableReferenceNode("CELL")), OperationTypes.CONCATENATION);
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        InterpreterDataType getData = interpreter.getIDT(node, hashMap);

        Assert.assertEquals("SUPERCELL", getData.toString());
    }
    @Test
    public void Test78() throws Exception {
        OperationNode node = new OperationNode(new VariableReferenceNode("x", Optional.of((new OperationNode(new VariableReferenceNode("x"), OperationTypes.POSTINC)))), OperationTypes.IN);
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        try{
            interpreter.getIDT(node, hashMap);
        }catch(Exception exception){
            Assert.assertEquals("Variable is not of type IADT!", exception.getMessage());
        }
    }
    @Test
    public void Test79() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode("1993"), OperationTypes.DOLLAR);
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        InterpreterDataType getData = interpreter.getIDT(node, hashMap);

        Assert.assertEquals("$1993", getData.toString());
    }
    @Test
    public void Test80() throws Exception{
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        StatementNode breakNode = new BreakNode();
        ReturnType breakResult = interpreter.ProcessStatement(hashMap, breakNode);
        Assert.assertEquals(ReturnTypes.BREAK, breakResult.getReturnTypes());
    }
    @Test
    public void Test81() throws Exception{
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();
        StatementNode continueNode = new ContinueNode();
        ReturnType continueResult = interpreter.ProcessStatement(hashMap, continueNode);
        Assert.assertEquals(ReturnTypes.CONTINUE, continueResult.getReturnTypes());
    }
    @Test
    public void Test82() throws Exception{
        ProgramNode programNode = new ProgramNode();
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> hashMap = new HashMap<>();

        VariableReferenceNode arrayName = new VariableReferenceNode("arr1");
        InterpreterDataType interpreterArrayDataType = new InterpreterArrayDataType(arrayName.toString());
        ((InterpreterArrayDataType) interpreterArrayDataType).arrayData.put("arr0", new InterpreterDataType("ValueAtIndex0"));
        ((InterpreterArrayDataType) interpreterArrayDataType).arrayData.put("arr1", new InterpreterDataType("ValueAtIndex1"));
        hashMap.put(String.valueOf(arrayName), interpreterArrayDataType);

        DeleteNode deleteNode = new DeleteNode(Optional.of(arrayName));

        ReturnType result = interpreter.ProcessStatement(hashMap, deleteNode);

        Assert.assertTrue(hashMap.containsKey(arrayName));
        InterpreterArrayDataType updatedArray = (InterpreterArrayDataType) hashMap.get(arrayName);
        Assert.assertNull(updatedArray.arrayData.get("0"));
        Assert.assertNotNull(updatedArray.arrayData.get("1"));
    }
}
