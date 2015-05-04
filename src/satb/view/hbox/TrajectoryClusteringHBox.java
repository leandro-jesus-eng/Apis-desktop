package satb.view.hbox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**Classe organiza as HBox da TrajectoryClusteringHBox.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class TrajectoryClusteringHBox 
{
    /**Construtor Default. */
    public TrajectoryClusteringHBox(){}
    
    /**Método que retorna um GridPane construído. */
    public GridPane createGrid()
    {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
    
        return grid;
    }
    
    
    /**Método que retorna os botões principais. */
    public HBox buttonsLine(Button one, Button two)
    {
        HBox buttonsLine = new HBox(10);
        buttonsLine.getChildren().addAll(one, two);
        buttonsLine.setAlignment(Pos.CENTER);
    
        return buttonsLine;
    }    

    /**Linha que retorna uma linha com uma comboBox para a seleção do algoritmo. */
    public HBox algorithmLine(ComboBox<String> comboAlgorithm)
    {
        HBox algorithmLine = new HBox(5); 
        algorithmLine.getChildren().addAll(new Label("Selecione o Algoritmo: "), comboAlgorithm);
        algorithmLine.setAlignment(Pos.BASELINE_LEFT);
        
        return algorithmLine;
    }

    /**Linha que retorna uma linha com uma comboBox de colar. */
    public HBox collarLine(ComboBox<String> comboCollar)
    {
        HBox collarLine = new HBox(5); 
        collarLine.getChildren().addAll(new Label("Colar: "), comboCollar);
        collarLine.setAlignment(Pos.BASELINE_LEFT);
        
        return collarLine;
    }
    
    /**Linha que devolve opções para hora. */
    public HBox hourLine(RadioButton rbNoOne, RadioButton rbInterval, RadioButton rbBefore, RadioButton rbAfter)
    {
        HBox hourLine = new HBox(5);
        hourLine.setAlignment(Pos.BASELINE_LEFT);
        
        hourLine.getChildren().addAll(new Label("Horário:"), rbNoOne, rbInterval, rbBefore, rbAfter);
                
        return hourLine;
    }
    
    
    /**Linha que devolve opções para data. */
    public HBox dateLine(RadioButton allDate, RadioButton oneDate, RadioButton intervalDate)
    {
        HBox dateLine = new HBox(5);
        dateLine.setAlignment(Pos.BASELINE_LEFT);
        
        dateLine.getChildren().addAll(new Label("Data: "), allDate, oneDate, intervalDate);
                
        return dateLine;
    }
    
  
    /**Linha que devolve opções para hora. */
    public HBox radioHourLine(RadioButton rbNoOne, RadioButton rbInterval, RadioButton rbBefore, RadioButton rbAfter, final TextField hourBeforeField, final TextField hourAfterField, final TextField hourBeginField, final TextField hourEndField)
    {
        final HBox radioHourLine = new HBox(5);
        radioHourLine.setAlignment(Pos.CENTER);
        final ToggleGroup tgHour = new ToggleGroup();
        
        rbNoOne.setOnAction(new EventHandler<ActionEvent>() 
        {
             @Override
             public void handle(ActionEvent e) 
             {
                 hourBeforeField.clear(); 
                 hourAfterField.clear(); 
                 hourBeginField.clear(); 
                 hourEndField.clear();
                 radioHourLine.getChildren().clear();                
             }
        });

        //Função que representa o Radio Button Depois
        rbAfter.setOnAction(new EventHandler<ActionEvent>() 
        {
             @Override
             public void handle(ActionEvent e) 
             {
                 hourBeforeField.clear(); 
                 hourAfterField.clear(); 
                 hourBeginField.clear(); 
                 hourEndField.clear();
                 radioHourLine.getChildren().clear();
                 radioHourLine.getChildren().addAll(new Label("Horário Depois:"), hourAfterField);                
             }
        });
       
        //Função que representa o Radio Button Intervalo Temporal
        rbInterval.setOnAction(new EventHandler<ActionEvent>() 
        {
             @Override
             public void handle(ActionEvent e) 
             {
                 hourBeforeField.clear(); 
                 hourAfterField.clear(); 
                 hourBeginField.clear(); 
                 hourEndField.clear();
                 radioHourLine.getChildren().clear();
                 radioHourLine.getChildren().addAll(new Label("Intervalo Horário:"), hourBeginField, new Label(" até "), hourEndField);
             }
        });
       
        //Função que representa o Radio Button Antes
        rbBefore.setOnAction(new EventHandler<ActionEvent>() 
        {
             @Override
             public void handle(ActionEvent e) 
             {
                 hourBeforeField.clear(); 
                 hourAfterField.clear(); 
                 hourBeginField.clear(); 
                 hourEndField.clear();
                 radioHourLine.getChildren().clear();
                 radioHourLine.getChildren().addAll(new Label("Horário Antes:"), hourBeforeField);
             }
        });
  
        rbNoOne.setSelected(true);
        tgHour.getToggles().addAll(rbNoOne, rbInterval, rbBefore, rbAfter);

        return radioHourLine;
    }
    
      
    /**Linha que devolve opções para data. */
    public HBox radioDateLine(RadioButton allDate, RadioButton oneDate, RadioButton intervalDate, final TextField dateBeginField, final TextField dateEndField, final TextField dateField)
    {
        final HBox radioDateLine = new HBox(5);
        radioDateLine.setAlignment(Pos.CENTER);
        final ToggleGroup tgDate = new ToggleGroup();
        
        //Função que representa o Radio Button allDate
        allDate.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {
               dateField.clear(); 
               dateBeginField.clear(); 
               dateEndField.clear();
               radioDateLine.getChildren().clear();                
            }
        });
       
        //Função que representa o Radio Button oneDate
        oneDate.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {
                
                dateBeginField.clear(); 
                dateEndField.clear();
                radioDateLine.getChildren().clear();
                radioDateLine.getChildren().addAll(new Label("Dia: "), dateField);                
            }
        });
       
        //Função que representa o Radio Button intervalDate
        intervalDate.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {               
               dateField.clear(); 
               radioDateLine.getChildren().clear();
               radioDateLine.getChildren().addAll(new Label("Intervalo de Dias:"), dateBeginField, new Label(" até "), dateEndField);
            }
        });
       
        allDate.setSelected(true);
        tgDate.getToggles().addAll(allDate, oneDate, intervalDate);
       
        return radioDateLine;
    }
    
    
    /**Retorna o valor da data a ser posicionado na janela de resultados. */
    public String date(String date, String dateBegin, String dateEnd)
    {
        if(!date.isEmpty())
        {
            return date;
        }
        else if(!dateBegin.isEmpty() && !dateEnd.isEmpty())
        {
            return "Entre " + dateBegin + " e " + dateEnd + ".";
        }
        else
        {
            return "Todas as coletadas.";
        }
    }
    
    /**Retorna o valor da hora a ser posicionado na janela de resultados. */
    public String hour(String hourBefore, String hourAfter, String hourBegin, String hourEnd)
    {
        if(!hourBefore.isEmpty())
        {
            return "Antes das " + hourBefore + ".";
        }
        else if(!hourAfter.isEmpty())
        {
            return "Depois das " + hourAfter + ".";
        }
        else if(!hourBegin.isEmpty() && !hourEnd.isEmpty())
        {
            return "Entre " + hourBegin + " e " + hourEnd + ".";
        }
        else
        {
            return "Todos os coletados.";
        }
    }
    
    /**Retorna uma linha com os elementos de tempo. */
    public HBox timeLine(String date, String dateBegin, String dateEnd, String hourBefore, String hourAfter, String hourBegin, String hourEnd)
    {
        HBox timeLine = new HBox(5);
        timeLine.setAlignment(Pos.BASELINE_LEFT);             
        timeLine.getChildren().addAll(new Label("Data(s): "), new Text(date(date, dateBegin, dateEnd)), new Label(" Horário: "), new Text(hour(hourBefore, hourAfter, hourBegin, hourEnd)));
   
        return timeLine;       
    }
    
    
    /**Retorna uma linha com os elementos relacionados ao algoritmo. */
    public HBox parameterLine(String algorithm, String eps, String minPts)
    {
        HBox parameterLine = new HBox(5);
        parameterLine.setAlignment(Pos.BASELINE_LEFT);
        
        switch(algorithm)
        {    
            case "DBSCAN":
                parameterLine.getChildren().addAll(new Label("Distância: "), new Text(eps), new Label(" Min de Pontos por Região: "), new Text(minPts));
                break;    
                 
             default:
                 break;
        }
        
        return parameterLine;       
    }
    
    
    /**Retorna uma linha com a id do colar e o número de pontos selecionados. */
    public HBox collarResultLine(String collar, int size)
    {
        HBox collarResultLine = new HBox(5); 
        collarResultLine.getChildren().addAll(new Label("Colar: "), new Text(collar), new Label("Número de Elementos: "), new Text(String.valueOf(size)));
        collarResultLine.setAlignment(Pos.BASELINE_LEFT);
        
        return collarResultLine;
    }
    
    
    /**Retorna uma linha com com o id da área de concentração (cluster). */
    public HBox clusterLine(int index)
    {
        HBox clusterLine = new HBox();
        clusterLine.setAlignment(Pos.BASELINE_LEFT);
        clusterLine.getChildren().addAll(new Label("Área de Concentração " + (index+1)  + ""));
        
        return clusterLine;       
    }
    
    /**Devolve uma linha com o número de pontos contidos. */
    public HBox elementsInClusterLine(int size, int total)
    {
        HBox elements = new HBox(5);
        elements.setAlignment(Pos.BASELINE_LEFT);
        float part = (float)size * 100 / (float)total;
        String percent = String.format("%.2f", part); 
        
        elements.getChildren().addAll(new Label("N° de Pontos Contidos: "), new Text(String.valueOf(size)), new Text(" / " + percent), new Label("% dos pontos."));
        
        return elements;
    }
    
    
    /**Devolve uma linha com os elementos dentro do clusters e outros fora. */
    public HBox outlineLine(String elements, String outline)
    {
        HBox outlineLine = new HBox(5);
        outlineLine.setAlignment(Pos.BASELINE_LEFT);
        outlineLine.getChildren().addAll(new Label("Elementos em Área: "), new Text(elements), new Label(" Ruídos/Outline: "), new Text(outline));
        
        return outlineLine;
    }
    
    
}
