package satb.view.hbox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**Classe organiza as HBox da TrajectoryHBox.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class TrajectoryHBox 
{
    /**Construtor Default. */
    public TrajectoryHBox(){}
    
    /**Método que retorna um GridPane construído. */
    public GridPane createGrid()
    {
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #EEEEEE;");
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
    
        return grid;
    }
        
    /**Cria uma linha com a seleção do algoritmo. */
    public HBox algorithmLine(ComboBox<String> comboAlgorithm)
    {
       HBox algorithmLine = new HBox(5);
       algorithmLine.setAlignment(Pos.BASELINE_LEFT);
       algorithmLine.getChildren().addAll(new Label("Algoritmo:"), comboAlgorithm);
    
       return algorithmLine;
    }
    
    
    /**Cria uma linha com a seleção do colar. */
    public HBox collarLine(ComboBox<String> searchCollar)
    {
       HBox collarLine = new HBox(5);
       collarLine.setAlignment(Pos.BASELINE_LEFT);
       collarLine.getChildren().addAll(new Label("Colar:"), searchCollar);
    
       return collarLine;
    }
    
    
    /**Cria uma linha com a seleção da trajetória (colar e data). */
    public HBox trajectoryLine(ComboBox<String> comboCollar, ComboBox<String> comboDate)
    {
       HBox trajectoryLine = new HBox(5);
       trajectoryLine.setAlignment(Pos.BASELINE_LEFT);
       trajectoryLine.getChildren().addAll(new Label("Colar:"), comboCollar, new Label(" Data:"), comboDate);
    
       return trajectoryLine;
    }
    
    
    /**Cria uma linha com um comboBox de pasto. */
    public HBox pastureLine(ComboBox<String> comboPasture)
    {
       HBox pastureLine = new HBox(5);
       pastureLine.setAlignment(Pos.BASELINE_LEFT);
       pastureLine.getChildren().addAll(new Label("Pasto:"), comboPasture);
    
       return pastureLine;
    }
    
    /**Linha que devolve opções para arquivo de entrada. */
    public HBox sourceLine(RadioButton baseButton, RadioButton arffButton)
    {
        HBox sourceLine = new HBox(5);
        sourceLine.setAlignment(Pos.BASELINE_LEFT);
        
        sourceLine.getChildren().addAll(new Label("Dados de Entrada: "), baseButton, arffButton);
                
        return sourceLine;
    }
    
    
    /**Linha que devolve opções para a linha de Arquivo de Entrada. */
    public HBox radioSourceLine(RadioButton baseButton, RadioButton arffButton, final ComboBox<String> comboCollar, final ComboBox<String> comboDate, final Button arrfOpenButton, final TextField pathField)
    {
        final HBox radioSourceLine = new HBox(5);
        radioSourceLine.setAlignment(Pos.BASELINE_LEFT);
        radioSourceLine.getChildren().addAll(new Label("Colar:"), comboCollar, new Label(" Data:"), comboDate);
        final ToggleGroup tgSource = new ToggleGroup();
        
        baseButton.setOnAction(new EventHandler<ActionEvent>() 
        {
             @Override
             public void handle(ActionEvent e) 
             {
                 radioSourceLine.getChildren().clear();
                 pathField.clear();
                 radioSourceLine.getChildren().addAll(new Label("Colar:"), comboCollar, new Label(" Data:"), comboDate);
                 
             }
        });

        //Função que representa o Radio Button Depois
        arffButton.setOnAction(new EventHandler<ActionEvent>() 
        {
             @Override
             public void handle(ActionEvent e) 
             {
                radioSourceLine.getChildren().clear();
                comboCollar.setValue(null);
                comboDate.getItems().clear();
                comboDate.setValue(null);
                radioSourceLine.getChildren().addAll(arrfOpenButton, pathField);
             }
        });
       

  
        baseButton.setSelected(true);
        tgSource.getToggles().addAll(baseButton, arffButton);

        return radioSourceLine;
    }
    
}
