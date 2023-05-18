import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class NodeTest {
    @Test
    @DisplayName("형제 노드의 색상 반환 테스트 - RED")
    void findBrotherColorTest() {
        Node parent = new Node(5);
        Node node = new Node(3);
        Node brother = new Node(7);

        parent.left = node;
        parent.right = brother;

        node.parent = parent;
        brother.parent = parent;

        assertThat(node.findBrotherColor()).isEqualTo(iNode.RED);
    }

    @Test
    @DisplayName("노드가 부모의 어느 쪽 자식인지 테스트 - 왼쪽")
    void findSideTest() {
        Node parent = new Node(5);
        Node node = new Node(3);

        parent.left = node;
        node.parent = parent;

        assertThat(node.findSide()).isEqualTo(-1);
    }

    @Test
    @DisplayName("노드의 자식 중 빨간색 노드가 있는지 테스트 - 양쪽 자식 모두 RED")
    void hasRedChildTest() {
        Node node = new Node(5);
        Node leftChild = new Node(3);
        Node rightChild = new Node(7);

        node.left = leftChild;
        node.right = rightChild;

        assertThat(node.hasRedChild()).isEqualTo(2);
    }
}