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
                else if (node.parent.findSide() == LEFT && node.findSide() == RIGHT) {
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
