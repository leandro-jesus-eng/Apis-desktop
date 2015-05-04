package satb.view;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import satb.controller.PastureController;
import satb.controller.SeeTrackController;
import satb.controller.TrajectoryController;
import satb.model.Trajectory;
import satb.view.hbox.TrajectoryHBox;

/**Classe referente a janela Trajetória Semântica
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public final class TrajectoryView 
{
    //Objeto que representa a tabela de colares
    private TableView<Trajectory> table = new TableView<>();
    
    //Objeto que representa uma lista de dados (uma colecao)
    private ObservableList<Trajectory> data = FXCollections.observableArrayList();
    
    private Stage subStage;
    private TrajectoryController trajectoryController;
    private PastureController pastureController;
    private SeeTrackController controller;
    private ObservableList<String> collars, pastures;
    
    //ComboBox
    private ComboBox<String> comboAlgorithm;
    private ComboBox<String> comboCollar; 
    private ComboBox<String> comboCollarRemove;
    private ComboBox<String> searchCollar;
    private ComboBox<String> comboDate;
    private ComboBox<String> comboDateRemove;
    private ComboBox<String> comboPasture; 
    
    //RadioButton
    private RadioButton arffButton = new RadioButton("Arquivo ARFF");       
    private RadioButton baseButton = new RadioButton("Banco de Dados");
    
    //Textfield
    TextField minTimeField = new TextField ();
    TextField speedLimitField = new TextField ();
    TextField avgField = new TextField();
    TextField pathField = new TextField();
    
    //Label
    private Label removeLabel = new Label ("Remover uma Trajetória com Semântica");
    private Label infoLabel = new Label ("Adicionar Semântica à Trajetória");
    private Label info = new Label ("Lista de Trajetórias Cadastradas");
    
    //Text
    private final Text actionTarget = new Text();
    
    //Button
    private Button addSemantic = new Button("Adicionar Semântica");
    private Button removeSemantic = new Button("Remover");
    private Button arrfOpenButton = new Button("Abrir ARRF");
    private Button removeButton = new Button("Remover");
    private Button cancelButton = new Button("Cancelar");
    private Button execute = new Button("Adicionar Semântica");
    private Button cancel = new Button("Cancelar");
    
    //HBox
    private HBox buttons = new HBox();
    private HBox buttonsLine = new HBox(5);
    
    /**Construtor. */
    public TrajectoryView() throws Exception
    {
        controller = new SeeTrackController();
        pastureController = new PastureController();
        trajectoryController = new TrajectoryController();
        
        table = formatTable();
        table.setPrefHeight(300); 
        
        collars = controller.selectCollar();
        comboCollar = new ComboBox<>(collars);
        comboCollar.setPromptText("Selecione");
        comboCollar.setPrefWidth(80);
        
        searchCollar = new ComboBox<>(collars);
        searchCollar.setPromptText("Selecione");
        searchCollar.setPrefWidth(80);
        
        comboCollarRemove = new ComboBox<>(collars);
        comboCollarRemove.setPromptText("Selecione");
        comboCollarRemove.setPrefWidth(80);
        
        pastures = pastureController.selectItens();
        comboPasture = new ComboBox<>(pastures);
        comboPasture.setPromptText("Selecione");

        comboDate = new ComboBox<>();
        comboDate.setPromptText("Selecione");
        comboDate.setPrefWidth(80);
    
        comboDateRemove = new ComboBox<>();
        comboDateRemove.setPromptText("Selecione");
        comboDateRemove.setPrefWidth(80);
        
        comboAlgorithm = new ComboBox<>(FXCollections.observableArrayList ("IB-SMoT","CB-SMoT"));
        comboAlgorithm.setPromptText("Selecione");
        comboAlgorithm.setPrefWidth(80);
                
        buttons.setPadding(new Insets(15, 12, 15, 12));
        buttons.setPrefSize(320, 55);
        buttons.setSpacing(10);
        buttons.setId("buttonhbox");
        buttons.getChildren().addAll(addSemantic, removeSemantic);
        buttons.setAlignment(Pos.CENTER);
        
        minTimeField.setPrefWidth(60);
        minTimeField.setText("00:10:00");
        speedLimitField.setPrefWidth(40);
        speedLimitField.setText("1.0");
        avgField.setPrefWidth(40);
        avgField.setText("0.8");
        
        pathField.setPrefWidth(320);
        
        execute.setId("addbutton");
        removeButton.setId("addbutton");
        cancel.setId("cancelbutton");
        cancelButton.setId("cancelbutton");
        infoLabel.setId("label-title");
        info.setId("label-title");
        removeLabel.setId("label-title");
        
        buttonsLine.getChildren().addAll(execute, cancel);
        buttonsLine.setAlignment(Pos.CENTER);
    }
 
    
    /**Método que executa a função principal da janela. */
    public void execute()
    {
        subStage = new Stage();
        subStage.setTitle("Trajetórias Semânticas");
        TrajectoryHBox trajHBox = new TrajectoryHBox();

        GridPane grid = trajHBox.createGrid();

        grid.add(buttons, 0, 0);
        grid.add(info, 0, 2);
        grid.add(trajHBox.collarLine(searchCollar), 0, 3);
        grid.add(table, 0, 4);
        
        searchCollar.setOnAction(new EventHandler<ActionEvent>() 
        {
             @Override
             public void handle(ActionEvent e)
             {
                 try 
                 {
                     table = insertTable(table, searchCollar.getValue());
                 } 
                 catch (Exception ex) 
                 {
                      Logger.getLogger(TrajectoryView.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }  
         });
       
        //Botão que chama a função addSemantic
        addSemantic.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e)
            {
               try 
               {               
                   addSemantic();                   
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(TrajectoryView.class.getName()).log(Level.SEVERE, null, ex);
               }             
            }  
        });
        
        //Botão que chama a função removeSemantic
        removeSemantic.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e)
            {
               try 
               {               
                   removeSemantic();                   
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(TrajectoryView.class.getName()).log(Level.SEVERE, null, ex);
               }             
            }  
        });
           

        Scene scene = new Scene(grid , 400, 600);
        scene.getStylesheets().add(TrajectoryView.class.getResource("lib/Commons.css").toExternalForm());
        subStage.setScene(scene);
        subStage.show(); 
    }
    
    
    /**Método que chama a função de adição de semântica para a trajetória. */
    public void addSemantic()
    {      
        final Stage semanticStage = new Stage();
        semanticStage.setTitle("Adicionar Semântica na Trajetória"); 
        TrajectoryHBox trajHBox = new TrajectoryHBox();
        final HBox parametersLine = new HBox(5);
        
        GridPane grid = trajHBox.createGrid();

        grid.add(infoLabel, 0, 0);
        grid.add(trajHBox.algorithmLine(comboAlgorithm), 0, 1);
        grid.add(parametersLine, 0, 2);
        grid.add(trajHBox.sourceLine(baseButton, arffButton), 0, 3);
        grid.add(trajHBox.radioSourceLine(baseButton, arffButton, comboCollar, comboDate, arrfOpenButton, pathField), 0, 4);
        grid.add(trajHBox.pastureLine(comboPasture), 0, 6);
        grid.add(buttonsLine, 0, 8);
        grid.add(actionTarget, 0, 9);

        
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
                        case "IB-SMoT":
                           parametersLine.getChildren().clear();    
                           break; 
                            
                        case "CB-SMoT":
                           parametersLine.getChildren().clear(); 
                           parametersLine.getChildren().addAll(new Label("Tempo Min.: "), minTimeField, new Label("Vel. Lim.: "), speedLimitField, new Label("Vel. Média: "), avgField);
                           parametersLine.setAlignment(Pos.BASELINE_LEFT); 
                           break;  
                            
                    }
                    
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(TrajectoryView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
        
       
       //Função de seleção do collar 
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
                      Logger.getLogger(TrajectoryView.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }  
       });

       
       //Fecha a janela e cancela as funções 
       arrfOpenButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
               try
               { 
                    String path = trajectoryController.createFile();
                    
                    if(path != null)
                    {
                        pathField.setText(path);
                    }
               }
               catch (Exception ex) 
               {
                        Logger.getLogger(TrajectoryView.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
       });

       //Mostra a trajetória do animal (ou de dois animais)
       execute.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
                try
                {
                    chooseAlgorithm();
                    
                }                
                catch (Exception ex) 
                {
                        Logger.getLogger(TrajectoryView.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
       
        //Fecha a janela e cancela as funções 
        cancel.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {
               semanticStage.close();
            }
        });

        Scene semantic = new Scene(grid, 550, 400);
        semanticStage.setScene(semantic);
        semantic.getStylesheets().add(TrajectoryView.class.getResource("lib/Commons.css").toExternalForm()); 
        semanticStage.show();
   }
    
  
   public void chooseAlgorithm() throws Exception
   {
       switch(comboAlgorithm.getValue())
       {                   
           case "IB-SMoT":
               if(comboCollar.getValue() != null && comboDate.getValue() != null && comboPasture.getValue() != null)
               {  
                   trajectoryController.selectTrajectory(comboCollar.getValue(), comboDate.getValue());
                   trajectoryController.SMoT(comboPasture.getValue(), comboCollar.getValue(), comboDate.getValue(), true);
                   trajectoryController.SMoTInShadow(comboPasture.getValue(), comboCollar.getValue(), comboDate.getValue(), true);
               }
               else
               {
                   if(comboCollar.getValue() == null && comboPasture.getValue() != null && pathField.getText().length() > 0)
                   {
                       trajectoryController.createTrajectory();
                       trajectoryController.SMoT(comboPasture.getValue(), comboCollar.getValue(), comboDate.getValue(), false);
                   }    
                   
               }
           break;
                       
                           
       case "CB-SMoT":
            if(comboCollar.getValue() != null && comboDate.getValue() != null && comboPasture.getValue() != null && avgField.getText().length() > 0 && minTimeField.getText().length() > 0 && speedLimitField.getText().length() > 0 )
            {
                trajectoryController.selectTrajectory(comboCollar.getValue(), comboDate.getValue());
                trajectoryController.cbSMoT(avgField.getText(), minTimeField.getText(), speedLimitField.getText(), true);
            }
            else 
            {
                if(comboCollar.getValue() == null && comboPasture.getValue() != null && pathField.getText().length() > 0)
                {
                    trajectoryController.createTrajectory();
                    trajectoryController.cbSMoT(avgField.getText(), minTimeField.getText(), speedLimitField.getText(), false);
                }                     
            }
            break;    

     }

   }
    
    
   /**Janela para remoção de uma trajetória semântica cadastrada. */
   public void removeSemantic() throws Exception 
   {  
       final Stage removeStage = new Stage();
       removeStage.setTitle("Remover uma Trajetória com Semântica");
       TrajectoryHBox trajHBox = new TrajectoryHBox();
       
       GridPane grid = trajHBox.createGrid();
       
       HBox removeButtons = new HBox(5);
       removeButtons.getChildren().addAll(removeButton, cancelButton);
       removeButtons.setAlignment(Pos.CENTER);
       
       grid.add(removeLabel, 0, 0);
       grid.add(trajHBox.trajectoryLine(comboCollarRemove, comboDateRemove), 0, 1);
       grid.add(removeButtons, 0, 3);
       
       comboCollarRemove.setOnAction(new EventHandler<ActionEvent>() 
       {
             @Override
             public void handle(ActionEvent e)
             {
                 try 
                 {
                      comboDateRemove.getItems().clear();
                      comboDateRemove.getItems().addAll(trajectoryController.selectDate(comboCollarRemove.getValue()));
                 } 
                 catch (Exception ex) 
                 {
                      Logger.getLogger(TrajectoryView.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }  
        });
       
       
        //Função do botão Remover
        removeButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e)
            {
                try
                {
                    trajectoryController.removeTrajectory(comboCollarRemove.getValue(), comboDateRemove.getValue());
                } 
                catch (Exception ex) 
                {
                    Logger.getLogger(TrajectoryView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
         });
       
       
        //Função do botão Cancelar
        cancelButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e)
            {     
                removeStage.close(); 
            }  
        });
       
       Scene removeScene = new Scene(grid, 350, 275);
       removeScene.getStylesheets().add(TrajectoryView.class.getResource("lib/Commons.css").toExternalForm());  
       removeStage.setScene(removeScene);
       removeStage.show();    
   }
   
    
    /**Formata a tabela com as informações dos colares. */
    public TableView<Trajectory> formatTable() throws Exception
    {
         TableView<Trajectory> tableFormat = new TableView<>();
         
         TableColumn<Trajectory,String> tdate = new TableColumn<>("Data");
         tdate.setCellValueFactory(new PropertyValueFactory<Trajectory,String>("date"));
         tdate.setPrefWidth(120);
       
         TableColumn<Trajectory,String> tbegin = new TableColumn<>("Inicio");
         tbegin.setCellValueFactory(new PropertyValueFactory<Trajectory,String>("beginHour"));
         tbegin.setPrefWidth(100);
                 
         TableColumn<Trajectory,String> tend = new TableColumn<>("Fim");
         tend.setCellValueFactory(new PropertyValueFactory<Trajectory,String>("endHour"));
         tend.setPrefWidth(100);
         
         //Adicionando as Colunas a tabela
         tableFormat.getColumns().add(tdate);
         tableFormat.getColumns().add(tbegin);
         tableFormat.getColumns().add(tend);
       
         //retorna a tabela formatada
         return tableFormat;
    }
    
    
    /**Método que insere os elementos na tabela. */
    public TableView<Trajectory> insertTable(TableView<Trajectory> t, String collar) throws Exception
    {         
         //Adiciona os elementos do BD na tabela 
         trajectoryController = new TrajectoryController();
         data = trajectoryController.selectAll(collar);
         table.setItems(data);
         
        //retorna a tabela formatada
        return t;
    }
    
}
