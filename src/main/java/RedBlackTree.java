import exceptions.CustomDuplicatedElementException;

import java.util.NoSuchElementException;

public class RedBlackTree implements iRedBlackTree{
    Node root;
    int size;


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
        //루트부터 넣을 자리 탐색
        Node node = root;
        Node parent = null;

        //넣을 자리를 찾을 때까지 반복
        while(node != null){
            //넣을 자리를 찾았을 때 node는 null이므로 그 부모 노드를 기록해놓아야 함
            parent = node;
            //넣을 값이 현재 node 값보다 작으면 왼쪽 서브트리로, 크면 오른쪽으로 이동
            if(value < node.value){
                node = node.left;
            }else if (value > node.value){
                node = node.right;
            }else{ // 해당 값이 이미 있으면 추가하지않고 끝
                throw new CustomDuplicatedElementException();
            }
        }

        Node newNode = new Node();
        newNode.value = value;
        //루트가 null인 경우 루트로 추가 그 외에는 값 비교해서 기록해 놓은 부모노드의 자식으로 추가
        if(parent == null){
            root = newNode;
            //루트는 항상 BLACK
            newNode.color = BLACK;
        }else if(value < parent.value){
            parent.left = newNode;
        }else{
            parent.right = newNode;
        }
        newNode.parent = parent;

        if(parent != null && parent.color == RED){
            //부모 RED, 부모의 형제(삼촌) RED -> recolor, 인자는 조부모
            if(parent.findBrotherColor() == RED){
                recolor(parent.parent);
            //부모 RED, 부모의 형제(삼촌) BLACK -> restructure
            }else{
                restructure(parent.parent);
            }
        }
        size++;
    }

    public void restructure(Node grandParentNode) {
        int parentSide = grandParentNode.hasRedChild();
        if (parentSide == -1) {
            //왼쪽 서브트리쪽으로 치우침, 넣을 노드가 자식의 왼쪽 : 오른쪽으로 회전 한번만
        /*     G                            P
             /                            /   \
           P                 ->          N     G
          /
         N

        왼쪽 서브트리쪽으로 치우침, 넣을 노드가 자식 오른쪽 : 자식 왼쪽 회전 후 노드 오른쪽 회전

                G                  G            N
              /                   /            / \
            P             ->    N     ->     P     G
             \                 /
              N               P
         */
            int insertedSide = grandParentNode.left.hasRedChild();
            if (insertedSide == 1) {
                rotateLeft(grandParentNode.left);
            }
            rotateRight(grandParentNode);

        } else if (parentSide == 1) {
            //오른쪽 서브트리쪽으로 치우침, 넣을 노드가 자식의 오른쪽 : 왼쪽 회전 한번만
            /*      G                          P
                      \                       /  \
                       P         ->          G    N
                         \
                          N
         오른쪽 서브트리쪽으로 치우침, 넣을 노드가 자식 왼쪽 : 자식 오른쪽 회전 후 노드 왼쪽 회전
                     G                 G           N
                       \                \         /  \
                         P       ->      N    -> G    P
                        /                 \
                       N                   P
             */
            int insertedSide = grandParentNode.right.hasRedChild();
            if (insertedSide == -1) {
                rotateRight(grandParentNode.right);
            }
            rotateLeft(grandParentNode);
        }
    }

    private void recolor(Node grandParentNode) {
        //조부모, 부모, 삼촌 노드 색 변경 -> 조부모 노드의 색이 바뀌면 그 위의 노드들 확인 필요
        grandParentNode.left.color = BLACK;
        grandParentNode.right.color = BLACK;
        if(grandParentNode.parent != null){
            grandParentNode.color = RED;
            if(grandParentNode.parent.color == RED){
                if(grandParentNode.parent.findBrotherColor() == RED){
                    recolor(grandParentNode.parent.parent);
                }else{
                    restructure(grandParentNode.parent.parent);
                }
            }
        }
    }

    @Override
    public boolean contains(int value) {
        Node node = root;

        while(node != null){
            if(value < node.value){
                node = node.left;
            }else if (value > node.value){
                node = node.right;
            }else{ // 값을 찾으면 true 리턴
                return true;
            }
        }
        return false;
    }

    public Node find(int value) {
        Node node = root;

        while(node != null){
            if(value < node.value){
                node = node.left;
            }else if (value > node.value){
                node = node.right;
            }else{ // 값을 찾으면 true 리턴
                return node;
            }
        }
        return null;
    }
    
    @Override
    public void delete(int value) {
        //value를 가진 노드(삭제할 노드) 찾기
        Node node = find(value);

        if(node != null){
            deleteNode(node);
            size--;
        }else{
            throw new NoSuchElementException();
        }
    }

    private void deleteNode(Node node) {
        //실제로 삭제되는 위치의 노드, 그 위치를 대체하는 노드
        Node deletedNode, replacedNode;
        //삭제할 노드의 자식이 없음(리프노드) => 부모노드의 자식에서 없애줌
        if(node.left == null && node.right == null){

            replacedNode = new Node();
            //루트를 삭제하는 경우
            if(node.parent == null){
                root = null;
            }else if(node.parent.left == node){
                node.parent.left = replacedNode;
            }else{
                node.parent.right = replacedNode;
            }
            deletedNode = node;

            replacedNode.parent = node.parent;
            replacedNode.color = BLACK;
            replacedNode.hasExtraBlack = true;

            //삭제할 노드가 한쪽 자식만 가짐 => 그 자식을 삭제할 노드의 부모노드의 자식으로 대체
            //루트면 그 자식이 새로운 루트가 됨
        }else if(node.right == null){
            if(node.parent == null) {
                root = node.left;
            }else if(node.parent.left == node){
                node.parent.left = node.left;
            }else{
                node.parent.right = node.left;
            }
            node.left.parent = node.parent;
            deletedNode = node;
            replacedNode = node.left;

        }else if(node.left == null){
            if(node.parent == null){
                root = node.right;
            }else if(node.parent.left == node){
                node.parent.left = node.right;
            }else{
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            deletedNode = node;
            replacedNode = node.right;


            //삭제할 노드가 2개의 자식을 가짐
        }else {
            //삭제할 노드의 왼쪽 서브트리에서 가장 큰 값 노드를 찾기 -> 실제로는 얘를 삭제
            deletedNode = findFromLeftSubTree(node);

            //deletedNode가 삭제할 노드의 왼쪽 자식이었다면 deletedNode의 왼쪽 자식을 deletedNode의 부모에(삭제할 노드 node) 붙여주면 끝
            //아니라면 deletedNode의 기존 왼쪽 자식 노드를 부모의 오른쪽 자식으로 처리 필요
            if(deletedNode.left != null){
                deletedNode.left.parent = deletedNode.parent;
                replacedNode = deletedNode.left;
            }else {
                replacedNode = new Node();
                replacedNode.parent = deletedNode.parent;
                replacedNode.color = BLACK;
                replacedNode.hasExtraBlack = true;
            }

            if(deletedNode != node.left){
                deletedNode.parent.right = replacedNode;
            }else{
                node.left = replacedNode;
            }


            //실제 삭제노드의 값을 덮어씌우기
            node.value = deletedNode.value;

        }

        //실제로 삭제되는 노드가 BLACK이면 트리 조정 필요
        if (deletedNode.color == BLACK) {
            balanceDuringDeletion(replacedNode);
        }

    }

    private void balanceDuringDeletion(Node replacedNode) {
        if(replacedNode.color == RED){
            replacedNode.color = BLACK;

        }else{
            if(replacedNode.findBrotherColor() == RED) {
                if(replacedNode.findSide() == -1){
                    rotateLeft(replacedNode.parent);
                }else{
                    rotateRight(replacedNode.parent);
                }
                balanceDuringDeletion(replacedNode);
            }else if(replacedNode.findBrotherColor() == BLACK){
                if(replacedNode.findSide() == -1){
                    int childOfBrother = replacedNode.parent.right.hasRedChild();
                    if(childOfBrother == 0){
                        replacedNode.parent.right.color = RED;
                        if(replacedNode.parent.color == RED) {
                            replacedNode.parent.color = BLACK;
                        }else{
                            balanceDuringDeletion(replacedNode.parent);
                        }
                    }else if(childOfBrother == -1){
                        rotateRight(replacedNode.parent.right);
                        balanceDuringDeletion(replacedNode);
                    }else {
                        replacedNode.parent.right.color = BLACK;
                        rotateLeft(replacedNode.parent);
                    }
                }else{
                    int childOfBrother = replacedNode.parent.left.hasRedChild();
                    if(childOfBrother == 0){
                        replacedNode.parent.left.color = RED;
                        if(replacedNode.parent.color == RED) {
                            replacedNode.parent.color = BLACK;
                        }else{
                            balanceDuringDeletion(replacedNode.parent);
                        }
                    }else if(childOfBrother == -1){
                        rotateLeft(replacedNode.parent.left);
                        balanceDuringDeletion(replacedNode);
                    }else {
                        replacedNode.parent.left.color = BLACK;
                        rotateRight(replacedNode.parent);
                    }
                }
            }
        }
        if(replacedNode.hasExtraBlack){
            if(replacedNode.parent != null) {
                if (replacedNode.findSide() == -1) {
                    replacedNode.parent.left = null;
                }else{
                    replacedNode.parent.right = null;
                }
            }
        }

    }

    public Node findFromLeftSubTree(Node node){
        //왼쪽 서브트리에서 제일 큰 값 => 오른쪽으로만 가다가 오른쪽 자식 없으면 해당 노드 리턴
        Node target = node.left;
        while(target.right != null){
            target = target.right;
        }

        return target;
    }

    @Override
    public void rotateLeft(Node tree) {
        Node temp = tree.right;
        Node temp2 = temp.left;
        if(temp2 != null){
            temp2.parent = tree;
        }

        temp.left = tree;
        temp.parent = tree.parent;
        if(temp.parent == null){
            root = temp;
        }else if(tree == temp.parent.left){
            temp.parent.left = temp;
        }else{
            temp.parent.right = temp;
        }

        tree.right = temp2;
        tree.parent = temp;

        //회전으로 부모관계가 바뀌는 두 노드의 색 변경
        int tempColor = tree.color;
        tree.color = temp.color;
        temp.color = tempColor;
    }
    
    @Override
    public void rotateRight(Node tree) {
        Node temp = tree.left;
        Node temp2 = temp.right;

        if(temp2 != null){
            temp2.parent = tree;
        }

        temp.right = tree;
        temp.parent = tree.parent;
        if(temp.parent == null){
            root = temp;
        }else if(tree == temp.parent.left){
            temp.parent.left = temp;
        }else{
            temp.parent.right = temp;
        }

        tree.left = temp2;
        tree.parent = temp;

        int tempColor = tree.color;
        tree.color = temp.color;
        temp.color = tempColor;

    }
    
    @Override
    public int countBlack(Node tree) {
        Node node = tree;
        int cnt = 0;
        while(node != null){
            node = node.left;
            if(node == null || node.color == BLACK) {
                cnt++;
            }
        }
        return cnt;

    }
}
