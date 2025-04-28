import java.util.List;
import java.util.Scanner;

public class Emulator {
    private static final int TAPE_SYMBOL_COUNT = 15;

    public static void main(String[] args) {
        List<Transition> transitions = TransitionHandler.getTransitions();
        TransitionHandler.printTransitions(transitions);
        String tape = TapeHandler.getTape();
        Boolean stepOn = setStepMode();
        emulate(transitions, tape, stepOn);
    }

    private static void emulate(List<Transition> transitions, String tape, boolean stepOn) {
        int currentState = 1;
        char currentTapeSymbol = tape.charAt(0);
        int position = 0;
        int counter = 0;
        Boolean endEmulation = false;
        Scanner sc = new Scanner(System.in);

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
                    switch (transition.moveDirection()) {
                        case 'l' -> position -= 1;
                        case 'r' -> position += 1;
                    }
                    counter++;
                    endEmulation = false;
                    break;
                }
            }
            if (stepOn || endEmulation) {
                printInformation(currentState, tape, position, counter);
                sc.nextLine();
            }
        } while (!endEmulation);

        String status = (currentState == 2) ? "accepted" : "rejected";
        System.out.println("Word is " + status);
        System.out.print("*Emulation ended*");
    }

    private static String replaceAtPosition(String tape, int position, char symbol) {
        char[] tapeArray = tape.toCharArray();
        tapeArray[position] = symbol;
        return new String(tapeArray);
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
}
