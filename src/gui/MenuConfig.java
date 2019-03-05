package gui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuConfig {

    public static void config(MenuBar menuBar){
        addFileMenu(menuBar);
    }

    private static void addFileMenu(MenuBar menuBar){
        Menu fileMenu = new Menu();
        fileMenu.setText("File");
        addFileMenuItem(fileMenu);
        menuBar.getMenus().add(fileMenu);
    }

    private static void addFileMenuItem(Menu fileMenu){
        MenuItem newMenuItem = new MenuItem();
        newMenuItem.setText("New");
        fileMenu.getItems().add(newMenuItem);
    }
}
