# 전체 적용 상수
- `RED = 1`
- `BLACK = -1`

# Node 클래스
## 필드
- `int value`
- `Node parent`
- `Node left`
- `Node right`
- `int color` (1 : RED, -1 : BLACK)
- `boolean hasExtraBlack`

## 생성자
- default color : RED
- default hasExtraBlack : false

## 메서드
- `int findBrotherColor()`
- `int findSide()` --> 1(right), -1(left), 0(root)
- `int hasRedChild()` --> 1(right), -1(left), 0(없다.), 2(양쪽)

## 구현 참고사항
- nil 노드는 있게끔 구현

# RedBlack tree
## 필드
- `Node root`
- `int size`

## 메서드
- `boolean isEmpty()`
- `int size()`
- `void insert(int value)`
- `boolean contains(int value)`
- `void delete(int value)`
- `void rotateLeft(Node node)` --> Node 간 링크만 수정
- `void rotateRight(Node node)` --> Node 간 링크만 수정
- `int countBlack(Node tree)` --> subtree의 black 수 계산 (자기 자신 제외 + nil 노드 포함)

## 구현 참고사항
- 자식 두개 일 때 삭제하는 과정에서 왼쪽 서브트리에서 가져온다.

## Tip
- 재귀로 구현하는 것
    - insert : 형제도 red일 때
    - delete : doublyblack의 형제도 검은색일 때