public class Node implements iNode{
    static final int LEFT = -1;
    static final int RIGHT = 1;
    static final int EMPTY = 0;
    static final int ROOT = 0;
    static final int BOTH = 2;

    static final Node NIL = new Node();

    int value;
    Node parent;
    Node left;
    Node right;
    int color;
    boolean hasExtraBlack;
    
    public Node(int value) {
        this.value = value;
        parent = null;
        left = NIL;
        right = NIL;

        // default value
        color = RED;
        hasExtraBlack = false;
    }

    // NIL Node 를 위한 생성자
    public Node() {
        parent = null;
        left = null;
        right = null;
        // default value
        this.color = BLACK;
        hasExtraBlack = false;
    }

    @Override
    public int findSide() {
        if (parent == null) {
            return 0;
        } else {
            return parent.left == this ? LEFT : RIGHT;
        }
    }

    public boolean isBrotherBlack() {
        return findBrotherColor() == BLACK;
    }

    @Override
    public int findBrotherColor() {
        int side = findSide();
        Node brother = null;
        switch(side) {
            case ROOT:
                return EMPTY;

            case LEFT:
                brother = parent.right;
                break;

            case RIGHT:
                brother = parent.left;
        }

        if (brother == null) {
            return EMPTY;
        }
        return brother.color;
    }

    @Override
    public int hasRedChild() {
        int childInfo = findChildInfo();
        switch (childInfo) {
            case EMPTY:
                return EMPTY;
            case LEFT:
                return left.color == RED ? LEFT : EMPTY;
            case RIGHT:
                return right.color == RED ? RIGHT : EMPTY;
            case BOTH:
                boolean isLeftRed = left.color == RED;
                boolean isRightRed = right.color == RED;
                if (isLeftRed && isRightRed) {
                    return BOTH;
                } else if (!isLeftRed && !isRightRed) {
                    return EMPTY;
                } else if (isLeftRed) {
                    return LEFT;
                } else {
                    return RIGHT;
                }
        }
        throw new IllegalArgumentException("유효하지 않은 노드입니다.");
    }

    public int findChildInfo() {
        if (left==null && right==null) {
            return EMPTY;
        }

        if (left != null && right != null) {
            return BOTH;
        }

        if (left != null) {
            return RIGHT;
        }
        return LEFT;
    }

    public boolean isNilNode() {
        return this == NIL;
    }
}
