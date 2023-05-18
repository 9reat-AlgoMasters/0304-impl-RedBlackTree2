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
        tree.insert(8);

        assertThat(tree.root.value).isEqualTo(5);
        assertThat(tree.root.color).isEqualTo(iNode.BLACK);

        assertThat(tree.root.left.value).isEqualTo(3);
        assertThat(tree.root.left.color).isEqualTo(iNode.RED);

        assertThat(tree.root.right.value).isEqualTo(8);
        assertThat(tree.root.right.color).isEqualTo(iNode.RED);
    }

    @Test
    @DisplayName("여러 원소를 가진 트리에 원소를 추가하는 경우")
    void insertIntoTreeWithMultipleElements() {
        RedBlackTree tree = new RedBlackTree();
        tree.insert(5);
        tree.insert(3);
        tree.insert(8);
        tree.insert(7);
        tree.insert(2);
        tree.insert(4);
        tree.insert(11);
        tree.insert(9);
        /*         5 (B)
                /      \
              3 (B)    8 (B)
            /   \      /   \
        2 (R)   4 (R) 7 (R) 11 (R)
                            /
                           9 (R)
               */

        assertThat(tree.root.value).isEqualTo(5);
        assertThat(tree.root.color).isEqualTo(iNode.BLACK);

        assertThat(tree.root.left.value).isEqualTo(3);
        assertThat(tree.root.left.color).isEqualTo(iNode.BLACK);

        assertThat(tree.root.right.value).isEqualTo(8);
        assertThat(tree.root.right.color).isEqualTo(iNode.BLACK);

        assertThat(tree.root.left.left.value).isEqualTo(2);
        assertThat(tree.root.left.left.color).isEqualTo(iNode.RED);

        assertThat(tree.root.left.right.value).isEqualTo(4);
        assertThat(tree.root.left.right.color).isEqualTo(iNode.RED);

        assertThat(tree.root.right.left.value).isEqualTo(7);
        assertThat(tree.root.right.left.color).isEqualTo(iNode.RED);

        assertThat(tree.root.right.right.value).isEqualTo(11);
        assertThat(tree.root.right.right.color).isEqualTo(iNode.RED);

        assertThat(tree.root.right.right.left.value).isEqualTo(9);
        assertThat(tree.root.right.right.left.color).isEqualTo(iNode.RED);
    }
    @Test
    @DisplayName("트리에서 검은 노드의 개수를 세는 경우")
    void countBlackNodes() {
        RedBlackTree tree = new RedBlackTree();
        tree.insert(5);
        tree.insert(3);
        tree.insert(8);
        tree.insert(7);
        tree.insert(2);
        tree.insert(4);
        tree.insert(11);
        tree.insert(9);

        int blackCount = tree.countBlack(tree.root);
        assertThat(blackCount).isEqualTo(3);
    }


}