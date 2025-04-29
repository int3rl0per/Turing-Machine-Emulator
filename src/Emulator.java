import java.util.*;

public class Emulator {
    public static final int TAPE_SYMBOL_COUNT = 15;
    public static final int TIMEOUT = 3_000;
    public static final boolean COUNT = true;
    public static final String TAPE_SYMBOLS = "[0|1|a-z]+";
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";

    public static void main(String[] args) {
        List<Transition> transitions = TransitionHandler.getTransitions();
        TransitionHandler.printTransitions(transitions);
        String tape = getTape();
        Boolean stepOn = setStepMode();
        emulate(transitions, tape, stepOn);
    }

    private static void emulate(List<Transition> transitions, String tape, boolean stepOn) {
        int currentState = 1;
        int position = 0;
        char currentTapeSymbol = tape.charAt(position);
        int counter = 0;
        Boolean endEmulation = false;
        Scanner sc = new Scanner(System.in);
        long startTime = System.currentTimeMillis();

        if (stepOn) {
            printInformation(currentState, tape, position, counter);
            sc.nextLine();
        }

        do {
            endEmulation = true;
            for (Transition transition : transitions) {
                if (currentState == transition.initialState() && currentTapeSymbol == transition.readTapeSymbol()) {
                    currentState = transition.nextState();
                    tape = replaceAtPosition(tape, position, transition.writeTapeSymbol());
                    if (position < 0) {
                        position++;
                    }
                    switch (transition.moveDirection()) {
                        case 'l' -> position -= 1;
                        case 'r' -> position += 1;
                    }
                    if (position < 0 || position >= tape.length()) {
                        currentTapeSymbol = '_';
                    } else {
                        currentTapeSymbol = tape.charAt(position);
                    }
                    counter++;
                    endEmulation = false;
                    break;
                }
            }
            if (stepOn && !endEmulation) {
                printInformation(currentState, tape, position, counter);
                sc.nextLine();
            }
            if (!stepOn && System.currentTimeMillis() - startTime > TIMEOUT) {
                endEmulation = true;
                System.out.println(RED + "Turing machine timed out - probably in an infinite loop" + RESET);
            }

        } while (!endEmulation);

        if (!stepOn) printInformation(currentState, tape, position, counter);
        if (COUNT) countUnary(tape);

        String status = (currentState == 2) ? "accepted" : "rejected";
        System.out.println("Word is " + status);
        System.out.print("*Emulation ended*");
    }

    private static String replaceAtPosition(String tape, int position, char symbol) {
        List<Character> tapeArray = new ArrayList<>();
        for (char c : tape.toCharArray()) {
            tapeArray.add(c);
        }

        if (position < 0) {
            tapeArray.addFirst(symbol);
        } else if (position >= tape.length()) {
            tapeArray.addLast(symbol);
        } else {
            tapeArray.set(position, symbol);
        }

        StringBuilder sb = new StringBuilder();
        for (char c : tapeArray) {
            sb.append(c);
        }

        return sb.toString();
    }

    private static void printInformation(int state, String tape, int position, int counter) {
        StringBuilder sb = new StringBuilder();

        // step counter
        sb.append("Step: " + counter);
        sb.append(System.lineSeparator());

        // current state & position
        sb.repeat(" ", TAPE_SYMBOL_COUNT);
        sb.append("▼ (q" + state + ")");
        sb.repeat(" ", TAPE_SYMBOL_COUNT);
        sb.append(System.lineSeparator());

        // current tape with 15 + 1 + 15 elements
        int firstSymbolPosition = position - TAPE_SYMBOL_COUNT;
        int lastSymbolPosition = position + TAPE_SYMBOL_COUNT;
        for (int i = firstSymbolPosition; i <= lastSymbolPosition; i++) {
            if (i < 0 || i >= tape.length()) {
                sb.append("_");
            } else {
                sb.append(tape.charAt(i));
            }
        }
        sb.append(System.lineSeparator());

        System.out.print(sb);
    }

    private static void countUnary(String tape) {
        char[] tapeArray = tape.toCharArray();
        int count = 0;
        for (char c : tapeArray) {
            if (c == '1') count++;
        }
        System.out.println("Count of '1' on tape: " + count);
    }

    private static boolean setStepMode() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Choose Mode - [S]tep-Mode / [R]un-Mode");
        System.out.print("Mode: ");
        while (!sc.hasNext("[SsRr]")) {
            System.out.println("Not valid - Please enter a valid character (S/s/R/r)");
            System.out.print("Mode: ");
            sc.next();
        }
        String input = sc.next();

        switch (input) {
            case "S", "s" -> {
                return true;
            }
            case "R", "r" -> {
                return false;
            }
        }
        return false;
    }

    private static String getTape() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Input for the Turing-Machine");
        System.out.print("Tape: ");
        while (!sc.hasNext(TAPE_SYMBOLS)) {
            System.out.println("Not valid - Please enter a valid Input");
            System.out.print("Tape: ");
            sc.nextLine();
        }
        return sc.nextLine().replaceAll(" ", "");
    }
}
