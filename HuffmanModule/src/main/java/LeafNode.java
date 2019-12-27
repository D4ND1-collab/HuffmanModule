public final class LeafNode extends Node {

    public final int symbol;


    public LeafNode(int sym) {
        if (sym < 0)
            throw new IllegalArgumentException("Symbol value must be non-negative");
        symbol = sym;
    }

}