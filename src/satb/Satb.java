package satb;

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
         primaryStage.setTitle("SATB - Sistema de Análise de Trajetórias Bovinas");
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
        launch(args);
    }             
}