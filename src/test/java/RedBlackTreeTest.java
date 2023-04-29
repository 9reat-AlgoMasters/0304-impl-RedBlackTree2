import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RedBlackTreeTest {
    @Test
    void simpleTest() {
        RedBlackTree tree = new RedBlackTree();
        assertThat(tree.isEmpty()).isFalse();
    }
}