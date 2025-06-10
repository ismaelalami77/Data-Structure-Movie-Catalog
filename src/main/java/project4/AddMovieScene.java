package project4;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;

public class AddMovieScene {
    private BorderPane root;

    private Text movieTitleText, descriptionText, releaseYearText, ratingText;
    private TextField titleTextField, descriptionTextField, ratingTextField;
    private DatePicker releaseYearDatePicker;

    private Button addButton;

    public AddMovieScene() {
        root = new BorderPane();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20));
        gridPane.setAlignment(Pos.CENTER);

        Text titleText = UIHelper.createTitleText("Add Movie");

        HBox titleBox = new HBox(titleText);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 0, 40, 0));

        movieTitleText = UIHelper.createInfoText("Title: ");
        titleTextField = UIHelper.createStyledTextField();

        descriptionText = UIHelper.createInfoText("Description: ");
        descriptionTextField = UIHelper.createStyledTextField();

        releaseYearText = UIHelper.createInfoText("Release Year: ");
        releaseYearDatePicker = UIHelper.createStyledDatePicker();

        ratingText = UIHelper.createInfoText("Rating: ");
        ratingTextField = UIHelper.createStyledTextField();

        addButton = UIHelper.createStyledButton("Add");

        gridPane.add(movieTitleText, 0, 0);
        gridPane.add(titleTextField, 1, 0);

        gridPane.add(descriptionText, 0, 1);
        gridPane.add(descriptionTextField, 1, 1);

        gridPane.add(releaseYearText, 0, 2);
        gridPane.add(releaseYearDatePicker, 1, 2);

        gridPane.add(ratingText, 0, 3);
        gridPane.add(ratingTextField, 1, 3);

        gridPane.add(addButton, 1, 4);

        VBox vBox = new VBox(20, titleBox, gridPane);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(40));
        vBox.setStyle("-fx-background-color: white");

        root.setCenter(vBox);

        addButton.setOnAction(e -> addAction());
    }

    public BorderPane getRoot() {
        return root;
    }

    private void addAction() {
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

        Movie movie = new Movie(title, description, year, rating);
        MainView.getMovieCatalog().add(movie);
        UIHelper.showAlert(Alert.AlertType.INFORMATION, "Movie: " + title + " added successfully.");

        titleTextField.clear();
        descriptionTextField.clear();
        releaseYearDatePicker.setValue(null);
        ratingTextField.clear();
    }
}
