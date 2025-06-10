package project4;

import DataStructure.AVL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ViewMoviesScene {
    private static TableView moviesTableView;
    private static ObservableList<Movie> moviesObservableList = FXCollections.observableArrayList();

    private TextField searchTextField;
    private Button editButton, deleteButton;

    private BorderPane root;

    private EditMovieScene editMovieScene = new EditMovieScene();

    public ViewMoviesScene() {
        root = new BorderPane();

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(50, 50, 50, 50));
        vBox.setAlignment(Pos.CENTER);

        VBox buttonsVbox = new VBox();
        buttonsVbox.setSpacing(10);
        buttonsVbox.setPadding(new Insets(50, 50, 50, 50));
        buttonsVbox.setAlignment(Pos.CENTER);

        editButton = UIHelper.createStyledButton("Edit");
        deleteButton = UIHelper.createStyledButton("Delete");

        buttonsVbox.getChildren().addAll(editButton, deleteButton);

        Text titleText = UIHelper.createTitleText("View Movies");

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

        searchTextField = UIHelper.createStyledTextField();

        vBox.getChildren().addAll(titleText, moviesTableView, searchTextField);

        root.setCenter(vBox);
        root.setRight(buttonsVbox);

        editButton.setOnAction(e -> editAction());
        deleteButton.setOnAction(e -> deleteAction());
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchAction();
        });


    }

    public static void fillTable() {
        moviesObservableList.clear();
        for (int i = 0; i < MainView.getMovieCatalog().getTableSize(); i++) {
            AVL avl = MainView.getMovieCatalog().getHashTable()[i];
            if (avl != null) {
                moviesObservableList.addAll(avl.getMoviesInOrder());
            }
        }
    }

    private void searchAction() {
        String searchText = searchTextField.getText().trim().toLowerCase();

        moviesObservableList.clear();

        if (searchText.isEmpty()) {
            fillTable();
            return;
        }

        for (int i = 0; i < MainView.getMovieCatalog().getTableSize(); i++) {
            AVL avl = MainView.getMovieCatalog().getHashTable()[i];
            if (avl != null) {
                for (Movie movie : avl.getMoviesInOrder()) {
                    if (movie.getTitle().toLowerCase().contains(searchText) ||
                            String.valueOf(movie.getReleaseYear()).contains(searchText)) {
                        moviesObservableList.add(movie);
                    }
                }
            }
        }
    }

    private void editAction() {
        Movie movie = (Movie) moviesTableView.getSelectionModel().getSelectedItem();
        if (movie == null) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "No movie selected");
            return;
        }
        editMovieScene.setMovie(movie);
        editMovieScene.show();
    }

    private void deleteAction() {
        Movie movie = (Movie) moviesTableView.getSelectionModel().getSelectedItem();
        if (movie == null) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "No movie selected");
            return;
        }

        MainView.getMovieCatalog().erase(movie.getTitle());
        moviesObservableList.remove(movie);
        UIHelper.showAlert(Alert.AlertType.INFORMATION, "Movie: " + movie.getTitle() + " successfully deleted!");
    }

    public BorderPane getRoot() {
        return root;
    }

}
