package satb.view;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import satb.controller.CollarController;
import satb.controller.PastureController;
import satb.controller.SimulatorPointsController;
import satb.view.hbox.SimulatorPointsHBox;
import org.postgis.Point;

/**Classe referente a janela de Simulador de Dados Georreferenciados.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class SimulatorPointsView 
{
    //Atributos
    private Stage subStage;
    private PastureController pastureController;
    private CollarController collarController; 
    private SimulatorPointsController controller;
    private ObservableList<String> collars, pastures;
    
    //ComboBox
    private ComboBox<String> comboCollar; 
    private ComboBox<String> comboPasture; 
    
    //TextField
    TextField dateField = new TextField();
    TextField hourBeginField = new TextField();
    TextField gapTimeField = new TextField();
    TextField distBeginField = new TextField();
    TextField distEndField = new TextField();
    TextField pointsField = new TextField();
    TextField longitudeField = new TextField();
    TextField latitudeField = new TextField();
    
    //HBox
    HBox typeLine = new HBox(5);
    HBox buttons = new HBox(10);
    
    //Buttons
    private Button execButton = new Button("Criar Trajetória");
    private Button randomPointButton = new Button("Ponto Inicial Aleatório");
    private Button closeButton = new Button("Fechar");
    
    /**Construtor Default. */
    public SimulatorPointsView() throws Exception
    {
        collarController = new CollarController();
        pastureController = new PastureController();
        controller = new SimulatorPointsController();
        
        collars = collarController.selectItens();
        comboCollar = new ComboBox<>(collars);
        comboCollar.setPrefWidth(80);
        comboCollar.setPromptText("Selecione");
        
        pastures = pastureController.selectItens();
        comboPasture = new ComboBox<>(pastures);
        comboPasture.setPrefWidth(125);
        comboPasture.setPromptText("Selecione");
        
        dateField.setPrefWidth(65);
        hourBeginField.setPrefWidth(65);
        
        gapTimeField.setPrefWidth(65);
        gapTimeField.setText("00:05:00");
        distBeginField.setPrefWidth(40);
        distBeginField.setText("2.0");
        distEndField.setPrefWidth(40);
        distEndField.setText("5.0");
        pointsField.setPrefWidth(40);
        pointsField.setText("10");
        
        longitudeField.setPrefWidth(115);
        longitudeField.setText("-54.72279558017398");
        latitudeField.setPrefWidth(115);
        latitudeField.setText("-20.44294224771539");
        
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(execButton, closeButton);
        
        closeButton.setId("cancelbutton");
        execButton.setId("addbutton"); 
    
    }
   
    /**Método que executa a janela da classe. */
    public void execute() throws Exception
    {
        subStage = new Stage();
        subStage.setTitle("Simulador de Dados"); 
        
        SimulatorPointsHBox simulatorHBox = new SimulatorPointsHBox();       
        GridPane grid = simulatorHBox.createGrid();

        Text title = new Text("Simulador de Dados Georeferênciados"); 
        title.setId("maintitle");
                      
        grid.add(title, 0, 0);
        grid.add(simulatorHBox.collarLine(comboCollar, comboPasture), 0, 2);
        grid.add(simulatorHBox.timeLine(dateField, hourBeginField), 0, 3);
        grid.add(simulatorHBox.pointsLine(pointsField, gapTimeField), 0, 4);
        grid.add(new Label("Intervalo de Distâncias entre Pontos em Metros."), 0, 6);
        grid.add(simulatorHBox.intervalDistLine(distBeginField, distEndField), 0, 7);
        grid.add(new Label("Ponto Inicial da Trajetória"), 0, 9);
        grid.add(simulatorHBox.coordinateLine(longitudeField, latitudeField, randomPointButton), 0, 10);
        grid.add(buttons, 0, 12);
        
        //Botão que pega uma coordenada de um pasto.
        randomPointButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {
                try 
                {   if(comboPasture.getValue() != null)
                    {
                        Point point = controller.generatePointInPasture(comboPasture.getValue());
                        longitudeField.setText(String.valueOf(point.x));
                        latitudeField.setText(String.valueOf(point.y));
                    }    
                } 
                catch (Exception ex) 
                {
                    Logger.getLogger(SimulatorPointsView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        //Botão que executa a ação desejada
        execButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {
                try 
                {   if(comboCollar.getValue() != null && comboPasture.getValue() != null)
                    {
                        controller.createInitalPoint(longitudeField.getText(), latitudeField.getText(), dateField.getText(), hourBeginField.getText());
                        controller.simulateTrajectory(gapTimeField.getText(), Integer.parseInt(pointsField.getText()), comboPasture.getValue(), comboCollar.getValue(), distBeginField.getText(), distEndField.getText());   
                    }
                    
                } 
                catch (Exception ex) 
                {
                    Logger.getLogger(SimulatorPointsView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        //Botão que fecha a janela
        closeButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {
                subStage.close();
            }
        });
        
        Scene scene = new Scene(grid, 550, 475);
        scene.getStylesheets().add(SimulatorPointsView.class.getResource("lib/Commons.css").toExternalForm());
        subStage.setScene(scene);
        subStage.show();
    }
    
}
