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

/**Classe organiza as HBox da SeeTrackView.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class SeeTrackHBox 
{
    /**Construtor Default. */
    public SeeTrackHBox(){}
    
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
    
    /**Método que retorna os botões principais. */
    public HBox buttonsLine(Button one, Button two)
    {
        HBox buttonsLine = new HBox(10);
        buttonsLine.getChildren().addAll(one, two);
        buttonsLine.setAlignment(Pos.CENTER);
    
        return buttonsLine;
    }
    
    /**Retorna uma linha com o botão para seleção do arquivo KML e KMZ e uma TextField para armazenar
     * o caminho do arquivo. */
    public HBox openButtonLine(Button openButton, TextField pathField)
    {
        
        HBox openButtonLine = new HBox(5);
        openButtonLine.setAlignment(Pos.BASELINE_LEFT);
        
        openButtonLine.getChildren().addAll(openButton, pathField);
    
        return openButtonLine;
    }
    
    
    /**Linha que devolve um número de trajetórias. */
    public HBox trackLine(ComboBox<Integer> numberTrack)
    {
        HBox trackLine = new HBox(5);
        trackLine.setAlignment(Pos.BASELINE_LEFT);
        
        trackLine.getChildren().addAll(new Label("Nº Trajetórias: "), numberTrack);
       
        return trackLine;
    }
    
    
    /**Linha que devolve um colar. */
    public HBox collarLine(ComboBox<String> comboCollar, ComboBox<String> comboDate, int i)
    {
        HBox collarLine = new HBox(5);
        collarLine.setAlignment(Pos.BASELINE_LEFT);
        
        if(i == 1)
        {   
            collarLine.getChildren().addAll(new Label("1º Colar: "), comboCollar, new Label("1º Data: "), comboDate);
        }
        else
        {
            collarLine.getChildren().addAll(new Label("2º Colar: "), comboCollar, new Label("2º Data: "), comboDate);            
        }
              
        return collarLine;
    }
    
    
    /**Linha que devolve opções para arquivo de entrada. */
    public HBox sourceLine(RadioButton kmlButton, RadioButton baseButton)
    {
        HBox sourceLine = new HBox(5);
        sourceLine.setAlignment(Pos.BASELINE_LEFT);
        
        sourceLine.getChildren().addAll(new Label("Dados de Entrada: "), kmlButton, baseButton);
                
        return sourceLine;
    }
    
    
    /**Linha que devolve opções para hora. */
    public HBox hourLine(RadioButton rbNoOne, RadioButton rbInterval, RadioButton rbBefore, RadioButton rbAfter)
    {
        HBox hourLine = new HBox(5);
        hourLine.setAlignment(Pos.BASELINE_LEFT);
        
        hourLine.getChildren().addAll(new Label("Horário:"), rbNoOne, rbInterval, rbBefore, rbAfter);
                
        return hourLine;
    }
    
    
    /**Linha que devolve opções para data.*/
    public HBox radioLine(RadioButton rbNoOne, RadioButton rbInterval, RadioButton rbBefore, RadioButton rbAfter, final TextField hourBeforeField, final TextField hourAfterField, final TextField hourBeginField, final TextField hourEndField)
    {
        final HBox radioLine = new HBox(5);
        radioLine.setAlignment(Pos.CENTER);
        final ToggleGroup tgHour = new ToggleGroup();
        
        rbNoOne.setOnAction(new EventHandler<ActionEvent>() 
        {
             @Override
             public void handle(ActionEvent e) 
             {
                 hourBeforeField.clear(); 
                 hourAfterField.clear(); 
                 hourBeginField.clear(); 
                 hourEndField.clear();
                 radioLine.getChildren().clear();                
             }
        });

        //Função que representa o Radio Button Depois
        rbAfter.setOnAction(new EventHandler<ActionEvent>() 
        {
             @Override
             public void handle(ActionEvent e) 
             {
                 hourBeforeField.clear(); 
                 hourAfterField.clear(); 
                 hourBeginField.clear(); 
                 hourEndField.clear();
                 radioLine.getChildren().clear();
                 radioLine.getChildren().addAll(new Label("Horário Depois:"), hourAfterField);                
             }
        });
       
        //Função que representa o Radio Button Intervalo Temporal
        rbInterval.setOnAction(new EventHandler<ActionEvent>() 
        {
             @Override
             public void handle(ActionEvent e) 
             {
                 hourBeforeField.clear(); 
                 hourAfterField.clear(); 
                 hourBeginField.clear(); 
                 hourEndField.clear();
                 radioLine.getChildren().clear();
                 radioLine.getChildren().addAll(new Label("Intervalo Horário:"), hourBeginField, new Label(" até "), hourEndField);
             }
        });
       
        //Função que representa o Radio Button Antes
        rbBefore.setOnAction(new EventHandler<ActionEvent>() 
        {
             @Override
             public void handle(ActionEvent e) 
             {
                 hourBeforeField.clear(); 
                 hourAfterField.clear(); 
                 hourBeginField.clear(); 
                 hourEndField.clear();
                 radioLine.getChildren().clear();
                 radioLine.getChildren().addAll(new Label("Horário Antes:"), hourBeforeField);
             }
        });
  
        rbNoOne.setSelected(true);
        tgHour.getToggles().addAll(rbNoOne, rbInterval, rbBefore, rbAfter);

        return radioLine;
    
    }
}
