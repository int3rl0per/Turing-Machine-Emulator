import java.util.List;

public class Emulator {
    private static final int TAPE_SYMBOL_COUNT = 15;

    public static void main(String[] args) {
        List<Transition> transitions = TransitionHandler.getTransitions();
        // get tape content
        // choose step / run mode
        // emulation
    }

    private static void emulate(List<Transition> transitions, String tape, boolean stepOn) {
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
}
