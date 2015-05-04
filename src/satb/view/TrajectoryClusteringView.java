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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import satb.controller.TrajectoryClusteringController;
import satb.model.ClusterPoints;
import satb.view.hbox.TrajectoryClusteringHBox;

/**Classe referente a janela TrajectoryClusteringView.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class TrajectoryClusteringView 
{
    private Stage subStage;
    private TrajectoryClusteringController controller;
    private ObservableList<String> data;
    
    //Caixas
    private final ComboBox<String> comboCollar, comboAlgorithm;
    
    //Label
    private final Label result = new Label ("");
    private final Label infoLabel = new Label ("Agrupamentos em Trajetórias");
 
    //RadioButton
    private RadioButton rbNoOne = new RadioButton("Nenhum");       
    private RadioButton rbInterval = new RadioButton("Intervalo");
    private RadioButton rbBefore = new RadioButton("Antes");
    private RadioButton rbAfter = new RadioButton("Depois");
    private RadioButton intervalDate = new RadioButton("Intervalo");
    private RadioButton allDate = new RadioButton("Todas");
    private RadioButton oneDate = new RadioButton("Uma");
    
    //TextField
    private TextField hourAfterField = new TextField();
    private TextField hourBeforeField = new TextField();
    private TextField hourBeginField = new TextField();
    private TextField hourEndField = new TextField();
    private TextField dateField = new TextField();
    private TextField dateBeginField = new TextField();
    private TextField dateEndField = new TextField();
    private TextField alphaField = new TextField();
    private TextField neighborsField = new TextField();
    private TextField epislonField = new TextField();
    private TextField minPtsField = new TextField();
     
    //Linhas
    private final HBox parametersLine = new HBox(5);
    
    //Botão
    private Button execute = new Button("Executar");
    private Button centroidShow = new Button("Ver Áreas");
    private Button cancel = new Button("Cancelar");
    private Button close = new Button("Fechar");
    
    
    /**Construtor. */
    public TrajectoryClusteringView() throws Exception
    {
        controller = new TrajectoryClusteringController();
         
        comboAlgorithm = new ComboBox<>(FXCollections.observableArrayList ("DBSCAN"));
        comboAlgorithm.setPromptText("Algoritmo");
        comboAlgorithm.setPrefWidth(80);
        
        data = controller.selectCollar();
        comboCollar = new ComboBox<>(data);
        comboCollar.setPromptText("Colar");
              
        dateField.setPrefWidth(60);
        dateBeginField.setPrefWidth(60);
        dateEndField.setPrefWidth(60);
        
        hourBeginField.setPrefWidth(60);
        hourEndField.setPrefWidth(60);
        hourAfterField.setPrefWidth(60);
        hourBeforeField.setPrefWidth(60);

        alphaField.setPrefWidth(45);
        alphaField.setText("4");
        neighborsField.setPrefWidth(40);
        neighborsField.setText("15");
        epislonField.setPrefWidth(45);
        epislonField.setText("20");
        minPtsField.setPrefWidth(45);
        minPtsField.setText("6");
        
        cancel.setId("cancelbutton");
        close.setId("cancelbutton");
        execute.setId("addbutton");   
        centroidShow.setId("yellowbutton");
        result.setId("label-title");
        infoLabel.setId("label-title");
    }
    
    
    /**Método de execução da função. */
    public void execute()
    { 
       subStage = new Stage();
       subStage.setTitle("Agrupamento em Trajetórias");
       TrajectoryClusteringHBox trajecHBox = new TrajectoryClusteringHBox();
       
       GridPane grid = trajecHBox.createGrid();

       //Função de seleção para o algoritmo escolhido
       comboAlgorithm.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {
                    switch(comboAlgorithm.getValue())
                    {   
                        case "DBSCAN":
                           parametersLine.getChildren().clear();  
                           parametersLine.getChildren().addAll(new Label("Distância: "), epislonField, new Label("Pts. Min. na Área: "), minPtsField);
                           parametersLine.setAlignment(Pos.BASELINE_LEFT);  
                           break; 
                
                    }                    
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(TrajectoryClusteringView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
       });

       grid.add(infoLabel, 0, 0);
       grid.add(trajecHBox.algorithmLine(comboAlgorithm), 0, 1);
       grid.add(parametersLine, 0, 2);
       grid.add(trajecHBox.collarLine(comboCollar), 0, 3);
       grid.add(trajecHBox.dateLine(allDate, oneDate, intervalDate), 0, 5);
       grid.add(trajecHBox.radioDateLine(allDate, oneDate, intervalDate, dateBeginField, dateEndField, dateField), 0, 6);
       grid.add(trajecHBox.hourLine(rbNoOne, rbInterval, rbBefore, rbAfter), 0, 7);
       grid.add(trajecHBox.radioHourLine(rbNoOne, rbInterval, rbBefore, rbAfter, hourBeforeField, hourAfterField, hourBeginField, hourEndField), 0, 8);
       grid.add(trajecHBox.buttonsLine(execute, cancel), 0, 9);
       
       //Função que executa o algoritmo com os parâmetros escolhidos
       execute.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
              try
              {
                     showClusters(comboAlgorithm.getValue());                      
              }
              catch (Exception ex) 
              {
                    Logger.getLogger(TrajectoryClusteringView.class.getName()).log(Level.SEVERE, null, ex);
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

       Scene scene = new Scene(grid, 500, 375); 
       scene.getStylesheets().add(TrajectoryClusteringView.class.getResource("lib/Commons.css").toExternalForm()); 
       subStage.setScene(scene);
       subStage.show();
    }
    
    
    /**Mostra o resultados do Algoritmo. */
    public void showClusters(final String op) throws Exception
    { 
       final Stage showStage = new Stage();
       TrajectoryClusteringHBox trajecHBox = new TrajectoryClusteringHBox();
       GridPane grid = trajecHBox.createGrid();
       
       switch(op)
       {   
           
           case "DBSCAN":
               showStage.setTitle("Agrupamento via DBSCAN");    
               controller.DBSCAN(comboCollar.getValue(), epislonField.getText(), minPtsField.getText(), dateField.getText(), dateBeginField.getText(), dateEndField.getText(), hourBeforeField.getText(), hourAfterField.getText(), hourBeginField.getText(), hourEndField.getText());
               result.setText("Clustering - DBSCAN");
               break;
             
       }
       
       HBox buttonLine = new HBox(10);
       buttonLine.getChildren().addAll(centroidShow, close);
       buttonLine.setAlignment(Pos.BASELINE_LEFT);
       
       int numberElements = controller.getNumberElements();
       
       grid.add(buttonLine, 0, 0);
       grid.add(result, 0, 2);      
       grid.add(trajecHBox.collarResultLine(comboCollar.getValue(), numberElements), 0, 3);
       grid.add(trajecHBox.timeLine(dateField.getText(), dateBeginField.getText(), dateEndField.getText(), hourBeforeField.getText(), hourAfterField.getText(), hourBeginField.getText(), hourEndField.getText()), 0, 4);
       grid.add(trajecHBox.parameterLine(op, epislonField.getText(), minPtsField.getText()), 0, 5);

       int i = 7;
       switch(op)
       { 

           case "DBSCAN":
               for(ClusterPoints c : controller.getDBSCAN())
               {
                    grid.add(trajecHBox.clusterLine(c.getClusterId() - 1), 0, i);
                    grid.add(trajecHBox.elementsInClusterLine(c.points.size(), numberElements), 0, i+1);
                    i = i + 2;
            
               }
               grid.add(trajecHBox.outlineLine(String.valueOf(controller.getClustersTotal()), String.valueOf(controller.getOutline())), 0, i+1);
               break;
                                   
       }
       
       //Botão para mostrar as áreas mais frequentemente visitadas
       centroidShow.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {     
                try 
                {
                    if(controller.isCreated())
                    {
                        switch(op)
                        {
                           case "DBSCAN":
                                controller.showDBSCAN(comboCollar.getValue());
                                break;

                            default:    
                                break;
                        }   
                    }
                }    
                catch (Exception ex) 
                {
                    Logger.getLogger(TrajectoryClusteringView.class.getName()).log(Level.SEVERE, null, ex);
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
       sp.setHbarPolicy(ScrollBarPolicy.NEVER);
       sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
       sp.setContent(grid);
      
       Scene scene = new Scene(sp, 425, 550);  
       scene.getStylesheets().add(TrajectoryClusteringView.class.getResource("lib/Results.css").toExternalForm()); 
       showStage.setScene(scene);
       showStage.show();
    }
    
}