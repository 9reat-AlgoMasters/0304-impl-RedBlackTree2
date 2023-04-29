public interface iRedBlackTree {
    // 상수 설정
    int RED = 1;
    int BLACK = -1;
    
    boolean isEmpty();
    int size();
    void insert(int value);
    boolean contains(int value);
    void delete(int value);
    void rotateLeft(Node tree);
    void rotateRight(Node tree);
    
    /**
     * 트리에 속한 BLACK Node의 개수를 반환합니다.
     * @param tree
     * @return 트리에 속한 BLACK Node의 개수
     */
    int countBlack(Node tree);
}
