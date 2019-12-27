import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


public final class BitInputStream implements AutoCloseable {


    private InputStream input;


    private int numberOfBitsRemaining;

    private int currentByte;


    public BitInputStream(InputStream in) {
        input = Objects.requireNonNull(in);
        currentByte = 0;
        numberOfBitsRemaining = 0;
    }


    public int read() throws IOException {
        if (currentByte == -1)
            return -1;
        if (numberOfBitsRemaining == 0) {
            currentByte = input.read();
            if (currentByte == -1)
                return -1;
            numberOfBitsRemaining = 8;
        }
        if (numberOfBitsRemaining <= 0)
            throw new AssertionError();
        numberOfBitsRemaining--;
        return (currentByte >>> numberOfBitsRemaining) & 1;
    }


    public int readNoEof() throws IOException {
        int result = read();
        if (result != -1)
            return result;
        else
            throw new EOFException();
    }


    public void close() throws IOException {
        input.close();
        currentByte = -1;
        numberOfBitsRemaining = 0;
    }

}