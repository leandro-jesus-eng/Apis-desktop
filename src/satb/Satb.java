package satb;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**Arquivo Principal.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class Satb extends Application 
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        //URLClassLoader sysLoader = (URLClassLoader) getClass().getClassLoader();
        //URL[] urls = sysLoader.getURLs();
    
         primaryStage.setTitle("SATB - Sistema de An�álise de Trajetórias Bovinas");
         GridPane grid = new GridPane();
          
         //cria a janela e define o tamanho
         Scene scene = new Scene(grid, 840, 615);
         ToolBar bar = new ToolBar();
   
         bar.getMenu().prefWidthProperty().bind(primaryStage.widthProperty());
         grid.getChildren().add(bar.getMenu());
      
         primaryStage.setScene(scene);
         scene.getStylesheets().add(Satb.class.getResource("view/lib/Mestrado.css").toExternalForm());
         primaryStage.show();
    }
    
    /**Método principal.*/
    public static void main(String[] args) 
    {
        System.out.println(Util.getCurrentRelativePath()+ File.separator);
        launch(args);
    }             
}