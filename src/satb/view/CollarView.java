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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import satb.controller.CollarController;
import satb.model.Collar;
import satb.view.hbox.CollarHBox;

/**Classe referente a janela Colares.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public final class CollarView 
{ 
    //Objeto que representa a tabela de colares
    private TableView<Collar> table = new TableView<>();
    
    //Objeto que representa uma lista de dados (uma colecao)
    private ObservableList<Collar> data = FXCollections.observableArrayList();
    
    private Stage subStage = new Stage();
    private CollarController controller;
    private ListView<String> dateList = new ListView<>();
    private ComboBox<String> comboCollar;
    private ObservableList<String> collars;  
    private ComboBox<String> comboCollarEdit;
    private ComboBox<String> comboCollarDate;
    
    //Linhas
    private HBox buttons = new HBox();
  
    //Label
    private Label info = new Label("Lista de Colares");
    private Label addInfo = new Label("Adicionar Colar");
    private Label removeInfo = new Label("Remover Colar");
    private Label selectCollarLabel = new Label ("Selecione o Colar a ser Editado");
    private Label editCollarLabel = new Label ("Editar Collar Selecionado");
    private Label collarDataLabel = new Label ("Datas de Coleta");
    
    //Textfield
    private final TextField collarField = new TextField();
    private final TextField companyField = new TextField();
    private final TextField editCollarField = new TextField();
    private final TextField editCompanyField = new TextField();
    
    //Buttons
    private Button addCollar = new Button("Novo");
    private Button removeCollar = new Button("Remover");
    private Button editCollar = new Button("Editar");
    private Button collarDate = new Button("Datas no Colar");
    private Button addCancel = new Button("Cancelar");
    private Button addButton = new Button("Adicionar");
    private Button removeCancel = new Button("Cancelar");
    private Button removeButton = new Button("Remover");
    private Button cancelEditButton = new Button("Cancelar");
    private Button closeButton = new Button("Fechar");
    private Button editButton = new Button("Editar");
    
    /**Contrutor.*/
    public CollarView() throws Exception
    {
        controller = new CollarController();
        
        table = formatTable();
        table.setPrefHeight(200); 
        
        dateList.setPrefHeight(200);
        dateList.setPrefWidth(50);
        
        collars = controller.selectItens();
        comboCollar = new ComboBox<>(collars);
        comboCollar.setPromptText("Selecione");
        comboCollar.setPrefWidth(80);
        
        comboCollarEdit = new ComboBox<>(collars);
        comboCollarEdit.setPromptText("Selecione");
        comboCollarEdit.setPrefWidth(80);
        
        comboCollarDate = new ComboBox<>(collars);
        comboCollarDate.setPromptText("Selecione");
        comboCollarDate.setPrefWidth(80);
        
        buttons.setPadding(new Insets(15, 12, 15, 12));
        buttons.setPrefSize(320, 55);
        buttons.setSpacing(10);
        buttons.setId("buttonhbox");
        buttons.getChildren().addAll(addCollar, removeCollar, editCollar, collarDate);
        buttons.setAlignment(Pos.CENTER);
        
        addCancel.setId("cancelbutton");
        addCancel.setPrefSize(80, 25);
        addButton.setId("addbutton");
        addButton.setPrefSize(80, 25);
        
        collarField.setPrefWidth(80);
        companyField.setPrefWidth(100);
        
        removeCancel.setId("cancelbutton");
        removeCancel.setPrefSize(80, 25);
        removeButton.setId("addbutton");
        removeButton.setPrefSize(80, 25);
        
        cancelEditButton.setId("cancelbutton");
        cancelEditButton.setPrefSize(80, 25);
        editButton.setId("addbutton");
        editButton.setPrefSize(80, 25);
        
        closeButton.setId("cancelbutton");
        closeButton.setPrefSize(80, 25);
        
        info.setId("label-title");  
        addInfo.setId("label-title"); 
        removeInfo.setId("label-title");
        selectCollarLabel.setId("label-title");
        collarDataLabel.setId("label-title");
        editCollarLabel.setId("label-subtitle");
    }
    
    /** Método que executa a função principal da janela.*/
    public void execute()
    {
        subStage = new Stage();
        subStage.setTitle("Colares para Rastreabilidade"); 
        CollarHBox collarHBox = new CollarHBox();
       
        GridPane grid = collarHBox.createGrid();

        grid.add(buttons, 0, 0);
        grid.add(info, 0, 2);
        grid.add(table, 0, 3);
        
        //Botão para a janela Adicionar Colar
        addCollar.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e)
            {
               try 
               {               
                   addCollar();       
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(CollarView.class.getName()).log(Level.SEVERE, null, ex);
               }
              
            }  
        });
       
       //Botão para a janela Remover Colar 
       removeCollar.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
               try 
               {               
                   removeCollar();     
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(CollarView.class.getName()).log(Level.SEVERE, null, ex);
               }
              
            }  
        });
        
       //Botão para a janela editar Collar
       editCollar.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
               try 
               {               
                   editCollar();   
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(CollarView.class.getName()).log(Level.SEVERE, null, ex);
               }
              
            }  
        });
       
       //Botão para a janela editar Collar
       collarDate.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
               try 
               {               
                   seeCollarDates();  
               } 
               catch (Exception ex) 
               {
                   Logger.getLogger(CollarView.class.getName()).log(Level.SEVERE, null, ex);
               }
              
            }  
        });

        Scene scene = new Scene(grid , 400, 550);
        scene.getStylesheets().add(CollarView.class.getResource("lib/Commons.css").toExternalForm());
        subStage.setScene(scene);
        subStage.show(); 
    }
    
  
   /**Abre uma janela para adição de um novo colar.*/
   public void addCollar() throws Exception 
   {
       subStage = new Stage();
       subStage.setTitle("Adicionar Colar");  
       CollarHBox collarHBox = new CollarHBox();
       
       GridPane grid = collarHBox.createGrid();
       
       grid.add(addInfo, 0, 1);
       grid.add(collarHBox.collarLine(collarField, companyField), 0, 2);
       grid.add(collarHBox.buttonsLine(addButton, addCancel), 0, 4);
 
       //Botão para adicionar os elementos do pasto cadastrado
       addButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {   
                
              if(collarField.getText().length() > 0 && companyField.getText().length() > 0)
               { 
                    try 
                    { 
                        controller.insertData(new Collar (collarField.getText(), companyField.getText()));
                        //Limpar os campos após executar a função
                        collarField.clear();
                        companyField.clear();
                    } 
                    catch (Exception ex) 
                    {
                        Logger.getLogger(CollarView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    subStage.close(); 
               }
            }  
            
        });
       
              
       //Função do Botao Cancelar
       addCancel.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {   
                //Limpar os campos após executar a função
                collarField.clear();
                companyField.clear();
                subStage.close(); 
            }  
        });
       
       Scene addScene = new Scene(grid, 500, 300);
       addScene.getStylesheets().add(CollarView.class.getResource("lib/Commons.css").toExternalForm());
       subStage.setScene(addScene);
       subStage.show();
   }
   
   
   /**Abre uma janela para remoção de um novo colar.*/
   public void removeCollar() throws Exception 
   {
       subStage = new Stage();
       subStage.setTitle("Remover Colar");  
       CollarHBox collarHBox = new CollarHBox();
       
       GridPane grid = collarHBox.createGrid();
   
       grid.add(removeInfo, 0, 1);
       grid.add(collarHBox.searchLine(comboCollar), 0, 2);
       grid.add(collarHBox.buttonsLine(removeButton, removeCancel), 0, 4);
  
       //Botão para adicionar os elementos do pasto cadastrado
       removeButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {        
              if(comboCollar.getValue().length() > 0)
               { 
                    try 
                    { 
                        controller.removeCollar(comboCollar.getValue());
                    } 
                    catch (Exception ex) 
                    {
                        Logger.getLogger(CollarView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    subStage.close(); 
               }
            }  
            
        });
       
       
       //Função do Botao Cancelar
       removeCancel.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {     

                subStage.close(); 
            }  
        });
          
       Scene removeScene = new Scene(grid, 500, 300);
       removeScene.getStylesheets().add(CollarView.class.getResource("lib/Commons.css").toExternalForm());
       subStage.setScene(removeScene);
       subStage.show();
   }
    
   
   /**Abre uma janela para a edição de dados de um novo colar.*/
   public void editCollar() throws Exception 
   {
       subStage = new Stage();
       subStage.setTitle("Editar Colar");  
       CollarHBox collarHBox = new CollarHBox();
       
       GridPane grid = collarHBox.createGrid();    
       
       //Adicionar elementos no Grid
       grid.add(selectCollarLabel, 0, 0);
       grid.add(collarHBox.searchLine(comboCollarEdit), 0, 1);
       grid.add(editCollarLabel, 0, 3);
       grid.add(collarHBox.collarLine(editCollarField, editCompanyField), 0, 4);
       grid.add(collarHBox.buttonsLine(editButton, cancelEditButton), 0, 6);
       
       comboCollarEdit.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {
                     editCollarField.setText(comboCollarEdit.getValue());
                     controller.selectCollar(comboCollarEdit.getValue());
                     editCompanyField.setText(controller.getCollar().getCompany());  
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(CollarView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
       

       //Função do Botao Editar
       editButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {            
                if(editCollarField.getText().length() > 0 && editCompanyField.getText().length() > 0)
                {
                    try 
                    {  
                       controller.updateCollar(editCollarField.getText(), editCompanyField.getText());
                       //Limpar os campos após executar a função
                       editCollarField.clear(); 
                       editCompanyField.clear();
                       
                    } 
                    catch (Exception ex) 
                    {
                        Logger.getLogger(CollarView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    subStage.close(); 

                 }         
            } 
       });
       
       //Função do Botão Cancelar
       cancelEditButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {   
                //Limpar os campos após executar a função
                editCollarField.clear(); 
                editCompanyField.clear();
                subStage.close(); 
            }  
       });
       
       
       Scene editScene = new Scene(grid, 550, 350);
       editScene.getStylesheets().add(CollarView.class.getResource("lib/Commons.css").toExternalForm());      
       subStage.setScene(editScene);
       subStage.show();
   }
   
   
   /**Abre uma janela para mostrar as datas de coletas presentes em cada colar.*/
   public void seeCollarDates() throws Exception 
   {
       subStage = new Stage();
       subStage.setTitle("Datas de coletas dos colares");  
       CollarHBox collarHBox = new CollarHBox();
       
       GridPane grid = collarHBox.createGrid();
          
       HBox buttonLine = new HBox(5); 
       buttonLine.getChildren().addAll(closeButton);
       buttonLine.setAlignment(Pos.CENTER);
       
       //Adicionar elementos no Grid
       grid.add(collarDataLabel, 0, 0);
       grid.add(collarHBox.searchLine(comboCollarDate), 0, 1);
       grid.add(dateList, 0, 2);
       grid.add(buttonLine, 0, 4);
       
       comboCollarDate.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {
                try 
                {
                     dateList.getItems().clear();
                     dateList.getItems().addAll(controller.selectCollarDates(comboCollarDate.getValue()));
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(CollarView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });

       //Função do Botao Fechar
       closeButton.setOnAction(new EventHandler<ActionEvent>() 
       {
            @Override
            public void handle(ActionEvent e)
            {   
                subStage.close(); 
            }  
        });
       
       
       Scene dateScene = new Scene(grid, 300, 450);
       dateScene.getStylesheets().add(CollarView.class.getResource("lib/Commons.css").toExternalForm());      
       subStage.setScene(dateScene);
       subStage.show();
   }
   
   
    
    /**Formata a tabela com as informações dos colares.*/
    public TableView<Collar> formatTable() throws Exception
    {
         TableColumn<Collar,String> tcollar = new TableColumn<>("Colar");
         tcollar.setCellValueFactory(new PropertyValueFactory<Collar,String>("collar"));
         tcollar.setPrefWidth(159);
         
         TableColumn<Collar,String> tcompany = new TableColumn<>("Empresa");
         tcompany.setCellValueFactory(new PropertyValueFactory<Collar,String>("company"));
         tcompany.setPrefWidth(159);
  
         //Adiciona os elementos do BD na tabela 
         controller = new CollarController();
         data = controller.selectAll();
         table.setItems(data);
         
         //Adicionando as Colunas a tabela
         table.getColumns().add(tcollar);
         table.getColumns().add(tcompany);
       
        //retorna a tabela formatada
        return table;
    }
        
}