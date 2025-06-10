package project4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TableViewScene {
    public TableView tableView;
    private BorderPane root;
    private VBox vBox;
    private ObservableList<Movie> moviesObservableList = FXCollections.observableArrayList();


    public TableViewScene() {
        root = new BorderPane();


        vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(50, 50, 50, 50));
        vBox.setAlignment(Pos.CENTER);

        Text titleText = UIHelper.createTitleText("Movies Catalog");

        tableView = new TableView();
        TableColumn<Movie, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Movie, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Movie, Integer> releaseYearColumn = new TableColumn<>("Release Year");
        releaseYearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        TableColumn<Movie, Double> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        tableView.getColumns().addAll(titleColumn, descriptionColumn, releaseYearColumn, ratingColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(moviesObservableList);

        vBox.getChildren().addAll(titleText, tableView);
        root.setCenter(vBox);
    }

    public BorderPane getRoot() {
        return root;
    }

    public void fillMovies() {
        moviesObservableList.clear();

        Movie movie1 = new Movie("Movie 1", "Description 1", 2020, 8.3);
        Movie movie2 = new Movie("Movie 2", "Description 2", 2019, 9.4);

        moviesObservableList.add(movie1);
        moviesObservableList.add(movie2);

        tableView.refresh();
    }
}
