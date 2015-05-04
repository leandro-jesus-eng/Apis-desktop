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

/**Classe organiza as HBox da AreaView.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class AreaHBox 
{
    /**Construtor Default. */
    public AreaHBox(){}
    
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
    
    /**Devolve uma linha com duas comboBox (uma de pastos e outra de área). */
    public HBox searchLine(ComboBox<String> comboPasture, ComboBox<String> comboArea)
    {
        HBox searchLine = new HBox(5); 
        searchLine.getChildren().addAll(new Label("Pasto: "), comboPasture, new Label(" Área: "), comboArea);
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
    
    
    /**Devolve uma linha com textfields referentes a informações da área. */
    public HBox areaLine(TextField nameField, TextField minTimeField)
    {
       HBox areaLine = new HBox(5); 
       areaLine.getChildren().addAll(new Label("Área: "), nameField, new Label(" Tempo mínimo de permanência: "), minTimeField);
       areaLine.setAlignment(Pos.BASELINE_LEFT);
       
       return areaLine;
    }
    
    /**Devolve uma linha com as coordenadas a serem adicionadas em uma tabela. */
    public HBox addCoordinateLine(TextField longitude, TextField latitude, Button addInTable, Button addARFFInTable)
    {
        HBox addCoordinateLine = new HBox(5); 
        addCoordinateLine.getChildren().addAll(longitude, latitude, addInTable, addARFFInTable);
        addCoordinateLine.setAlignment(Pos.BASELINE_LEFT); 
        
        return addCoordinateLine;
    }
    
    
    /**Retorna uma linha com o valor do minimumTime. */
    public HBox minimumTimeLine(Text minimumTimeText)
    {
        HBox minimumTimeLine = new HBox(5); 
        minimumTimeLine.getChildren().addAll(new Label("Tempo Mínimo de Permanência: "), minimumTimeText);
        minimumTimeLine.setAlignment(Pos.BASELINE_LEFT); 
        
        return minimumTimeLine;
    }
}
