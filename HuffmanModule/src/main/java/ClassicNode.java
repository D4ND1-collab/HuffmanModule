import java.util.Objects;



public final class ClassicNode extends Node {

    public final Node leftChild;  // Not null

    public final Node rightChild;  // Not null



    public ClassicNode(Node left, Node right) {
        leftChild = Objects.requireNonNull(left);
        rightChild = Objects.requireNonNull(right);
    }

}