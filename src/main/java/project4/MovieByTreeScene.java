package project4;

import DataStructure.AVL;
import DataStructure.MovieCatalog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MovieByTreeScene {
    public static TableView moviesTableView;
    private static ObservableList<Movie> moviesObservableList = FXCollections.observableArrayList();

    private BorderPane root;
    private Button nextButton, previousButton, ratingButton;

    private Text treeHeightText, hashIndexText;
    private ComboBox<String> sortingComboBox;

    private MovieCatalog movieCatalog;
    private int currentIndex = 0;


    public MovieByTreeScene() {
        root = new BorderPane();

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(50, 50, 50, 50));
        vBox.setAlignment(Pos.CENTER);

        Text titleText = UIHelper.createTitleText("View Sorted Movies");

        VBox treeDataVBox = new VBox();
        treeDataVBox.setSpacing(10);
        treeDataVBox.setPadding(new Insets(50, 50, 50, 50));
        treeDataVBox.setAlignment(Pos.CENTER);

        treeHeightText = UIHelper.createInfoText("Tree Height: 0");
        hashIndexText = UIHelper.createInfoText("Hash Index: 0");

        sortingComboBox = UIHelper.createComboBox();
        sortingComboBox.setPromptText("Sort");
        sortingComboBox.getItems().addAll("Ascending", "Descending");

        ratingButton = UIHelper.createStyledButton("Rating");

        treeDataVBox.getChildren().addAll(treeHeightText, hashIndexText, sortingComboBox, ratingButton);

        moviesTableView = new TableView();

        TableColumn<Movie, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Movie, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Movie, Integer> releaseYearColumn = new TableColumn<>("Release Year");
        releaseYearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        TableColumn<Movie, Double> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        moviesTableView.getColumns().addAll(titleColumn, descriptionColumn, releaseYearColumn, ratingColumn);
        moviesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        moviesTableView.setItems(moviesObservableList);
        descriptionColumn.setPrefWidth(200);
        descriptionColumn.setMaxWidth(200);
        descriptionColumn.setMinWidth(300);


        HBox buttonsHBox = new HBox();
        buttonsHBox.setSpacing(10);
        buttonsHBox.setAlignment(Pos.CENTER);
        buttonsHBox.setPadding(new Insets(20, 20, 20, 20));

        nextButton = UIHelper.createStyledButton("Next");
        previousButton = UIHelper.createStyledButton("Previous");

        buttonsHBox.getChildren().addAll(previousButton, nextButton);

        vBox.getChildren().addAll(titleText, moviesTableView, buttonsHBox);

        root.setCenter(vBox);
        root.setRight(treeDataVBox);

        nextButton.setOnAction(e -> nextAction());
        previousButton.setOnAction(e -> previousAction());
        sortingComboBox.setOnAction(e -> sort());
        ratingButton.setOnAction(e -> rankedMovies());
    }

    public BorderPane getRoot() {
        return root;
    }

    public void fillTable(int index) {
        AVL avl = MainView.getMovieCatalog().getHashTable()[index];
        if (avl != null && avl.getRoot() != null) {
            moviesObservableList.setAll(avl.getMoviesInOrder());
        } else {
            moviesObservableList.clear();
        }

        int height = (avl != null) ? avl.height() : 0;

        treeHeightText.setText("Tree Height: " + height);
        hashIndexText.setText("Hash Index: " + index);

        sort();
    }


    private void sort() {
        String sortOption = sortingComboBox.getValue();
        if (sortOption == null || sortOption.isEmpty()) {
            return;
        }
        if (sortingComboBox.getValue().equals("Ascending")) {
            moviesObservableList.sort((m1, m2) -> m1.getTitle().compareToIgnoreCase(m2.getTitle()));
        } else {
            moviesObservableList.sort((m1, m2) -> m2.getTitle().compareToIgnoreCase(m1.getTitle()));
        }
        moviesTableView.refresh();
    }

    private void nextAction() {
        int tableSize = MainView.getMovieCatalog().getTableSize();
        currentIndex = (currentIndex + 1 + tableSize) % tableSize;
        fillTable(currentIndex);

    }

    private void previousAction() {
        int tableSize = MainView.getMovieCatalog().getTableSize();
        currentIndex = (currentIndex - 1 + tableSize) % tableSize;
        fillTable(currentIndex);
    }

    private void rankedMovies() {
        AVL avl = MainView.getMovieCatalog().getHashTable()[currentIndex];
        if (avl == null || avl.getRoot() == null) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "No Movies to Show");
            return;
        }

        Movie topRankedMovie = avl.getTopRatedMovie();
        Movie leastRankedMovie = avl.getLeastRatedMovie();

        String topRankedMovieString = "Top rated Movie\nTitle: " + topRankedMovie.getTitle() + "\nRating: " + topRankedMovie.getRating();
        String leastRankedMovieString = "Least rated Movie\nTitle: " + leastRankedMovie.getTitle() + "\nRating: " + leastRankedMovie.getRating();

        UIHelper.showAlert(Alert.AlertType.INFORMATION, topRankedMovieString + "\n\n" + leastRankedMovieString);
    }
}
