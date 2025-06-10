package project4;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EditMovieScene {
    private Stage stage;
    private Scene scene;
    private BorderPane root;

    private Text movieTitleText, descriptionText, releaseYearText, ratingText;
    private TextField titleTextField, descriptionTextField, ratingTextField;
    private DatePicker releaseYearDatePicker;

    private Button editButton, cancelButton;

    private Movie currentMovie;

    public EditMovieScene() {
        root = new BorderPane();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20));
        gridPane.setAlignment(Pos.CENTER);

        Text titleText = UIHelper.createTitleText("Edit Movie");

        HBox titleBox = new HBox(titleText);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 0, 40, 0));

        movieTitleText = UIHelper.createTitleText("Title:");
        titleTextField = UIHelper.createStyledTextField();

        descriptionText = UIHelper.createTitleText("Description:");
        descriptionTextField = UIHelper.createStyledTextField();

        releaseYearText = UIHelper.createTitleText("Release Year:");
        releaseYearDatePicker = UIHelper.createStyledDatePicker();

        ratingText = UIHelper.createTitleText("Rating:");
        ratingTextField = UIHelper.createStyledTextField();

        editButton = UIHelper.createStyledButton("Edit");
        cancelButton = UIHelper.createStyledButton("Cancel");

        gridPane.add(movieTitleText, 0, 0);
        gridPane.add(titleTextField, 1, 0);
        gridPane.add(descriptionText, 0, 1);
        gridPane.add(descriptionTextField, 1, 1);
        gridPane.add(releaseYearText, 0, 2);
        gridPane.add(releaseYearDatePicker, 1, 2);
        gridPane.add(ratingText, 0, 3);
        gridPane.add(ratingTextField, 1, 3);
        gridPane.add(editButton, 0, 4);
        gridPane.add(cancelButton, 1, 4);

        VBox vBox = new VBox(20, titleBox, gridPane);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(40));
        vBox.setStyle("-fx-background-color: white");

        root.setCenter(vBox);

        stage = new Stage();
        scene = new Scene(root, 700, 600);
        stage.setScene(scene);
        stage.setTitle("Edit Movie");
        stage.setResizable(false);

        cancelButton.setOnAction(e -> stage.close());
        editButton.setOnAction(e -> editAction());
    }

    public void setMovie(Movie movie) {
        currentMovie = movie;

        titleTextField.setText(movie.getTitle());
        descriptionTextField.setText(movie.getDescription());
        releaseYearDatePicker.setValue(LocalDate.of(movie.getReleaseYear(), 1, 1));
        ratingTextField.setText(String.valueOf(movie.getRating()));
    }

    private void editAction() {
        String title = titleTextField.getText().trim();
        String description = descriptionTextField.getText().trim();
        LocalDate releaseDate = releaseYearDatePicker.getValue();
        String ratingString = ratingTextField.getText().trim();

        if (title.isEmpty()) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Please enter a title.");
            return;
        }
        if (description.isEmpty()) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Please enter a description.");
            return;
        }
        if (releaseDate == null) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Please enter a release date.");
            return;
        }
        if (ratingString == null) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Please enter a rating.");
            return;
        }

        int year = releaseDate.getYear();
        int currentYear = LocalDate.now().getYear();
        if (year > currentYear) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Invalid year.");
            return;
        }

        double rating;
        try {
            rating = Double.parseDouble(ratingString);
            if (rating < 0 || rating > 10) {
                UIHelper.showAlert(Alert.AlertType.ERROR, "Invalid rating.");
                return;
            }
        } catch (NumberFormatException e) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Rating must be a number.");
            return;
        }

        MainView.getMovieCatalog().erase(currentMovie.getTitle());

        Movie editedMovie = new Movie(title, description, year, rating);
        MainView.getMovieCatalog().add(editedMovie);

        ViewMoviesScene.fillTable();
        UIHelper.showAlert(Alert.AlertType.INFORMATION, "Movie edited Successfully.");
        stage.close();
    }

    public void show() {
        stage.show();
    }
}
