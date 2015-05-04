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
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import satb.controller.AreaShadowController;
import satb.controller.PastureController;
import satb.controller.TreeController;
import satb.model.AreaShadow;
import satb.model.Coordinate;
import satb.view.hbox.AreaShadowHBox;

/**Classe referente a janela Área Sombreada.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public final class AreaShadowView 
{
   //Atributos      
   private TableView<Coordinate> table = new TableView<>(); 
   private TableView<Coordinate> addTable = new TableView<>(); 
   private TableView<Coordinate> editTable = new TableView<>();
   
    //Objeto que representa uma lista de dados (uma colecao)
   private ObservableList<Coordinate> points = FXCollections.observableArrayList();
   private ObservableList<AreaShadow> areas = FXCollections.observableArrayList(); 
   
   private Stage subStage;
   private PastureController controller;
   private TreeController treeController;
   private AreaShadowController areaController;
   private ObservableList<String> data;
   
   //ComboBox
   private ComboBox<String> comboPasture;
   private ComboBox<String> comboTree;
   private ComboBox<String> comboAreaShadow;
   private ComboBox<String> comboPastureAdd;
   private ComboBox<String> comboTreeAdd;
   private ComboBox<String> comboPastureRemove;
   private ComboBox<String> comboTreeRemove;
   private ComboBox<String> comboAreaShadowRemove;
   private ComboBox<String> comboAreaShadowEdit;
   private ComboBox<String> comboPastureEdit;
   private ComboBox<String> comboPastureEdited;
   private ComboBox<String> comboTreeEdit;
   private ComboBox<String> comboTreeEdited;
   
   //Linhas
   private HBox buttons = new HBox(5);

   //TextField
   TextField nameField = new TextField();
   TextField hourBeginField = new TextField();
   TextField hourEndField = new TextField();
   TextField latitudeField = new TextField();
   TextField longitudeField = new TextField();
   TextField minTimeField = new TextField();
   TextField editLatitudeField = new TextField();
   TextField editLongitudeField = new TextField();
   TextField editNameField = new TextField();
   TextField editHourBeginField = new TextField();
   TextField editHourEndField = new TextField();
   TextField editMinTimeField = new TextField();
   
   //Text
   private Text hourBegin = new Text(" ");
   private Text hourEnd = new Text(" ");
   
   //Label
   private Label infoLabel = new Label ("Áreas Sombreadas Cadastradas");
   private Label addLabel = new Label ("Adicionar Área Sombreada");
   private Label removeLabel = new Label ("Remover Área Sombreada");
   private Label addHoursLabel = new Label ("Invervalo da Sombra: ");
   private Label tableLabel = new Label ("Coordenadas da Área Sombreada");
   private Label selectAreaShadowLabel = new Label ("Selecione a Área Sombreada a ser Editada");
   private Label editAreaShadowLabel = new Label ("Editar Área Sombreada Selecionada");
   
   //Botoes
   private Button addAreaShadow = new Button("Novo");
   private Button removeAreaShadow = new Button("Remover");
   private Button editAreaShadow = new Button("Editar");
   private Button insertButton = new Button("Adicionar");
   private Button addCancelButton = new Button("Cancelar");
   private Button addInTable = new Button("Adicionar");
   private Button openARRF = new Button("Abrir ARFF");
   private Button addInEditTable = new Button("Adicionar");
   private Button openARFFEdit = new Button("Abrir ARFF");
   private Button removeButton = new Button("Remover");
   private Button cancelRemoveButton = new Button("Cancelar");
   private Button cancelEditButton = new Button("Cancelar");  
   private Button editButton = new Button("Editar");
   private Button showArea = new Button("Ver Área");
   
   /**Construtor. */
   public AreaShadowView() throws Exception
   {
        controller = new PastureController();
        treeController = new TreeController();
        areaController = new AreaShadowController();
        
        table = formatTablePoints();
        table.setPrefSize(300, 200);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //elimina Ghost Column
        
        editTable = formatTablePoints();
        editTable.setPrefSize(300, 200);
        editTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //elimina Ghost Column
        
        addTable = formatTablePoints();
        addTable.setPrefSize(300, 200);
        addTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //elimina Ghost Column
        
        data = controller.selectItens();
        
        comboPasture = new ComboBox<>(data);
        comboPasture.setPromptText("Selecione");
        comboPasture.setPrefWidth(125);
        
        comboPastureAdd = new ComboBox<>(data);
        comboPastureAdd.setPromptText("Selecione");
        comboPastureAdd.setPrefWidth(125);
        
        comboPastureRemove = new ComboBox<>(data);
        comboPastureRemove.setPromptText("Selecione");
        comboPastureRemove.setPrefWidth(125);
        
        comboPastureEdit = new ComboBox<>(data);
        comboPastureEdit.setPromptText("Selecione");
        comboPastureEdit.setPrefWidth(125);
        
        comboPastureEdited = new ComboBox<>(data);
        comboPastureEdited.setPromptText("Selecione");
        comboPastureEdited.setPrefWidth(125);
        
        comboTreeAdd = new ComboBox<>();
        comboTreeAdd.setPromptText("Selecione");
        comboTreeAdd.setPrefWidth(90);
     
        comboTree = new ComboBox<>();
        comboTree.setPromptText("Selecione");
        comboTree.setPrefWidth(90);
        
        comboAreaShadow = new ComboBox<>();
        comboAreaShadow.setPromptText("Selecione");
        comboAreaShadow.setPrefWidth(120);
        
        comboTreeEdited = new ComboBox<>();
        comboTreeEdited.setPromptText("Selecione");
        comboTreeEdited.setPrefWidth(90);
        
        comboTreeEdit = new ComboBox<>();
        comboTreeEdit.setPromptText("Selecione");
        comboTreeEdit.setPrefWidth(90);
        
        comboTreeRemove = new ComboBox<>();
        comboTreeRemove.setPromptText("Selecione");
        comboTreeRemove.setPrefWidth(90);
        
        comboAreaShadowRemove = new ComboBox<>();
        comboAreaShadowRemove.setPromptText("Selecione");
        comboAreaShadowRemove.setPrefWidth(90);
           
        comboAreaShadowEdit = new ComboBox<>();
        comboAreaShadowEdit.setPromptText("Selecione");
        comboAreaShadowEdit.setPrefWidth(120);
        
        buttons.setPadding(new Insets(15, 12, 15, 12));
        buttons.setPrefSize(320, 55);
        buttons.setSpacing(10);
        buttons.setId("buttonhbox");
        buttons.getChildren().addAll(addAreaShadow, removeAreaShadow, editAreaShadow);
        buttons.setAlignment(Pos.CENTER);        
        
        nameField.setPrefWidth(80);
        minTimeField.setPrefWidth(60);
        editMinTimeField.setPrefWidth(60);
        
        hourBeginField.setPrefWidth(60);
        hourEndField.setPrefWidth(60);
        editHourBeginField.setPrefWidth(60);
        editHourEndField.setPrefWidth(60);
                
        latitudeField.setPromptText("Latitude");
        latitudeField.setPrefWidth(80);
        longitudeField.setPromptText("Longitude");
        longitudeField.setPrefWidth(80);
        
        editLatitudeField.setPromptText("Latitude");
        editLatitudeField.setPrefWidth(80);
        editLongitudeField.setPromptText("Longitude");
        editLongitudeField.setPrefWidth(80);
        
        addCancelButton.setId("cancelbutton");
        addCancelButton.setPrefSize(80, 25);
        insertButton.setId("addbutton");
        insertButton.setPrefSize(80, 25);
        
        cancelRemoveButton.setId("cancelbutton");
        cancelRemoveButton.setPrefSize(80, 25);
        removeButton.setId("addbutton");
        removeButton.setPrefSize(80, 25);
        
        cancelEditButton.setId("cancelbutton");
        cancelEditButton.setPrefSize(80, 25);
        editButton.setId("addbutton");
        editButton.setPrefSize(80, 25);
        
        showArea.setVisible(false);
        showArea.setId("yellowbutton");
        
        infoLabel.setId("label-title");
        addLabel.setId("label-title");
        removeLabel.setId("label-title");
        selectAreaShadowLabel.setId("label-title");
        editAreaShadowLabel.setId("label-subtitle");
        addHoursLabel.setId("label-subtitle");
        tableLabel.setId("label-subtitle");
   }
    
   /**Método de execução da função. */
   public void execute()
   {
       subStage = new Stage();
       subStage.setTitle("Áreas Sombreadas Cadastradas");  
       AreaShadowHBox shadowHBox = new AreaShadowHBox();              
       GridPane grid = shadowHBox.createGrid();
       
       grid.add(buttons, 0, 0);
       grid.add(infoLabel,0,1);
       grid.add(shadowHBox.searchLine(comboPasture, comboTree), 0, 2);
       grid.add(shadowHBox.searchAreaLine(comboAreaShadow), 0, 3);
       grid.add(shadowHBox.intervalTimeLine(hourBegin, hourEnd), 0, 5);
       grid.add(tableLabel,0, 6);
       grid.add(table, 0, 7);
       grid.add(showArea, 0, 8);
      
       //ComboBox para selecionar os pastos.
       comboPasture.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {   
                   comboTree.getItems().clear();
                   comboTree.getItems().addAll(treeController.selectItem(comboPasture.getValue()));
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
       });
       
       //ComboBox para selecionar as áreas do pasto selecionado.
       comboTree.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {   
               try 
               {       
                   comboAreaShadow.getItems().clear();
                   comboAreaShadow.getItems().addAll(areaController.selectItens(comboTree.getValue()));

               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
               }
            }  

       });
       
       //ComboBox para selecionar as áreas sombreadas da árvore.
       comboAreaShadow.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {   
               try 
               {       
                   areaController.selectAreaShadow(comboAreaShadow.getValue());
                   hourBegin.setText(areaController.getAreaShadow().getHourBegin());
                   hourEnd.setText(areaController.getAreaShadow().getHourEnd());
                   table = insertTablePoints(areaController.getAreaShadow().getCoordinates(), table);
                   showArea.setVisible(true);
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
               }
            }  
            
       });
       
       //Botão que mostra a área no Google Earth.
       showArea.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
               try 
               {      
                   if(comboAreaShadow.getValue() != null)
                   {                       
                        areaController.showArea(comboAreaShadow.getValue(), areaController.getAreaShadow().getCoordinates());
                   }
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
               }
              
            }  
        });
       
        //Botão para a janela Adicionar Area Sombreada
        addAreaShadow.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e)
            {   
               try 
               {                
                   addShadowArea();
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
               }
            }  
            
        });
       
       //Botão para a janela Remover Area Sombreada.
       removeAreaShadow.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {   
               try 
               {                
                   removeShadowArea();
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
               }
            }  
            
        });
       
       //Botão para a janela Editar Area.
       editAreaShadow.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
               try 
               {               
                   editAreaShadow();   
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
               }
              
            }  
        });
            
       Scene scene = new Scene(grid, 400, 600);
       scene.getStylesheets().add(AreaShadowView.class.getResource("lib/Commons.css").toExternalForm());
       subStage.setScene(scene);
       subStage.show();
    }
     
      
   /**Abre uma janela para adição de uma nova área sombreada de uma árvore em um pasto. */
   public void addShadowArea() throws Exception 
   {
       subStage = new Stage();
       subStage.setTitle("Adicionar Área Sombreada"); 
       AreaShadowHBox shadowHBox = new AreaShadowHBox();
       
       GridPane grid = shadowHBox.createGrid();
      
       //Adicionar elementos no Grid
       grid.add(addLabel, 0, 0);
       grid.add(shadowHBox.searchLine(comboPastureAdd, comboTreeAdd), 0, 1);
       grid.add(shadowHBox.nameAreaLine(nameField, minTimeField), 0, 2);
       grid.add(addHoursLabel, 0, 3);
       grid.add(shadowHBox.hourIntervalLine(hourBeginField, hourEndField), 0, 4);
       grid.add(addTable, 0, 5);
       grid.add(shadowHBox.addCoordinateLine(longitudeField, latitudeField, addInTable, openARRF), 0, 6);
       grid.add(shadowHBox.buttonsLine(insertButton, addCancelButton), 0, 7);
      
       comboPastureAdd.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {   
                   comboTreeAdd.getItems().clear();
                   comboTreeAdd.getItems().addAll(treeController.selectItem(comboPastureAdd.getValue()));
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
       
       
       
       
       //Botão para adicionar os elementos na tabela
       addInTable.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {   
                
              if(latitudeField.getText().length() > 0 && longitudeField.getText().length() > 0)
               { 
                    points.add(new Coordinate(Double.parseDouble(longitudeField.getText()), Double.parseDouble(latitudeField.getText()), null)); 
                    addTable.setItems(points);

                    latitudeField.clear();
                    longitudeField.clear();
               }
            }  
            
        });
       
       //Botão para adicionar os elementos na tabela através de um .arff
       openARRF.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
               try
               { 
                   points = areaController.readARFF();
                   addTable.setItems(points);
               }
               catch (Exception ex) 
               {
                        Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
      });
       
      //Botão para adicionar os elementos do pasto cadastrado
      insertButton.setOnAction(new EventHandler<ActionEvent>() 
      {
            @Override
            public void handle(ActionEvent e)
            {   
                
              if(nameField.getText().length() > 0 && hourBeginField.getText().length() > 0 && hourEndField.getText().length() > 0  && !points.isEmpty())
               { 
                    try 
                    { 
                        areaController.insertAreaShadow(nameField.getText(), comboPastureAdd.getValue(), comboTreeAdd.getValue(), hourBeginField.getText(), hourEndField.getText(), points, minTimeField.getText());
                    } 
                    catch (Exception ex) 
                    {
                        Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    subStage.close(); 
               }
            }  
            
       });
       
       //Função do Botao Cancelar
       addCancelButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {     
                subStage.close(); 
            }  
        });
                
       Scene addScene = new Scene(grid, 500, 650);
       addScene.getStylesheets().add(AreaShadowView.class.getResource("lib/Commons.css").toExternalForm());      
       subStage.setScene(addScene);
       subStage.show();
   }
 
   
   /**Abre uma janela para remover a área sombreada e as coordenadas. */
   public void removeShadowArea() throws Exception 
   {
       subStage = new Stage();
       subStage.setTitle("Remover Área Sombreada");  
       AreaShadowHBox shadowHBox = new AreaShadowHBox();    
       
       GridPane grid = shadowHBox.createGrid();

       //Adicionar elementos no Grid
       grid.add(removeLabel, 0, 0);
       grid.add(shadowHBox.searchLine(comboPastureRemove, comboTreeRemove), 0, 1);
       grid.add(shadowHBox.searchAreaLine(comboAreaShadowRemove), 0, 2);
       grid.add(shadowHBox.buttonsLine(removeButton, cancelRemoveButton), 0, 4);
              
       comboPastureRemove.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {
                     comboTreeRemove.getItems().clear();
                     comboTreeRemove.getItems().addAll(treeController.selectItem(comboPastureRemove.getValue()));
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
       
       comboTreeRemove.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {
                     comboAreaShadowRemove.getItems().clear();
                     comboAreaShadowRemove.getItems().addAll(areaController.selectItens(comboTreeRemove.getValue()));
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
       
       
       //Função do Botao Remover
       removeButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {            
                if(comboAreaShadowRemove.getValue().length() > 0)
                {
                    try 
                    {
                        areaController.removeAreaShadow(comboAreaShadowRemove.getValue());
                        subStage.close(); 
                    } 
                    catch (Exception ex) 
                    {
                        Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }  
       });
       
       
       //Função do Botao Cancelar
       cancelRemoveButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {     
                subStage.close(); 
            }  
       });
       
       
       Scene removeScene = new Scene(grid, 450, 300);
       removeScene.getStylesheets().add(AreaShadowView.class.getResource("lib/Commons.css").toExternalForm());   
       subStage.setScene(removeScene);
       subStage.show();
   }
   
   
   /**Janela para edição de uma area sombreada cadastrada. */
   public void editAreaShadow() throws Exception 
   {
       subStage = new Stage();
       subStage.setTitle("Editar Área");  
       final ObservableList<Coordinate> editPoints = FXCollections.observableArrayList();
       AreaShadowHBox shadowHBox = new AreaShadowHBox();
       
       GridPane grid = shadowHBox.createGrid();
       editTable.setEditable(true); 
       
       //Adicionar elementos no Grid
       grid.add(selectAreaShadowLabel, 0, 0);
       grid.add(shadowHBox.searchLine(comboPastureEdit, comboTreeEdit), 0, 1);
       grid.add(shadowHBox.searchAreaLine(comboAreaShadowEdit), 0, 2);
       grid.add(editAreaShadowLabel, 0, 3);
       grid.add(shadowHBox.searchLine(comboPastureEdited, comboTreeEdited), 0, 4);
       grid.add(shadowHBox.nameAreaLine(editNameField, editMinTimeField), 0, 5);
       grid.add(shadowHBox.hourIntervalLine(editHourBeginField, editHourEndField), 0, 6);
       grid.add(editTable, 0, 7);
       grid.add(shadowHBox.addCoordinateLine(editLongitudeField, editLatitudeField, addInEditTable, openARFFEdit), 0, 8);
       grid.add(shadowHBox.buttonsLine(editButton, cancelEditButton), 0, 9);
       
       comboPastureEdit.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {    
                   comboTreeEdit.getItems().clear();
                   comboAreaShadowEdit.getItems().clear();
                   comboTreeEdit.getItems().addAll(treeController.selectItem(comboPastureEdit.getValue()));             
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
       
       
       comboTreeEdit.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {   
                   comboAreaShadowEdit.getItems().clear();
                   comboAreaShadowEdit.getItems().addAll(areaController.selectItens(comboTreeEdit.getValue()));
                   
                }
                catch (Exception ex) 
                {
                     Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
      });
       
       
      comboAreaShadowEdit.setOnAction(new EventHandler<ActionEvent>() 
      {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {   
                   comboPastureEdited.setValue(comboPastureEdit.getValue()); 
                   comboTreeEdited.setValue(comboTreeEdit.getValue());
                   editNameField.setText(comboAreaShadowEdit.getValue());
                   
                   areaController.selectAreaShadow(comboAreaShadowEdit.getValue());
                   editMinTimeField.setText(areaController.getAreaShadow().getMinTime());
                   editHourBeginField.setText(areaController.getAreaShadow().getHourBegin());
                   editHourEndField.setText(areaController.getAreaShadow().getHourEnd()); 
                   editTable = insertTablePoints(areaController.getAreaShadow().getCoordinates(), editTable);
                   editPoints.addAll(areaController.getAreaShadow().getCoordinates());
                }
                catch (Exception ex) 
                {
                     Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
       
       comboPastureEdited.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {    
                   comboTreeEdited.getItems().clear();
                   comboTreeEdited.getItems().addAll(treeController.selectItem(comboPastureEdited.getValue()));             
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
       
       //Botão para adicionar os elementos na tabela
       addInEditTable.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {   
                
              if(editLatitudeField.getText().length() > 0 && editLongitudeField.getText().length() > 0)
               { 
                    editPoints.add(new Coordinate(Double.parseDouble(editLongitudeField.getText()), Double.parseDouble(editLatitudeField.getText()), null)); 
                    editTable.setItems(editPoints);

                    editLatitudeField.clear();
                    editLongitudeField.clear();
               }
            }  
            
       });
       
       //Botão para adicionar os elementos na tabela através de um .arff
       openARFFEdit.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e) 
            {
               try
               { 
                   ObservableList<Coordinate> pointsList = areaController.readARFF();
                   editTable.setItems(pointsList);
               }
               catch (Exception ex) 
               {
                        Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
       });
       
       
       //Função do Botao Edição
       editButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {            
                if(editNameField.getText().length() > 0 && editHourBeginField.getText().length() > 0 && editHourEndField.getText().length() > 0 && comboPastureEdited.getValue().length() > 0)
                {
                    try 
                    {  
                       areaController.updateAreaShadow(comboPastureEdited.getValue(), comboTreeEdited.getValue(), editNameField.getText(), editHourBeginField.getText(), editHourEndField.getText(), editTable.getItems(), editMinTimeField.getText());
                    } 
                    catch (Exception ex) 
                    {
                        Logger.getLogger(AreaShadowView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    subStage.close(); 
                 }         
            } 
       });
       
       
       //Função do Botao Cancelar
       cancelEditButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {     
                subStage.close(); 
            }  
       });
       
       
       Scene editScene = new Scene(grid, 550, 700);
       editScene.getStylesheets().add(AreaShadowView.class.getResource("lib/Commons.css").toExternalForm());      
       subStage.setScene(editScene);
       subStage.show();
   }
 
   /**Formata tabela com duas colunas. */
   public TableView<Coordinate> formatTablePoints() throws Exception
   {
        TableView<Coordinate> tableView = new TableView<>();
        Callback<TableColumn<Coordinate, Double>, TableCell<Coordinate, Double>> cellFactory = new Callback<TableColumn<Coordinate, Double>, TableCell<Coordinate, Double>>() 
        {
             @Override
             public TableCell<Coordinate, Double> call(TableColumn<Coordinate, Double> p) 
             {
                 return new AreaShadowView.EditingCell();
             }
        };
        
        TableColumn<Coordinate,Double> tlon = new TableColumn<>("Longitude");
        tlon.setCellValueFactory(new PropertyValueFactory<Coordinate,Double>("longitudeX"));
        tlon.setPrefWidth(300/2);
        tlon.setCellFactory(cellFactory);
        tlon.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Coordinate, Double>>() 
        {
            @Override 
            public void handle(TableColumn.CellEditEvent<Coordinate, Double> t) 
            {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setLongitudeX(t.getNewValue());
            }
        });
        
        TableColumn<Coordinate,Double> tlat = new TableColumn<>("Latitude");
        tlat.setCellValueFactory(new PropertyValueFactory<Coordinate,Double>("latitudeY"));
        tlat.setPrefWidth(300/2);  
        tlat.setCellFactory(cellFactory);
        tlat.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Coordinate, Double>>() 
        {
            @Override 
            public void handle(TableColumn.CellEditEvent<Coordinate, Double> t) 
            {
                 t.getTableView().getItems().get(t.getTablePosition().getRow()).setLatitudeY(t.getNewValue());
            }
        });
         
        //Adicionando as Colunas a tabela
        tableView.getColumns().add(tlon);
        tableView.getColumns().add(tlat);
         
        //retorna a tabela formatada
        return tableView;
    }
   
   
    /**Método que insere os elementos na tabela. */
    public TableView<Coordinate> insertTablePoints(ObservableList<Coordinate> coords, TableView<Coordinate> t) throws Exception
    {         
         //Adiciona os elementos do BD na tabela 
         t.setItems(coords);
         
         //retorna a tabela formatada
         return t;
    }
   
   
    /**Insere elementos na tabela. */
    public TableView<AreaShadow> insertTable(String name, TableView<AreaShadow> t) throws Exception
    {         
         //Adiciona os elementos do BD na tabela 
         areas = areaController.selectAll(name);
         t.setItems(areas);
         
        //retorna a tabela formatada
        return t;
    }
    
    
    /**Classe Especial para a edição de células da TableView. */
    class EditingCell extends TableCell<Coordinate, Double> 
    {
      private TextField textField; 
      public EditingCell() {}
     
      @Override
      public void startEdit() 
      {
          super.startEdit();
          if (textField == null) 
          {
              createTextField();
          }
         
          setGraphic(textField);
          setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
          textField.selectAll();
      }
     
      @Override
      public void cancelEdit() 
      {
          super.cancelEdit();
         
          setText(String.valueOf(getItem()));
          setContentDisplay(ContentDisplay.TEXT_ONLY);
      }
 
      @Override
      public void updateItem(Double item, boolean empty) 
      {
          super.updateItem(item, empty);   
          if (empty) 
          {
              setText(null);
              setGraphic(null);
          } 
          else 
          {
              if (isEditing()) 
              {
                  if (textField != null) 
                  {
                      textField.setText(getString());
                  }
                  setGraphic(textField);
                  setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
              } else 
              {
                  setText(getString());
                  setContentDisplay(ContentDisplay.TEXT_ONLY);
              }
          }
      }
 
      private void createTextField() 
      {
          textField = new TextField(getString());
          textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()*2);
          textField.setOnKeyPressed(new EventHandler<KeyEvent>() 
          {   
              @Override
              public void handle(KeyEvent t) 
              {
                  if (t.getCode() == KeyCode.ENTER) 
                  {
                      commitEdit(Double.parseDouble(textField.getText()));
                  } 
                  else if (t.getCode() == KeyCode.ESCAPE) 
                  {
                      cancelEdit();
                  }
              }
          });
      }
     
      private String getString() 
      {
          return getItem() == null ? "" : getItem().toString();
      }
  }   
  
}
