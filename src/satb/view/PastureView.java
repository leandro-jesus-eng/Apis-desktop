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
import satb.controller.PastureController;
import satb.model.Coordinate;
import satb.view.hbox.PastureHBox;

/**Classe referente a janela Pastagens.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public final class PastureView 
{     
   //Objetos que representam a tabela de colares
   private TableView<Coordinate> tablePoints = new TableView<>();
   private TableView<Coordinate> addTable = new TableView<>();
   private TableView<Coordinate> editTable = new TableView<>();
   
   //Objeto que representa uma lista de dados (uma colecao)
   private ObservableList<Coordinate> coord = FXCollections.observableArrayList();
   private ObservableList<Coordinate> points = FXCollections.observableArrayList();
   
   private Stage subStage;
   private PastureController controller;
   private ObservableList<String> data;  
   private ComboBox<String> comboPasture;
   private ComboBox<String> comboPastureRemove;
   private ComboBox<String> comboPastureEdit;
   
   //Linhas
   private HBox buttons = new HBox(5);
   
   //TextField
   TextField nameField = new TextField ();
   TextField hectaresField = new TextField ();
   TextField latitudeField = new TextField();
   TextField longitudeField = new TextField();
   TextField editLatitudeField = new TextField();
   TextField editLongitudeField = new TextField();
   TextField editNameField = new TextField();
   TextField editHectaresField = new TextField();
   
   //Text
   private Text areaText = new Text(" ");
   private Text nameText = new Text(" ");
             
   //Label
   private Label selectPastureLabel = new Label ("Selecione o Pasto a ser Editado");
   private Label editPastureLabel = new Label ("Editar Pasto Selecionado");
   private Label infoLabel = new Label ("Pastagens Cadastradas");
   private Label coordinatesLabel = new Label ("Coordenadas do Pasto");
   private Label addLabel = new Label ("Adicionar Pastagem");
   private Label removeLabel = new Label ("Remover Pastagem");
   
   //Botões
   private Button addPasture = new Button("Novo");
   private Button removePasture = new Button("Remover");
   private Button editPasture = new Button("Editar");
   private Button showArea = new Button("Ver Área");
   private Button addButton = new Button("Adicionar");
   private Button cancelButton = new Button("Cancelar");
   private Button addInTable = new Button("Adiciona Dados");
   private Button openARFF = new Button("Abrir ARFF");
   private Button addInEditTable = new Button("Adiciona Dados");
   private Button openARFFEdit = new Button("Abrir ARFF");
   private Button removeButton = new Button("Remover");
   private Button cancelRemoveButton = new Button("Cancelar");
   private Button cancelEditButton = new Button("Cancelar");  
   private Button editButton = new Button("Editar");
   
   /**Construtor. */
   public PastureView() throws Exception
   {
        controller = new PastureController();
        
        tablePoints = formatTable();
        tablePoints.setPrefSize(300, 200);   
        tablePoints.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //elimina Ghost Column
        
        addTable = formatTable();
        addTable.setPrefSize(300, 200);
        addTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //elimina Ghost Column
        
        editTable = formatTable();
        editTable.setPrefSize(300, 200);
        editTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //elimina Ghost Column

        data = controller.selectItens();
        
        comboPasture = new ComboBox<>(data);
        comboPasture.setPromptText("Selecione");
        
        comboPastureRemove = new ComboBox<>(data);
        comboPastureRemove.setPromptText("Selecione");
        
        comboPastureEdit = new ComboBox<>(data);
        comboPastureEdit.setPromptText("Selecione");

        buttons.setPadding(new Insets(15, 12, 15, 12));
        buttons.setPrefSize(320, 55);
        buttons.setSpacing(10);
        buttons.setId("buttonhbox");
        buttons.getChildren().addAll(addPasture, removePasture, editPasture);
        buttons.setAlignment(Pos.CENTER);
        
        latitudeField.setPromptText("Latitude");
        latitudeField.setPrefWidth(80);
        longitudeField.setPromptText("Longitude");
        longitudeField.setPrefWidth(80);
        
        editLatitudeField.setPromptText("Latitude");
        editLatitudeField.setPrefWidth(80);
        editLongitudeField.setPromptText("Longitude");
        editLongitudeField.setPrefWidth(80);
        
        nameField.setPrefWidth(110);
        hectaresField.setPrefWidth(55);
        editNameField.setPrefWidth(110);
        editHectaresField.setPrefWidth(55);
        
        cancelButton.setId("cancelbutton");
        cancelButton.setPrefSize(80, 25);
        addButton.setId("addbutton");
        addButton.setPrefSize(80, 25);
        
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
        
        areaText.setId("result");
        removeLabel.setId("label-title");
        addLabel.setId("label-title");
        infoLabel.setId("label-title");  
        selectPastureLabel.setId("label-title");
        editPastureLabel.setId("label-subtitle");
        coordinatesLabel.setId("label-subtitle");
   }
      
   /**Método de execução da função. */
   public void execute()
   {      
       subStage = new Stage();
       subStage.setTitle("Pastagens Cadastradas"); 
       PastureHBox pastureHBox = new PastureHBox();
       
       GridPane grid = pastureHBox.createGrid();
       
       grid.add(buttons, 0, 0);
       grid.add(infoLabel,0 ,1);
       grid.add(pastureHBox.pastureLine(comboPasture), 0, 2);
       grid.add(pastureHBox.hectarLine(areaText), 0, 3);
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
                    controller.selectPasture(comboPasture.getValue());
                    nameText.setText(controller.getPasture().getName());
                    areaText.setText(String.valueOf(controller.getPasture().getHectars()));
                    
                    tablePoints = insertTable(controller.getPasture().getCoordinates(), tablePoints);
                    showArea.setVisible(true);
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(PastureView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
       
       //Botão para a janela Adicionar Pastagem
       addPasture.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
               try 
               {               
                   addPasture();                 
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(PastureView.class.getName()).log(Level.SEVERE, null, ex);
               }              
            }  
        });
       
       //Botão para a janela Remover Pastagem
       removePasture.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {   
               try 
               {                
                   removePasture();
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(PastureView.class.getName()).log(Level.SEVERE, null, ex);
               }
            }  
            
        });
       
       //Botão para a janela Editar Pastagem
       editPasture.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
               try 
               {               
                   editPasture();                 
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(PastureView.class.getName()).log(Level.SEVERE, null, ex);
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
               {      if(comboPasture.getValue() != null)
                      {
                        controller.showArea(comboPasture.getValue(), controller.getPasture().getCoordinates());
                      }
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(AreaView.class.getName()).log(Level.SEVERE, null, ex);
               }
              
            }  
        });
       
  
       Scene scene = new Scene(grid, 400, 650);
       scene.getStylesheets().add(PastureView.class.getResource("lib/Commons.css").toExternalForm()); 
       subStage.setScene(scene);
       subStage.show();
    }
   
    
   /**Abre uma janela para adição de uma nova pastagem. */
   public void addPasture() throws Exception 
   {
       PastureHBox pastureHBox = new PastureHBox();
       subStage = new Stage();
       subStage.setTitle("Adicionar Pastagem");  
       
       GridPane grid = pastureHBox.createGrid();
  
       //Adicionar elementos no Grid
       grid.add(addLabel, 0, 0);
       grid.add(pastureHBox.createLine(nameField, hectaresField), 0, 1);
       grid.add(addTable, 0, 3);
       grid.add(pastureHBox.addCoordinateLine(longitudeField, latitudeField, addInTable, openARFF), 0, 4);
       grid.add(pastureHBox.buttonsLine(addButton, cancelButton), 0, 6);
 
       //Função do Botao Cancelar
       cancelButton.setOnAction(new EventHandler<ActionEvent>() 
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
                
              if(latitudeField.getText().length() > 0 && longitudeField.getText().length() > 0)
               { 
                    points.add(new Coordinate( Double.parseDouble(longitudeField.getText()), Double.parseDouble(latitudeField.getText()), null)); 
                    addTable.setItems(points);

                    latitudeField.clear();
                    longitudeField.clear();
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
                   points = controller.readARFF();
                   addTable.setItems(points);
               }
               catch (Exception ex) 
               {
                        Logger.getLogger(PastureView.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
       });
       
       //Botão para adicionar os elementos do pasto cadastrado
       addButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {   
                
              if(nameField.getText().length() > 0 && hectaresField.getText().length() > 0 && !points.isEmpty())
               { 
                    try 
                    { 
                        controller.insertPasture(nameField.getText(), Float.parseFloat(hectaresField.getText()), points);
                    } 
                    catch (Exception ex) 
                    {
                        Logger.getLogger(PastureView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    subStage.close(); 
               }
            }  
            
        });
       
       Scene addScene = new Scene(grid, 400, 600);
       addScene.getStylesheets().add(PastureView.class.getResource("lib/Commons.css").toExternalForm());
       subStage.setScene(addScene);
       subStage.show();
   }
   
   
   /**Janela para remoção de uma pastagem cadastrada. */
   public void removePasture() throws Exception 
   {  
       subStage = new Stage();
       subStage.setTitle("Remover Pastagem");
       PastureHBox pastureHBox = new PastureHBox();
       
       GridPane grid = pastureHBox.createGrid();
       
       grid.add(removeLabel, 0, 0);
       grid.add(pastureHBox.pastureLine(comboPastureRemove), 0, 1);
       grid.add(pastureHBox.buttonsLine(removeButton, cancelRemoveButton), 0, 3);
       
       //Função do Botao Cancelar
       removeButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {     
                if(comboPastureRemove.getValue().length() > 0)
                {
                    try 
                    {
                        controller.removePasture(comboPastureRemove.getValue());
                        subStage.close(); 
                    } 
                    catch (Exception ex) 
                    {
                        Logger.getLogger(PastureView.class.getName()).log(Level.SEVERE, null, ex);
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
       
       Scene removeScene = new Scene(grid, 325, 250);
       removeScene.getStylesheets().add(PastureView.class.getResource("lib/Commons.css").toExternalForm());  
       subStage.setScene(removeScene);
       subStage.show();    
   }
   
   
   /**Janela para edição de uma pastagem cadastrada. */
   public void editPasture() throws Exception 
   {
       subStage = new Stage();
       subStage.setTitle("Editar Pastagem");  
       PastureHBox pastureHBox = new PastureHBox();
       final ObservableList<Coordinate> editPoints = FXCollections.observableArrayList();
       
       GridPane grid = pastureHBox.createGrid();

       editTable.setEditable(true); 
       
       //Adicionar elementos no Grid
       grid.add(selectPastureLabel, 0, 0);
       grid.add(pastureHBox.pastureLine(comboPastureEdit), 0, 1);
       grid.add(editPastureLabel, 0, 3);
       grid.add(pastureHBox.createLine(editNameField, editHectaresField), 0, 4);
       grid.add(editTable, 0, 6);
       grid.add(pastureHBox.addCoordinateLine(editLongitudeField, editLatitudeField, addInEditTable, openARFFEdit), 0, 7);
       grid.add(pastureHBox.buttonsLine(editButton, cancelEditButton), 0, 9);
       
       comboPastureEdit.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {    
                    editNameField.setText(comboPastureEdit.getValue());
                    controller.selectPasture(comboPastureEdit.getValue());
                    editTable = insertTable(controller.getPasture().getCoordinates(), editTable);                    
                    editHectaresField.setText(String.valueOf(controller.getPasture().getHectars()));  
                    editPoints.addAll(controller.getPasture().getCoordinates());
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(PastureView.class.getName()).log(Level.SEVERE, null, ex);
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
                   ObservableList<Coordinate> pointsList = controller.readARFF();
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
                try 
                {         
                    if(editNameField.getText().length() > 0 && editHectaresField.getText().length() > 0)
                    { 
                         controller.updatePasture(editNameField.getText(), Double.parseDouble(editHectaresField.getText()), editTable.getItems());
                    }      
                } 
                catch (Exception ex) 
                {
                        Logger.getLogger(PastureView.class.getName()).log(Level.SEVERE, null, ex);
                }
                subStage.close(); 
                          
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
       
       
       Scene editScene = new Scene(grid, 450, 650);
       editScene.getStylesheets().add(PastureView.class.getResource("lib/Commons.css").toExternalForm());      
       subStage.setScene(editScene);
       subStage.show();
   }
   
   
    /**Formata tabela com duas colunas. */
    public TableView<Coordinate> formatTable() throws Exception
    {
        TableView<Coordinate> tableView = new TableView<>(); 
        Callback<TableColumn<Coordinate, Double>, TableCell<Coordinate, Double>> cellFactory = new Callback<TableColumn<Coordinate, Double>, TableCell<Coordinate, Double>>() 
        {
             @Override
             public TableCell<Coordinate, Double> call(TableColumn<Coordinate, Double> p) 
             {
                 return new EditingCell();
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
   
   
    /**Insere elementos na tabela. */
    public TableView<Coordinate> insertTable(ObservableList<Coordinate> coords, TableView<Coordinate> t) throws Exception
    {         
        //Adiciona os elementos do BD na tabela 
        t.setItems(coords);
         
        //retorna a tabela formatada
        return t;
    }
    
   
    /**Classe Especial para a edição de células da TableView.*/
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