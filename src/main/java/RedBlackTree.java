public class RedBlackTree implements iRedBlackTree{
    Node root;
    int size;

    public RedBlackTree() {
        root=null;
        size=0;
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public void insert(int value) {
        root = insert(root, value);
        root.color = Node.BLACK;  // 항상 루트를 검은색으로 설정
    }

    // 추가 메서드-1
    private Node insert(Node node, int value) {
        // 표준 BST 삽입 수행
        if (node == null) {
            size++;
            return new Node(value);  // 새 노드는 빨간색으로 설정
        }
        if (value < node.value) {
            node.left = insert(node.left, value);
            node.left.parent = node;
        } else if (value > node.value) {
            node.right = insert(node.right, value);
            node.right.parent = node;
        }

        // 레드-블랙 트리의 규칙을 유지하도록 노드를 조정
         fixAfterInsert(node);

        return node;
    }

    // 추가 메서드-2
    private void fixAfterInsert(Node node) {
        Node parent = null;
        Node grandparent = null;

        while ((node != root) && (node.color != Node.BLACK) && (node.parent.color == Node.RED)) {

            parent = node.parent;
            grandparent = parent.parent;

            // 경우 1: 부모 노드가 할아버지 노드의 왼쪽 자식인 경우
            if (parent == grandparent.left) {
                Node uncle = grandparent.right;

                // 경우 1-1: 삼촌 노드도 RED인 경우 - 색상을 반전 및 상위 노드로 이동
                if (uncle != null && uncle.color == Node.RED) {
                    grandparent.color = Node.RED;
                    parent.color = Node.BLACK;
                    uncle.color = Node.BLACK;
                    node = grandparent;
                } else {
                    // 경우 1-2: Node가 부모의 오른쪽 자식인 경우 - 왼쪽 회전을 수행
                    if (node == parent.right) {
                        rotateLeft(parent);
                        node = parent;
                        parent = node.parent;
                    }

                    // 경우 1-3: Node가 부모의 왼쪽 자식인 경우 - 오른쪽 회전을 수행
                    rotateRight(grandparent);
                    int tempColor = parent.color;
                    parent.color = grandparent.color;
                    grandparent.color = tempColor;
                    node = parent;
                }
            } else { // 경우 2: 부모 노드가 할아버지 노드의 오른쪽 자식인 경우
                Node uncle = grandparent.left;

                // 경우 2-1: 삼촌 노드도 RED인 경우 - 색 반전 및 상위 노드로 이동
                if (uncle != null && uncle.color == Node.RED) {
                    grandparent.color = Node.RED;
                    parent.color = Node.BLACK;
                    uncle.color = Node.BLACK;
                    node = grandparent;
                } else {
                    // 경우 2-2: Node가 부모의 왼쪽 자식인 경우 - 오른쪽 회전
                    if (node == parent.left) {
                        rotateRight(parent);
                        node = parent;
                        parent = node.parent;
                    }

                    // 경우 2-3: Node가 부모의 오른쪽 자식인 경우 - 왼쪽 회전
                    rotateLeft(grandparent);
                    int tempColor = parent.color;
                    parent.color = grandparent.color;
                    grandparent.color = tempColor;
                    node = parent;
                }
            }
        }

        // 루트를 항상 검정색으로
        root.color = Node.BLACK;
    }
    
    @Override
    public boolean contains(int value) {
        return false;
    }
    
    @Override
    public void delete(int value) {
    
    }

    // 트리의 특정 노드를 중심으로 왼쪽으로 회전하는 메서드
    @Override
    public void rotateLeft(Node tree) {
        Node rightChild = tree.right;

        tree.right = rightChild.left;  // 노드의 오른쪽 자식을 오른쪽 자식의 왼쪽 자식으로 변경
        if (tree.right != null)
            tree.right.parent = tree;  // 변경된 오른쪽 자식의 부모 설정

        if (tree.parent == null)  // 노드가 루트인 경우
            root = rightChild;
        else if (tree == tree.parent.left)  // 노드가 부모의 왼쪽 자식인 경우
            tree.parent.left = rightChild;
        else  // 노드가 부모의 오른쪽 자식인 경우
            tree.parent.right = rightChild;

        rightChild.left = tree;  // 오른쪽 자식의 왼쪽 자식을 노드로 설정
        tree.parent = rightChild;  // 노드의 부모를 오른쪽 자식으로 설정
    }

    // 트리의 특정 노드를 중심으로 오른쪽으로 회전하는 메서드
    @Override
    public void rotateRight(Node tree) {
        Node leftChild = tree.left;

        tree.left = leftChild.right;  // 노드의 왼쪽 자식을 왼쪽 자식의 오른쪽 자식으로 변경
        if (tree.left != null)
            tree.left.parent = tree;  // 변경된 왼쪽 자식의 부모 설정

        if (tree.parent == null)  // 노드가 루트인 경우
            root = leftChild;
        else if (tree == tree.parent.left)  // 노드가 부모의 왼쪽 자식인 경우
            tree.parent.left = leftChild;
        else  // 노드가 부모의 오른쪽 자식인 경우
            tree.parent.right = leftChild;

        leftChild.right = tree;  // 왼쪽 자식의 오른쪽 자식을 노드로 설정
        tree.parent = leftChild;  // 노드의 부모를 왼쪽 자식으로 설정
    }
    
    @Override
    public int countBlack(Node tree) {
        return 0;
    }
}
