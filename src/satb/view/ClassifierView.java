package satb.view;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import satb.controller.ClassifierController;
import satb.controller.PastureController;
import satb.controller.SeeTrackController;
import satb.model.Classifier;
import satb.model.Coordinate;
import satb.view.hbox.ClassifierHBox;

/**Classe referente a janela Classificação.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class ClassifierView 
{
    private Stage subStage;
    private SeeTrackController controller;
    private ClassifierController classifier;
    private PastureController controllerB;
    private ObservableList<String> data;
    
    //Caixas
    final ComboBox<String> comboCollar, comboDate, comboPasture;
    
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
    private TextField dateBeginField = new TextField();
    private TextField dateEndField = new TextField();
    
    //Linhas
    private final HBox firstLine = new HBox(5);
    private final HBox classifierButtons = new HBox(10);
    
    //Labels
    private final Label result = new Label ("Classificação Baseada em Regras");
  
    //Botão
    private Button execute = new Button("Executar");
    private Button cancel = new Button("Cancelar"); 
    private Button close = new Button("Fechar");
    
    /**Construtor.*/
    public ClassifierView() throws Exception
    {
        controller = new SeeTrackController();
        controllerB = new PastureController();
        classifier = new ClassifierController();
        
        data = controller.selectCollar();
        comboCollar = new ComboBox<>(data);
        comboCollar.setPromptText("Colar");

        data = controllerB.selectItens();
        comboPasture = new ComboBox<>(data);
        comboPasture.setPromptText("Pasto"); 
        
        comboDate = new ComboBox<>();
        comboDate.setPromptText("Data");
        comboDate.setPrefWidth(80); 
        
        dateBeginField.setPrefWidth(60);
        dateEndField.setPrefWidth(60);
        
        hourBeginField.setPrefWidth(60);
        hourEndField.setPrefWidth(60);
        hourAfterField.setPrefWidth(60);
        hourBeforeField.setPrefWidth(60);
        
        execute.setId("addbutton");
        cancel.setId("cancelbutton");
        close.setId("cancelbutton");
        result.setId("label-title");
    }
      
    /**Método de execução da função.*/
    public void execute()
    { 
       subStage = new Stage();
       ClassifierHBox classHBox = new ClassifierHBox();
       subStage.setTitle("Algoritmo Baseado em Regras (Classificação)");    
          
       GridPane grid = new GridPane();
       grid.setStyle("-fx-background-color: #EEEEEE;");
       grid.setAlignment(Pos.CENTER);
       grid.setVgap(4);
       grid.setHgap(10);
       grid.setPadding(new Insets(10, 10, 10, 10));
       
       //Função que representa o combobox Colar
       comboCollar.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {
                     comboDate.getItems().clear();
                     comboDate.getItems().addAll(controller.selectDate(comboCollar.getValue()));
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(ClassifierView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
 
       firstLine.getChildren().addAll(new Label("Colar: "), comboCollar, new Label("Pasto: "), comboPasture);
       firstLine.setAlignment(Pos.BASELINE_LEFT);

       classifierButtons.getChildren().addAll(execute, cancel);
       classifierButtons.setAlignment(Pos.CENTER);
  
       grid.add(firstLine, 0, 0);
       grid.add(classHBox.dateLine(allDate, oneDate, intervalDate), 0, 1);
       grid.add(classHBox.radioDateLine(allDate, oneDate, intervalDate, dateBeginField, dateEndField, comboDate), 0, 2);
       grid.add(classHBox.hourLine(rbNoOne, rbInterval, rbBefore, rbAfter), 0, 3);     
       grid.add(classHBox.radioHourLine(rbNoOne, rbInterval, rbBefore, rbAfter, hourBeforeField, hourAfterField, hourBeginField, hourEndField), 0, 4);
       grid.add(classifierButtons, 0, 7);
        
       //Função que representa o botão Execução
       execute.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
                try 
                {    
                    if(comboCollar.getValue() != null && comboPasture.getValue() != null)
                    {
                        showRules(comboCollar.getValue(), comboDate.getValue(), comboPasture.getValue(), hourBeforeField.getText(), hourAfterField.getText(), hourBeginField.getText(), hourEndField.getText());
                                               
                    } 
                }   
                catch (Exception ex) 
                {
                    Logger.getLogger(ClassifierView.class.getName()).log(Level.SEVERE, null, ex);
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
            
       Scene scene = new Scene(grid, 500, 300); 
       scene.getStylesheets().add(ClassifierView.class.getResource("lib/Commons.css").toExternalForm()); 
       subStage.setScene(scene);
       subStage.show();
    }
    
    
    //Mostra o resultados do Algoritmo
    public void showRules(String collarS, String dateS, String pastureS, String hourBefore, String hourAfter, String hourBegin, String hourEnd) throws Exception
    { 
       final Stage showStage = new Stage();
       ClassifierHBox classHBox = new ClassifierHBox();
       showStage.setTitle("Classificação Baseada em Regras");    
       classifier.ruleBased(collarS, dateS, dateBeginField.getText(), dateEndField.getText(), pastureS, hourBefore, hourAfter, hourBegin, hourEnd);

       HBox buttonLine = new HBox(10);
       buttonLine.getChildren().addAll(close);
       
       GridPane grid = new GridPane();
       grid.setAlignment(Pos.CENTER);
       grid.setHgap(10);
       grid.setVgap(10);
       grid.setPadding(new Insets(25, 25, 25, 25));
       
       //Adicionando elementos no grid
       grid.add(buttonLine, 0, 0);
       grid.add(result, 0, 1);
       grid.add(classHBox.collarLine(collarS, pastureS), 0, 2);
       grid.add(classHBox.timeLine(comboDate.getValue(), dateBeginField.getText(), dateEndField.getText(), hourBeforeField.getText(), hourAfterField.getText(), hourBeginField.getText(), hourEndField.getText()), 0, 3);
       grid.add(classHBox.pointsLine(classifier.getInPasture(), classifier.getInPastureTrue(), classifier.getInPastureFalse()), 0, 4);

       int i = 7;
       
       for(Classifier c : classifier.getArea())
       {
           grid.add(classHBox.areaLine(c.getArea()), 0, i);
           grid.add(classHBox.pointsInAreaLine(String.valueOf(c.getPointsTrue())), 0, i+1);
           i = i + 2;
           
           if(c.getPointsTrue() > 0)
           {          
               for(Coordinate coord : c.getCoordinates())
               {
                   grid.add(classHBox.pointsLine(coord.getDate(), coord.getHour()), 0, i);
                   i++;
               }            
           }     
           i = i + 2;
       }
       
       grid.add(classHBox.shadowLine(classifier.getInShadow()), 0, i);
       i++;
       if(!classifier.getAreaShadow().isEmpty())
       {    
            for(Coordinate coord : classifier.getAreaShadow())
            {
                  grid.add(classHBox.pointsLine(coord.getDate(), coord.getHour()), 0, i);
                  i++;
             }
       }     
     
       ScrollPane sp = new ScrollPane();
       sp.setHbarPolicy(ScrollBarPolicy.NEVER);
       sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
       sp.setContent(grid);
       
       //Fecha a janela e cancela as funções 
       close.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
               showStage.close();
            }
       });
      
       Scene scene = new Scene(sp, 425, 550);  
       scene.getStylesheets().add(ClassifierView.class.getResource("lib/Results.css").toExternalForm()); 
       showStage.setScene(scene);
       showStage.show();
    }
    
}