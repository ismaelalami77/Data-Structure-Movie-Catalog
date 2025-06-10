package DataStructure;

import javafx.scene.control.Alert;
import project4.Movie;
import project4.UIHelper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MovieCatalog {
    //load factor for resizing
    private static final int TREE_LOAD_FACTOR = 3;
    private int tableSize;
    private AVL[] hashTable;

    public MovieCatalog(int tableSize) {
        allocate(tableSize);
    }

    //allocate the hashtable with the given data
    private void allocate(int size) {
        this.tableSize = nextPrime(size);
        this.hashTable = new AVL[tableSize];
    }

    //hash function to generate index from movie title
    private int hashFunction(String movieTitle) {
        //default hash value
        int hash = 0;
        //prime number to reduce collision
        int prime = 37;
        //loop through each character in the title
        for (int i = 0; i < movieTitle.length(); i++) {
            //multiply current hash by prime and add the ASCII code of it
            hash = (hash * prime + movieTitle.charAt(i)) % tableSize;
        }
        return Math.abs(hash);
    }

    //add method to take a movie and insert it in the hash table
    public void add(Movie movie) {
        //get the index from the hash function
        int index = hashFunction(movie.getTitle());
        if (hashTable[index] == null) {
            hashTable[index] = new AVL();
        }
        //insert the movie in the tree
        hashTable[index].insert(movie);

        //check if the average tree height after inserting a new movie
        //if the average is greater than the load factor the table will be resized
        if (averageTreeHeight() > TREE_LOAD_FACTOR) {
            resizeTable();
        }
    }

    //get method to retrieve movie by title
    public Movie get(String movieTitle) {
        int index = hashFunction(movieTitle);
        if (hashTable[index] == null) {
            return null;
        }
        return hashTable[index].search(movieTitle);
    }

    //erase method to delete a movie by title
    public void erase(String movieTitle) {
        //get the index from the movie title
        int index = hashFunction(movieTitle);
        //delete the movie from the tree
        if (hashTable[index] != null) {
            hashTable[index].delete(movieTitle);
        }
    }

    //read movies from file method
    public void readMovies(File file) {
        //counter for tracking errors
        int errorCount = 0;
        //check if file is found
        if (file == null) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "File Not Found");
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            //dummy data
            String title = null;
            String description = null;
            int releaseYear = 0;
            double rating = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                //check for empty lines
                if (line.isEmpty()) {
                    continue;
                }

                //check for each line content
                if (line.startsWith("Title:")) {
                    title = line.substring("Title:".length()).trim();
                } else if (line.startsWith("Description:")) {
                    description = line.substring("Description:".length()).trim();
                } else if (line.startsWith("Release Year:")) {
                    //validate that year and rating are numbers not strings
                    try {
                        releaseYear = Integer.parseInt(line.substring("Release Year:".length()).trim());
                    } catch (NumberFormatException e) {
                        errorCount++;
                    }
                } else if (line.startsWith("Rating:")) {
                    try {
                        rating = Double.parseDouble(line.substring("Rating:".length()).trim());
                    } catch (NumberFormatException e) {
                        errorCount++;
                    }

                    if (title != null && description != null &&
                            releaseYear > 0 && rating >= 0.0) {
                        Movie movie = new Movie(title, description, releaseYear, rating);
                        add(movie);
                    } else {
                        errorCount++;
                    }
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "File Not Found");
        }

        if (errorCount > 0) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "File read successfully\nerror reading: " + errorCount + " movies");
        } else {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "File read successfully");
        }

    }

    //save movies to file
    public void saveMovies(File file) {
        if (file == null) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "File Not Found");
            return;
        }
        try (PrintWriter writer = new PrintWriter(file)) {
            //loop around the whole table and print the movies
            for (AVL avl : hashTable) {
                if (avl != null) {
                    printMoviesInOrder(avl.getRoot(), writer);
                }
            }
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "File written successfully");
        } catch (IOException e) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "File Not Found");
        }
    }

    //retrieve movies from each cell in the table
    private void printMoviesInOrder(AVLTreeNode root, PrintWriter writer) {
        if (root != null) {
            //print left tree
            printMoviesInOrder(root.getLeft(), writer);
            //get the movie from the node
            Movie movie = (Movie) root.getElement();
            //print movies details
            writer.println("Title: " + movie.getTitle());
            writer.println("Description: " + movie.getDescription());
            writer.println("Release Year: " + movie.getReleaseYear());
            writer.println("Rating: " + movie.getRating());
            writer.println();
            //print right tree
            printMoviesInOrder(root.getRight(), writer);
        }

    }

    //calculate the average of trees height for rehashing the table
    private double averageTreeHeight() {
        //sum of all trees height
        int totalHeight = 0;
        //number of trees
        int numberOfTrees = 0;
        //loop the whole table
        for (AVL avl : hashTable) {
            if (avl != null) {
                //get the height of the tree add it to totalHeight
                int height = avl.height();
                totalHeight += height;
                //increment the number of trees
                numberOfTrees++;
            }
        }
        //if the table is empty return 0
        if (numberOfTrees == 0) {
            return 0;
        } else {
            //get the average height
            return (double) totalHeight / numberOfTrees;
        }
    }

    //resizes the hash table to reduce load and improve efficiency
    private void resizeTable() {
        //calculate the next prime
        int newSize = nextPrime(tableSize * 2);
        //create a new table
        AVL[] newHashTable = new AVL[newSize];
        tableSize = newHashTable.length;
        //reinsert all movies in the new table
        for (AVL avl : hashTable) {
            if (avl != null) {
                rehash(avl.getRoot(), newHashTable, newSize);
            }
        }
        //replace the old table with the new one
        hashTable = newHashTable;
    }

    //re-inserts all nodes from an old AVL tree into the new table
    private void rehash(AVLTreeNode node, AVL[] newHashTable, int newSize) {
        if (node != null) {
            //traverse the left subtree
            rehash(node.getLeft(), newHashTable, newSize);

            Movie movie = (Movie) node.getElement();
            int newIndex = hashFunction(movie.getTitle()) % newSize;

            //if there is no tree create a new one
            if (newHashTable[newIndex] == null) {
                newHashTable[newIndex] = new AVL();
            }

            //insert the movie into the avl tree
            newHashTable[newIndex].insert(movie);

            //traverse the right subtree
            rehash(node.getRight(), newHashTable, newSize);
        }
    }

    //clears the entire hash table by setting all entries to null
    public void deallocate() {
        if (hashTable != null) {
            //iterate through each slot in the hash table
            for (int i = 0; i < hashTable.length; i++) {
                //set the AVL tree at this index to null
                hashTable[i] = null;
            }
        }
    }

    //a method to get the next prime number
    private int nextPrime(int n) {
        while (!isPrime(n)) {
            n++;
        }
        return n;
    }

    //a method to check if a number is prime
    private boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    //return table size
    public int getTableSize() {
        return tableSize;
    }

    //return table
    public AVL[] getHashTable() {
        return hashTable;
    }
}
