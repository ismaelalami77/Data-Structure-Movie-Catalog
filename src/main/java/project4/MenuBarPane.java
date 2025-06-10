package project4;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuBarPane {
    MenuBar menuBar;

    Menu fileMenu, movieMenu;

    MenuItem openFileMenuItem, saveFileMenuItem, exitMenuItem;
    MenuItem addMovieMenuItem, viewMoviesMenuItem, movieByAVLMenuItem, deallocateMenuItem;

    public MenuBarPane() {
        menuBar = new MenuBar();


        //file menu
        fileMenu = new Menu("File");
        openFileMenuItem = new MenuItem("Open File");
        saveFileMenuItem = new MenuItem("Save File");
        exitMenuItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(openFileMenuItem, saveFileMenuItem, exitMenuItem);


        //movie menu
        movieMenu = new Menu("Movie");
        addMovieMenuItem = new MenuItem("Add Movie");
        viewMoviesMenuItem = new MenuItem("View Movies");
        movieByAVLMenuItem = new MenuItem("Movie By AVL");
        deallocateMenuItem = new MenuItem("Deallocate");

        movieMenu.getItems().addAll(addMovieMenuItem, viewMoviesMenuItem, movieByAVLMenuItem, deallocateMenuItem);

        menuBar.getMenus().addAll(fileMenu, movieMenu);
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }


}
