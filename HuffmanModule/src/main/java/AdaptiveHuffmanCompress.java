import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


public final class AdaptiveHuffmanCompress {


    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println("Usage: java AdaptiveHuffmanCompress InputFile OutputFile");
            System.exit(1);
            return;
        }
        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);


        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile))) {
            try (BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                compress(in, out);
            }
        }
    }


    static void compress(InputStream in, BitOutputStream out) throws IOException {
        int[] initFreqs = new int[257];
        Arrays.fill(initFreqs, 1);

        FrequencyTable freqs = new FrequencyTable(initFreqs);
        HuffmanEncoder enc = new HuffmanEncoder(out);
        enc.codeTree = freqs.buildCodeTree();
        int count = 0;
        while (true) {

            int symbol = in.read();
            if (symbol == -1)
                break;
            enc.write(symbol);
            count++;


            freqs.increment(symbol);
            if (count < 262144 && isPowerOf2(count) || count % 262144 == 0)
                enc.codeTree = freqs.buildCodeTree();
            if (count % 262144 == 0)
                freqs = new FrequencyTable(initFreqs);
        }
        enc.write(256);  // EOF
    }


    private static boolean isPowerOf2(int x) {
        return x > 0 && Integer.bitCount(x) == 1;
    }

}