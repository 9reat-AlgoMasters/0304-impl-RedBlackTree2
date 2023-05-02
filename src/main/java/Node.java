import java.util.NoSuchElementException;

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
    
    public static Node getDoublyBlackNilNode(Node parent) {
        Node doublyBlackNilNode = new Node();
        doublyBlackNilNode.setExtraBlack();
        doublyBlackNilNode.parent = parent;
        return doublyBlackNilNode;
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

    public Node findBrother() {
        int side = findSide();
        Node brother = null;
        switch(side) {
            case ROOT:
                throw new IllegalStateException("[Node][findBrother] 부모 노드가 없습니다.");

            case LEFT:
                brother = parent.right;
                break;

            case RIGHT:
                brother = parent.left;
        }

        if (brother == null) {
            throw new NoSuchElementException("[Node][findBrother] 형제 노드가 존재하지 않습니다.");
        }

        return brother;
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

    public void giveBlackToChild() {
        if (isNilNode()) {
            throw new IllegalArgumentException("NIL 노드는 자식이 없어서 BLACK을 줄 수 없습니다.");
        }

        if (isRed()) {
            throw new IllegalStateException("해당 노드가 RED입니다. BLACK을 자식에게 줄 수 없습니다.");
        }

        if (hasExtraBlack) {
            throw new IllegalArgumentException("Doubly Black에서 자식에게로 BLACK을 줄 수 없습니다.");
        }

        color = RED;
        int[] childSide = {LEFT, RIGHT};
        for (int side : childSide) {
            Node child = side == LEFT ? left : right;
            if (child == NIL) {
                addDoublyBlackChild(side);
            } else {
                child.addBlack();
            }
        }
    }

    /**
     * 모든 자식으로부터 BLACK을 가져옵니다.
     * @return 현재 노드가 DoublyBlack이 되었는지 여부를 boolean 으로 반환
     */
    public boolean takeBlackFromChild() {
        if (isNilNode()) {
            throw new IllegalArgumentException("NIL 노드는 자식이 없어서 BLACK을 가져올 수 없습니다.");
        }

        if (hasExtraBlack) {
            throw new IllegalArgumentException("Doubly Black에서 자식으로부터 BLACK을 가져올 수 없습니다.");
        }

        int[] childSide = {LEFT, RIGHT};
        // 자식으로 부터 BLACK을 뺍니다.
        for (int side : childSide) {
            Node child = side == LEFT ? left : right;
            if (child.isRed()) {
                throw new IllegalStateException(String.format("%s 자식이 RED입니다. BLACK을 가져올 수 없습니다.", side==LEFT ? "왼쪽" : "오른쪽"));
            }
            child.subBlack();
        }

        // 노드에 BLACK을 더합니다.
        addBlack();
        return hasExtraBlack;
    }

    public boolean isNilNode() {
        return this == NIL;
    }

    public void setBlack() {
        color = BLACK;
    }

    public void setRed() {
        color = RED;
    }
    public void addBlack() {
        if (color == RED) {
            color = BLACK;
        } else {
            setExtraBlack();
        }
    }

    public void subBlack() {
        if (color == RED) {
            throw new IllegalStateException("현재노드가 RED이기 때문에 BLACK을 뺄 수 없습니다.");
        } else {
            if (hasExtraBlack) {
                hasExtraBlack = false;
            } else {
                color = RED;
            }
        }
    }
    
    public void addDoublyBlackChild(int side) {
        if (side == LEFT) {
            left = getDoublyBlackNilNode(this);
        } else if (side == RIGHT) {
            right = getDoublyBlackNilNode(this);
        } else {
            throw new IllegalArgumentException("LEFT(-1), RIGHT(1) 이외의 다른 값이 인자로 주어졌습니다.");
        }
    }
    
    public boolean isBlack() {
        return color == BLACK;
    }
    public boolean isRed() {
        return color == RED;
    }
    
    public void setExtraBlack() {
        if (color == RED) {
            throw new IllegalStateException("RED 노드는 extra black을 가질 수 없습니다.");
        } else if (hasExtraBlack) {
            throw new IllegalArgumentException("이미 extra black을 가진 노드는 추가로 black을 가질 수 없습니다.");
        }
        
        hasExtraBlack = true;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
}
