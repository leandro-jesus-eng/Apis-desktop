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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import satb.controller.PastureController;
import satb.controller.TreeController;
import satb.model.Tree;
import satb.view.hbox.TreeHBox;

/**Classe referente a janela Árvores.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public final class TreeView 
{
   //Objetos que representam a tabela de colares
   private TableView<Tree> table = new TableView<>();
   
   //Objeto que representa uma lista de dados (uma colecao)
   private ObservableList<Tree> coord = FXCollections.observableArrayList();
   
   //Atributos 
   private Stage subStage;
   private PastureController controller;
   private TreeController treeController;
   private ObservableList<String> data;  
   private ComboBox<String> comboPasture;
   private ComboBox<String> comboPastureAdd;
   private ComboBox<String> comboPastureRemove;
   private ComboBox<String> comboTree;
   private ComboBox<String> comboTreeEdit;
   private ComboBox<String> comboPastureEdit;
   private ComboBox<String> comboPastureEdited;
   
   //Linhas
   private HBox buttons = new HBox(5);

   //Label
   private Label selectTreeLabel = new Label ("Selecione a Árvore a ser Editada");
   private Label editTreeLabel = new Label ("Editar Árvore Selecionada");
   private Label removeTreeLabel = new Label ("Remover Árvore");
   private Label addTreeLabel = new Label ("Adicionar Árvore");
   private Label infoLabel = new Label ("Árvores Cadastradas");
   
   //TextField - Adicionar
   TextField latitudeField = new TextField();
   TextField longitudeField = new TextField();
   TextField numberTreeField = new TextField();
   TextField editLatitudeField = new TextField();
   TextField editLongitudeField = new TextField();
   TextField editNumberTreeField = new TextField();

   //Botoes
   private Button addTree = new Button("Novo");
   private Button removeTree = new Button("Remover");
   private Button editTree = new Button("Editar");
   private Button addButton = new Button("Adiciona");
   private Button removeButton = new Button("Remover");
   private Button editButton = new Button("Editar");
   private Button cancelAddButton = new Button("Cancelar");
   private Button cancelRemoveButton = new Button("Cancelar");
   private Button cancelEditButton = new Button("Cancelar");   
   
   /**Construtor. */
   public TreeView() throws Exception
   {
        controller = new PastureController();
        treeController = new TreeController();
        
        table = formatTable();
        table.setPrefHeight(300);   
        
        data = controller.selectItens();
        comboPasture = new ComboBox<>(data);
        comboPasture.setPromptText("Selecione");
        
        comboPastureAdd = new ComboBox<>(data);
        comboPastureAdd.setPromptText("Selecione"); 
        
        comboPastureRemove = new ComboBox<>(data);
        comboPastureRemove.setPromptText("Selecione");
        
        comboPastureEdit = new ComboBox<>(data);
        comboPastureEdit.setPromptText("Selecione");
        
        comboPastureEdited = new ComboBox<>(data);     
        
        comboTree = new ComboBox<>();
        comboTree.setPromptText("Selecione"); 
        comboTree.setPrefWidth(80);
        
        comboTreeEdit = new ComboBox<>();
        comboTreeEdit.setPromptText("Selecione"); 
        comboTreeEdit.setPrefWidth(80);
        
        buttons.setPadding(new Insets(15, 12, 15, 12));
        buttons.setPrefSize(320, 55);
        buttons.setSpacing(10);
        buttons.setId("buttonhbox");
        buttons.getChildren().addAll(addTree, removeTree, editTree);
        buttons.setAlignment(Pos.CENTER);
             
        cancelAddButton.setId("cancelbutton");
        cancelAddButton.setPrefSize(80, 25);
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
        
        numberTreeField.setPrefWidth(85);
        latitudeField.setPrefWidth(100);
        longitudeField.setPrefWidth(100);
        
        infoLabel.setId("label-title");
        addTreeLabel.setId("label-title"); 
        removeTreeLabel.setId("label-title"); 
        selectTreeLabel.setId("label-title");
        editTreeLabel.setId("label-subtitle");
   }
      
   /**Método de execução da função. */
   public void execute()
   {
       subStage = new Stage();
       subStage.setTitle("Árvores Cadastradas");
       TreeHBox treeHBox = new TreeHBox();
       
       GridPane grid = treeHBox.createGrid();
       
       grid.add(buttons,0,0);
       grid.add(infoLabel,0,1);
       grid.add(treeHBox.pastureLine(comboPasture), 0, 2);
       grid.add(table, 0, 3);
     
       comboPasture.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {   
                    table = insertTable(comboPasture.getValue(), table); 
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(TreeView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
       
       //Botão para a janela Adicionar Árvore
       addTree.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
               try 
               {               
                   addTree();              
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(TreeView.class.getName()).log(Level.SEVERE, null, ex);
               }
              
            }  
        });
       
       //Botão para a janela Remover Árvore
       removeTree.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
               try 
               {               
                   removeTree();                  
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(TreeView.class.getName()).log(Level.SEVERE, null, ex);
               }
              
            }  
        });
       
        //Botão para a janela Remover Árvore
       editTree.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
               try 
               {               
                   editTree();                 
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(TreeView.class.getName()).log(Level.SEVERE, null, ex);
               }
              
            }  
        });
            
       Scene scene = new Scene(grid, 350, 550);
       scene.getStylesheets().add(TreeView.class.getResource("lib/Commons.css").toExternalForm()); 
       subStage.setScene(scene);
       subStage.show();
    }
   
 
   /**Abre uma janela para adição da coordenada de uma árvore em uma  pastagem. */
   public void addTree() throws Exception 
   {
       subStage = new Stage();
       subStage.setTitle("Adicionar Árvore");  
       TreeHBox treeHBox = new TreeHBox();
       
       GridPane grid = treeHBox.createGrid();
              
       //Adicionar elementos no Grid
       grid.add(addTreeLabel, 0, 0);
       grid.add(treeHBox.pastureLine(comboPastureAdd), 0, 1);
       grid.add(treeHBox.treeIDLine(numberTreeField), 0, 2);
       grid.add(treeHBox.pointsLine(latitudeField, longitudeField), 0, 3);
       grid.add(treeHBox.buttonsLine(addButton, cancelAddButton), 0, 5);
       
       //Função do Botao Adicionar
       addButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {            
                if(latitudeField.getText().length() > 0 && longitudeField.getText().length() > 0 && comboPastureAdd.getValue().length() > 0 && numberTreeField.getText().length() > 0 )
                {
                    try 
                    {
                       
                       treeController.insertTree(comboPastureAdd.getValue(), numberTreeField.getText(), Double.parseDouble(latitudeField.getText()), Double.parseDouble(longitudeField.getText()));
                    } 
                    catch (Exception ex) 
                    {
                        Logger.getLogger(TreeView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    subStage.close(); 
                 }  
            }  
       });
       
       
        //Função do Botao Cancelar
       cancelAddButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {     
                subStage.close(); 
            }  
       });
       
       
       Scene addScene = new Scene(grid, 450, 300);
       addScene.getStylesheets().add(TreeView.class.getResource("lib/Commons.css").toExternalForm());
       
       subStage.setScene(addScene);
       subStage.show();
   }
   
   
   /**Abre uma janela para adição da coordenada de uma árvore em uma pastagem. */
   public void removeTree() throws Exception 
   {
       subStage = new Stage();
       subStage.setTitle("Remover a Árvore");
       TreeHBox treeHBox = new TreeHBox();
              
       GridPane grid = treeHBox.createGrid();
              
       //Adicionar elementos no Grid
       grid.add(removeTreeLabel, 0, 0);
       grid.add(treeHBox.searchLine(comboPastureRemove, comboTree), 0, 1);
       grid.add(treeHBox.buttonsLine(removeButton, cancelRemoveButton), 0, 3);
       
       comboPastureRemove.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {
                     comboTree.getItems().clear();
                     comboTree.getItems().addAll(treeController.selectItem(comboPastureRemove.getValue()));
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(TreeView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
       
       
       //Função do Botao Remover
       removeButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {            
                if(comboTree.getValue().length() > 0)
                {
                    try 
                    {
                        treeController.removeTree(comboTree.getValue());
                        subStage.close(); 
                    } 
                    catch (Exception ex) 
                    {
                        Logger.getLogger(TreeView.class.getName()).log(Level.SEVERE, null, ex);
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
       removeScene.getStylesheets().add(TreeView.class.getResource("lib/Commons.css").toExternalForm());
       
       subStage.setScene(removeScene);
       subStage.show();
   }
   
   
   /**Janela para edição de uma árvore cadastrada. */
   public void editTree() throws Exception 
   {
       subStage = new Stage();
       subStage.setTitle("Editar Árvore");  
       TreeHBox treeHBox = new TreeHBox();
       
       GridPane grid = treeHBox.createGrid();
       
       //Adicionar elementos no Grid
       grid.add(selectTreeLabel, 0, 0);
       grid.add(treeHBox.searchLine(comboPastureEdit, comboTreeEdit), 0, 1);
       grid.add(editTreeLabel, 0, 3);
       grid.add(treeHBox.pastureLine(comboPastureEdited), 0, 4);
       grid.add(treeHBox.treeIDLine(editNumberTreeField), 0, 5);
       grid.add(treeHBox.pointsLine(editLatitudeField, editLongitudeField), 0, 6);
       grid.add(treeHBox.buttonsLine(editButton, cancelEditButton), 0, 7);
       
       comboPastureEdit.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {
                     comboTreeEdit.getItems().clear();
                     comboTreeEdit.getItems().addAll(treeController.selectItem(comboPastureEdit.getValue()));
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(TreeView.class.getName()).log(Level.SEVERE, null, ex);
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
                    comboPastureEdited.setValue(comboPastureEdit.getValue());
                    editNumberTreeField.setText(comboTreeEdit.getValue());
                    treeController.selectTree(comboPastureEdit.getValue(), comboTreeEdit.getValue());
                    editLatitudeField.setText(String.valueOf(treeController.getTree().getLatitude()));
                    editLongitudeField.setText(String.valueOf(treeController.getTree().getLongitude()));
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(TreeView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
       
       
       
       //Função do Botao Edição
       editButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {            
                if(editLatitudeField.getText().length() > 0 && editLongitudeField.getText().length() > 0 && comboPastureEdited.getValue().length() > 0 && editNumberTreeField.getText().length() > 0 )
                {
                    try 
                    {  
                       treeController.updateTree(comboPastureEdited.getValue(), editNumberTreeField.getText(), Double.parseDouble(editLatitudeField.getText()), Double.parseDouble(editLongitudeField.getText()));
                    } 
                    catch (Exception ex) 
                    {
                        Logger.getLogger(TreeView.class.getName()).log(Level.SEVERE, null, ex);
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
       
       
       Scene editScene = new Scene(grid, 550, 350);
       editScene.getStylesheets().add(TreeView.class.getResource("lib/Commons.css").toExternalForm());
       
       subStage.setScene(editScene);
       subStage.show();
   }
    
  
   /**Formata tabela com três colunas (Árvore - Latitude - Longitude). */
   public TableView<Tree> formatTable() throws Exception
   {
        TableView<Tree> tableView = new TableView<>();
        
        TableColumn<Tree, String> tnt = new TableColumn<>("Árvore");
        tnt.setCellValueFactory(new PropertyValueFactory<Tree, String>("numberTree"));
        tnt.setPrefWidth(85); 
        
        TableColumn<Tree, Double> tlon = new TableColumn<>("Longitude");
        tlon.setCellValueFactory(new PropertyValueFactory<Tree, Double>("longitude"));
        tlon.setPrefWidth(117);
        
        TableColumn<Tree, Double> tlat = new TableColumn<>("Latitude");
        tlat.setCellValueFactory(new PropertyValueFactory<Tree, Double>("latitude"));
        tlat.setPrefWidth(117);    

        //Adicionando as Colunas a tabela
        tableView.getColumns().add(tnt);
        tableView.getColumns().add(tlon);
        tableView.getColumns().add(tlat);
         
        //retorna a tabela formatada
        return tableView;
    }
    
    
    /**Insere elementos na tabela. */
    public TableView<Tree> insertTable(String name, TableView<Tree> t) throws Exception
    {         
         //Adiciona os elementos do BD na tabela 
         treeController = new TreeController();
         coord = treeController.selectAll(name);
         t.setItems(coord);
         
         //retorna a tabela formatada
         return t;
    }
    
}