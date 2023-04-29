public interface iNode {
    // 상수 설정
    int RED = 1;
    int BLACK = -1;
    
    /**
     * 형제 노드의 색을 반환합니다.
     * @return RED : 1, BLACK : -1, 형제 없음 : 0
     */
    int findBrotherColor();
    
    /**
     * 현재 노드가 부모의 어느쪽 자식인지 확인합니다.
     * @return 왼쪽 자식 : -1, 오른쪽 자식 : 1, root Node(부모 X) : 0
     */
    int findSide();
    
    /**
     * 현재 노드의 자식 중 Red Node가 있는지 확인합니다.
     *
     * @return 왼쪽자식 : -1, 오른쪽 자식 : 1, 양쪽 : 2, 없음 : 0
     */
    int hasRedChild();
}
