import java.util.Scanner;

public class Emulator {
    private static final String TRANSITION_SYMBOLS = "[0|1]+";
    private static final String TRANSITION_DELIMITER = "11";
    private static final String TRANSITION_CODE = "0+10+10+10+10+";

    public static void main(String[] args) {
        // get TM-Code from User
        String TMCode = getTMCode();
        // split TM-Code into transitions
        String[] transitions = TMCode.split(TRANSITION_DELIMITER);
        // remove potential leading 1 from first transition
        if (transitions[0].startsWith("1")) transitions[0] = transitions[0].substring(1);
        // check validity of transitions
        if (!checkTransitions(transitions)) return;
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
                System.out.print("Transition " + (i+1) + " is invalid");
                return false;
            }
        }
        return true;
    }
}
