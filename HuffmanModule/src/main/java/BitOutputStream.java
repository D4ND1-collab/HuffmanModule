import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;


public final class BitOutputStream implements AutoCloseable {


    private OutputStream output;


    private int currentByte;


    private int numberOFBitsFilled;



    public BitOutputStream(OutputStream out) {
        output = Objects.requireNonNull(out);
        currentByte = 0;
        numberOFBitsFilled = 0;
    }



    public void write(int b) throws IOException {
        if (b != 0 && b != 1)
            throw new IllegalArgumentException("Argument must be 0 or 1");
        currentByte = (currentByte << 1) | b;
        numberOFBitsFilled++;
        if (numberOFBitsFilled == 8) {
            output.write(currentByte);
            currentByte = 0;
            numberOFBitsFilled = 0;
        }
    }


    public void close() throws IOException {
        while (numberOFBitsFilled != 0)
            write(0);
        output.close();
    }

}