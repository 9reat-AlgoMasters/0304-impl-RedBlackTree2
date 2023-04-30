import java.util.ArrayDeque;
import java.util.Deque;

public class RedBlackTree implements iRedBlackTree{
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
    
    }
    
    @Override
    public boolean contains(int value) {
        return false;
    }
    
    @Override
    public void delete(int value) {
    
    }
    
    @Override
    public void rotateLeft(Node tree) {
        if (tree == Node.NIL) {
            throw new IllegalArgumentException("NIL 노드에서 회전이 일어났습니다.");
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
            throw new IllegalArgumentException("NIL 노드에서 회전이 일어났습니다.");
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
