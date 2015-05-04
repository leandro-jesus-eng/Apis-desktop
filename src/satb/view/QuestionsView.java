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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import satb.controller.AreaController;
import satb.controller.PastureController;
import satb.controller.QuestionsController;
import satb.controller.SeeTrackController;
import satb.model.DensityPoints;
import satb.model.Stop;
import satb.model.Trajectory;
import satb.view.hbox.QuestionsHBox;


/**Classe referente a janela Perguntas e Respostas.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class QuestionsView 
{
    private Stage subStage;
    private SeeTrackController trackController;
    private QuestionsController controller;
    private PastureController pastureController;
    private AreaController areaController;
    private ObservableList<String> data, pastures;
    
    //ComboBox
    private ComboBox<String> comboPasture;
    private ComboBox<String> comboArea;
    private ComboBox<String> comboCollar; 
    private ComboBox<String> comboDate;
    private ComboBox<String> comboQuestions; 
    
    //TextField
    private TextField dateBeginField = new TextField();
    private TextField dateEndField = new TextField();
    
    //Radio Buttons
    private RadioButton intervalDate = new RadioButton("Intervalo");
    private RadioButton allDate = new RadioButton("Todas");
    private RadioButton oneDate = new RadioButton("Uma");
    
    //Linhas
    private final HBox parametersLine = new HBox(5);
    
    //Labels
    private Label infoLabel = new Label ("Perguntas e Respostas");
    private final Label result = new Label ("Resposta para a Pergunta");
    
    //Botões
    private Button execute = new Button("Executar");
    private Button cancel = new Button("Cancelar");
    private Button close = new Button("Fechar");
    
    /**Construtor. */
    public QuestionsView() throws Exception
    {
        trackController = new SeeTrackController();
        controller = new QuestionsController();
        pastureController = new PastureController();
        areaController = new AreaController();

        comboQuestions = new ComboBox<>(FXCollections.observableArrayList ("O animal foi para...",
         "O animal esteve na Sombra?", "Qual a distância percorrida, a duração e a velocidade média do animal na trajetória?",
         "Existe pontos se concentrando na Sombra?"));
 
        comboQuestions.setPromptText("Selecione");
        comboQuestions.setPrefWidth(300);
       
        data = trackController.selectCollar();
        comboCollar = new ComboBox<>(data);
        comboCollar.setPromptText("Selecione");
        comboCollar.setPrefWidth(80);
        
        comboDate = new ComboBox<>();
        comboDate.setPromptText("Data");
        comboDate.setPrefWidth(80); 
        
        pastures = pastureController.selectItens();
        comboPasture = new ComboBox<>(pastures);
        comboPasture.setPromptText("Selecione");
        comboPasture.setPrefWidth(125);
        
        comboArea = new ComboBox<>();
        comboArea.setPromptText("Selecione");
        comboArea.setPrefWidth(90);
                    
        dateBeginField.setPrefWidth(60);
        dateEndField.setPrefWidth(60);
        
        execute.setId("addbutton");
        cancel.setId("cancelbutton");
        close.setId("cancelbutton");
        infoLabel.setId("label-title");
        result.setId("label-title");
    }
    
    
    /**Método de execução da função. */
    public void execute()
    {      
       subStage = new Stage();
       subStage.setTitle("Perguntas e Respostas"); 
       final QuestionsHBox questionHBox = new QuestionsHBox();
       
       GridPane grid = questionHBox.createGrid();
    
       comboQuestions.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {
                   switch(comboQuestions.getValue())
                   {
                        case "O animal foi para...":
                            parametersLine.getChildren().clear(); 
                            parametersLine.getChildren().addAll(new Label("Pasto: "), comboPasture, new Label(" Área: "), comboArea);
                            parametersLine.setAlignment(Pos.BASELINE_LEFT);
                                  
                            break;
                             
                        default:
                            parametersLine.getChildren().clear();
                            break;
                    }
                
                } 
            catch (Exception ex) 
            {
                Logger.getLogger(QuestionsView.class.getName()).log(Level.SEVERE, null, ex);
            }
          }  
      });
       
      comboPasture.setOnAction(new EventHandler<ActionEvent>() 
      {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {   
                   comboArea.getItems().clear();
                   comboArea.getItems().addAll(areaController.selectItens(comboPasture.getValue()));
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(QuestionsView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
       });
       
       grid.add(infoLabel, 0, 0);
       grid.add(questionHBox.collarLine(comboCollar), 0, 1);
       grid.add(questionHBox.dateLine(allDate, oneDate, intervalDate), 0, 2);
       grid.add(questionHBox.radioDateLine(allDate, oneDate, intervalDate, dateBeginField, dateEndField, dateEndField), 0, 3);
       grid.add(questionHBox.askLine(comboQuestions), 0, 4);
       grid.add(parametersLine, 0, 5);
       grid.add(questionHBox.buttonsLine(execute, cancel), 0, 8);
   
       //Função que representa o botão Execução
       execute.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
                try 
                {     
                   switch(comboQuestions.getValue())
                   {
                        case "O animal foi para...":
                             if(comboCollar.getValue() != null && comboQuestions.getValue() != null && comboArea.getValue() != null)
                             {
                                showAnswers();
                             }    
                            break;
                            
                        default:
                             if(comboCollar.getValue() != null && comboQuestions.getValue() != null)
                             {
                                showAnswers();
                             } 
                             break;
                    }      
                }   
                catch (Exception ex) 
                {
                    Logger.getLogger(QuestionsView.class.getName()).log(Level.SEVERE, null, ex);
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
       
       Scene scene = new Scene(grid, 450, 300);
       subStage.setScene(scene);
       scene.getStylesheets().add(QuestionsView.class.getResource("lib/Commons.css").toExternalForm()); 
       subStage.show();
    }
    
    
    /**Mostra o Resultado dos Algoritmos. */
    public void showAnswers() throws Exception
    { 
       final Stage showStage = new Stage();
       showStage.setTitle("Respostas para a Pergunta");    
       controller.answerTheQuestions(comboCollar.getValue(), comboQuestions.getValue(), comboDate.getValue(), dateBeginField.getText(), dateEndField.getText(), comboPasture.getValue(), comboArea.getValue());

       QuestionsHBox questionHBox = new QuestionsHBox();
       GridPane grid = questionHBox.createGrid();

       //Adicionando elementos no grid
       grid.add(questionHBox.onebuttonLine(close), 0, 0);
       grid.add(result, 0, 1); 
       grid.add(questionHBox.timeLine(comboDate.getValue(), dateBeginField.getText(), dateEndField.getText(), comboCollar.getValue()), 0, 2);
       grid.add(questionHBox.questionLine(comboQuestions.getValue()), 0, 3);
       int i = 5;
       
       switch(comboQuestions.getValue())
       {            
            //Pergunta 1 
            case "O animal foi para...":
                
                if(!controller.hasStops())
                {
                    grid.add(new Text("Não foi para " + comboArea.getValue() +"."), 0, i);
                }
                else
                {
                    grid.add(new Text("Ele foi para " + comboArea.getValue() +" nas seguintes ocasiões:"), 0, i++);
                    String hourTotal = "00:00:00";
                    
                    for(Trajectory t : controller.getTrajectories())
                    {
                        
                        if(!t.getStops().isEmpty())
                        {   
                            String hour = "00:00:00";
                            grid.add(questionHBox.answerTimeLine(t.getDate(), t.trajectoryDuration()), 0, i++);

                            for(Stop s : t.getStops())
                            {
                                grid.add(questionHBox.answerIntervalLine(s.getStartTime(), s.getEndTime()), 0, i++);
                                hour = controller.plusHours(hour, s.stopDuration());
                            }
                            grid.add(questionHBox.durationLine(comboArea.getValue(), hour, t.trajectoryDurationInSeconds()), 0, i++);                            
                            hourTotal = controller.plusHours(hour, hourTotal);
                            i = i + 3;
                        }
                        
                    }
                    
                }
                break;   
                
            //Pergunta 2    
            case "O animal esteve na Sombra?":
                if(!controller.hasStops())
                {
                    grid.add(new Text("O Animal não esteve na sombra."), 0, i);
                }
                else
                {
                    grid.add(new Text("Ele esteve na sombra nas seguintes ocasiões:"), 0, i++);
                    String hourTotal = "00:00:00";
                    
                    for(Trajectory t : controller.getTrajectories())
                    {
                        
                        if(!t.getStops().isEmpty())
                        {   
                            String hour = "00:00:00";
                            grid.add(questionHBox.answerTimeLine(t.getDate(), t.trajectoryDuration()), 0, i++);

                            for(Stop s : t.getStops())
                            {
                                grid.add(questionHBox.answerLocationLine(s.getAreaName()), 0, i++);
                                grid.add(questionHBox.answerIntervalLine(s.getStartTime(), s.getEndTime()), 0, i++);
                                hour = controller.plusHours(hour, s.stopDuration());
                            }
                            grid.add(questionHBox.durationLine("Sombra", hour, t.trajectoryDurationInSeconds()), 0, i++);                            
                            hourTotal = controller.plusHours(hour, hourTotal);
                            i = i + 3;
                        }
                        
                    }
                    
                }
                break;
            
            //Pergunta 3    
            case "Qual a distância percorrida, a duração e a velocidade média do animal na trajetória?": 
                
                if(controller.getTrajectories().isEmpty())
                {
                    grid.add(new Text("Não há trajetórias registradas."), 0, i);
                }
                else
                {
                    grid.add(new Text("Distância percorrida (metros), velocidade média (m/s) e duração (horas)."), 0, i++);
                    for(Trajectory t : controller.getTrajectories())
                    {
                        grid.add(questionHBox.distanceLine(t.getDate(), t.getDistance(), t.meanSpeed(), t.trajectoryDuration()), 0, i++);
                    }
                }    
                break;  
                
                
            //Pergunta 4    
            case "Existe pontos se concentrando na Sombra?":
                if(controller.getTrajectories().isEmpty())
                {
                    grid.add(new Text("Não há trajetórias registradas."), 0, i);
                }
                else
                {
                    grid.add(new Text("O animal se concentrou na sombra nas seguintes ocasiões"), 0, i++);
                    for(Trajectory t : controller.getTrajectories())
                    {
                        grid.add(questionHBox.answerTimeLine(t.getDate(), t.trajectoryDuration()), 0, i++);
                        
                        if(t.getDensityPoints().isEmpty())
                        {    
                            grid.add(new Text("Não há pontos de concentração na sombra."), 0, i);
                        }
                        else
                        {
                            for(DensityPoints d : t.getDensityPoints())
                            {
                                 grid.add(questionHBox.densityInShadowLine(d.getHourBegin(), d.getHourEnd(), d.getAreaShadow()), 0, i++);
                            }
                        }    
                        i = i + 2;
                    }
                }    
                break;    
        
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
      
       Scene scene = new Scene(sp, 600, 500);  
       scene.getStylesheets().add(QuestionsView.class.getResource("lib/Results.css").toExternalForm()); 
       showStage.setScene(scene);
       showStage.show();
    } 
}
