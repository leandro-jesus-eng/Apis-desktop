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

/**Classe organiza as HBox da TrajectoryPattern.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class TrajectoryPatternHBox 
{
    /**Construtor Default. */
    public TrajectoryPatternHBox(){}
    
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
    
    /**Cria uma linha com a seleção de padrão. */
    public HBox patternLine(ComboBox<String> comboPattern)
    {
       HBox patternLine = new HBox(5);
       patternLine.setAlignment(Pos.BASELINE_LEFT);
       patternLine.getChildren().addAll(new Label("Padrão: "), comboPattern);
    
       return patternLine;
    }
    
    
    /**Cria uma linha com a seleção da trajetória (colar e data). */
    public HBox trajectoryLine(ComboBox<String> comboCollar, ComboBox<String> comboPasture)
    {
       HBox trajectoryLine = new HBox(5);
       trajectoryLine.setAlignment(Pos.BASELINE_LEFT);
       trajectoryLine.getChildren().addAll(new Label("Colar:"), comboCollar, new Label("Pasto:"), comboPasture);
    
       return trajectoryLine;
    }
    
    
    /**Cria uma linha com os parâmetros Raio da Região Alvo e Raio da Região de Desvio (ou Região de Interesse) de avoidance. */
    public HBox parameterLine(TextField radiusField, TextField radiusROIField)
    {
       HBox parameterLine = new HBox(10);
       parameterLine.getChildren().addAll(new Label("Raio: "), radiusField, new Label("Raio do Região de Desvio: "), radiusROIField);
       parameterLine.setAlignment(Pos.BASELINE_LEFT); 
       
       return parameterLine;
    }
    
    
    /**Cria uma segunda linha com os parâmetros de avoidance. */
    public HBox parameterSecondLine(TextField lengthField, TextField distanceGridField)
    {
       HBox parameterSecondLine = new HBox(10);
       parameterSecondLine.getChildren().addAll(new Label("Tam. mín. da Subtrajetória: "), lengthField, new Label("Dist. entre pts. do grid: "), distanceGridField);
       parameterSecondLine.setAlignment(Pos.BASELINE_LEFT); 
       
       return parameterSecondLine;
    }
    
    /**Retorna a linha com a informação do colar e o número de divisões do pasto. */
    public HBox collarResultLine(String collar, int size)
    {
       HBox collarResultLine = new HBox(5);
       collarResultLine.getChildren().addAll(new Label("Colar: "), new Text(collar), new Label("Divisões no Pasto:"), new Text(String.valueOf(size)));
       collarResultLine.setAlignment(Pos.BASELINE_LEFT); 
       
       return collarResultLine;
    }
    
    
    /**Retorna a linha dos parâmetros resultantes. */
    public HBox parameterResultLine(String radiusField, String radiusROIField, String length)
    {
       HBox parameterResultLine = new HBox(10);
       parameterResultLine.getChildren().addAll(new Label("Raio: "), new Text(radiusField), new Label(" Raio da Reg. de Desvio: "), new Text(radiusROIField), new Label("Tam: "), new Text(length));
       parameterResultLine.setAlignment(Pos.BASELINE_LEFT); 
       
       return parameterResultLine;
    }
    
    
    public HBox trajectoryResultLine(int index, String date)
    {
        HBox trajectoryResultLine = new HBox(5);
        trajectoryResultLine.setAlignment(Pos.BASELINE_LEFT);
        trajectoryResultLine.getChildren().addAll(new Label("Trajetória " + (index)  + ""), new Label(" Data:"), new Text(date));
        
        return trajectoryResultLine;       
    }
    
    /**Cria uma linha que retorna o valor avoidance de uma trajetória. */
    public HBox avoidanceLine(double avk)
    {
        HBox avoidanceLine = new HBox(5);
        String avoid = String.format("%.3f", avk);
        
        avoidanceLine.setAlignment(Pos.BASELINE_LEFT);
        avoidanceLine.getChildren().addAll(new Label("Avoidance: "), new Text(avoid));
        
        return avoidanceLine;       
    }
        
}
