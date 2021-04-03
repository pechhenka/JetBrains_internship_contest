
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class task_1Test {
    private ByteArrayOutputStream sink;
    private PrintStream controlledOut;
    private ByteArrayInputStream controlledIn;
    private PrintStream defaultOut;
    private InputStream defaultIn;

    private final static String alph = "abcdefghijklmnopqrstuvwxyz";
    private final static String alphWithoutFirstFive = "fghijklmnopqrstuvwxyz";
    private final static String alphWithoutLastFive = "abcdefghijklmnopqrstu";


    @ParameterizedTest
    @MethodSource({"testAlphabetFirstFive", "testStandardAlphabet"})
    void testAlphabet(final String input, final String expected) {
        setControlledStreamsWithInput(input);
        try {
            task_1.main(new String[]{});
            controlledOut.flush();
            assertEquals(expected, sink.toString().trim());
        } finally {
            setStandardStreams();
        }
    }

    @ParameterizedTest
    @MethodSource("testImpossible")
    void testImpossible(final String input) {
        setControlledStreamsWithInput(input);
        try {
            task_1.main(new String[]{});
            controlledOut.flush();
            assertEquals("Impossible", sink.toString().trim(), "Error on input " + input);
        } finally {
            setStandardStreams();
        }
    }

    static Stream<Arguments> testStandardAlphabet() {
        return Stream.of(
                Arguments.of("1 a", alph),
                Arguments.of("3 x y z", alph),
                Arguments.of("2 b c", alph),
                Arguments.of("2 xy xyz", alph)
        );
    }

    static Stream<Arguments> testAlphabetFirstFive() {
        return Stream.of(
                Arguments.of("2 b a", "bacde" + alphWithoutFirstFive),
                Arguments.of("3 bc ca ed", "bcade" + alphWithoutFirstFive)
        );
    }

    static Stream<Arguments> testAlphabetLastFive() {
        return Stream.of(
                Arguments.of("1 a", alph),
                Arguments.of("3 x y z", alph),
                Arguments.of("2 b c", alph),
                Arguments.of("2 xy xyz", alph)
        );
    }

    static Stream<Arguments> testImpossible() {
        return Stream.of(
                Arguments.of("3 a b a"),
                Arguments.of("5 z y z y a"),
                Arguments.of("3 abcd e abc")
        );
    }

    private void setControlledStreamsWithInput(final String input) {
        sink = new ByteArrayOutputStream();
        controlledOut = new PrintStream(sink);
        controlledIn = new ByteArrayInputStream(input.getBytes());

        defaultOut = System.out;
        defaultIn = System.in;

        System.setOut(controlledOut);
        System.setIn(controlledIn);
    }

    private void setStandardStreams() {
        System.setOut(defaultOut);
        System.setIn(defaultIn);
    }
}