public class RedBlackTree implements iRedBlackTree{
    Node nil = new Node(Integer.MAX_VALUE, BLACK);

    Node root;
    int size;

    public RedBlackTree() {
        this.root = null;
        this.size = size;
    }

    @Override
    public boolean isEmpty() {
        if (size == 0) {
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
        Node node = new Node();
        if (root == null){
            root = node;
            root.color = BLACK;
            root.left = nil;
            root.right = nil;
            // 여기서 left right의 부모를 root로 지정해줘야할까?
            return;
        }

        findPositionAndInsert(value);
    }

    private void findPositionAndInsert(int value) {
        Node node = root;
        while (true) {
            if (value  <= node.value) {
                if(node.left != null) {
                    if (node.left == nil) {
                        node.left = new Node(value, node, nil, nil);
                        modifyAfterInsert(node.left);
                        break;
                    } else {
                        node = node.left;
                    }
                }
            } else {
                if (node.right != null) {
                    if (node.right == nil) {
                        node.right = new Node(value, node, nil, nil);
                        modifyAfterInsert(node.right);
                        break;
                    } else {
                        node = node.right;
                    }
                }

            }
        }
    }

    private void modifyAfterInsert(Node node) {
        Node parent = node.parent;
        Node grandParent = parent.parent;
        if (node.parent.color == BLACK) {
            return;
        }
        
        if (grandParent == null) { // 조부모 없으면 내 부모가 root임
            node.parent.color = BLACK;
            return;
        }

        Node uncle = findUncle(node);

        // 삼촌 부모 나 다 red => recoloring
        if (uncle != nil && uncle.color == RED) {
            uncle.color = BLACK;
            parent.color = BLACK;

            grandParent.color = RED;
            
            // 루트까지 올라가면서 수정
            modifyAfterInsert(grandParent);
        }

        // parent가 부모의 왼쪽 자식
        if(uncle.color == BLACK && parent.findSide() == -1) {
            // left right 로 꺽인 경우
            if (node.findSide() == 1) {
                rotateLeft(parent);
                // node를 회전된 서브트리의 노드로
                parent = node;
            }

            rotateRight(grandParent);

            parent.color = BLACK;
            // parent의 왼쪽
            grandParent.color = RED;
        }

        if (uncle.color == BLACK && parent.findSide() == 1) {
            // right left 로 꺽인 경우
            if (node.findSide() == -1) {
                rotateRight(parent);

                parent = node;
            }

            // right right 인 경우
            rotateLeft(grandParent);

            parent.color = BLACK;
            // parent의 오른쪽
            grandParent.color = RED;
        }
    }

    private Node findUncle(Node node) {
        Node parent = node.parent;

        if (parent.findSide() == -1) {
            return parent.right;
        } else if (parent.findSide() == 1) {
            return parent.left;
        }

        return null;
    }

    @Override
    public boolean contains(int value) {
        return false;
    }
    
    @Override
    public void delete(int value) {
        Node node = root;
        
        // 위치 찾기
        while (node != nil && node.value != value) {
            if (value < node.value) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        // 찾는 값 없는 경우


        //
        Node node2;
        int deleteNodeColor;

        // 자식이 없거나 하나만 있는 간단한 경우
        if (node.left == nil || node.right == nil) {
            // 중복 코드로 메서드화 필요!
            deleteNodeColor = node.color;
            if (node.left != nil) { // 왼쪽 자식만
                modifyParentNode(node.parent, node, node.left);
                node2 =  node.left; // 올라가질 노드
            } else if (node.right != nil) { // 오른쪽 자식만
                modifyParentNode(node.parent, node, node.right);
                node2 =  node.right; 
            } else { // 자식 없음
                node = nil;
                node2 = nil;
            }
        } else { // 자식 2개인 경우
            // 왼쪽 서브트리에서 가장 큰값 가져온다.
            Node findNode = findLeftSubtree(node.left);

            // 값만 가져온다.
            node.value = findNode.value;

            // 자식이 없거나 한개인 경우 처럼 삭제(후에 삭제로인한 색깔바꾸기나 restructuring 필요)
            // 중복 코드로 메서드화 필요!
            deleteNodeColor = node.color;
            if (findNode.left != nil) { // 왼쪽 자식만
                modifyParentNode(findNode.parent, findNode, findNode.left);
                node2 =  findNode.left; // 올라가질 노드
            } else if (findNode.right != nil) { // 오른쪽 자식만
                modifyParentNode(findNode.parent, findNode, findNode.right);
                node2 =  findNode.right;
            } else { // 자식 없음
                findNode = nil;
                node2 = nil;
            }
        }


    }

    private Node findLeftSubtree(Node node) {
        while (node.right != nil) {
            node = node.left;
        }
        return node;
    }
    
    @Override
    public void rotateLeft(Node tree) {
        Node parent = tree.parent;
        Node rightChild = tree.right;

        tree.right = rightChild.left;
        if (rightChild.left != nil) {
            rightChild.left.parent = tree;
        }

        rightChild.left = tree;
        tree.parent = rightChild;

        modifyParentNode(parent, tree, rightChild);
    }
    
    @Override
    public void rotateRight(Node tree) {
        Node parent = tree.parent;
        Node leftChild = tree.left;

        tree.left = leftChild.right;
        
        if (leftChild.right != nil) {
            leftChild.right.parent = tree;
        }

        leftChild.right = tree;
        tree.parent = leftChild;

        modifyParentNode(parent, tree, leftChild);
    }

    public void modifyParentNode(Node parent, Node tree, Node changeChild) {
        if(parent == null) {
            root = changeChild;
        } else if (parent.left == tree) {
            parent.left = changeChild;
        } else if (parent.right == tree) {
            parent.right = changeChild;
        }

        changeChild.parent = parent;
    }


    @Override
    public int countBlack(Node tree) {
        return 0;
    }
}
