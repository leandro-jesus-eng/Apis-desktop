package satb.view.hbox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**Classe organiza as HBox da PastureView.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class PastureHBox 
{
    /**Contrutor Default. */
    public PastureHBox(){}
    
    /**Método que retorna um GridPane construído. */
    public GridPane createGrid()
    {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
    
        return grid;
    }
    
    /**Método que retorna os botões principais. */
    public HBox buttonsLine(Button one, Button two)
    {
        HBox buttonsLine = new HBox(10);
        buttonsLine.getChildren().addAll(one, two);
        buttonsLine.setAlignment(Pos.CENTER);
    
        return buttonsLine;
    }
    
    /**Devolve uma linha com uma comboBox de pastos. */
    public HBox pastureLine(ComboBox<String> comboPasture)
    {
       HBox pastureLine = new HBox(5); 
       pastureLine.getChildren().addAll(new Label("Pasto: "), comboPasture);
       pastureLine.setAlignment(Pos.BASELINE_LEFT);
       
       return pastureLine;
    }
    
    
    /**Devolve uma linha com quantidade de hetares de um pasto. */
    public HBox hectarLine(Text areaText)
    {
       HBox hectarLine = new HBox(5); 
       hectarLine.getChildren().addAll(new Label("Hectares: "), areaText);
       hectarLine.setAlignment(Pos.BASELINE_LEFT);
       
       return hectarLine;
    }
    
    
    /**Devolve uma linha com as coordenadas a serem adicionadas em uma tabela. */
    public HBox addCoordinateLine(TextField longitude, TextField latitude, Button addInTable, Button addARFFInTable)
    {
        HBox addCoordinateLine = new HBox(5); 
        addCoordinateLine.getChildren().addAll(longitude, latitude, addInTable, addARFFInTable);
        addCoordinateLine.setAlignment(Pos.BASELINE_LEFT); 
        
        return addCoordinateLine;
    }
    
    /**Devolve uma linha com textfields referentes a informações ao pasto. */
    public HBox createLine(TextField nameField, TextField hectaresField)
    {
        HBox createLine= new HBox(5); 
        createLine.getChildren().addAll(new Label("Pasto: "), nameField, new Label(" Hectares: "), hectaresField);
        createLine.setAlignment(Pos.BASELINE_LEFT); 
        
        return createLine;
    }
    
    
}
