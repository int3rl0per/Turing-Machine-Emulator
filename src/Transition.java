public record Transition(
    int initialState,
    char readTapeSymbol,
    int nextState,
    char writeTapeSymbol,
    char moveDirection) {}