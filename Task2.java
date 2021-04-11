import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task2 {
    public boolean matches(String text, String regex) {
        return Pattern.compile(regex).matcher(text).matches();
    }


    public boolean matches2(String text, String regex) throws MatcherInterruptedException {
        return matches2(text, regex, 1000);
    }

    public boolean matches2(String text, String regex, final long millisTimeLimit) throws MatcherInterruptedException { // полностью эквивалентно трём строкам выше
        Pattern pattern = Pattern.compile(regex);
        final boolean[] res = new boolean[1];

        Runnable runnable = () -> {
            Matcher matcher = pattern.matcher(new InterruptibleCharSequence(text));
            res[0] = matcher.matches();
        };

        Thread thread = new Thread(runnable);
        thread.start();

        try {
            Thread.sleep(millisTimeLimit);
        } catch (InterruptedException e) {
            throw new MatcherInterruptedException(e);
        }

        try {
            thread.interrupt();
        } catch (MatcherLongTimeException e) {
            throw new MatcherInterruptedException(e);
        }

        return res[0];
    }
}

class MatcherLongTimeException extends RuntimeException {
    public MatcherLongTimeException(String message) {
        super(message);
    }
}

class MatcherInterruptedException extends Exception {
    public MatcherInterruptedException(Throwable cause) {
        super(cause);
    }
}

class InterruptibleCharSequence implements CharSequence {
    CharSequence inner;

    public InterruptibleCharSequence(CharSequence inner) {
        super();
        this.inner = inner;
    }

    @Override
    public char charAt(int index) {
        if (Thread.currentThread().isInterrupted()) {
            throw new MatcherLongTimeException("Time left");
        }
        return inner.charAt(index);
    }

    @Override
    public int length() {
        return inner.length();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new InterruptibleCharSequence(inner.subSequence(start, end));
    }

    @Override
    public String toString() {
        return inner.toString();
    }
}