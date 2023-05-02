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

    /*안나가 만든 생성자*/
    public Node (Node parent, Node left, Node right, int value, int color){
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.value = value;
        this.color = color;
        hasExtraBlack = false;
    }

    /**
     * 형제 노드의 색을 반환합니다.
     * @return RED : 1, BLACK : -1, 형제 없음 : 0
     */
    @Override
    public int findBrotherColor() {
        int findSideResult = findSide();
        //현재 노드가 부모의 왼쪽(-1) 노드에 있다면 형제 노드는 부모의 오른쪽에 있다.
        if(findSideResult == -1){
            //부모의 오른쪽 노드가 존재한다면
            if(parent.right != null){
                //존재하는 노드가 빨강색이면
                if(parent.right.color == RED){
                    //red 반환
                    return RED;
                }else{
                    //검정색이면 blacak 반환
                    return BLACK;
                }
            }
        }else if(findSideResult  == 1 ){
            if(parent.left != null) {
                if(parent.left.color == RED){
                    return RED;
                }else {
                    return BLACK;
                }
            }
        }
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


    /**
     * 현재 노드의 자식 중 Red Node가 있는지 확인합니다.
     *
     * @return 왼쪽자식 : -1, 오른쪽 자식 : 1, 양쪽 : 2, 없음 : 0
     */
    @Override
    public int hasRedChild() {
        if(right.color == RED || left.color == RED) {
            if(right.color == RED && left.color == RED){
                return 2;
            }else if(right.color == RED){
                return 1;
            }else{
                return -1;
            }
        }
        return 0;
    }
}
