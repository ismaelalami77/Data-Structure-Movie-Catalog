package project4;

import DataStructure.AVL;
import DataStructure.MovieCatalog;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class MainView {
    private static MovieCatalog movieCatalog = new MovieCatalog(13);
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private MenuBarPane menuBarPane = new MenuBarPane();
    private AddMovieScene addMovieScene = new AddMovieScene();
    private ViewMoviesScene viewMoviesScene = new ViewMoviesScene();
    private MovieByTreeScene movieByTreeScene = new MovieByTreeScene();

    public MainView() {
        root = new BorderPane();

        initializeUI();

        scene = new Scene(root, 800, 600);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Movie Catalog");
        scene.getStylesheets().add(getClass().getResource("/Styles.css").toExternalForm());
        stage.setMaximized(true);
    }

    public static MovieCatalog getMovieCatalog() {
        return movieCatalog;
    }

    public void showStage() {
        stage.show();
    }

    private File fileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(stage);
    }

    private void initializeUI() {
        root.setTop(menuBarPane.getMenuBar());
        root.setCenter(addMovieScene.getRoot());

        //file menu actions
        menuBarPane.openFileMenuItem.setOnAction(e -> {
            File readFile = fileChooser(stage);
            if (readFile != null) {
                movieCatalog.readMovies(readFile);
            }
        });

        menuBarPane.saveFileMenuItem.setOnAction(e -> {
            File file = fileChooser(stage);
            if (file != null) {
                movieCatalog.saveMovies(file);
            }
        });
        menuBarPane.exitMenuItem.setOnAction(e -> stage.close());

        //movie menu actions
        menuBarPane.addMovieMenuItem.setOnAction(e -> root.setCenter(addMovieScene.getRoot()));
        menuBarPane.viewMoviesMenuItem.setOnAction(e -> {
            viewMoviesScene.fillTable();
            root.setCenter(viewMoviesScene.getRoot());
        });
        menuBarPane.movieByAVLMenuItem.setOnAction(e -> {
            root.setCenter(movieByTreeScene.getRoot());
        });
        menuBarPane.deallocateMenuItem.setOnAction(e -> {
            movieCatalog.deallocate();
            root.setCenter(addMovieScene.getRoot());
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "Data cleared successfully");
        });
    }


}
