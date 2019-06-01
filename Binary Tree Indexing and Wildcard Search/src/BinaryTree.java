
import java.util.*;
import java.util.stream.Stream;

/**
 * @author a node in a binary search tree
 */
class BTNode {
    BTNode left, right;
    String term;
    ArrayList<Integer> docLists;

    /**
     * Create a tree node using a term and a document list
     *
     * @param term    the term in the node
     * @param docList the ids of the documents that contain the term
     */
    public BTNode(String term, ArrayList<Integer> docList) {
        this.term = term;
        this.docLists = docList;
    }

}

/**
 * Binary search tree structure to store the term dictionary
 */
public class BinaryTree {
    ArrayList<BTNode> btWildCardFound;

    /**
     * insert a node to a subtree
     *
     * @param node  root node of a subtree
     * @param iNode the node to be inserted into the subtree
     */
    public void add(BTNode node, BTNode iNode) {
        if (iNode.term.compareTo(node.term) < 0) {
            if (node.left != null) {
                add(node.left, iNode);
            } else {
                System.out.println(" Inserted " + iNode.term +
                        " to left node " + node.term);
                node.left = iNode;
            }
        } else if (iNode.term.compareTo(node.term) > 0) {
            if (node.right != null) {
                add(node.right, iNode);
            } else {
                System.out.println(" Inserted " + iNode.term +
                        " to right of node " + node.term);
                node.right = iNode;
            }
        }
    }

    /**
     * Search a term in a subtree
     *
     * @param n   root node of a subtree
     * @param key a query term
     * @return tree nodes with term that match the query term or null if no match
     */
    public BTNode search(BTNode n, String key) {
        if (n == null) return null;
        if (n.term.compareTo(key) == 0) return n;
        else if (n.term.compareTo(key) > 0) return search(n.left, key);
        else return search(n.right, key);
    }

    /**
     * Do a wildcard search in a subtree
     *
     * @param n   the root node of a subtree
     * @param key a wild card term, e.g., class (terms like classification will be returned)
     * @return tree nodes that match the wild card
     */
    public ArrayList<BTNode> wildCardSearch(BTNode n, String key) {
        btWildCardFound = new ArrayList<BTNode>();
        BTNode root = findWildCardRoot(n, key);
        searchForWildCard(root, key, btWildCardFound);
        return btWildCardFound;
    }

    /**
     * Add all the nodes that match the wildcard to btWildCardFound list search Inorder
     *
     * @param root  root of the subtree
     * @param key   wildcard
     * @param nodes nodes that contain wild card
     */
    private void searchForWildCard(BTNode root, String key, List<BTNode> nodes) {
        if (root != null) {
            searchForWildCard(root.left, key, nodes);
            if (root.term.contains(key)) {
                nodes.add(root);
            }
            searchForWildCard(root.right, key, nodes);
        }
    }

    /**
     * This will return subtree root for term that contains the subtree
     *
     * @param n   root of actual tree
     * @param key wild card
     * @return root node of subtree
     */
    private BTNode findWildCardRoot(BTNode n, String key) {
        BTNode result = null;
        if (n == null) return null;
        if (n.term.contains(key)) return n;
        if (n.term.compareTo(key) > 0) result = findWildCardRoot(n.left, key);
        if (n.term.compareTo(key) < 0) result = findWildCardRoot(n.right, key);
        return result;
    }

    /**
     * Print the inverted index based on the increasing order of the terms in a subtree
     *
     * @param node the root node of the subtree
     */
    public void printInOrder(BTNode node) {
        if (node != null) {
            printInOrder(node.left);
            System.out.println(" Traversed " + node.term);
            Stream.of(node.docLists).forEach(s -> System.out.println(s));
            printInOrder(node.right);
        }
    }
}
