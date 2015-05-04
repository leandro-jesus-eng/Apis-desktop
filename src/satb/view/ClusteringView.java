package satb.view;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import satb.controller.ClusteringController;
import satb.controller.SeeTrackController;
import satb.model.Clustering;
import satb.view.hbox.ClusteringHBox;

/**Classe referente a janela Agregamento/Clustering.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class ClusteringView 
{
    private Stage subStage;
    private SeeTrackController controller;
    private ClusteringController clustering;
    private ObservableList<String> data, collars;
    private List<String> collarList = new ArrayList<>();
    private String collar = "";
    
    //Caixas
    private ChoiceBox<String> choiceCluster;
    private final ComboBox<String> comboCollar, comboAlgorithm;
    
    //Label
    private final Label result = new Label ("");
 
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
    private TextField epislonField = new TextField();
    private TextField minPtsField = new TextField();
    private TextField alphaField = new TextField();
    private TextField neighborsField = new TextField();
     
    //Linhas
    private final HBox algorithmLine = new HBox(5);
    private final HBox parametersLine = new HBox(5);
    private final HBox firstLine = new HBox(5);
    private final HBox secondLine = new HBox(5);
    private final HBox clusteringButtons = new HBox(10);
    
    //Botão
    private Button execute = new Button("Executar");
    private Button clearList = new Button("Limpa Colares Selecionados");
    private Button centroidShow = new Button("Ver Áreas");
    private Button cancel = new Button("Cancelar");
    private Button close = new Button("Fechar");
    
    
    /**Construtor. */
    public ClusteringView() throws Exception
    {
        controller = new SeeTrackController();
        clustering = new ClusteringController();
         
        comboAlgorithm = new ComboBox<>(FXCollections.observableArrayList ("DBSCAN","K-Means","LSDBC","PAM"));
        comboAlgorithm.setPromptText("Algoritmo");
        comboAlgorithm.setPrefWidth(80);
        
        data = controller.selectCollar();
        comboCollar = new ComboBox<>(data);
        comboCollar.setPromptText("Colar");
         
        choiceCluster = new ChoiceBox<>(FXCollections.observableArrayList ("1", "2", "3", "4", "5", "6", "7", "8","9","10", "11", "12", "13", "14", "15"));
        choiceCluster.setValue("3"); //valor default do choice box
              
        dateField.setPrefWidth(60);
        dateBeginField.setPrefWidth(60);
        dateEndField.setPrefWidth(60);
        
        hourBeginField.setPrefWidth(60);
        hourEndField.setPrefWidth(60);
        hourAfterField.setPrefWidth(60);
        hourBeforeField.setPrefWidth(60);
        
        epislonField.setPrefWidth(45);
        epislonField.setText("0.000135");
        minPtsField.setPrefWidth(40);
        minPtsField.setText("6");
        
        alphaField.setPrefWidth(45);
        alphaField.setText("4");
        neighborsField.setPrefWidth(40);
        neighborsField.setText("15");
        
        cancel.setId("cancelbutton");
        close.setId("cancelbutton");
        execute.setId("addbutton");   
        centroidShow.setId("yellowbutton");
        result.setId("label-title");
    }
    
    
    /**Método de execução da função. */
    public void execute()
    { 
       subStage = new Stage();
       subStage.setTitle("Algoritmos de Agrupamento (Clustering)");
       ClusteringHBox clusterHBox = new ClusteringHBox();
       
       GridPane grid = clusterHBox.createGrid();
       
       algorithmLine.getChildren().addAll(new Label("Selecione o Algoritmo: "), comboAlgorithm);
       algorithmLine.setAlignment(Pos.BASELINE_LEFT);
       
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
                           parametersLine.getChildren().addAll(new Label("Epsilon: "), epislonField, new Label("Pts. Min. no Cluster: "), minPtsField);
                           parametersLine.setAlignment(Pos.BASELINE_LEFT);  
                           break; 
                        
                        case "K-Means":
                           parametersLine.getChildren().clear();  
                           parametersLine.getChildren().addAll(new Label("N° Clusters: "), choiceCluster);
                           parametersLine.setAlignment(Pos.BASELINE_LEFT); 
                           break;
                        
                        case "LSDBC":
                           parametersLine.getChildren().clear();  
                           parametersLine.getChildren().addAll(new Label("Alpha: "), alphaField, new Label("N. de Vizinhos: "), neighborsField);
                           parametersLine.setAlignment(Pos.BASELINE_LEFT);  
                           break;
                            
                        case "PAM":
                           parametersLine.getChildren().clear(); 
                           parametersLine.getChildren().addAll(new Label("N° Clusters: "), choiceCluster);
                           parametersLine.setAlignment(Pos.BASELINE_LEFT); 
                           break;  
                            
                    }
                    
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(ClusteringView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
       });
       
       //Função para seleção do collar
       comboCollar.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
               try 
               {
                     if(!collarList.contains(comboCollar.getValue()))
                     {
                        collar = collar.concat(comboCollar.getValue() + " ");
                        collarList.add(comboCollar.getValue());
                        secondLine.getChildren().clear();
                        secondLine.getChildren().addAll(new Label("Colares Selecionados: "), new Text(collar));
                     }
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(ClusteringView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
       });
       
       firstLine.getChildren().addAll(new Label("Colar: "), comboCollar, clearList);
       firstLine.setAlignment(Pos.BASELINE_LEFT);
       
       secondLine.getChildren().addAll(new Label("Colares Selecionados: "), new Text(collar));
       secondLine.setAlignment(Pos.BASELINE_LEFT);
         
       clusteringButtons.getChildren().addAll(execute, cancel);
       clusteringButtons.setAlignment(Pos.CENTER); 
       
       grid.add(algorithmLine, 0, 0);
       grid.add(parametersLine, 0, 1);
       grid.add(new Label("-------------------------------------------------------"), 0, 3);
       grid.add(firstLine, 0, 5);
       grid.add(secondLine, 0, 6);
       grid.add(clusterHBox.dateLine(allDate, oneDate, intervalDate), 0, 8);
       grid.add(clusterHBox.radioDateLine(allDate, oneDate, intervalDate, dateBeginField, dateEndField, dateField), 0, 9);
       grid.add(clusterHBox.hourLine(rbNoOne, rbInterval, rbBefore, rbAfter), 0, 10);
       grid.add(clusterHBox.radioHourLine(rbNoOne, rbInterval, rbBefore, rbAfter, hourBeforeField, hourAfterField, hourBeginField, hourEndField), 0, 11);
       grid.add(clusteringButtons, 0, 14);
       
       //Função que limpa as listas de colares selecionados
       clearList.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
              try
              {
                    collar = "";
                    collarList.clear();
                    secondLine.getChildren().clear();
                    secondLine.getChildren().addAll(new Label("Colares Selecionados: "), new Text(collar));
              }
              catch (Exception ex) 
              {
                    Logger.getLogger(ClusteringView.class.getName()).log(Level.SEVERE, null, ex);
              }     
           }
        });
       
       
       //Função que executa o algoritmo com os parâmetros escolhidos
       execute.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
              try
              {
                 if(!collarList.isEmpty())
                 {
                     collars = FXCollections.observableList(collarList); 
                     FXCollections.sort(collars);
                     showClusters(comboAlgorithm.getValue());
                 }     
              }
              catch (Exception ex) 
              {
                    Logger.getLogger(ClusteringView.class.getName()).log(Level.SEVERE, null, ex);
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

       Scene scene = new Scene(grid, 450, 350); 
       scene.getStylesheets().add(ClusteringView.class.getResource("lib/Commons.css").toExternalForm()); 
       subStage.setScene(scene);
       subStage.show();
    }
    
    
    /**Mostra o resultados do Algoritmo.*/
    public void showClusters(final String op) throws Exception
    { 
       final Stage showStage = new Stage();
       ClusteringHBox clusterHBox = new ClusteringHBox();
       
       switch(op)
       {
           case "DBSCAN":
               showStage.setTitle("Agrupamento via DBSCAN");    
               clustering.DBSCAN(collars, epislonField.getText(), minPtsField.getText(), dateField.getText(), dateBeginField.getText(), dateEndField.getText(), hourBeforeField.getText(), hourAfterField.getText(), hourBeginField.getText(), hourEndField.getText());
               result.setText("Clustering - DBSCAN");
               break;
           
           case "K-Means":
               showStage.setTitle("Agrupamento via K-Means");    
               clustering.kMeans(collars, choiceCluster.getValue(), dateField.getText(), dateBeginField.getText(), dateEndField.getText(), hourBeforeField.getText(), hourAfterField.getText(), hourBeginField.getText(), hourEndField.getText());
               result.setText("Clustering - K-Means");
               break;
               
           case "LSDBC":
               showStage.setTitle("Agrupamento via LSDBC");    
               clustering.LSDBC(collars, alphaField.getText(), neighborsField.getText(), dateField.getText(), dateBeginField.getText(), dateEndField.getText(), hourBeforeField.getText(), hourAfterField.getText(), hourBeginField.getText(), hourEndField.getText());
               result.setText("Clustering - LSDBC");
               break;
               
           case "PAM":
               showStage.setTitle("Agrupamento via PAM");    
               clustering.PAM(collars, choiceCluster.getValue(), dateField.getText(), dateBeginField.getText(), dateEndField.getText(), hourBeforeField.getText(), hourAfterField.getText(), hourBeginField.getText(), hourEndField.getText());
               result.setText("Clustering - PAM");
               break;
       }

       GridPane grid = clusterHBox.createGrid();
       
       HBox buttonLine = new HBox(10);
       buttonLine.getChildren().addAll(centroidShow, close);
       buttonLine.setAlignment(Pos.BASELINE_LEFT);
       
       grid.add(buttonLine, 0, 0);
       grid.add(result, 0, 2);      
       grid.add(clusterHBox.collarLine(collars, String.valueOf(clustering.getNumberElements())), 0, 3);
       grid.add(clusterHBox.timeLine(dateField.getText(), dateBeginField.getText(), dateEndField.getText(), hourBeforeField.getText(), hourAfterField.getText(), hourBeginField.getText(), hourEndField.getText()), 0, 4);
       grid.add(clusterHBox.parameterLine(op, choiceCluster.getValue() , epislonField.getText(), alphaField.getText(), minPtsField.getText(), neighborsField.getText()), 0, 5);

       int numberTotal = clustering.getNumberElements();
       
       int i = 8;
       switch(op)
       {
           case "DBSCAN":
               for(Clustering c : clustering.getClusters())
               {
                    grid.add(clusterHBox.clusterLine(c.getClusterId() - 1), 0, i);
                    grid.add(clusterHBox.elementsInClusterLine(c.getSize(), numberTotal), 0, i+1);
                    i = i + 3;
            
               }
               grid.add(clusterHBox.outlineLine(String.valueOf(clustering.getClustersTotal()), String.valueOf(clustering.getOutline())), 0, i+1);
               break;
        
               
           case "K-Means":              
               for(Clustering c : clustering.getClusters())
               {
                    grid.add(clusterHBox.clusterLine(c.getClusterId() - 1), 0, i); 
                    grid.add(clusterHBox.elementsInClusterLine(c.getSize(), numberTotal), 0, i+1);
                    grid.add(clusterHBox.clusterCenterLine("K-Means", c.getCentroid()), 0, i+2);

                    i = i + 5;
               }
               grid.add(clusterHBox.outlineLine(String.valueOf(clustering.getClustersTotal()), String.valueOf(clustering.getOutline())), 0, i+1);
               break;

               
           case "LSDBC":
               for(Clustering c : clustering.getClusters())
               {
                    grid.add(clusterHBox.clusterLine(c.getClusterId() - 1), 0, i);
                    grid.add(clusterHBox.elementsInClusterLine(c.getSize(), numberTotal), 0, i+1);
                    i = i + 3;
            
               }
               grid.add(clusterHBox.outlineLine(String.valueOf(clustering.getClustersTotal()), String.valueOf(clustering.getOutline())), 0, i+1);
               break;    
               
               
               
            case "PAM":
               for(Clustering c : clustering.getClusters())
               {  
                    grid.add(clusterHBox.clusterLine(c.getClusterId() - 1), 0, i); 
                    grid.add(clusterHBox.elementsInClusterLine(c.getSize(), numberTotal), 0, i+1);
                    grid.add(clusterHBox.clusterCenterLine("PAM", c.getMedoid()), 0, i+2);

                    i = i + 5;
               }
               grid.add(clusterHBox.outlineLine(String.valueOf(clustering.getClustersTotal()), String.valueOf(clustering.getOutline())), 0, i+1);
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
                    if(clustering.isCreated())
                    {
                        switch(op)
                        {
                            case "DBSCAN":
                                clustering.showDBSCAN(collars);
                                break;
                                
                            case "K-Means":   
                                clustering.showKMeans(collars, choiceCluster.getValue());
                                break;
                                
                            case "LSDBC":
                                clustering.showLSDBC(collars);
                                break;
                                    
                            case "PAM":   
                                clustering.showPAM(collars);
                                break;
                                
                            default:    
                                break;
                        }   
                    }
                }    
                catch (Exception ex) 
                {
                    Logger.getLogger(ClusteringView.class.getName()).log(Level.SEVERE, null, ex);
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
       scene.getStylesheets().add(ClusteringView.class.getResource("lib/Results.css").toExternalForm()); 
       showStage.setScene(scene);
       showStage.show();
    }
    
}