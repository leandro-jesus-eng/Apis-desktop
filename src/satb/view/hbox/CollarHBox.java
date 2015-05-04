package satb.view.hbox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**Classe organiza as HBox da CollarView.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class CollarHBox 
{
    /**Construtor Default. */
    public CollarHBox(){}
    
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
    
    public HBox searchLine(ComboBox<String> comboCollar)
    {
        HBox searchLine = new HBox(5); 
        searchLine.getChildren().addAll(new Label("Colar: "), comboCollar);
        searchLine.setAlignment(Pos.BASELINE_LEFT);
        
        return searchLine;
    }
    
    
    public HBox collarLine(TextField collarField, TextField companyField)
    {
       HBox collarLine = new HBox(5);
       collarLine.getChildren().addAll(new Label("Colar: "), collarField, new Label(" Empresa: "), companyField);
       collarLine.setAlignment(Pos.BASELINE_LEFT); 
       
       return collarLine;
    }


}
