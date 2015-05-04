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
import satb.controller.AreaController;
import satb.controller.PastureController;
import satb.model.Coordinate;
import satb.view.hbox.AreaHBox;

/**Classe referente a janela Áreas.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public final class AreaView 
{
   //Atributos      
   private TableView<Coordinate> tablePoints = new TableView<>(); 
   private TableView<Coordinate> addTable = new TableView<>(); 
   private TableView<Coordinate> editTable = new TableView<>();
   
    //Objeto que representa uma lista de dados (uma colecao)
   private ObservableList<Coordinate> points = FXCollections.observableArrayList();
    
   private Stage subStage;
   private PastureController controller;
   private AreaController areaController;
   private ObservableList<String> data;
   
   //ComboBox
   private ComboBox<String> comboPasture;
   private ComboBox<String> comboPastureAdd;
   private ComboBox<String> comboPastureRemove;
   private ComboBox<String> comboArea;
   private ComboBox<String> comboAreaRemove;
   private ComboBox<String> comboPastureEdit;
   private ComboBox<String> comboPastureEdited;
   private ComboBox<String> comboAreaEdit;
   
   //Linhas
   private HBox buttons = new HBox(10);
   
   //TextField
   TextField minimumTimeField = new TextField();
   TextField nameField = new TextField();
   TextField latitude = new TextField();
   TextField longitude = new TextField();
   TextField editLatitudeField = new TextField();
   TextField editLongitudeField = new TextField();
   TextField editminTimeField = new TextField();
   TextField editNameField = new TextField();
   
   //Text
   private Text minimumTimeText = new Text(" ");
   
   //Label
   private Label infoLabel = new Label ("Áreas de Interesse Cadastradas");
   private Label addLabel = new Label ("Adicionar Área de Interesse");
   private Label removeLabel = new Label ("Remover Área de Interesse");
   private Label coordinatesLabel = new Label ("Coordenadas da Área");
   private Label selectAreaLabel = new Label ("Selecione a Área de Interesse a ser Editada");
   private Label editAreaLabel = new Label ("Editar Área de Interesse Selecionada");
   
   //Botoes
   private Button addArea = new Button("Novo");
   private Button removeArea = new Button("Remover");
   private Button editArea = new Button("Editar");
   private Button showArea = new Button("Ver Área");
   private Button insertButton = new Button("Adicionar");
   private Button addCancelButton = new Button("Cancelar");
   private Button addInTable = new Button("Adicionar");
   private Button openARFF = new Button("Abrir ARFF");
   private Button addInEditTable = new Button("Adicionar");
   private Button openARFFEdit = new Button("Abrir ARFF");
   private Button removeButton = new Button("Remover");
   private Button cancelRemoveButton = new Button("Cancelar");
   private Button cancelEditButton = new Button("Cancelar");  
   private Button editButton = new Button("Editar");
   
   /**Construtor. */
   public AreaView() throws Exception
   {
        controller = new PastureController();
        areaController = new AreaController();
        
        tablePoints = formatTablePoints();
        tablePoints.setPrefSize(300, 200);   
        tablePoints.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //elimina Ghost Column
        
        addTable = formatTablePoints();
        addTable.setPrefSize(300, 200);
        addTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //elimina Ghost Column
        
        editTable = formatTablePoints();
        editTable.setPrefSize(300, 200);
        editTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //elimina Ghost Column
        
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
        
        comboArea = new ComboBox<>();
        comboArea.setPromptText("Selecione");
        comboArea.setPrefWidth(90);
        
        comboAreaEdit = new ComboBox<>();
        comboAreaEdit.setPromptText("Selecione");
        comboAreaEdit.setPrefWidth(90);
        
        comboAreaRemove = new ComboBox<>();
        comboAreaRemove.setPromptText("Selecione");
        comboAreaRemove.setPrefWidth(90);
        
        buttons.setPadding(new Insets(15, 12, 15, 12));
        buttons.setSpacing(10);
        buttons.setId("buttonhbox");
        buttons.getChildren().addAll(addArea, removeArea, editArea);
        buttons.setAlignment(Pos.CENTER); 
        buttons.setPrefSize(320, 55);
        
        nameField.setPrefWidth(120);
        minimumTimeField.setPrefWidth(60);
        editNameField.setPrefWidth(100);
        editminTimeField.setPrefWidth(60);
        
        latitude.setText("Latitude");
        latitude.setPrefWidth(100);
        longitude.setText("Longitude");
        longitude.setPrefWidth(100);
        
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
        selectAreaLabel.setId("label-title");
        editAreaLabel.setId("label-subtitle");
        coordinatesLabel.setId("label-subtitle");
   }
   
   /**Método de execução da função. */
   public void execute()
   {
       subStage = new Stage();
       subStage.setTitle("Áreas de Interesse Cadastradas"); 
       AreaHBox areaHBox = new AreaHBox();
              
       GridPane grid = areaHBox.createGrid();
 
       grid.add(buttons, 0, 1);
       grid.add(infoLabel,0,2);
       grid.add(areaHBox.searchLine(comboPasture, comboArea), 0, 3);
       grid.add(areaHBox.minimumTimeLine(minimumTimeText), 0, 4);
       grid.add(coordinatesLabel, 0, 5);
       grid.add(tablePoints, 0, 6);
       grid.add(showArea, 0, 7);
       
       //ComboBox para selecionar o pasto desejado.
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
                     Logger.getLogger(AreaView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
       
       //ComboBox para selecionar as áreas do pasto selecionado.
       comboArea.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {   
               try 
               {   
                   areaController.selectArea(comboArea.getValue());
                   minimumTimeText.setText(areaController.getArea().getMinimumTime());
                   tablePoints = insertTablePoints (areaController.getArea().getCoordinates(), tablePoints);
                   showArea.setVisible(true);
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(AreaView.class.getName()).log(Level.SEVERE, null, ex);
               }
            }  
            
        });
       
       //Botão para a janela Adicionar Área
       addArea.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {   
               try 
               {                
                   addArea();
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(AreaView.class.getName()).log(Level.SEVERE, null, ex);
               }
            }  
            
        });
       
       //Botão para a janela "Remover Área".
       removeArea.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {   
               try 
               {                
                   removeArea();
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(AreaView.class.getName()).log(Level.SEVERE, null, ex);
               }
            }  
            
      });
            
      //Botão para a janela "Editar Área".
      editArea.setOnAction(new EventHandler<ActionEvent>() 
      {
            @Override
            public void handle(ActionEvent e)
            {
               try 
               {               
                   editArea();   
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(AreaView.class.getName()).log(Level.SEVERE, null, ex);
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
               {      if(comboArea.getValue() != null)
                      {
                        areaController.showArea(comboArea.getValue(), areaController.getArea().getCoordinates());
                      }
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(AreaView.class.getName()).log(Level.SEVERE, null, ex);
               }
              
            }  
        });
       
       Scene scene = new Scene(grid, 425, 625);
       scene.getStylesheets().add(AreaView.class.getResource("lib/Commons.css").toExternalForm());
       subStage.setScene(scene);
       subStage.show();
    }
   
   
      
   /**Abre uma janela para adição de uma nova área de interesse em um pasto. */
   public void addArea() throws Exception 
   {
       subStage = new Stage();
       subStage.setTitle("Adicionar Área de Interesse");  
       AreaHBox areaHBox = new AreaHBox();
       
       GridPane grid = areaHBox.createGrid();

       //Adicionar elementos no Grid
       grid.add(addLabel, 0, 0);
       grid.add(areaHBox.pastureLine(comboPastureAdd), 0, 1);
       grid.add(areaHBox.areaLine(nameField, minimumTimeField), 0, 2);
       grid.add(addTable, 0, 3);
       grid.add(areaHBox.addCoordinateLine(longitude, latitude, addInTable, openARFF), 0, 4);
       grid.add(areaHBox.buttonsLine(insertButton, addCancelButton), 0, 6);

       //Função do Botao Cancelar
       addCancelButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {     
                subStage.close(); 
            }  
        });
       
       
       //Botão para adicionar os elementos na tabela
       addInTable.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {   
                
              if(latitude.getText().length() > 0 && longitude.getText().length() > 0)
               { 
                    points.add(new Coordinate(Double.parseDouble(longitude.getText()), Double.parseDouble(latitude.getText()), null)); 
                    addTable.setItems(points);

                    latitude.clear();
                    longitude.clear();
               }
            }  
            
       });
       
       //Botão para adicionar os elementos na tabela através de um .arff
       openARFF.setOnAction(new EventHandler<ActionEvent>() 
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
                
              if(nameField.getText().length() > 0 && minimumTimeField.getText().length() > 0 && !points.isEmpty())
               { 
                    try 
                    { 
                        areaController.insertArea(comboPastureAdd.getValue(), nameField.getText(), minimumTimeField.getText(), points);
                    } 
                    catch (Exception ex) 
                    {
                        Logger.getLogger(AreaView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    subStage.close(); 
               }
            }  
            
        });
       
       Scene addScene = new Scene(grid, 500, 600);
       addScene.getStylesheets().add(AreaView.class.getResource("lib/Commons.css").toExternalForm());
       subStage.setScene(addScene);
       subStage.show();
   }
 
   
   /**Abre uma janela para remover a área e as coordenadas. */
   public void removeArea() throws Exception 
   {
       subStage = new Stage();
       subStage.setTitle("Remover a Área de Interesse");
       AreaHBox areaHBox = new AreaHBox();
              
       GridPane grid = areaHBox.createGrid();
          
       //Adicionar elementos no Grid
       grid.add(removeLabel, 0, 0);
       grid.add(areaHBox.searchLine(comboPastureRemove, comboAreaRemove), 0, 1);
       grid.add(areaHBox.buttonsLine(removeButton, cancelRemoveButton), 0, 3);
              
       comboPastureRemove.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {
                     comboAreaRemove.getItems().clear();
                     comboAreaRemove.getItems().addAll(areaController.selectItens(comboPastureRemove.getValue()));
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(AreaView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
       
       
       //Função do Botao Remover
       removeButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {            
                if(comboAreaRemove.getValue().length() > 0)
                {
                    try 
                    {
                        areaController.removeArea(comboAreaRemove.getValue());
                        subStage.close(); 
                    } 
                    catch (Exception ex) 
                    {
                        Logger.getLogger(AreaView.class.getName()).log(Level.SEVERE, null, ex);
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
       
       
       Scene removeScene = new Scene(grid, 400, 250);
       removeScene.getStylesheets().add(AreaView.class.getResource("lib/Commons.css").toExternalForm());      
       subStage.setScene(removeScene);
       subStage.show();
   }
   
   
   /**Janela para edição de uma area cadastrada. */
   public void editArea() throws Exception 
   {
       subStage = new Stage();
       subStage.setTitle("Editar Área de Interesse");  
       AreaHBox areaHBox = new AreaHBox();
       final ObservableList<Coordinate> editPoints = FXCollections.observableArrayList();
       
       GridPane grid = areaHBox.createGrid();
       
       editTable.setEditable(true); 
       
       //Adicionar elementos no Grid
       grid.add(selectAreaLabel, 0, 0);
       grid.add(areaHBox.searchLine(comboPastureEdit, comboAreaEdit), 0, 1);
       grid.add(editAreaLabel, 0, 3);
       grid.add(areaHBox.pastureLine(comboPastureEdited), 0, 4);
       grid.add(areaHBox.areaLine(editNameField, editminTimeField), 0, 5);
       grid.add(editTable, 0, 6);
       grid.add(areaHBox.addCoordinateLine(editLongitudeField, editLatitudeField, addInEditTable, openARFFEdit), 0, 7);
       grid.add(areaHBox.buttonsLine(editButton, cancelEditButton), 0, 9);
       
       comboPastureEdit.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {    
                   comboAreaEdit.getItems().clear();
                   comboAreaEdit.getItems().addAll(areaController.selectItens(comboPastureEdit.getValue()));              
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(AreaView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
       
       
       comboAreaEdit.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {   
                    comboPastureEdited.setValue(comboPastureEdit.getValue());
                    editNameField.setText(comboAreaEdit.getValue());
                    areaController.selectArea(comboAreaEdit.getValue());
                    editminTimeField.setText(areaController.getArea().getMinimumTime());
                    editTable = insertTablePoints (areaController.getArea().getCoordinates(), editTable);
                    editPoints.addAll(areaController.getArea().getCoordinates());
                   
                }
                catch (Exception ex) 
                {
                     Logger.getLogger(AreaView.class.getName()).log(Level.SEVERE, null, ex);
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
                if(editNameField.getText().length() > 0 && editminTimeField.getText().length() > 0 && comboPastureEdited.getValue().length() > 0)
                {
                    try 
                    {  
                       areaController.updateArea(comboPastureEdited.getValue(), editNameField.getText(), editminTimeField.getText(), editTable.getItems());
                    } 
                    catch (Exception ex) 
                    {
                        Logger.getLogger(AreaView.class.getName()).log(Level.SEVERE, null, ex);
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
       
       
       Scene editScene = new Scene(grid, 450, 700);
       editScene.getStylesheets().add(AreaView.class.getResource("lib/Commons.css").toExternalForm());      
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
                 return new AreaView.EditingCell();
             }
        };
        

        TableColumn<Coordinate,Double> tlon = new TableColumn<>("Longitude");
        tlon.setCellValueFactory(new PropertyValueFactory<Coordinate,Double>("longitudeX"));
        tlon.setPrefWidth(150);
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
        tlat.setPrefWidth(150);  
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
