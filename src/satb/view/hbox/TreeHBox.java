package satb.view.hbox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**Classe organiza as HBox da TreeView.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class TreeHBox 
{
    /**Construtor Default. */
    public TreeHBox(){}
    
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
    
    public HBox searchLine(ComboBox<String> comboPastureEdit, ComboBox<String> comboTreeEdit)
    {
        HBox searchLine = new HBox(5); 
        searchLine.getChildren().addAll(new Label("Pasto: "), comboPastureEdit, new Label(" Árvore: "), comboTreeEdit);
        searchLine.setAlignment(Pos.BASELINE_LEFT);
        
        return searchLine;
    }
    
    
    /**Devolve uma linha com uma comboBox de pastos. */
    public HBox pastureLine(ComboBox<String> comboPasture)
    {
       HBox pastureLine = new HBox(5); 
       pastureLine.getChildren().addAll(new Label("Pasto: "), comboPasture);
       pastureLine.setAlignment(Pos.BASELINE_LEFT);
       
       return pastureLine;
    }
    
    
    /**Devolve uma linha com uma textfield referente a identificação da árvore. */
    public HBox treeIDLine(TextField editNumberTreeField)
    {
        HBox treeIDLine = new HBox(5); 
        treeIDLine.getChildren().addAll(new Label("Número da Árvore (ID): "), editNumberTreeField);
        treeIDLine.setAlignment(Pos.BASELINE_LEFT);
        
        return treeIDLine;
    }
    
    
    /**Devolve uma linha com as coordenadas de uma árvore. */
    public HBox pointsLine(TextField latitudeField, TextField  longitudeField)
    {
        HBox pointsLine = new HBox(5); 
        pointsLine.getChildren().addAll(new Label("Longitude: "), longitudeField, new Label(" Latitude: "), latitudeField);
        pointsLine.setAlignment(Pos.BASELINE_LEFT);
        
        return pointsLine;
    }
    
}
