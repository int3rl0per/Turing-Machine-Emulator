import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransitionHandler {
    private static final String TRANSITION_SYMBOLS = "[0|1]+";
    private static final String TRANSITION_DELIMITER = "11";
    private static final String TRANSITION_CODE = "0+10+10+10+10{1,2}";
    private static final String ELEMENT_DELIMITER = "1";

    public static List<Transition> getTransitions() {
        String[] transitions;
        do {
            // get TM-Code from User
            String TMCode = getTMCode();
            // split TM-Code into transitions
            transitions = TMCode.split(TRANSITION_DELIMITER);
            // remove potential leading 1 from first transition
            if (transitions[0].startsWith("1")) transitions[0] = transitions[0].substring(1);
        } while (!checkTransitions(transitions));

        // convert all strings into transition objects and add to list
        List<Transition> transitionList = new ArrayList<>();
        for (String transition : transitions) {
            transitionList.add(convertStringIntoTransition(transition));
        }
        return transitionList;
    }

    private static String getTMCode() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Turing-Machine Code");
        System.out.print("TM: ");
        while (!sc.hasNext(TRANSITION_SYMBOLS)) {
            System.out.println("Not valid - Please enter a valid Code");
            System.out.print("TM: ");
            sc.nextLine();
        }
        return sc.nextLine();
    }

    private static boolean checkTransitions(String[] transitions) {
        for (int i = 0; i < transitions.length; i++) {
            if (!transitions[i].matches(TRANSITION_CODE)) {
                System.out.println("ERROR");
                System.out.println("Transition " + (i+1) + " is invalid");
                return false;
            }
        }
        return true;
    }

    private static Transition convertStringIntoTransition(String transitionString) {
        String[] elements = transitionString.split(ELEMENT_DELIMITER);

        int initialState = elements[0].length();

        char readTapeSymbol = '0';
        switch (elements[1].length()) {
            case 1 -> readTapeSymbol = '0';
            case 2 -> readTapeSymbol = '1';
            case 3 -> readTapeSymbol = '_';
            default -> readTapeSymbol = (char) (elements[1].length() + 93);
        }

        int nextState = elements[2].length();

        char writeTapeSymbol = '0';
        switch (elements[3].length()) {
            case 1 -> writeTapeSymbol = '0';
            case 2 -> writeTapeSymbol = '1';
            case 3 -> writeTapeSymbol = '_';
            default -> writeTapeSymbol = (char) (elements[3].length() + 93);
        }

        char moveDirection = '0';
        switch (elements[4].length()) {
            case 1 -> moveDirection = 'l';
            case 2 -> moveDirection = 'r';
        }

        return new Transition(initialState, readTapeSymbol, nextState, writeTapeSymbol, moveDirection);
    }

    public static void printTransitions(List<Transition> transitions) {
        System.out.println("State | Read | Next State | Write | Move");
        StringBuilder sb = new StringBuilder();
        for (Transition transition : transitions) {
            sb.append("q" + transition.initialState());
            sb.repeat(" ", 5 - String.valueOf(transition.initialState()).length());
            sb.append("| ");

            sb.append(transition.readTapeSymbol() + "    | ");

            sb.append("q" + transition.initialState());
            sb.repeat(" ", 10 - String.valueOf(transition.initialState()).length());
            sb.append("| ");

            sb.append(transition.writeTapeSymbol() + "     | ");

            sb.append(Character.toUpperCase(transition.moveDirection()));

            System.out.println(sb);
            sb.setLength(0);
        }
    }
}
