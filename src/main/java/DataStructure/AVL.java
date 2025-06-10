package DataStructure;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project4.Movie;

public class AVL {
    private AVLTreeNode root;

    public AVL() {
        root = null;
    }

    //return the height of a given node
    private int height(AVLTreeNode e) {
        if (e == null) {
            return -1;
        } else {
            return e.getHeight();
        }
    }

    public int height() {
        return height(root);
    }

    //right rotation
    private AVLTreeNode rotateWithLeftChild(AVLTreeNode k2) {
        AVLTreeNode k1 = k2.getLeft();
        k2.setLeft(k1.getRight());
        k1.setRight(k2);

        k2.setHeight(Math.max(height(k2.getLeft()), height(k2.getRight())) + 1);
        k1.setHeight(Math.max(height(k1.getLeft()), height(k2)) + 1);

        return k1;
    }

    //left rotation
    private AVLTreeNode rotateWithRightChild(AVLTreeNode k1) {
        AVLTreeNode k2 = k1.getRight();
        k1.setRight(k2.getLeft());
        k2.setLeft(k1);

        k1.setHeight(Math.max(height(k1.getLeft()), height(k1.getRight())) + 1);
        k2.setHeight(Math.max(height(k2.getRight()), height(k1)) + 1);

        return k2;
    }

    //double rotation left right
    private AVLTreeNode doubleWithLeftChild(AVLTreeNode k3) {
        k3.setLeft(rotateWithRightChild(k3.getLeft()));
        return rotateWithLeftChild(k3);
    }

    //double rotation right left
    private AVLTreeNode doubleWithRightChild(AVLTreeNode k3) {
        k3.setRight(rotateWithLeftChild(k3.getRight()));
        return rotateWithRightChild(k3);
    }

    private AVLTreeNode balance(AVLTreeNode node) {
        if (node == null) return null;

        node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);

        //calculate the balance factor
        int balanceFactor = height(node.getLeft()) - height(node.getRight());

        //if it is left heavy
        if (balanceFactor > 1) {
            //if left-left do right rotation
            if (height(node.getLeft().getLeft()) >= height(node.getLeft().getRight())) {
                node = rotateWithLeftChild(node);
            }
            //if left-right do double rotation
            else {
                node = doubleWithLeftChild(node);
            }
        }
        //if it is right heavy
        else if (balanceFactor < -1) {
            //if right-right do left rotation
            if (height(node.getRight().getRight()) >= height(node.getRight().getLeft())) {
                node = rotateWithRightChild(node);
            }
            //if right-left do double rotation
            else {
                node = doubleWithRightChild(node);
            }
        }

        return node;
    }

    private AVLTreeNode insert(Movie movie, AVLTreeNode node) {
        if (node == null) {
            return new AVLTreeNode(movie);
        }

        //get the new movie title and the old node title
        String movieTitle = movie.getTitle();
        String nodeTitle = ((Movie) node.getElement()).getTitle();

        //if the movie title is less insert left
        if (movieTitle.compareTo(nodeTitle) < 0) {
            node.setLeft(insert(movie, node.getLeft()));
        }
        //if the movie title is greater insert right
        else if (movieTitle.compareTo(nodeTitle) > 0) {
            node.setRight(insert(movie, node.getRight()));
        }
        //if the movie have the same title replace the data
        else {
            node.setElement(movie);
            return node;
        }

        return balance(node);

    }

    public void insert(Movie movie) {
        root = insert(movie, root);
    }

    private AVLTreeNode delete(String title, AVLTreeNode node) {
        if (node == null) {
            return null;
        }

        //get movie title
        String nodeTitle = ((Movie) node.getElement()).getTitle();

        //traverse the left subtree
        if (title.compareTo(nodeTitle) < 0) {
            node.setLeft(delete(title, node.getLeft()));
        }
        //traverse the right subtree
        else if (title.compareTo(nodeTitle) > 0) {
            node.setRight(delete(title, node.getRight()));
        }
        //title found
        else {
            //if node have no left child, replace with right child
            if (node.getLeft() == null) {
                return node.getRight();
            }
            //if node have no right child, replace with left child
            else if (node.getRight() == null) {
                return node.getLeft();
            }
            //node have no children
            else {
                //find max node in the left subtree
                AVLTreeNode maxNode = findMax(node.getLeft());
                //replace current node with max node data
                node.setElement(maxNode.getElement());
                //delete the duplicate node
                node.setLeft(delete(((Movie) maxNode.getElement()).getTitle(), node.getLeft()));
            }
        }

        return balance(node);
    }

    public void delete(String title) {
        root = delete(title, root);
    }

    private AVLTreeNode findMax(AVLTreeNode node) {
        //traverse to the right most node
        while (node.getRight() != null) {
            node = node.getRight();
        }
        return node;
    }

    private Movie search(String title, AVLTreeNode node) {
        if (node == null) {
            return null;
        }
        //get movie title
        String nodeTitle = ((Movie) node.getElement()).getTitle();
        //if title is less than current node title go left
        if (title.compareTo(nodeTitle) < 0) {
            return search(title, node.getLeft());
        }
        //if title is grater than current node title go right
        else if (title.compareTo(nodeTitle) > 0) {
            return search(title, node.getRight());
        }
        //title found
        else {
            return (Movie) node.getElement();
        }
    }

    public Movie search(String title) {
        return search(title, root);
    }

    public AVLTreeNode getRoot() {
        return root;
    }

    public ObservableList<Movie> getMoviesInOrder() {
        //store movies in order
        ObservableList<Movie> movies = FXCollections.observableArrayList();
        //fill list
        getMoviesInOrder(root, movies);
        return movies;
    }

    private void getMoviesInOrder(AVLTreeNode node, ObservableList<Movie> movies) {
        if (node != null) {
            //traverse left subtree
            getMoviesInOrder(node.getLeft(), movies);
            // Add the current node to the list
            movies.add((Movie) node.getElement());
            //traverse right subtree
            getMoviesInOrder(node.getRight(), movies);
        }
    }

    public Movie getTopRatedMovie() {
        if (root == null) {
            return null;
        }
        return getTopRatedMovie(root, null);
    }

    private Movie getTopRatedMovie(AVLTreeNode node, Movie topRatedMovie) {
        if (node == null) {
            return topRatedMovie;
        }
        Movie movie = (Movie) node.getElement();
        //update topRatedMovie if current movie rating is higher
        if (topRatedMovie == null || movie.getRating() > topRatedMovie.getRating()) {
            topRatedMovie = movie;
        }
        //check left subtree for higher rated movie
        topRatedMovie = getTopRatedMovie(node.getLeft(), topRatedMovie);
        //check right subtree for higher rated movie
        return getTopRatedMovie(node.getRight(), topRatedMovie);
    }

    public Movie getLeastRatedMovie() {
        if (root == null) {
            return null;
        }
        return getLeastRatedMovie(root, null);
    }

    private Movie getLeastRatedMovie(AVLTreeNode node, Movie leastRatedMovie) {
        if (node == null) {
            return leastRatedMovie;
        }
        Movie movie = (Movie) node.getElement();
        //update topRatedMovie if current movie rating is loswer
        if (leastRatedMovie == null || movie.getRating() < leastRatedMovie.getRating()) {
            leastRatedMovie = movie;
        }
        //check left subtree for lower rated movie
        leastRatedMovie = getLeastRatedMovie(node.getLeft(), leastRatedMovie);
        //check right subtree for lower rated movie
        return getLeastRatedMovie(node.getRight(), leastRatedMovie);
    }

    @Override
    public String toString() {
        return "AVL{" +
                "root=" + getMoviesInOrder() +
                '}';
    }
}