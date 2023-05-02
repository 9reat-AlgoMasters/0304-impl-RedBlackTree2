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


    /**
     * 현재 노드가 부모의 어느쪽 자식인지 확인합니다.
     * @return 왼쪽 자식 : -1, 오른쪽 자식 : 1, root Node(부모 X) : 0
     */
    @Override
    public int findSide() {
        //현 노드가 부모가 없는 루트노드
        if(parent == null) {
            return 0;
        }

        //부모의 왼쪽자식이 현재 노드
        if(parent.left.value == value){
            return -1;
        }

        //부모의 오른쪽 자식이 현재 노드
        if(parent.right.value == value){
            return 1;
        }

        return 0;
    }

    @Override
    public int hasRedChild() {
        return 0;
    }
}
