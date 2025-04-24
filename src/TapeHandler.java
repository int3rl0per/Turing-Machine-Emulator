import java.util.Scanner;

public class TapeHandler {
    private static final String TAPE_SYMBOLS = "[0|1]+";

    public static String getTape() {
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
