package satb.view;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import satb.controller.ActivityRecognitionController;
import satb.view.hbox.TrajectoryPatternHBox;

/**
* @author Leandro de Jesus
*/
public class ActivityRecognitionView
{
    private Stage subStage;
    //private PastureController controller;
        
    //TextField
    
    /*Integer degreesForSameDirection = 40;
    Integer historyLength = 4;
    Double minSpeed = 0.3;    
    Double segmentSeconds = 160.0;*/
    private TextField degreesForSameDirectionFieldA = new TextField();
    private TextField degreesForSameDirectionFieldB = new TextField();
    private TextField historyLengthFieldA = new TextField();
    private TextField historyLengthFieldB = new TextField();
    private TextField minSpeedFieldA = new TextField();
    private TextField minSpeedFieldB = new TextField();
    private TextField segmentSecondsFieldA = new TextField();
    private TextField segmentSecondsFieldB = new TextField();
    
    //private CheckBox valueA = new CheckBox();
         
    //Label    
    private final Label infoLabel = new Label ("Treinar classificador");
    
    //Botão
    private Button execute = new Button("trainClassifier");    
    private Button executeParametros = new Button("trainClassifierParametros");    
    private Button executeLomba = new Button("trainClassifierLomba");    
    
    private Button cancel = new Button("Cancelar");
    
    private ActivityRecognitionController activityRecognitionController;
    
    
    /**Construtor. */
    public ActivityRecognitionView() throws Exception
    {
        //controller = new PastureController();
        
        degreesForSameDirectionFieldA.setPrefWidth(50);
        degreesForSameDirectionFieldA.setText("10");
        degreesForSameDirectionFieldB.setPrefWidth(50);
        degreesForSameDirectionFieldB.setText("50");
        
        historyLengthFieldA.setPrefWidth(50);
        historyLengthFieldA.setText("1");
        historyLengthFieldB.setPrefWidth(50);
        historyLengthFieldB.setText("10");
        
        minSpeedFieldA.setPrefWidth(50);
        minSpeedFieldA.setText("0.1");
        minSpeedFieldB.setPrefWidth(50);
        minSpeedFieldB.setText("0.5");
        
        segmentSecondsFieldA.setPrefWidth(50);
        segmentSecondsFieldA.setText("30");
        segmentSecondsFieldB.setPrefWidth(50);
        segmentSecondsFieldB.setText("180");
        
        cancel.setId("cancelbutton");
        execute.setId("addbutton");   
        executeParametros.setId("addbutton");   
        executeLomba.setId("addbutton");   
        infoLabel.setId("label-title");
        
        activityRecognitionController = new ActivityRecognitionController();
    }
    
    
    /**Método de execução da função. */
    public void execute()
    { 
       subStage = new Stage();
       subStage.setTitle("Treinar classificador");
       TrajectoryPatternHBox trajecHBox = new TrajectoryPatternHBox();      
       GridPane grid = trajecHBox.createGrid();
       
       // coluna linha
       grid.add(infoLabel, 0, 0);
       
       HBox hbox = new HBox(5);
       hbox.setAlignment(Pos.BASELINE_LEFT);
       hbox.getChildren().addAll(new Label("degreesForSameDirectionFieldA :"), degreesForSameDirectionFieldA, new Label("degreesForSameDirectionFieldB :"), degreesForSameDirectionFieldB);
       grid.add(hbox, 0, 1);
       
       hbox = new HBox(5);
       hbox.setAlignment(Pos.BASELINE_LEFT);
       hbox.getChildren().addAll(new Label("historyLengthFieldA :"), historyLengthFieldA, new Label("historyLengthFieldB :"), historyLengthFieldB);
       grid.add(hbox, 0, 2);
       
       hbox = new HBox(5);
       hbox.setAlignment(Pos.BASELINE_LEFT);
       hbox.getChildren().addAll(new Label("minSpeedFieldA :"), minSpeedFieldA, new Label("minSpeedFieldB :"), minSpeedFieldB);
       grid.add(hbox, 0, 3);
       
       hbox = new HBox(5);
       hbox.setAlignment(Pos.BASELINE_LEFT);
       hbox.getChildren().addAll(new Label("segmentSecondsFieldA :"), segmentSecondsFieldA, new Label("segmentSecondsFieldB :"), segmentSecondsFieldB);
       grid.add(hbox, 0, 4);
       
              
       grid.add(trajecHBox.buttonsLine(execute, cancel), 0, 6);
        
       //Função que executa o algoritmo SEM os parâmetros escolhidos
       execute.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
              try
              {
                    new ActivityRecognitionController().trainClassifier();
              }
              catch (Exception ex) 
              {
                    Logger.getLogger(ActivityRecognitionView.class.getName()).log(Level.SEVERE, null, ex);
              }     
           }
        });
       
       
       //Função que executa o algoritmo com os parâmetros escolhidos
       executeParametros.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
              try
              {
                    Integer degreesForSameDirectionA = Integer.valueOf( degreesForSameDirectionFieldA.getText() );
                    Integer historyLengthA = Integer.valueOf( historyLengthFieldA.getText() );
                    Double minSpeedA = Double.valueOf( minSpeedFieldA.getText() );
                    Double segmentSecondsA = Double.valueOf( segmentSecondsFieldA.getText() );
        
                    Integer degreesForSameDirectionB = Integer.valueOf( degreesForSameDirectionFieldB.getText() );
                    Integer historyLengthB = Integer.valueOf( historyLengthFieldB.getText() );
                    Double minSpeedB = Double.valueOf( minSpeedFieldB.getText() );
                    Double segmentSecondsB = Double.valueOf( segmentSecondsFieldB.getText() );
                  
                    new ActivityRecognitionController().trainClassifier(degreesForSameDirectionA, historyLengthA, minSpeedA, segmentSecondsA,
                        degreesForSameDirectionB, historyLengthB, minSpeedB, segmentSecondsB);
                    /*if(comboPattern.getValue() != null && comboPasture.getValue() != null && comboCollar.getValue() != null)
                    {   
                        showPattern();
                    }*/
              }
              catch (Exception ex) 
              {
                    Logger.getLogger(ActivityRecognitionView.class.getName()).log(Level.SEVERE, null, ex);
              }     
           }
        });
       
       
       //Função que executa o algoritmo com os parâmetros escolhidos
       executeLomba.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
              try
              {
                    Integer degreesForSameDirectionA = Integer.valueOf( degreesForSameDirectionFieldA.getText() );
                    Integer historyLengthA = Integer.valueOf( historyLengthFieldA.getText() );
                    Double minSpeedA = Double.valueOf( minSpeedFieldA.getText() );
                    Double segmentSecondsA = Double.valueOf( segmentSecondsFieldA.getText() );
        
                    Integer degreesForSameDirectionB = Integer.valueOf( degreesForSameDirectionFieldB.getText() );
                    Integer historyLengthB = Integer.valueOf( historyLengthFieldB.getText() );
                    Double minSpeedB = Double.valueOf( minSpeedFieldB.getText() );
                    Double segmentSecondsB = Double.valueOf( segmentSecondsFieldB.getText() );
                  
                    /*activityRecognitionController.trainClassifierLomba(degreesForSameDirectionA, historyLengthA, minSpeedA, segmentSecondsA,
                        degreesForSameDirectionB, historyLengthB, minSpeedB, segmentSecondsB);*/
                    
                    activityRecognitionController.trainClassifierLomba();
                    /*if(comboPattern.getValue() != null && comboPasture.getValue() != null && comboCollar.getValue() != null)
                    {   
                        showPattern();
                    }*/
              }
              catch (Exception ex) 
              {
                    Logger.getLogger(ActivityRecognitionView.class.getName()).log(Level.SEVERE, null, ex);
              }     
           }
        });
       
       grid.add(trajecHBox.buttonsLine(executeParametros, executeLomba), 0, 8);
       
        //Fecha a janela e cancela as funções 
        cancel.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {
               subStage.close();
            }
        });

        Scene scene = new Scene(grid, 550, 375); 
        scene.getStylesheets().add(ActivityRecognitionView.class.getResource("lib/Commons.css").toExternalForm()); 
        subStage.setScene(scene);
        subStage.show();
    }
}