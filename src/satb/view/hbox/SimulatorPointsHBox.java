package satb.view.hbox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**Classe organiza as HBox da SimuladorPointsView.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class SimulatorPointsHBox 
{
    /**Construtor Default. */
    public SimulatorPointsHBox(){}
    
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
    
    /**Devolve uma linha com um ComboBox de colar e de um pasto. */
    public HBox collarLine(ComboBox<String> comboCollar, ComboBox<String> comboPasture)
    {
        HBox collarLine = new HBox(5);
        collarLine.getChildren().addAll(new Label("Colar: "), comboCollar, new Label("Pasto: "), comboPasture);
        collarLine.setAlignment(Pos.BASELINE_LEFT);

        return collarLine;       
    }
    
    /**Devolve uma linha com dois textfields temporais. */
    public HBox timeLine(TextField date, TextField hourBegin)
    {
        HBox timeLine = new HBox(5);
        timeLine.getChildren().addAll(new Label("Data: "), date, new Label("Hora Inicial: "), hourBegin);
        timeLine.setAlignment(Pos.BASELINE_LEFT);

        return timeLine;       
    }
    
    /**Devolve uma linha com dois textfields: um com a qnt de pontos e o gap temporal entre os pontos. */
    public HBox pointsLine(TextField pointsBegin, TextField gapTime)
    {
        HBox pointsLine = new HBox(5);
        pointsLine.getChildren().addAll(new Label("Qnt. de Points: "), pointsBegin, new Label("Intervalo temporal entre os Pontos: "), gapTime);
        pointsLine.setAlignment(Pos.BASELINE_LEFT);

        return pointsLine;       
    }
    
    
    /**Devolve uma linha com a latitude e longitude de uma coordenada. */
    public HBox coordinateLine(TextField longitude, TextField latitude, Button generatePoint)
    {
        HBox coordinateLine = new HBox(10);
        coordinateLine.getChildren().addAll(new Label("Longitude: "), longitude, new Label("Latitude: "), latitude, generatePoint);
        coordinateLine.setAlignment(Pos.BASELINE_LEFT);

        return coordinateLine;       
    }
    
    
    /**Devolve uma linha com dois textfields temporais. */
    public HBox intervalDistLine(TextField distBegin, TextField distEnd)
    {
        HBox intervalDistLine = new HBox(5);
        intervalDistLine.getChildren().addAll(new Label("Entre "), distBegin, new Label(" e "), distEnd);
        intervalDistLine.setAlignment(Pos.BASELINE_LEFT);

        return intervalDistLine;       
    }
    
}
