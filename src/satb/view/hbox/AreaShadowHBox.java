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

/**Classe organiza as HBox da AreaShadowView.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class AreaShadowHBox 
{   
    /**Contrutor Default. */
    public AreaShadowHBox(){}
    
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
    
    /**Elabora uma linha com um comboPasture e um comboTree. */
    public HBox searchLine(ComboBox<String> comboPasture, ComboBox<String> comboTree)
    {
       HBox searchLine = new HBox(5); 
       searchLine.getChildren().addAll(new Label("Pasto: "), comboPasture, new Label(" Árvore: "), comboTree);
 
       searchLine.setAlignment(Pos.BASELINE_LEFT);
       
       return searchLine;
    }
    
    /**Elabora uma linha com um comboAreaShadow. */
    public HBox searchAreaLine(ComboBox<String> comboAreaShadow)
    {
       HBox searchAreaLine = new HBox(5); 
       searchAreaLine.getChildren().addAll(new Label("Área Sombreada: "), comboAreaShadow);
       searchAreaLine.setAlignment(Pos.BASELINE_LEFT);
       
       return searchAreaLine;
    }
    
    
    /**Devolve uma linha com as coordenadas a serem adicionadas em uma tabela. */
    public HBox intervalTimeLine(Text hourBegin, Text hourEnd)
    {
        HBox intervalTimeLine = new HBox(5); 
        intervalTimeLine.getChildren().addAll(new Label ("Intervalo Horário: "), hourBegin, new Label(" à "), hourEnd);
        intervalTimeLine.setAlignment(Pos.BASELINE_LEFT); 
        
        return intervalTimeLine;
    }
    
    
    /**Devolve uma linha com as coordenadas a serem adicionadas em uma tabela. */
    public HBox addCoordinateLine(TextField longitude, TextField latitude, Button addInTable, Button addARFFInTable)
    {
        HBox addCoordinateLine = new HBox(5); 
        addCoordinateLine.getChildren().addAll(longitude, latitude, addInTable, addARFFInTable);
        addCoordinateLine.setAlignment(Pos.BASELINE_LEFT); 
        
        return addCoordinateLine;
    }
    
    
    /**Retorna uma linha para adição do nome de Área Sombreada e o tempo mínimo. */
    public HBox nameAreaLine(TextField nameField, TextField minTimeField)
    {
        HBox nameAreaLine = new HBox(5); 
        nameAreaLine.getChildren().addAll(new Label("Área Sombreada: "), nameField, new Label("Tempo Mínimo: "), minTimeField);
        nameAreaLine.setAlignment(Pos.BASELINE_LEFT);
        
        return nameAreaLine;
    }
    
    
    /**Retorna uma linha para adição do intervalo de sombra. */
    public HBox hourIntervalLine(TextField hourBeginField, TextField hourEndField)
    {
        HBox hourIntervalLine = new HBox(5); 
        hourIntervalLine.getChildren().addAll(new Label("Inicio: "), hourBeginField, new Label(" Fim: "), hourEndField);
        hourIntervalLine.setAlignment(Pos.BASELINE_LEFT);
        
        return hourIntervalLine;
    }
}
