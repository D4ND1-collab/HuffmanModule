import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public final class CodeTree {


    public final ClassicNode root;


    private List<List<Integer>> codes;


    public CodeTree(ClassicNode root, int symbolLimit) {
        this.root = Objects.requireNonNull(root);
        if (symbolLimit < 2)
            throw new IllegalArgumentException("At least 2 symbols needed");

        codes = new ArrayList<List<Integer>>();
        for (int i = 0; i < symbolLimit; i++)
            codes.add(null);
        buildCodeList(root, new ArrayList<Integer>());
    }


    private void buildCodeList(Node node, List<Integer> prefix) {
        if (node instanceof ClassicNode) {
            ClassicNode internalNode = (ClassicNode) node;

            prefix.add(0);
            buildCodeList(internalNode.leftChild, prefix);
            prefix.remove(prefix.size() - 1);

            prefix.add(1);
            buildCodeList(internalNode.rightChild, prefix);
            prefix.remove(prefix.size() - 1);

        } else if (node instanceof LeafNode) {
            LeafNode leaf = (LeafNode) node;
            if (leaf.symbol >= codes.size())
                throw new IllegalArgumentException("Symbol exceeds symbol limit");
            if (codes.get(leaf.symbol) != null)
                throw new IllegalArgumentException("Symbol has more than one code");
            codes.set(leaf.symbol, new ArrayList<Integer>(prefix));

        } else {
            throw new AssertionError("Illegal node type");
        }
    }


    public List<Integer> getCode(int symbol) {
        if (symbol < 0)
            throw new IllegalArgumentException("Illegal symbol");
        else if (codes.get(symbol) == null)
            throw new IllegalArgumentException("No code for given symbol");
        else
            return codes.get(symbol);
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString("", root, sb);
        return sb.toString();
    }


    private static void toString(String prefix, Node node, StringBuilder sb) {
        if (node instanceof ClassicNode) {
            ClassicNode internalNode = (ClassicNode) node;
            toString(prefix + "0", internalNode.leftChild, sb);
            toString(prefix + "1", internalNode.rightChild, sb);
        } else if (node instanceof LeafNode) {
            sb.append(String.format("Code %s: Symbol %d%n", prefix, ((LeafNode) node).symbol));
        } else {
            throw new AssertionError("Illegal node type");
        }
    }

}