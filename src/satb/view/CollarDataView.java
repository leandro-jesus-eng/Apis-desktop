package satb.view;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import satb.controller.CollarDataController;

/**Classe referente a janela Coletor de Dados Georreferenciados.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class CollarDataView
{     
    //Atributos
    private Stage subStage;
    private CollarDataController controller;
    private ComboBox<String> comboTypeSource; 
    
    //HBox
    HBox typeLine = new HBox(5);
    HBox buttons = new HBox(10);
    
    //Buttons
    private Button execButton = new Button("Abrir");
    private Button closeButton = new Button("Fechar");
    
    /**Construtor Default. */
    public CollarDataView()
    {
        controller = new CollarDataController();
        
        comboTypeSource = new ComboBox<>(FXCollections.observableArrayList ("ARFF", "OTAG", "OTAG2nd", "VenusGPS"));
        comboTypeSource.setPrefWidth(80);
        comboTypeSource.setPromptText("Selecione");
        
        typeLine.setAlignment(Pos.CENTER);
        typeLine.getChildren().addAll(new Label("Arquivo de Entrada: "), comboTypeSource);
        
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(execButton, closeButton);
        
        closeButton.setId("cancelbutton");
        execButton.setId("addbutton"); 
    
    }
   
    /**Método que executa a janela da classe. */
    public void execute() throws Exception
    {
        subStage = new Stage();
        subStage.setTitle("Sistema Coletor de Dados");

        GridPane grid = new GridPane();
        grid.getStylesheets().add(CollarDataView.class.getResource("lib/Commons.css").toExternalForm());
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Text title = new Text("Coletor de Dados Georeferênciados"); 
        title.setId("maintitle");
                      
        grid.add(title, 1, 2);
        grid.add(typeLine, 1, 3);
        grid.add(buttons, 1, 7);

        //Botão que executa a ação desejada
        execButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {
                switch(comboTypeSource.getValue())
                {
                    case "OTAG":
                        controller.readTxt();
                        break;
                        
                    case "OTAG2nd":
                        controller.readTxt2nd("0007");
                        break;
                    
                    case "VenusGPS":
                        controller.readTxtVenus("0007");
                        break;

                        
                    case "ARFF":
                        try 
                        {
                            controller.readARFF();
                        } 
                        catch (Exception ex) 
                        {
                            Logger.getLogger(CollarDataView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    
                }
                    
            }
        });

        //Botão que fecha a janela
        closeButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {
                subStage.close();
            }
        });
        
        Scene scene = new Scene(grid, 325, 300);
        subStage.setScene(scene);
        subStage.show();
    }
}