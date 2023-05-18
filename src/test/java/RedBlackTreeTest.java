import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class RedBlackTreeTest {
    @Test
    void simpleTest() {
        RedBlackTree tree = new RedBlackTree();
        assertThat(tree.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("빈 트리에 원소를 추가하는 경우")
    void insertIntoEmptyTree() {
        RedBlackTree tree = new RedBlackTree();
        tree.insert(5);

        assertThat(tree.root.value).isEqualTo(5);
        assertThat(tree.root.color).isEqualTo(iNode.BLACK);
    }

    @Test
    @DisplayName("하나의 원소가 있는 트리에 원소를 추가하는 경우")

    void insertIntoTreeWithOneElement() {
        RedBlackTree tree = new RedBlackTree();
        tree.insert(5);
        tree.insert(3);

        assertThat(tree.root.value).isEqualTo(5);
        assertThat(tree.root.color).isEqualTo(iNode.BLACK);
        assertThat(tree.root.left.value).isEqualTo(3);
        assertThat(tree.root.left.color).isEqualTo(iNode.RED);
    }
}