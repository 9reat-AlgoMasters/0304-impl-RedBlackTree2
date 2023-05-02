import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class RedBlackTree implements iRedBlackTree{
    static final int LEFT = -1;
    static final int RIGHT = 1;
    static final int EMPTY = 0;
    static final int ROOT = 0;
    static final int BOTH = 2;

    Node root;
    int size;

    public RedBlackTree() {
        root = null;
        size = 0;
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
        if (isEmpty()) {
            root = new Node(value);
            root.color = BLACK;
        } else {
            insertRecur(root, value);
        }

        size++;
    }

    private void insertRecur(Node node, int value) {
        if (node.value == value) {
            throw new IllegalArgumentException(String.format("[insert] 이미 %d는 트리에 있습니다.", value));
        }

        Node targetChild;
        if (value < node.value) {
            targetChild = node.left;
        } else {
            targetChild = node.right;
        }

        if (targetChild == Node.NIL) { // 주어진 값이 삽입될 곳(targetChild)을 찾은 경우
            targetChild = new Node(value);
            targetChild.parent = node;
            postProcessOfInsert(targetChild);
        } else { // 주어진 값이 삽입될 곳을 못 찾은 경우 해당 자식으로 다시 찾는다. (재귀)
            insertRecur(targetChild, value);
        }
    }

    /**
     * 1. node의 부모가 BLACK일 경우 바로 종료합니다.
     * 2. node의 부모가 RED일 경우 삼촌 노드(부모의 형제 노드)의 색을 확인해 Restructuring 또는 Recoloring 처리를 합니다.
     * 3. node가 root 일 경우는 잘못된 함수 호출이므로 IllegalArgumentException을 던집니다.
     * @param node 삽입된 노드
     */
    private void postProcessOfInsert(Node node) {
        if (node == root) {
            throw new IllegalArgumentException("[insert] root 노드에서 insert 후처리가 일어났습니다.");
        }

        // 1. 부모가 BLACK 노드라면 바로 종료합니다.
        if (node.parent.color == BLACK) {
            return;
        }

        /*
        2. 부모가 RED 라면 RED 가 연속으로 붙어 있다는 것이므로 Case 별로 나누어서 처리를 합니다.
        참고 1) RedBlack Tree 속성에 의해 root 노드는 항상 BLACK이므로 부모가 RED 일경우 반드시 node의 할아버지 노드가 BLACK으로 존재한다는 것이 보장됩니다.
        참고 2) 할아버지 노드가 존재하지 않을 경우 IllegalArgumentException를 던집니다.
         */
        if (node.parent == root) {
            throw new IllegalArgumentException("[insert][2] Red가 연속으로 두개 붙어있는 상황에서 할아버지 노드가 존재하지 않습니다. -- 원인 : 부모 노드가 root 노드입니다.");
        } else if (node.parent.parent == null) {
            throw new IllegalArgumentException("[insert][2] Red가 연속으로 두개 붙어있는 상황에서 할아버지 노드가 존재하지 않습니다. -- 원인 : 부모 노드가 root 노드가 아니지만 할아버지 노드가 존재하지 않습니다.");
        } else if (node.parent.parent.color == RED) {
            throw new IllegalArgumentException("[insert][2] Red가 연속으로 두개 붙어있는 상황에서 할아버지 노드 또한 RED 입니다.");
        }
        // 2-1. 삼촌 노드(부모의 형제 노드)가 BLACK 이라면 Restructuring 처리
        if (node.parent.isBrotherBlack()) {
            // 2-1-1. node 부터 할아버지 노드까지 일직선으로 연결되어 있을 경우
            if (node.findSide() == node.parent.findSide()) {
                // 1) 왼쪽으로만 이어진 경우
                if (node.findSide() == LEFT) {
                    /*
                    a) node의 할아버지 노드를 루트로 하는 서브트리를 오른쪽으로 회전합니다.
                    b) 기존의 할아버지 노드(BLACK)와 부모(RED)의 색을 서로 바꿔줍니다.
                     */
                    rotateRight(node.parent.parent); // 오른쪽 회전
                    node.parent.setBlack(); // 부모노드 RED -> BLACK
                    node.parent.right.setRed(); // 기존 할아버지 노드 BLACK -> RED
                } else { // 2) 오른쪽으로만 이어진 경우
                    if (node.findSide() == EMPTY) {
                        throw new IllegalArgumentException("[insert][Restructuring][2-1-1] node 부모에 대한 예외처리가 된 상황에서 부모가 존재하지 않습니다. Node 클래스에 오류가 있는지 확인해보세요.");
                    }

                    /*
                    a) node의 할아버지 노드를 루트로 하는 서브트리를 왼쪽으로 회전합니다.
                    b) 기존의 할아버지 노드(BLACK)와 부모(RED)의 색을 서로 바꿔줍니다.
                     */
                    rotateLeft(node.parent.parent); // 오른쪽 회전
                    node.parent.setBlack(); // 부모노드 RED -> BLACK
                    node.parent.left.setRed(); // 기존 할아버지 노드 BLACK -> RED
                }
            }

            // 2-1-2. node 부터 할아버지 노드까지 부모 노드에서 한번 꺾인 경우
            else {
                // 1) 왼쪽 -> 오른쪽 으로 꺾여 있는 경우
                if (node.parent.findSide() == LEFT && node.findSide() == RIGHT) {
                    /*
                    a) node의 부모 노드를 루트로 하는 서브트리를 오른쪽으로 회전합니다.
                    b) node의 할아버지 노드를 루트로 하는 서브트리를 왼쪽으로 회전합니다.
                    c) 기존의 할아버지 노드(BLACK)와 node(RED)의 색을 서로 바꿔줍니다.
                     */

                    rotateRight(node.parent);
                    rotateLeft(node.parent.parent);
                    node.setBlack(); // node RED -> BLACK
                    node.left.setRed(); // 기존 할아버지 노드 BLACK -> RED

                }
                // 2) 오른쪽 -> 왼쪽 으로 꺾여 있는 경우
                else if (node.parent.findSide() == RIGHT && node.findSide() == LEFT) {
                    /*
                    a) node의 부모 노드를 루트로 하는 서브트리를 왼쪽으로 회전합니다.
                    b) node의 할아버지 노드를 루트로 하는 서브트리를 오른쪽으로 회전합니다.
                    c) 기존의 할아버지 노드(BLACK)와 node(RED)의 색을 서로 바꿔줍니다.
                     */

                    rotateLeft(node.parent);
                    rotateRight(node.parent.parent);
                    node.setBlack(); // node RED -> BLACK
                    node.right.setRed(); // 기존 할아버지 노드 BLACK -> RED
                } else {
                    if (node.parent.findSide() == EMPTY) {
                        throw new IllegalArgumentException("[insert][Restructuring][2-1-2] 할아버지 노드가 존재하지 않음에 대한 예외처리가 이미 진행되었지만 할아버지 노드가 존재하지 않습니다.");
                    } else {
                        throw new IllegalArgumentException("[insert][Restructuring][2-1-2] 부모 노드가 존재하지 않음에 대한 예외처리가 이미 진행되었지만 부모 노드가 존재하지 않습니다.");
                    }
                }
            } // [2-1-2] 꺾인 경우 종료
        } // [2-1] Restructuring 종료

        // 2-2. 삼촌(부모의 형제) 노드가 RED 라면 Recoloring 후 할아버지 노드에서 postProcessOfInsert 호출
        else {
            /*
            a) 부모와 삼촌 노드의 색을 BLACK으로 바꿔줍니다.
            b) 할아버지 노드의 색을 RED로 바꿔줍니다.
            c) 할아버지 노드에서 postProcessOfInsert를 호출합니다.
             */

            node.parent.setBlack();
            if (node.parent.findBrother().isNilNode()) {
                throw new NoSuchElementException("[insert][Recoloring] 삼촌 노드가 NIL 노드 입니다.");
            }
            node.parent.findBrother().setBlack();
            node.parent.parent.setRed();
            postProcessOfInsert(node.parent.parent);
        }
    }

    @Override
    public boolean contains(int value) {
        return containsRecur(root, value);
    }

    private boolean containsRecur(Node node, int value) {
        if (node == null) {
            throw new NullPointerException("[contains] 탐색하고자 하는 Node가 null입니다.");
        }

        if (node == Node.NIL) {
            return false;
        }

        if (node.value == value) {
            return true;
        }

        if (value < node.value) {
            return containsRecur(node.left, value);
        } else {
            return containsRecur(node.right, value);
        }
    }
    
    @Override
    public void delete(int value) {
        /*
        value와 값이 같은 노드를 target으로 지정합니다.
        만약 찾지 못한다면 IllegalArgumentException을 던집니다.
         */
        Node target = findByValue(root, value);
        int childInfo = target.findChildInfo();
        Node postProcessNode = null;

        switch (childInfo) {
            // 1. 자식이 없을 때
            case EMPTY:
                postProcessNode = deleteNodeWithNoChild(target);
                break;

            // 2. 자식이 하나만 있을 때
            case LEFT:
            case RIGHT:
                // next : target의 자식 노드
                postProcessNode = deleteNodeWithOneChild(target);
                break;

            // 3. 자식이 둘 다 있을 때
            /*
            관리 링크
            1) target 부모 - replaceNode (target이 root라면 필요 없음)
            2) replaceNode - target의 왼쪽 자식
            3) replaceNode - target의 오른쪽 자식
            4) replaceNode의 부모 - replaceNode의 왼쪽 자식 (없을 수도 있음)
             */
            case BOTH:
                // replaceNode : target의 자리를 대체할 노드
                Node replaceNode = findReplaceNode(target.left);
                
                // target의 값을 replaceNode의 값으로 수정한다.
                target.setValue(replaceNode.value);
                
                /*
                이 경우 replaceNode가 삭제되는 노드로 처리할 수 있고
                replaceNode는 오른쪽 자식이 없기 때문에 자식이 한개 이거나 없다.
                 */
                int replaceNodeChildInfo = replaceNode.findChildInfo();
                if (replaceNodeChildInfo == EMPTY) {
                    postProcessNode = deleteNodeWithNoChild(replaceNode);
                } else if (replaceNodeChildInfo == LEFT) {
                    postProcessNode = deleteNodeWithOneChild(replaceNode);
                } else {
                    throw new IllegalStateException("[delete] replace node에 오른쪽 자식이 존재합니다.");
                }
        }

        size--;
        if (postProcessNode != null) {
            postProcessOfDelete(postProcessNode);
        }
    }
    
    /**
     * 자식이 없는 노드를 삭제합니다.
     * @param target 삭제될 노드
     * @return 삭제하는 과정에서 Doubly Dlack NIL Node가 생긴다면 해당 노드를 반환합니다. 생기지 않는다면 null을 반환합니다.
     */
    private Node deleteNodeWithNoChild(Node target) {
        // target이 root라면 root를 null로 바꿔준다.
        if (target == root) {
            root = null;
            size--;
            return null;
        }

                /*
                target의 부모가 존재한다면
                1. target이 BLACK 이라면 target의 부모에게 doublyBlackNil노드를 연결한다.
                    - 그 후 postProcessNode를 해당 doublyBlackNil로 지정한다.
                2. target이 RED 라면 target의 부모에게 NIL 을 연결한다.
                 */
        if (target.parent.left == target) {
            if (target.isBlack()) {
                target.parent.left = Node.getDoublyBlackNilNode(target.parent);
                return target.parent.left;
            } else {
                target.parent.left = Node.NIL;
            }
            
        } else {
            if (target.isBlack()) {
                target.parent.right = Node.getDoublyBlackNilNode(target.parent);
                return target.parent.right;
            } else {
                target.parent.right = Node.NIL;
            }
        }
        return null;
    }
    
    /**
     * 자식이 한개만 있는 노드를 삭제합니다.
     * @param target 삭제될 노드
     * @return 삭제하는 과정에서 Doubly Dlack NIL Node가 생긴다면 해당 노드를 반환합니다. 생기지 않는다면 null을 반환합니다.
     */
    private Node deleteNodeWithOneChild(Node target) {
        Node next = target.left == Node.NIL ? target.right : target.left;

                /*
                target이 root일 경우 root 링크를 target의 자식으로 바꾼다.
                ** target이 BLACK이라면 루트가 될 자식 노드의 색을 BLACK으로 바꾼다.
                    - 이미 BLACK 이라면 바꾸지 않아도 된다.
                 */
        if (target == root) {
            root = next;
            if (target.isBlack()) {
                root.setBlack();
            }
            size--;
            return null;
        }
        
        // next와 target의 부모를 이어준다.
        next.parent = target.parent;
        if (target.parent.left == target) {
            target.parent.left = next;
        } else {
            target.parent.right = next;
        }
                
                /*
                target이 BLACK이라면 RedBlack Tree의 속성을 유지하기 위해 BLACK을 next에게 보낸다.
                    - next도 BLACK 이라면 extra black을 설정하고 postProcessNode로 지정한다.
                    - next가 RED 라면 BLACK으로 바꿔준다.
                 */
        if (target.isBlack()) {
            if (next.isBlack()) {
                next.setExtraBlack();
                return next;
            } else {
                next.setBlack();
            }
        }
        return null;
    }
    
    private void postProcessOfDelete(Node postProcessNode) {
        /*
        < CASE STUDY >
        0. postProcessNode가 root일 때
        1. 형제가 BLACK 이고 RED 자식을 가지고 있을 때
        2. 형제가 BLACK 이고 RED 자식이 없을 때
        3. 형제가 RED일 때
         */

        // case0 - postProcessNode가 root라면 BLACK으로 바꿔준다.
        if (postProcessNode == root) {
            postProcessNode.setBlack();
            return;
        }
        
        // CASE 1,2 - 형제가 BLACK 이라면
        if (postProcessNode.isBrotherBlack()) {
            // CASE 1 - 형제가 BLACK 이고 RED 자식을 가지고 있을 때
            Node brotherNode = postProcessNode.findBrother();
            if (brotherNode.hasRedChild() != EMPTY) {
                /*
                1-1) 형제가 가진 RED 자식이 postProcessNode의 부모로부터 직선으로 이어져 있을 때
                1-2) 형제가 가진 RED 자식이 postProcessNode의 부모로부터 한번 꺾여서 이어져 있을 때
                 */
                // 1-1. node 부터 할아버지 노드까지 일직선으로 연결되어 있을 경우
                if (brotherNode.hasRedChild() == BOTH || brotherNode.findSide() == brotherNode.hasRedChild()) {
                    // 1) 왼쪽으로만 이어진 경우
                    if (brotherNode.findSide() == LEFT) {
                        treatDoublyBlackOfCaseOne(postProcessNode, brotherNode, LEFT);

                    } else { // 2) 오른쪽으로만 이어진 경우
                        treatDoublyBlackOfCaseOne(postProcessNode, brotherNode, RIGHT);
                    }
                }
                
                // 1-2. node 부터 할아버지 노드까지 부모 노드에서 한번 꺾인 경우
                else {
                    // 1) 왼쪽 -> 오른쪽 으로 꺾여 있는 경우
                    if (brotherNode.parent.findSide() == LEFT && brotherNode.hasRedChild() == RIGHT) {
                        /*
                        a) brotherNode를 기준으로 왼쪽 회전한다.
                        b) brotherNode를 brotherNode의 자식으로 바꾼다.
                        c) brotherNode(RED)(기존 자식)은 BLACK로, brotherNode의 자식(오른쪽)(기존 brotherNode)은 RED으로 바꾼다.
                        d) 1-1 case로 처리한다.
                         */
                        rotateLeft(brotherNode); // a)
                        brotherNode = brotherNode.parent; // b)
                        brotherNode.setBlack(); // c)
                        brotherNode.right.setRed(); // c)
                        treatDoublyBlackOfCaseOne(postProcessNode, brotherNode, LEFT);
                    }
                    // 2) 오른쪽 -> 왼쪽 으로 꺾여 있는 경우
                    else if (brotherNode.parent.findSide() == RIGHT && brotherNode.hasRedChild() == LEFT) {
                        /*
                        a) brotherNode를 기준으로 오른쪽 회전한다.
                        b) brotherNode를 brotherNode의 자식으로 바꾼다.
                        c) brotherNode(RED)(기존 자식)은 BLACK로, brotherNode의 자식(왼쪽)(기존 brotherNode)은 RED으로 바꾼다.
                        d) 1-1 case로 처리한다.
                         */
                        rotateRight(brotherNode); // a)
                        brotherNode = brotherNode.parent; // b)
                        brotherNode.setBlack(); // c)
                        brotherNode.left.setRed(); // c)
                        treatDoublyBlackOfCaseOne(postProcessNode, brotherNode, RIGHT);
                    }
                } // [2-1-2] 꺾인 경우 종료
            }
            
            // CASE 2 - 형제가 BLACK 이고 RED 자식이 없을 때
            else {
                /*
                a) postProcessNode의 부모가 자식들의 BLACK을 가져간다.
                b) postProcessNode의 부모에서 postProcessOfDelete를 호출한다.
                 */
                postProcessNode.parent.takeBlackFromChild();
                postProcessOfDelete(postProcessNode.parent);
            }
        }
        
        // CASE 3 - 형제가 RED 라면 (postProcessNode가 BLACK임이 보장된다.)
        else {
            if (postProcessNode.parent.isRed()) {
                throw new IllegalStateException("[delete][postProcess] postProcessNode의 형제가 RED인데 부모도 RED입니다.");
            }

            Node brotherNode = postProcessNode.findBrother();
            // 3-1. doubly black이 부모의 왼쪽 자식인 경우
            if (postProcessNode.findSide() == LEFT) {
                /*
                a) postProcessNode의 부모노드를 기준으로 왼쪽 회전한다.
                b) 색 교환
                    - 기존 postProcessNode의 부모노드는 BLACK -> RED
                    - 기존 형제노드는 RED -> BLACK
                c) postProcessOfDelete를 호출해서 CASE 1 또는 CASE 2로 처리되게 한다.
                 */

                rotateLeft(postProcessNode.parent); // a)
                postProcessNode.parent.setRed(); // b)
                brotherNode.setBlack(); // b)
                postProcessOfDelete(postProcessNode);
            }

            // 3-2. doubly black이 부모의 오른쪽 자식인 경우
            if (postProcessNode.findSide() == RIGHT) {
                /*
                a) postProcessNode의 부모노드를 기준으로 오른쪽 회전한다.
                b) 색 교환
                    - 기존 postProcessNode의 부모노드는 BLACK -> RED
                    - 기존 형제노드는 RED -> BLACK
                c) postProcessOfDelete를 호출해서 CASE 1 또는 CASE 2로 처리되게 한다.
                 */

                rotateRight(postProcessNode.parent); // a)
                postProcessNode.parent.setRed(); // b)
                brotherNode.setBlack(); // b)
                postProcessOfDelete(postProcessNode);
            }
        }
    }

    private void treatDoublyBlackOfCaseOne(Node postProcessNode, Node brotherNode, int side) {
        /*
        a) brotherNode의 BLACK을 자식으로 내려준다.
        b) postProcessNode의 부모 노드를 기준으로 회전한다. --> brother 노드가 서브트리 루트로 바뀐다.
            - 왼쪽으로 이어진 경우 (side == LEFT) ---> 오른쪽 회전
            - 오른쪽으로 이어진 경우 (side == RIGHT) ---> 왼쪽 회전
        c) brotherNode(RED)와 postProcessNode 부모(BLACK)의 색을 바꿔준다.
        d) postProcessNode의 부모노드 (항상 RED) 가 자식노드들의 BLACK을 가져간다.
            - 자식노드들은 BLACK 또는 Doubly Black 이므로 각각 RED로 바꾸거나 BLACK 으로 바꾼다.
            - postProcessNode의 부모노드는 BLACK이 된다.
         */
        
        brotherNode.giveBlackToChild(); // a)
        if (side == LEFT) { // b)
            rotateRight(brotherNode.parent);
        } else {
            rotateLeft(brotherNode.parent);
        }
        brotherNode.setBlack(); // c)
        postProcessNode.parent.setRed(); // c)
        if (postProcessNode.parent.takeBlackFromChild()) {
            throw new IllegalStateException("[delete][postProcess] 자식에게서 BLACK을 가져간 뒤 Doubly Black이 되었습니다.");
        }
    }

    private Node findReplaceNode(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private Node findByValue(Node node, int value) {
        if (node == null) {
            throw new IllegalArgumentException(String.format("[findByValue] %d는 트리에 존재하지 않습니다.", value));
        }

        if (node.value == value) {
            return node;
        }

        if (value < node.value) {
            return findByValue(node.left, value);
        } else {
            return findByValue(node.right, value);
        }
    }
    
    @Override
    public void rotateLeft(Node tree) {
        if (tree == Node.NIL) {
            throw new IllegalArgumentException("[rotateLeft] NIL 노드에서 회전이 일어났습니다.");
        }
        // nextCenter : 회전 후 해당 트리의 root가 될 노드
        Node nextCenter = tree.right;

        /*
        nextCenter의 왼쪽 자식이 NIL Node가 아니라면 tree Node의 오른쪽 자식으로 붙여주고
        왼쪽 자식의 부모도 tree로 바꿔준다.
         */
        if (nextCenter.left.isNilNode()) {
            tree.right = Node.NIL; // 원래 tree의 right는 nextCenter였기 때문에 반드시 바꿔주어야 한다.
        } else {
            tree.right = nextCenter.left;
            nextCenter.left.parent = tree;
        }

        /*
        1. tree의 부모와 nextCenter 간의 링크를 이어준다.
        2. tree가 전체 트리의 root 라면 root 링크를 nextCenter로 바꿔주고 nextCenter의 부모를 null 처리한다.
         */
        if (tree.parent != null) { // tree의 부모가 있다면
            if (tree.parent.left == tree) {
                tree.parent.left = nextCenter;
            } else {
                tree.parent.right = nextCenter;
            }
            nextCenter.parent = tree.parent;
        } else { // tree 가 전체 트리의 root 라면
            nextCenter.parent = null;
            root = nextCenter;
        }

        // tree와 nextCenter의 바뀐 부모 자식 관계를 재설정해준다.
        tree.parent = nextCenter;
        nextCenter.left = tree;
    }
    
    @Override
    public void rotateRight(Node tree) {
        if (tree == Node.NIL) {
            throw new IllegalArgumentException("[rotateRight] NIL 노드에서 회전이 일어났습니다.");
        }

        Node nextCenter = tree.left;

        /*
        nextCenter의 오른쪽 자식이 NIL Node가 아니라면 tree Node의 왼쪽 자식으로 붙여주고
        오른쪽 자식의 부모도 tree로 바꿔준다.
         */
        if (nextCenter.right.isNilNode()) {
            tree.left = Node.NIL;
        } else {
            tree.left = nextCenter.right;
            nextCenter.right.parent = tree;
        }

        /*
        1. tree의 부모와 nextCenter 간의 링크를 이어준다.
        2. tree가 전체 트리의 root 라면 root 링크를 nextCenter로 바꿔주고 nextCenter의 부모를 null 처리한다.
         */
        if (tree.parent != null) {
            if (tree.parent.left == tree) {
                tree.parent.left = nextCenter;
            } else {
                tree.parent.right = nextCenter;
            }
            nextCenter.parent = tree.parent;
        } else {
            nextCenter.parent = null;
            root = nextCenter;
        }

        // tree와 nextCenter의 바뀐 부모 자식 관계를 재설정해준다.
        tree.parent = nextCenter;
        nextCenter.right = tree;
    }
    
    @Override
    public int countBlack(Node tree) {
        Deque<Node> q = new ArrayDeque<>();
        q.add(tree);
        int blackCnt = 0;

        while (!q.isEmpty()) {
            Node now = q.poll();
            if (now.color == BLACK) {
                blackCnt++;
            }

            if (now.isNilNode()) {
                continue;
            }

            q.add(now.left);
            q.add(now.right);
        }

        return blackCnt;
    }
}
