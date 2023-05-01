public class Node implements iNode{
    int value;
    Node parent;
    Node left;
    Node right;
    int color;
    boolean hasExtraBlack;
    
    public Node() {
        
        // default value
        color = RED;
        hasExtraBlack = false;
    }

    
    @Override
    public int findBrotherColor() {
        Node brother = null;
        int side = findSide();
        if(side == -1){
            brother = parent.right;
        }else if(side == 1){
            brother = parent.left;
        }

        if(brother == null){
            return 0;
        }
        return brother.color;
    }
    
    @Override
    public int findSide() {
        if(parent == null){
            return 0;
        }

        if(parent.left == this){
            return -1;
        }

        return 1;
    }
    
    @Override
    public int hasRedChild() {
        if(left != null && left.color == RED){
            if(right != null && right.color == RED){
                return 2;
            }
            return -1;
        }else if(right != null && right.color == RED){
            return 1;
        }

        return 0;
    }
}
