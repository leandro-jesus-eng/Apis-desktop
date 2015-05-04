package satb.view;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import satb.controller.SeeTrackController;
import satb.view.hbox.SeeTrackHBox;

/**Classe referente a janela "Observar Trajetória".
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class SeeTrackView 
{
    private Stage subStage;
    private SeeTrackController controller;
    private ObservableList<String> data;
    private SeeTrackHBox seeHBox = new SeeTrackHBox();
    
    //RadioButton
    private RadioButton baseButton = new RadioButton("Banco de Dados");
    private RadioButton kmlButton = new RadioButton("Arquivo KML e KMZ");       
        
    //Radio Button
    private RadioButton rbNoOne = new RadioButton("Nenhum");       
    private RadioButton rbInterval = new RadioButton("Intervalo");
    private RadioButton rbBefore = new RadioButton("Antes");
    private RadioButton rbAfter = new RadioButton("Depois");
    private RadioButton rbNoOneB = new RadioButton("Nenhum");       
    private RadioButton rbIntervalB = new RadioButton("Intervalo");
    private RadioButton rbBeforeB = new RadioButton("Antes");
    private RadioButton rbAfterB = new RadioButton("Depois");
    
    //TextField
    private TextField hourAfterField = new TextField();
    private TextField hourBeforeField = new TextField();
    private TextField hourBeginField = new TextField();
    private TextField hourEndField = new TextField();
    private TextField hourAfterFieldB = new TextField();
    private TextField hourBeforeFieldB = new TextField();
    private TextField hourBeginFieldB = new TextField();
    private TextField hourEndFieldB = new TextField();
    private TextField pathField = new TextField();
 
    //ComboBox
    private ComboBox<String> comboCollar; 
    private ComboBox<String> comboDate; 
    private ComboBox<String> comboSecondCollar; 
    private ComboBox<String> comboSecondDate;
    private ComboBox<Integer> numberTrack;
    
    //Label
    private Label infoLabel = new Label ("Observar Trajetórias");
    
    //Button
    private Button openButton = new Button("Abrir");
    private Button show = new Button("Mostrar Rastro");
    private Button cancel = new Button("Cancelar");
    
    /**Construtor. */
    public SeeTrackView() throws Exception
    {
        controller = new SeeTrackController();
        
        data = controller.selectCollar();
        comboCollar = new ComboBox<>(data);
        comboCollar.setPromptText("Selecione");
        comboCollar.setPrefWidth(80);

        comboSecondCollar = new ComboBox<>(data);
        comboSecondCollar.setPromptText("Selecione");
        comboSecondCollar.setPrefWidth(80);
              
        comboDate = new ComboBox<>();
        comboDate.setPromptText("Selecione");
        comboDate.setPrefWidth(80);
        
        comboSecondDate = new ComboBox<>();
        comboSecondDate.setPromptText("Selecione");
        comboSecondDate.setPrefWidth(80);
        
        hourBeginField.setPrefWidth(60);
        hourEndField.setPrefWidth(60);
        hourAfterField.setPrefWidth(60);
        hourBeforeField.setPrefWidth(60);
        hourBeginFieldB.setPrefWidth(60);
        hourEndFieldB.setPrefWidth(60);
        hourAfterFieldB.setPrefWidth(60);
        hourBeforeFieldB.setPrefWidth(60);
        
        pathField.setPrefWidth(320);
        
        show.setId("addbutton");
        cancel.setId("cancelbutton");
        infoLabel.setId("label-title");
        
        numberTrack = new ComboBox<>(FXCollections.observableArrayList (1, 2));
        numberTrack.setValue(1); //valor default do choice box
    }
 
    
    /**Método de execução da função. */
    public void execute()
    {      
       subStage = new Stage();
       subStage.setTitle("Trajetória Animal"); 
       
       final GridPane grid = seeHBox.createGrid();
       final VBox base = new VBox(10);
       final VBox secondBase = new VBox(10);
       
       final ToggleGroup tgSource = new ToggleGroup();
       
       grid.add(infoLabel, 0, 2);
       grid.add(seeHBox.sourceLine(kmlButton, baseButton), 0, 4);
       base.getChildren().add(seeHBox.openButtonLine(openButton, pathField));
       
       kmlButton.setOnAction(new EventHandler<ActionEvent>() 
       {
           @Override
           public void handle(ActionEvent e) 
           {
               numberTrack.setValue(1);
               comboCollar.setValue(null);
               comboDate.setValue(null);
               comboSecondCollar.setValue(null);
               comboSecondDate.setValue(null);
               base.getChildren().clear();
               secondBase.getChildren().clear();
               base.getChildren().add(seeHBox.openButtonLine(openButton, pathField));
           }
       });
       
       baseButton.setOnAction(new EventHandler<ActionEvent>() 
       {
           @Override
           public void handle(ActionEvent e) 
           {
               pathField.clear();
               base.getChildren().clear();
               secondBase.getChildren().clear();
               base.getChildren().add(seeHBox.trackLine(numberTrack));
               base.getChildren().add(seeHBox.collarLine(comboCollar, comboDate, 1));
               base.getChildren().add(seeHBox.hourLine(rbNoOne, rbInterval, rbBefore, rbAfter));
               base.getChildren().add(seeHBox.radioLine(rbNoOne, rbInterval, rbBefore, rbAfter, hourBeforeField, hourAfterField, hourBeginField, hourEndField));
    
               final HBox collarLine = seeHBox.collarLine(comboSecondCollar, comboSecondDate, 2);
               final HBox hourLine = seeHBox.hourLine(rbNoOneB, rbIntervalB, rbBeforeB, rbAfterB);
               final HBox radioLine = seeHBox.radioLine(rbNoOneB, rbIntervalB, rbBeforeB, rbAfterB,  hourBeforeFieldB, hourAfterFieldB, hourBeginFieldB, hourEndFieldB);

               collarLine.setVisible(false);
               hourLine.setVisible(false);
               radioLine.setVisible(false);

               //Escolhe o número de trajtórias a ser escolhidas
               numberTrack.setOnAction(new EventHandler<ActionEvent>() 
               {
                    @Override
                    public void handle(ActionEvent e)
                    {
                        try 
                        {
                            if(numberTrack.getValue() == 1)
                            {                    
                                collarLine.setVisible(false);
                                hourLine.setVisible(false);
                                radioLine.setVisible(false);                  
                            }
                            else
                            {
                                collarLine.setVisible(true);
                                hourLine.setVisible(true);
                                radioLine.setVisible(true);
                            }
                        } 
                        catch (Exception ex) 
                        {
                             Logger.getLogger(SeeTrackView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }  
                });
               
               secondBase.getChildren().add(collarLine);
               secondBase.getChildren().add(hourLine);
               secondBase.getChildren().add(radioLine);
           }
       });

       kmlButton.setSelected(true);
       tgSource.getToggles().addAll(kmlButton, baseButton);
      
       grid.add(base, 0, 6);
       grid.add(secondBase, 0, 7);
       grid.add(seeHBox.buttonsLine(show, cancel), 0, 8);
         
       //Botão que escolhe o colar.
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
                     Logger.getLogger(SeeTrackView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });

        //Botão que escolhe o segundo colar.
        comboSecondCollar.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {
                     comboSecondDate.getItems().clear();
                     comboSecondDate.getItems().addAll(controller.selectDate(comboSecondCollar.getValue()));
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(SeeTrackView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
        
        
       //Fecha a janela e cancela as funções 
       openButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
               try
               { 
                    String path = controller.createFile();
                    
                    if(path != null)
                    {
                        pathField.setText(path);
                    }
               }
               catch (Exception ex) 
               {
                        Logger.getLogger(SeeTrackView.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
       });

       //Mostra a trajetória do animal (ou de dois animais)
       show.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
                try 
                {
                    if(comboCollar.getValue() != null && comboDate.getValue() != null)
                    {
                        
                        if(comboSecondCollar.getValue() != null && comboSecondDate.getValue() != null)
                        {
                            controller.showCoordinates(comboCollar.getValue(), comboDate.getValue(), comboSecondCollar.getValue(), comboSecondDate.getValue(), hourBeforeField.getText(), hourAfterField.getText(), hourBeginField.getText(), hourEndField.getText(), hourBeforeFieldB.getText(), hourAfterFieldB.getText(), hourBeginFieldB.getText(), hourEndFieldB.getText());
                        }
                        else
                        {
                            controller.showCoordinates(comboCollar.getValue(), comboDate.getValue(), hourBeforeField.getText(), hourAfterField.getText(), hourBeginField.getText(), hourEndField.getText());
                        }
                    }
                    else
                    {
                        if(pathField.getText().length() > 0)
                        {
                            controller.callGoogleEarth();
                        }
                    }
                } 
                catch (Exception ex) 
                {
                        Logger.getLogger(SeeTrackView.class.getName()).log(Level.SEVERE, null, ex);
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

       Scene scene = new Scene(grid, 425, 375);
       subStage.setScene(scene);
       scene.getStylesheets().add(SeeTrackView.class.getResource("lib/Commons.css").toExternalForm()); 
       subStage.show();
    }
    
}
