// 참고 : https://zeddios.tistory.com/m/237

public class RedBlackTree implements iRedBlackTree{

    static final int BLACK = -1;
    static final int RED = 1;
    static final int LEFT = -1;
    static final int RIGHT = 1;
    static final  int NOTEXIT = 0;
    static final int BOTH = 2;

    public static Node NIL = new Node(null, null, null, 0, BLACK);
    Node root;
    int size;
    @Override
    public boolean isEmpty() {
        if(root == null){
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void insert(int value) {
        if(root == null){
            root = new Node();
            root.value = value;
            root.color = BLACK;
            root.right = NIL;
            root.left = NIL;

            size ++;
        }else{
            insertNode(root, value);
        }
    }

    private void insertNode(Node node, int value) {

        //삽입과정
        if(value < node.value){
            if(node.left == NIL){
                node.left = new Node(node, NIL, NIL, value, RED);
                size++;
            }else{
                insertNode(node.left, value);
            }
        }else if(value > node.value){
            if(node.right == NIL){
                node.right = new Node(node, NIL, NIL, value, RED);
                size++;
            }else{
                insertNode(node.right, value);
            }
        }else{
//            throw new CustomDuplicatedElementException();
        }


        //부모의 색이 검정이면 그대로 끝내면 됨
        //부모의 색이 빨강일 때 삼촌노드의 색을 확인하고 색에 따라 적절히 밸런스를 맞춰준다
        //삼촌 노드가 레드? => Recoloring
        //삼촌 노드가 블랙? => ReStucturing
        if(node.color == RED){
            //삽입한 노드의 부모의 형제 색깔이 RED인 경우 Recoloring
            if(node.findBrotherColor() == RED){
                Recoloring(node.parent);
            }else{
                //삽입한 노드의 부모의 형제 색깔이 BLACK이거나 null인 경우 Restructuring 진행
                Restructuring(node, value);
            }
        }
    }
    /*
        1. 삽입된 노드의 부모와 그 형제를 검정으로 하고 조부모(node)는 빨강으로 한다.
        2. 조부모가 루트가 아니라면 조부모(node)의 부모가 빨강인지 확인한다.
        3. 빨강이라면 restructuring 또는 recoloring 진행한다.
     */
    private void Recoloring(Node node) {
        node.right.color = BLACK;
        node.left.color = BLACK;
        node.color = RED;

        if(node == root){
            node.color = BLACK;
        }else{
            //red의 자식이 red가 되버리는 경우
            if(node.parent.color == RED){
                if(node.parent.findBrotherColor()==RED){
                    Recoloring(node.parent.parent);
                }else{
                    Restructuring(node.parent, node.value);
                }
            }
        }

    }

    private void Restructuring(Node node , int value) {

        //case 2. LR의 경우 =>  node(삽입한 노드의 부모) 기준으로 RotateLeft LL으로 만들어주기 다음 case1 LL 경우 실행
        if(node.findSide() == LEFT && node.right.value ==value){
            rotateLeft(node);
            value = node.value;
            node = node.parent;
        }

        //case 2. RL의 경우 =>  node(삽입한 노드의 부모) 기준으로 RotateRight RR으로 만들어주기 다음 case1 RR 경우 실행
        if(node.findSide() == RIGHT && node.left.value ==value){
            rotateRight(node);
            value = node.value;
            node = node.parent;
        }

        //case 1. LL의 경우 => node(삽입한 노드의 부모)의 부모를 기준으로 RotateRight
        if(node.findSide() == LEFT && node.left.value ==value){
            //삽입한 노드의 부모(node)와 조부모(node.parent)의 색을 바꿔준다.
            node.color = BLACK;
            node.parent.color =  RED;
            //조부모 노드를 기준으로 회전
            rotateRight(node.parent);
            return;
        }

        //case 1. RR의 경우 =>  node(삽입한 노드의 부모)의 부모를 기준으로 RotateLeft
        if(node.findSide() == RIGHT && node.right.value ==value){
            //삽입한 노드의 부모(node)와 조부모(node.parent)의 색을 바꿔준다.
            node.color = BLACK;
            node.parent.color =  RED;
            //조부모 노드를 기준으로 회전
            rotateLeft(node.parent);
            return;
        }

    }

    @Override
    public boolean contains(int value) {
        return containsNode(root, value);
    }

    private boolean containsNode(Node node, int value) {
        if(node == null) {
            return false;
        }

        if(node.value == value){
            return true;
        }

        if(value < node.value){
            return containsNode(node.left, value);
        }else{
            return containsNode(node.right, value);
        }
    }

    @Override
    public void delete(int value) {
        //루트 삭제시 null로 바꾸기
        if(!contains(value)){
//            throw new CustomNoSuchElementException();
        }

        Node target = findNodeByValue(root, value);

        //삭제할 노드에 자식이 없는 경우
        if(target.left == NIL && target.right == NIL){

            if(target.color == RED){ //삭제할 노드의 색이 RED 이면 삭제하면 됨
                //-> 부모에서 타겟과 연결된 쪽의 연결 끊기
                if(target.findSide() == LEFT){
                    target.parent.left = NIL;
                }else{
                    target.parent.right = NIL;
                }
            }else if(target.color == BLACK){ //단말노드를 삭제할때 그 노드의 색이 블랙인 경우
                if(target == root){
                    root = null;
                    return;
                }
                //기존의 타켓 노드를 부모노드와 끊고 부모노드의 링크를 doublyblack으로 변경
                Node doublyBlackNIL = new Node(target.parent, null,null,0,iNode.BLACK);
                doublyBlackNIL.hasExtraBlack = true;
                if(target.findSide() == LEFT){
                    target.parent.left = doublyBlackNIL;
                }else{
                    target.parent.right = doublyBlackNIL;
                }

                removeExtraBlack(doublyBlackNIL);
            }


        }else if(target.left != NIL && target.right != NIL) { //자식이 둘다 있는 경우
            Node replaceNode = findReplaceNode(target.left);
            if (target == root) {
                root.value = replaceNode.value;
                if (replaceNode.findSide() == LEFT) {
                    replaceNode.parent.left = NIL;
                } else {
                    replaceNode.parent.right = NIL;
                }
            } else {
                target.value = replaceNode.value;
            }

            if(replaceNode.color == RED){
                if(replaceNode.findSide() == LEFT){
                    replaceNode.parent.left = NIL;
                }else {
                    replaceNode.parent.right = NIL;
                }

            }else if(replaceNode.color == BLACK){
                replaceNode.right = null;
                replaceNode.left = null;
                replaceNode.hasExtraBlack = true;

                removeExtraBlack(replaceNode);
            }
        }
        else {
            //왼쪽 자식만 있거나 오른쪽 자식만 있는 경우
            Node child ;
            if(target.right != NIL){
                child = target.right;
            }else{
                child = target.left;
            }

            if(target == root) {
                root = child;
                root.color = BLACK; // root 노드는 항상 black
                return;
            }

            child.parent = target.parent;//자식의 부모를 타겟에서 타겟의 부모로 변경
            //타겟의 부모에서 타겟과 연결된 자식이 왼쪽인지 오른쪽인지 찾아서 타켓의 자식과 연결
            if(target.findSide()== LEFT) {
                target.parent.left = child;
            }else {
                target.parent.right = child;
            }

            if(target.color == BLACK){
                if(target == root) {
                    root = child;
                    root.color = BLACK;
                    return;
                }
                child.hasExtraBlack = true; //Red and Black
                removeExtraBlack(child);
            }
        }

    }

    private void removeExtraBlack(Node extraBlackNode) {
        if(extraBlackNode.color == RED) {
            extraBlackNode.color = BLACK;
            return;
        }
/**
 * 현재 노드의 자식 중 Red Node가 있는지 확인합니다.
 *
 * @return 왼쪽자식 : -1, 오른쪽 자식 : 1, 양쪽 : 2, 없음 : 0
 */

        //case 4 : 형제의 색이 빨강인 경우
        // 형제를 검정으로 만들어 아래의 케이스 중에서 한번 더 걸리게 된다.
        if(extraBlackNode.parent.hasRedChild() == RIGHT || extraBlackNode.hasRedChild() == LEFT){
            if(extraBlackNode.findSide() == LEFT){
                extraBlackNode.parent.right.color = BLACK;
                extraBlackNode.parent.color = RED;
                rotateLeft(extraBlackNode.parent);
            }else{
                extraBlackNode.parent.left.color = BLACK;
                extraBlackNode.parent.color = RED;
                rotateRight(extraBlackNode.parent);
            }

        }


        //case 1, 2, 3: 더블블랙의 형제가 블랙인경우
        if(extraBlackNode.parent.hasRedChild() == NOTEXIT){

            //case 1 형제가 왼쪽에 존재하고 형제의 왼쪽에 자식이 RED 일때
            if(extraBlackNode.findSide() == LEFT && extraBlackNode.parent.right.right.color == RED){
                Node brother = extraBlackNode.parent.right;

                brother.color = extraBlackNode.parent.color; //형제의 색은 부모의 색과 동일하게 만들고
                //형제의 부모와 빨간 자식을 black 으로 바꿈
                extraBlackNode.parent.color = BLACK;
                brother.right.color = BLACK;

                rotateLeft(extraBlackNode.parent); //부모를 기준으로 왼쪽으로 회전
                extraBlackNode.hasExtraBlack = false;

            }else if(extraBlackNode.findSide() == RIGHT && extraBlackNode.parent.left.left.color == RED){
                Node brother = extraBlackNode.parent.left; //형제노드

                brother.color = extraBlackNode.parent.color; //형제의 색은 부모의 색과 동일하게 만들고
                //형제의 부모와 빨간 자식을 black 으로 바꿈
                extraBlackNode.parent.color = BLACK;
                brother.left.color = BLACK;

                rotateRight(extraBlackNode.parent); //부모를 기준으로 오른쪽으로 회전
                extraBlackNode.hasExtraBlack = false;
            }




        }


    }

    //삭제할 노드와 대체할 노드 찾기
    private Node findReplaceNode(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private Node findNodeByValue(Node node, int value) {
        if (node.value == value) {
            return node;
        }

        if (value < node.value) {
            return findNodeByValue(node.left, value);
        } else {
            return findNodeByValue(node.right, value);
        }
    }


    //RR인 경우
    /*     node           	   R
               R     =>  node     RR
     * 		 RL RR          RL
     */
    @Override
    public void rotateLeft(Node tree) {
        if(tree == root) {
            root = tree.right;
        }

        Node R = tree.right;
        Node RL = tree.right.left;

        if(tree.parent != null) {
            tree.parent.right = R;
        }
        R.parent = tree.parent;

        tree.parent = R;
        tree.right = RL;
        R.left = tree;

        if(RL != null) {
            RL.parent = tree;
        }
    }

    //LL인 경우
    /*     node           L
          L       =>  LL    node
     *  LL LR              LR
     */
    @Override
    public void rotateRight(Node tree) {
        if(tree == root) {
            root = tree.left;
        }

        Node L = tree.left;
        Node LR = tree.left.right;

        if(tree.parent != null) {
            tree.parent.left = L;
        }
        L.parent = tree.parent;

        tree.parent = L;
        tree.left = LR;
        L.right = tree;

        if(LR != null) {
            LR.parent = tree;
        }
    }
    //subtree의 black 수 계산 (자기 자신 제외 + nil 노드 포함)
    @Override
    public int countBlack(Node tree) {
        int count =0;

        while (true){
            if(tree == NIL){
                count ++;
                break;
            }
            if(tree.right.color == BLACK) {
                count++;
            }
            tree= tree.right;
        }

        return count;

    }
}
