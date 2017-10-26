import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class Main {

    private static Stack<String> inputStack = new Stack <>();
    private static Stack<String> terminalNonTerminalStack = new Stack <>();
    private static List<String> nonTerminalPositionKeeper = new ArrayList <>();
    private static List<String> terminalPositionKeeper = new ArrayList <>();
    private static String inputString;
    private static String[][] parserTable;
    private static BufferedReader bufferedReader;

    static {
        parserTable = new String[3][4];
    }

    public static void main(String[] args) {
        initTable();
        initInputStack();
        checkingStack();
        predictiveParser();
    }

    public static void predictiveParser(){
        int x =1;
        while (!Objects.equals(inputStack.lastElement(), "$") && !terminalNonTerminalStack.isEmpty()){

            System.out.println("***** Input String *****\n"+ inputString +"\n***** PASS "+x+" *****");
            String inputSymbol = inputStack.lastElement();
            String checkingSymbol = terminalNonTerminalStack.lastElement();

            if (terminalPositionKeeper.contains(checkingSymbol)){
                if (Objects.equals(inputSymbol, checkingSymbol)){
                    System.out.println(inputSymbol + " and " + checkingSymbol + " Matched");
                    inputStack.pop();
                    terminalNonTerminalStack.pop();
                }
                else {
                    System.out.println("Error occurred in first if");
                    System.exit(-1);
                }
            }
            else {
                int row = nonTerminalPositionKeeper.indexOf(checkingSymbol);
                int col = terminalPositionKeeper.indexOf(inputSymbol);

                if (!parserTable[row][col].isEmpty()){

                    String lastPopped = terminalNonTerminalStack.pop();
                    String newFromTable = parserTable[row][col];
                    String reverse = new StringBuilder(newFromTable).reverse().toString();
                    char[] tableChar = reverse.toCharArray();

                    for (int i = 0; i < tableChar.length; i++) {
                        terminalNonTerminalStack.push(String.valueOf(tableChar[i]));
                    }
                    if (Objects.equals(terminalNonTerminalStack.lastElement(), "e")){
                        System.out.println("epsilon Detected, popping " + terminalNonTerminalStack.pop());
                    }
                    System.out.println("Output: " + lastPopped + "->" +newFromTable);
                }
                else {
                    System.out.println("Error occurred in second if");
                    System.exit(-1);
                }
            }
            x++;
            System.out.println();

        }
        if (Objects.equals(inputStack.lastElement(), "$")){
            System.out.println("\nEnd of input $ reached\nSuccessfully parsing completed \nParser will now exit");
            System.exit(-1);
        }
        else {
            System.out.println("\nParser could not resolve the string\nTop of the input stack is "
                    +inputStack.lastElement());
            System.exit(-1);
        }
    }

    public static void initInputStack(){

        String revInputString = new StringBuilder(inputString).reverse().toString();
        char[] inputChar = revInputString.toCharArray();
        inputStack.push("$");
        for (int i = 0; i < inputChar.length; i++) {
            inputStack.push(String.valueOf(inputChar[i]));
        }

    }

    public static void checkingStack(){
        String input;
        String[] splitInput;
        try {
            bufferedReader = new BufferedReader(new FileReader("symbols.txt"));

            input = bufferedReader.readLine();
            splitInput = input.split("\\|");
            for (int i = 0; i < splitInput.length; i++) {
                nonTerminalPositionKeeper.add(splitInput[i]);
            }
            terminalNonTerminalStack.push(splitInput[0]);

            input = bufferedReader.readLine();
            splitInput = input.split("\\|");
            for (int i = 0; i < splitInput.length; i++) {
                terminalPositionKeeper.add(splitInput[i]);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initTable(){
        int i = 0;
        try {
            bufferedReader = new BufferedReader(new FileReader("inputs.txt"));
            String input = bufferedReader.readLine();
            inputString = input.substring(12);

            input = bufferedReader.readLine();
            while (!(input==null)){
                String[] splitInput = input.split("\\|");
                for (int j = 0; j < 4; j++) {
                    parserTable[i][j] = splitInput[j].trim();
                }
                input = bufferedReader.readLine();
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
