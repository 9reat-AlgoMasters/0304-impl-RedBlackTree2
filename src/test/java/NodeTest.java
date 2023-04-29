import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class NodeTest {
    @Test
    void simpleTest() {
        Node node = new Node();
        assertThat(node.findBrotherColor()).isEqualTo(0);
    }
}