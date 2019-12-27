import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public final class CanonicalCode {


    private int[] codeLengths;


    public CanonicalCode(int[] codeLength) {

        Objects.requireNonNull(codeLength);
        if (codeLength.length < 2)
            throw new IllegalArgumentException("At least 2 symbols needed");
        for (int cl : codeLength) {
            if (cl < 0)
                throw new IllegalArgumentException("Illegal code length");
        }


        codeLengths = codeLength.clone();
        Arrays.sort(codeLengths);
        int currentLevel = codeLengths[codeLengths.length - 1];
        int numberOfNodesAtCurrentLevel = 0;
        for (int i = codeLengths.length - 1; i >= 0 && codeLengths[i] > 0; i--) {
            int cl = codeLengths[i];
            while (cl < currentLevel) {
                if (numberOfNodesAtCurrentLevel % 2 != 0)
                    throw new IllegalArgumentException("Under-full Huffman code tree");
                numberOfNodesAtCurrentLevel /= 2;
                currentLevel--;
            }
            numberOfNodesAtCurrentLevel++;
        }
        while (currentLevel > 0) {
            if (numberOfNodesAtCurrentLevel % 2 != 0)
                throw new IllegalArgumentException("Under-full Huffman code tree");
            numberOfNodesAtCurrentLevel /= 2;
            currentLevel--;
        }
        if (numberOfNodesAtCurrentLevel < 1)
            throw new IllegalArgumentException("Under-full Huffman code tree");
        if (numberOfNodesAtCurrentLevel > 1)
            throw new IllegalArgumentException("Over-full Huffman code tree");

        System.arraycopy(codeLength, 0, codeLengths, 0, codeLength.length);
    }


    public CanonicalCode(CodeTree tree, int symbolLimit) {
        Objects.requireNonNull(tree);
        if (symbolLimit < 2)
            throw new IllegalArgumentException("At least 2 symbols needed");
        codeLengths = new int[symbolLimit];
        buildCodeLengths(tree.root, 0);
    }


    private void buildCodeLengths(Node node, int depth) {
        if (node instanceof ClassicNode) {
            ClassicNode internalNode = (ClassicNode) node;
            buildCodeLengths(internalNode.leftChild, depth + 1);
            buildCodeLengths(internalNode.rightChild, depth + 1);
        } else if (node instanceof LeafNode) {
            int symbol = ((LeafNode) node).symbol;
            if (symbol >= codeLengths.length)
                throw new IllegalArgumentException("Symbol exceeds symbol limit");

            if (codeLengths[symbol] != 0)
                throw new AssertionError("Symbol has more than one code");
            codeLengths[symbol] = depth;
        } else {
            throw new AssertionError("Illegal node type");
        }
    }


    public int getSymbolLimit() {
        return codeLengths.length;
    }


    public int getCodeLength(int symbol) {
        if (symbol < 0 || symbol >= codeLengths.length)
            throw new IllegalArgumentException("Symbol out of range");
        return codeLengths[symbol];
    }


    public CodeTree toCodeTree() {
        List<Node> nodes = new ArrayList<Node>();
        for (int i = max(codeLengths); i >= 0; i--) {
            if (nodes.size() % 2 != 0)
                throw new AssertionError("Violation of canonical code invariants");
            List<Node> newNodes = new ArrayList<Node>();

            if (i > 0) {
                for (int j = 0; j < codeLengths.length; j++) {
                    if (codeLengths[j] == i)
                        newNodes.add(new LeafNode(j));
                }
            }

            for (int j = 0; j < nodes.size(); j += 2)
                newNodes.add(new ClassicNode(nodes.get(j), nodes.get(j + 1)));
            nodes = newNodes;
        }

        if (nodes.size() != 1)
            throw new AssertionError("Violation of canonical code invariants");
        return new CodeTree((ClassicNode) nodes.get(0), codeLengths.length);
    }


    private static int max(int[] array) {
        int result = array[0];
        for (int x : array)
            result = Math.max(x, result);
        return result;
    }

}