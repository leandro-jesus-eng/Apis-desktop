package satb.view;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import satb.controller.PastureController;
import satb.controller.SeeTrackController;
import satb.controller.TrajectoryPatternController;
import satb.model.Trajectory;
import satb.view.hbox.TrajectoryPatternHBox;

/**Classe referente a janela TrajectoryPatternView.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class TrajectoryPatternView
{
    private Stage subStage;
    private PastureController controller;
    private TrajectoryPatternController trajectoryController;
    private SeeTrackController seeTrackController;
    private ObservableList<String> collars, pastures;
    
    //Caixas
    private ComboBox<String> comboPasture;
    private ComboBox<String> comboCollar;
    private ComboBox<String> comboPattern;
    
    //TextField
    private TextField lengthField = new TextField();
    private TextField radiusField = new TextField();
    private TextField radiusROIField = new TextField();
    private TextField distanceGridField = new TextField();
     
    //Label
    private final Label result = new Label ("");
    private final Label infoLabel = new Label ("Padrões em Trajetórias");
    
    //Botão
    private Button execute = new Button("Executar");
    private Button mapShow = new Button("Ver Áreas");
    private Button cancel = new Button("Cancelar");
    private Button close = new Button("Fechar");
    
    
    /**Construtor. */
    public TrajectoryPatternView() throws Exception
    {
        seeTrackController = new SeeTrackController();
        trajectoryController = new TrajectoryPatternController();
        controller = new PastureController();
        
        collars = seeTrackController.selectCollar();
        comboCollar = new ComboBox<>(collars);
        comboCollar.setPromptText("Selecione");
        comboCollar.setPrefWidth(80);
        
        comboPattern = new ComboBox<>(FXCollections.observableArrayList ("Avoidance"));
        comboPattern.setPromptText("Selecione");
        
        pastures = controller.selectItens();
        comboPasture = new ComboBox<>(pastures);
        comboPasture.setPromptText("Selecione");
        
        lengthField.setPrefWidth(30);
        lengthField.setText("2");
        radiusField.setPrefWidth(30);
        radiusField.setText("5");
        radiusROIField.setPrefWidth(30);
        radiusROIField.setText("15");
        distanceGridField.setPrefWidth(30);
        distanceGridField.setText("15");
        
        
        cancel.setId("cancelbutton");
        close.setId("cancelbutton");
        execute.setId("addbutton");   
        mapShow.setId("yellowbutton");
        result.setId("label-title");
        infoLabel.setId("label-title");
    }
    
    
    /**Método de execução da função. */
    public void execute()
    { 
       subStage = new Stage();
       subStage.setTitle("Padrões em Trajetória");
       TrajectoryPatternHBox trajecHBox = new TrajectoryPatternHBox();      
       GridPane grid = trajecHBox.createGrid();
       
       grid.add(infoLabel, 0, 0);
       grid.add(trajecHBox.patternLine(comboPattern), 0, 1);
       grid.add(trajecHBox.trajectoryLine(comboCollar, comboPasture), 0, 2);
       grid.add(trajecHBox.parameterLine(radiusField, radiusROIField), 0, 3);
       grid.add(trajecHBox.parameterSecondLine(lengthField, distanceGridField), 0, 4);
       grid.add(trajecHBox.buttonsLine(execute, cancel), 0, 6);
        
       //Função que executa o algoritmo com os parâmetros escolhidos
       execute.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
              try
              {
                  if(comboPattern.getValue() != null && comboPasture.getValue() != null && comboCollar.getValue() != null)
                  {   
                    showPattern();
                  }
              }
              catch (Exception ex) 
              {
                    Logger.getLogger(TrajectoryPatternView.class.getName()).log(Level.SEVERE, null, ex);
              }     
           }
        });
       
        //Fecha a janela e cancela as funções 
        cancel.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {
               subStage.close();
            }
        });

        Scene scene = new Scene(grid, 400, 375); 
        scene.getStylesheets().add(TrajectoryPatternView.class.getResource("lib/Commons.css").toExternalForm()); 
        subStage.setScene(scene);
        subStage.show();
    }
    
    
    /**Mostra os resultados do Algoritmo. */
    public void showPattern() throws Exception
    { 
        final Stage showStage = new Stage();
        TrajectoryPatternHBox trajecHBox = new TrajectoryPatternHBox();
        GridPane grid = trajecHBox.createGrid();

        switch(comboPattern.getValue())
        {             
           case "Avoidance":
               showStage.setTitle("Padrão Avoidance");    
               trajectoryController.buildGrid(comboPasture.getValue(), comboCollar.getValue(), radiusField.getText(), radiusROIField.getText(), lengthField.getText(), distanceGridField.getText());
               result.setText("Padrão - Avoidance");
               break;     
        }

        HBox buttonLine = new HBox(10);
        buttonLine.getChildren().addAll(mapShow, close);
        buttonLine.setAlignment(Pos.BASELINE_LEFT);

        grid.add(buttonLine, 0, 0);
        grid.add(result, 0, 2);      
        grid.add(trajecHBox.collarResultLine(comboCollar.getValue(), trajectoryController.numberOfTargets()), 0, 3);
        grid.add(trajecHBox.parameterResultLine(radiusField.getText(), radiusROIField.getText(), lengthField.getText()), 0, 4);

        int i = 6;
        switch(comboPattern.getValue())
        { 
           case "Avoidance":

               for(Trajectory t : trajectoryController.getTrajectories())
               {
                   grid.add(trajecHBox.trajectoryResultLine(t.getId(), t.getDate()), 0, i+1);
                   grid.add(trajecHBox.avoidanceLine(t.getAvoidance()), 0, i+2);
                   i = i + 3;
               }
               break;                                 
        }

        //Botão para mostrar as áreas mais frequentemente visitadas
        mapShow.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {     
                try 
                {
                    trajectoryController.showTargetObject();
                }    
                catch (Exception ex) 
                {
                    Logger.getLogger(TrajectoryPatternView.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });


        //Fecha a janela 
        close.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {
              showStage.close();
            }
        });

        ScrollPane sp = new ScrollPane();
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp.setContent(grid);

        Scene scene = new Scene(sp, 425, 550);  
        scene.getStylesheets().add(TrajectoryPatternView.class.getResource("lib/Results.css").toExternalForm()); 
        showStage.setScene(scene);
        showStage.show();
    }
 
}