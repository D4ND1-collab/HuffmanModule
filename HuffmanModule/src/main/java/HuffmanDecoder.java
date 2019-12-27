import java.io.IOException;
import java.util.Objects;


public final class HuffmanDecoder {


    private BitInputStream input;


    public CodeTree codeTree;


    public HuffmanDecoder(BitInputStream in) {
        input = Objects.requireNonNull(in);
    }


    public int read() throws IOException {
        if (codeTree == null)
            throw new NullPointerException("Code tree is null");

        ClassicNode currentNode = codeTree.root;
        while (true) {
            int temp = input.readNoEof();
            Node nextNode;
            if (temp == 0) nextNode = currentNode.leftChild;
            else if (temp == 1) nextNode = currentNode.rightChild;
            else throw new AssertionError("Invalid value from readNoEof()");

            if (nextNode instanceof LeafNode)
                return ((LeafNode) nextNode).symbol;
            else if (nextNode instanceof ClassicNode)
                currentNode = (ClassicNode) nextNode;
            else
                throw new AssertionError("Illegal node type");
        }
    }

}