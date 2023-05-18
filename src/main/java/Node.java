public class Node implements iNode{
    int value;
    Node parent;
    Node left;
    Node right;
    int color;
    boolean hasExtraBlack;
    public Node() {

        // default value
        this.color = RED;
        this.hasExtraBlack = false;
    }
    
    public Node(int value) {
        
        // default value
        this.color = RED;
        this.hasExtraBlack = false;
        this.value = value;
    }
    
    @Override
    public int findBrotherColor() {
        if (parent == null)
            return 0;  // 형제 노드 없음

        Node brother = (this == parent.left) ? parent.right : parent.left;

        if (brother != null)
            return brother.color;

        return 0;  // 형제 노드 없음
    }
    
    @Override
    public int findSide() {
        if (parent == null)
            return 0;  // root node

        return (this == parent.left) ? -1 : 1;  // -1: 왼쪽 자식, 1: 오른쪽 자식
    }
    
    @Override
    public int hasRedChild() {
        int leftColor = (left != null) ? left.color : BLACK;
        int rightColor = (right != null) ? right.color : BLACK;

        if (leftColor == RED && rightColor == RED)
            return 2;  // 양쪽 자식이 모두 RED
        else if (leftColor == RED)
            return -1;  // 왼쪽 자식만 RED
        else if (rightColor == RED)
            return 1;  // 오른쪽 자식만 RED

        return 0;  // 양쪽 자식이 모두 RED가 아님
    }
}
