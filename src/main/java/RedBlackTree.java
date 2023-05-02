public class RedBlackTree implements iRedBlackTree{

    static final int BLACK = -1;
    static final int RED = 1;
    static Node NIL= new Node();
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
                node.left = new Node();
                Node newNode = node.left;
                newNode.value = value;
                newNode.color = RED;
                newNode.right = NIL;
                newNode.left = NIL;
                newNode.parent = node;
                size++;
            }else{
                insertNode(node.left, value);
            }
        }else if(value > node.value){
            if(node.right == NIL){
                node.right = new Node();
                Node newNode = node.right;
                newNode.value = value;
                newNode.color = RED;
                newNode.right = NIL;
                newNode.left = NIL;
                newNode.parent = node;
                size++;
            }else{
                insertNode(node.right, value);
            }
        }else{
//            throw new CustomDuplicatedElementException();
        }

        if(node.color == RED){
            /*/**
             * 현재 노드가 부모의 어느쪽 자식인지 확인합니다.
             * @return 왼쪽 자식 : -1, 오른쪽 자식 : 1, root Node(부모 X) : 0
            int findSide();
            */
            //LL인 경우
            if(node.left.value == value){
                if(node.findSide() == -1){
                    // LL인 경우
                } else if (node.findSide() == 1) {
                    // LR인 경우
                    if( ){

                    }

                }
            }

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
