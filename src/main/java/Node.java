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

    public Node(int value, int c) {
        this.value = value;
        this.color = c;
    }

    public Node(int value, Node parent, Node left, Node right) {
        this.value = value;
        this.parent = parent;
        this.left = left;
        this.right = right;
    }
    
    @Override
    public int findBrotherColor() {
        Node p = parent;
        int brotherColor = 0;

        // 내가 부모의 왼쪽 자식인 경우
        if (p.left.value == this.value) {
            // 형제가 없는 경우
            if (p.right == null) {
                brotherColor = 0;
            } else { // 있는 경우
                brotherColor = p.right.color;
            }

        }

        // 내가 부모의 오른쪽 자식인 경우
        if (p.right.value == this.value) {
            // 형제가 없는 경우
            if (p.left == null) {
                brotherColor = 0;
            } else { // 있는 경우
                brotherColor = p.left.color;
            }
        }
        return brotherColor;
    }
    
    @Override
    public int findSide() {
        Node p = parent;
        int side = 0;
        // root
        if (p == null) {
            side = 0;
        }

        // 내가 부모의 왼쪽 자식인 경우
        if (p.left.value == this.value) {
            side = -1;
        }

        // 내가 부모의 오른쪽 자식인 경우
        if (p.right.value == this.value) {
            side = 1;
        }
        return side;
    }
    
    @Override
    public int hasRedChild() {

        int redCnt = 0;

        if(this.left.color != RED && this.right.color != RED) {
            return 0;
        }

        if (this.left.color == RED) {
            if (this.right.color == RED) {
                redCnt = 2;
            } else {
                redCnt = -1;
            }
        }

        if (this.right.color == RED) {
            if (this.left.color == RED) {
                redCnt = 2;
            } else {
                redCnt = 1;
            }
        }
        return redCnt;
    }
}
