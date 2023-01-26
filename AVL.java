import java.util.LinkedList;
import java.util.Queue;

public class AVL<T extends Comparable<T>> 
{
    private class Node<E> {
        E data_; // Object to be stored.
        Node<E> left, right; // Right and left subtrees.
        int height; // Distance from the farthest leaf.
        Node(E data){
            data_ = data;
            left = right = null;
            height = 0;  
        } 
    }
    private Node<T> rootNode; // Root of the tree.
    private int totalNodes; // Amount of data stored in the tree.

    AVL(){ // Prepare an empty AVL tree for use later.
        rootNode = null;
        totalNodes = 0;
    }

    /**
     * Builds a new AVL tree with a sinlge node, 
     * containg specified data.
     * @param data the data to be added to the root.
     */
    AVL(T data){
        rootNode = new Node<>(data);
        totalNodes = 1;
    }

    public int size(){
        return totalNodes;
    }

    /**
     * Iterates down the tree searching each node 
     * for the specified data. If the node's current 
     * data is less than the specified data move on to 
     * the node's right child. If the node's data is 
     * larger move on to the left child. If the node ever 
     * becomes null or contains data of an equal value 
     * return if the node equals null. The node being null 
     * means the data is not in the tree,
     * not being null means the data is inside the tree.
     * @param data the data to search for.
     * @return a boolean indicating if the data is inside the tree.
     */
    public boolean contains(T data){
        Node<T> current = rootNode;
        while(current != null && current.data_.compareTo(data) != 0)
        current = current.data_.compareTo(data) > 0 
                                     ? current.left : current.right; 
        return current != null;
    }
    
    /*
     * Iterates down a tree's right subtrees 
     * until the furthest right node is reached.
     * returns data of the right most node.
     * If the root is null return null.
     */
    public T max(){
        if(rootNode == null) return null;
        Node<T> current = rootNode;
        while(current.right != null)
            current = current.right;
        return current.data_; 
    }

    /*
     * Iterates down a tree's left subtrees 
     * until the furthest left node is reached.
     * returns data of the left most node.
     * If the root is null return null.
     */
    public T min(){
        if(rootNode == null) return null;
        Node<T> current = rootNode;
        while(current.left != null)
            current = current.left;
        return current.data_;
    }

    /**
     * Takes in an object that may be added to the tree. 
     * Stores the current amount of nodes in the tree.
     * Then attempts to add the object into the tree using helper method insert.
     * Insert will return this same tree but with the object inside it.
     * Once the tree is updated the previous size is compared with the current 
     * size. If they differ the object was added into the tree. 
     * Otherwise the object was already in the tree.
     * @param data the data to be added to the tree.
     * @return a boolean indicating if the data was added to the tree.
     */
    public boolean add(T data){
        int iniSize = totalNodes;
        rootNode = insert(rootNode, data);
        return iniSize != totalNodes;
    }
    
    /**
     * Recursively iterates down the tree comparing each node and its data 
     * with the specified data. If the specified data is of larger value than the node's data 
     * the node's right child is set to be this method passing in its right child. 
     * If its smaller, its left child is set to be this method with its left child passed. 
     * If the node's data is of equal value then simply return the node.
     * However if the current node is null create a new node containg the data and return it.
     * @param current the current node being iterated over.
     * @param data the data to be added.
     * @return recursively balances and returns every node 
     * iterated over with the additon of the added node, (assuming it was not already in the tree).
     */
    private Node<T> insert(Node<T> current, T data){
        if(current == null){ 
            current = new Node<>(data);
            totalNodes++;
        }
        else if(current.data_.compareTo(data) > 0)
            current.left = insert(current.left, data);
        else if(current.data_.compareTo(data) < 0)
            current.right = insert(current.right, data);
        
        return balance(current);
    }
    
    /**
     * Takes in an object for removal. 
     * stores the current amount of nodes the tree has.
     * Passes the root and the data to a helper method 
     * called delete. delete will return 
     * the same tree but without the object. 
     * The orignal amount of nodes is then compared
     * to the new amount. Return a boolean based on 
     * a change in node size. False if nothing was 
     * removed, otherwise true.
     * @param data the data to search for and remove.
     * @return a boolean indicating the removal of an object.
     */
    public boolean remove(T data){
        int iniSize = totalNodes;
        rootNode = delete(rootNode, data);
        return iniSize != totalNodes;
    }

    /**
     * Takes in a node and compares its data to find a 
     * node cloest in value to it. The node returned will be of 
     * its right subtree. If the node's right subtree only has 
     * a single node that node will be returned. 
     * @param node a none null node that has a right child.
     * @return the left most node of a node's right subtree.
     */
    private Node<T> inOrderSuccessor(Node<T> node){
        node = node.right;
        while(node.left != null)
              node = node.left;
        return node;
    }
    
    /**
     * Recursively iterates down a tree checking for 
     * a node containg the specified data. If the current node being examined contains 
     * data of equal value, The node is set for removal. Removal of 
     * the node depends on its subtree structure. The node having a left and right 
     * child means a successor must be found to replace the node. Once a successor 
     * is found the node's data is set to the successor's data. Then the node passes 
     * its right child and successor's data into this same method for removal.
     * If the node only has a single child return that child. If the node has no 
     * children return null. The reason for returning children or null is because whenever a 
     * node that does not contain the data is being iteratied over the data is compared to 
     * see if it has less or more value than the current node's. If the data is of higher 
     * value then the node's right child is set to be this method with its right child passed 
     * to it. If the data is of lesser value its left child is set to this method with 
     * its left child passed.
     * @param current the current node to being examined. 
     * @param data the data to be searched for and removed.
     * @return recursively balances and returns every node.
     * iterated over with the possible excetion of the node that was removed.
     */
    private Node<T> delete(Node<T> current, T data){
        if(current == null) return null;
        else if(current.data_.compareTo(data) == 0)
        {    
            if(current.left != null & current.right != null){
                current.data_ = inOrderSuccessor(current).data_;
                current.right = delete(current.right, current.data_);
            } else if(current.left != null){
                current = current.left;
                totalNodes--;
            } else if(current.right != null){
                current = current.right;
                totalNodes--;
            } else{
               totalNodes--;
               return null;
            }
        }
        else if(current.data_.compareTo(data) > 0)
            current.left = delete(current.left, data);
        else
            current.right = delete(current.right, data);
        
        return balance(current);
    }

    /**
     * Takes in a node and sets its height to be one 
     * larger than its tallest child. If the node has no 
     * children the height will be updated to zero due 
     * to null nodes having a height of -1.
     * @param node the node that will have its height updated.
     */
    private void updateHeight(Node<T> node){
        node.height = 1 + Math.max(getHeight(node.left),
                                    getHeight(node.right));
    }
    
    /**
     * Returns an integer representing the 
     * longest path of edges connecting 
     * the current node to a leaf node.
     * The node not existing implies it has a height of -1,
     * because a leaf node has a height of 0.
     * @param node the node who's height will be returned.
     * @return integer associated with a node's height.
     */
    private int getHeight(Node<T> node){
       return node == null ? -1 : node.height;
    }

    /**
     * Returns an integer calculated from the
     * difference between the root's right and left height.
     * If root is null returns 0.
     * The returning of ±1 or 0 signifies left and right heights differ by
     * at most 1. In AVL trees a height difference 
     * of 1 or 0 is considered balanced => root is balanced.
     * However if any other value is returned the tree is unbalanced.
     * @param root the root of a tree.
     * @return an integer representing the balance level of the tree. 
     */
    private int getBalance(Node<T> root){
        return root == null ? 0 : 
        getHeight(root.left) - getHeight(root.right);
    }

    /**
     * Takes in a node and updates its height to be 
     * 1 + its tallest subtree. Balance of the tree is 
     * then computed and stored. If balance is not on 
     * the interval -1 to 1 action will be needed to balance the tree. 
     * Balance being greater than 1 means the tree is 
     * unevenly weighted leaning towards the left.
     * Two options can be taken for a tree that is left leaning.  
     * The operation to be performed is determined by the 
     * heights of the left and right subtrees of the tree's left child. 
     * If the left's height is larger, a doubleLeftRight
     * rotation will be performed. Otherwise a singleRight rotation will do. 
     * If balance is less than -1 the inverse of the aformetion is done.
     * The balance falling on the interval of 1 to -1 means the tree is balanced.
     * @param current the current node, (tree/subtree) to balance. 
     * @return the balanced version of the tree with an updated height.
     */
   private Node<T> balance(Node<T> current){
      updateHeight(current);
      int balance = getBalance(current);
      if(balance > 1)
        current = getHeight(current.left.right) > getHeight(current.left.left) ? 
                            doubleLeftRight(current) : singleRight(current);
      else if(balance < -1)
        current = getHeight(current.right.left) > getHeight(current.right.right) ? 
                            doubleRightLeft(current) : singleLeft(current);
      return current;
    }
    
    /**
     * Stores the left child of node in temp, transfers
     * temp's right child to be node's left child.
     * Temp's right child is set to be node.
     * The height is then updated for node and temp.
     * It's important that node's height is updated before temp
     * because it will always be at a lower depth than temp. 
     * (temp and node will be the only nodes who's height is affected)
     * @param node The root of a tree.
     * @return The new root of the tree once shifted.
     *     |                          |                    |
     *     |             (15)=>(node) |     (9)=>(temp)    |
     *     |             /  \         |     / \            |
     *     |   (temp)<=(9)   (20)     |   (8) (15)=>(node) |
     *     |           / \            |   /   /  \         |
     *     |         (8) (11)         | (7) (11) (20)      |
     *     |         /                |                    |
     *     |       (7)                |                    |
     */                 
    private Node<T> singleRight(Node<T> node){
        Node<T> temp = node.left;
        node.left = temp.right; 
        temp.right = node;
        updateHeight(node);
        updateHeight(temp);
        return temp;
    }

    /**
     * Stores the right child of node in temp, transfers
     * temp's left child to be node's right child.
     * Temp's left child is set to be node.
     * The height is then updated for node and temp.
     * It's important that node's height is updated before temp
     * because it will always be at a lower depth than temp. 
     * (temp and node will be the only nodes who's height is affected)
     * @param node The root of a tree.
     * @return The new root of the tree once shifted.
     *    |   (10)=>(node)    |             (20)=>(temp) |
     *    |   / \             |             /  \         |
     *    | (9) (20)=>(temp)  |  (node)<=(10)  (21)      | 
     *    |     /  \          |           / \     \      | 
     *    |   (15) (21)       |         (9) (15)  (22)   |   
     *    |           \       |                          | 
     *    |          (22)     |                          | 
     */
    private Node<T> singleLeft(Node<T> node){
       Node<T> temp = node.right;
       node.right = temp.left;
       temp.left = node;
       updateHeight(node);
       updateHeight(temp);
       return temp;
    }

    /**
     * Takes in a node representing the root of an unbalanced tree.
     * Performs a left rotation on the root's left child.
     * Then performs a right rotation on the root.
     * @param node The root of a tree that's unbalanced.
     * @return The new root of the balanced tree.
     */
    private Node<T> doubleLeftRight(Node<T> node){
        node.left = singleLeft(node.left);
        return singleRight(node);
    }

    /**
     * Takes in a node representing the root of an unbalanced tree.
     * Performs a right rotation on the root's right child.
     * Then performs a left rotation on the root.
     * @param node The root of a tree that's unbalanced.
     * @return The new root of the balanced tree.
     */
    private Node<T> doubleRightLeft(Node<T> node){
        node.right = singleRight(node.right);
        return singleLeft(node);
    }

    //----------------------------------------------------------TRAVERSAL-------------------------------------------------------------------------------
    
    /**
     * Constructs a linked list of type T to store all elements inside the tree.
     * Passes rootNode along with the list to a helper method. 
     * The helper method will recursively navigate the tree in an 
     * in-order traversal, inserting the data of each node into the list. 
     * @return list containing all elements inside the tree, ascending order.
     */
    public LinkedList<T> inOrder(){
        LinkedList<T> list = new LinkedList<>();
        inOrderTraversal(rootNode, list);
        return list;
    }
    private void inOrderTraversal(Node<T> currentNode, LinkedList<T> list){
        if(currentNode == null) return;
        inOrderTraversal(currentNode.left, list);
        list.add(currentNode.data_);
        inOrderTraversal(currentNode.right, list);
    }

    /**
     * Constructs a linked list of type T to store all elements inside the tree.
     * Passes rootNode along with the list to a helper method. 
     * The helper method will recursively navigate the tree in a 
     * pre-order traversal, inserting the data of each node into the list. 
     * @return list containing all elements inside the tree, pre-order.
     */
    public LinkedList<T> preOrder(){
        LinkedList<T> list = new LinkedList<>();
        preOrderTraversal(rootNode, list);
        return list;
    }
    private void preOrderTraversal(Node<T> currentNode, LinkedList<T> list){
        if(currentNode == null) return;
        list.add(currentNode.data_);
        preOrderTraversal(currentNode.left, list);
        preOrderTraversal(currentNode.right, list);  
    }

    /**
     * Constructs a linked list of type T to store all elements inside the tree.
     * Passes rootNode along with the list to a helper method. 
     * The helper method will recursively navigate the tree in an 
     * post-order traversal, inserting the data of each node into the list. 
     * @return list containing all elements inside tree, post-order.
     */   
    public LinkedList<T> postOrder(){
        LinkedList<T> list = new LinkedList<>();
        postOrderTraversal(rootNode, list);
        return list;
    }
    private void postOrderTraversal(Node<T> currentNode, LinkedList<T> list){
        if(currentNode == null) return;
        postOrderTraversal(currentNode.left, list);
        postOrderTraversal(currentNode.right, list);
        list.add(currentNode.data_);
    }

    /**
     * Constructs a list containing lists for each level of the tree. 
     * Each sublist contains all the data of nodes of its level. If the root is null, 
     * it returns an empty list. However, if the root is none null, 
     * it adds a list containing the root's data to the original list. 
     * After the list is updated, a queue is created, and the root's children are checked. 
     * If they are none null, they are added to the queue. Once the queue is prepared, 
     * it along with the list are passed to a helper method. 
     * The helper method will recursively traverse the tree, updating the list.
     * @return list containing lists for each level of the tree. 
     * Each sublist contains all the data of its level.
     */
    public LinkedList<LinkedList<T>> levelOrder(){
        LinkedList<LinkedList<T>> list = new LinkedList<>();
        if(rootNode == null) return list;
        list.add(new LinkedList<T>());
        list.peekFirst().add(rootNode.data_);
        Queue<Node<T>> queue = new LinkedList<>();
        if(rootNode.left != null) queue.add(rootNode.left);
        if(rootNode.right != null) queue.add(rootNode.right);
        breadthFirstSearch(list, queue);
        return list;
    }

    /**
     * Recuresivly traverses a tree using the breadth-first search 
     * alogrithm. Creates a list for each level traversed adding the data of each node
     * of its level. Adds the created list into the main list. 
     * Stops once all nodes have been searched. 
     * @param list main list to stores all sublist.
     * @param queue aids in traversal.
     */
    private void breadthFirstSearch(LinkedList<LinkedList<T>> list, 
                                             Queue<Node<T>> queue){
        if(queue.isEmpty()) return;
        LinkedList<T> currentLevel = new LinkedList<>();
        for(int i = queue.size(); i > 0; i--){
            Node<T> current = queue.poll();
            if(current.left != null) queue.add(current.left);
            if(current.right != null) queue.add(current.right);
            currentLevel.add(current.data_);
        }
        list.add(currentLevel);
        breadthFirstSearch(list, queue);
    }
}
