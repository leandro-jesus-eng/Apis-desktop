package satb.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**Classe referente a janela Sobre.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class About 
{
    //Atributo
    private Stage subStage;
    private Text title;
    private Text description;
    private Text version;
    private Text creator;
    
    /**Construtor. */
    public About()
    {        
        title = new Text("Sistema de Análise de Trajetórias Bovinas"); 
        title.setId("title-about");
 
        description = new Text("Utiliza técnicas de análise trajetória de bovinos em um ambiente "
                + "de pastagem\n a fim de descobrir novas informações comportamentais a respeito "
                + "dos animais.");
        description.setId("description");
        
        version = new Text("Versão 1.0");
        version.setId("description");
          
        creator = new Text("Criador: Marcel Tolentino Pinheiro de Oliveira");
        creator.setId("description");
    }   
    
    /**Método que executa a janela. */
    public void execute()
    {
        subStage = new Stage();
        subStage.setTitle("Sobre o Software");  
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);      
        grid.add(version, 0, 1);
        grid.add(creator, 0, 2);
        grid.add(description, 0, 3);        
       
        Scene scene = new Scene(grid, 550, 275);         
        subStage.setScene(scene);
        scene.getStylesheets().add(About.class.getResource("lib/Commons.css").toExternalForm());
        subStage.show();
    }
    
    
    
}