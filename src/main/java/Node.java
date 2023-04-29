public class Node implements iNode{
    int value;
    Node parent;
    Node left;
    Node right;
    int color;
    boolean hasExtraBlack;
    
    public Node() {
        
        // default value
        color = RED;
        hasExtraBlack = false;
    }
    
    @Override
    public int findBrotherColor() {
        return 0;
    }
    
    @Override
    public int findSide() {
        return 0;
    }
    
    @Override
    public int hasRedChild() {
        return 0;
    }
}
