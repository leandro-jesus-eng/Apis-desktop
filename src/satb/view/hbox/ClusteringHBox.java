package satb.view.hbox;

import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


/**Classe organiza as HBox da ClusteringView.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class ClusteringHBox 
{
    /**Construtor Default.*/
    public ClusteringHBox(){}
    
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
    

    /**Linha que devolve opções para hora. */
    public HBox hourLine(RadioButton rbNoOne, RadioButton rbInterval, RadioButton rbBefore, RadioButton rbAfter)
    {
        HBox hourLine = new HBox(5);
        hourLine.setAlignment(Pos.BASELINE_LEFT);
        
        hourLine.getChildren().addAll(new Label("Horário:"), rbNoOne, rbInterval, rbBefore, rbAfter);
                
        return hourLine;
    }
    
    
    /**Linha que devolve opções para data.*/
    public HBox dateLine(RadioButton allDate, RadioButton oneDate, RadioButton intervalDate)
    {
        HBox dateLine = new HBox(5);
        dateLine.setAlignment(Pos.BASELINE_LEFT);
        
        dateLine.getChildren().addAll(new Label("Data: "), allDate, oneDate, intervalDate);
                
        return dateLine;
    }
    
  
    /**Linha que devolve opções para hora.*/
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
    
    
    
    /**Linha que devolve opções para data.*/
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
    
    
    /**Retorna o valor da data a ser posicionado na janela de resultados.*/
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
    
    /**Retorna o valor da hora a ser posicionado na janela de resultados.*/
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
    
    /**Devolve uma linha com os elementos de tempo.*/
    public HBox collarLine(ObservableList<String> collars, String numberOfElements)
    {
        HBox collarLine = new HBox(5);   
        
        String collarList = "";
        Iterator<String> iterator = collars.iterator();
        
        while(iterator.hasNext())
        {
            collarList = collarList.concat(iterator.next());
            if(iterator.hasNext())
            {
                collarList = collarList.concat(", ");
            }           
        } 
           
        collarLine.getChildren().addAll(new Label("Colar(es): "), new Text(collarList), new Label("  N° de Coordenadas: "), new Text(numberOfElements));
        collarLine.setAlignment(Pos.BASELINE_LEFT);
       
        return collarLine;       
    }
   
    /**Devolve uma linha com os elementos de tempo.*/
    public HBox timeLine(String date, String dateBegin, String dateEnd, String hourBefore, String hourAfter, String hourBegin, String hourEnd)
    {
        HBox timeLine = new HBox(5);
        timeLine.setAlignment(Pos.BASELINE_LEFT);             
        timeLine.getChildren().addAll(new Label("Data(s): "), new Text(date(date, dateBegin, dateEnd)), new Label(" Horário: "), new Text(hour(hourBefore, hourAfter, hourBegin, hourEnd)));
   
        return timeLine;       
    }
    
    
    /**Devolve uma linha com os elementos relacionados ao algoritmo.*/
    public HBox parameterLine(String algorithm, String numberOfClusters, String eps, String alpha, String minPts, String neighbors)
    {
        HBox parameterLine = new HBox(5);
        parameterLine.setAlignment(Pos.BASELINE_LEFT);
        
        switch(algorithm)
        {
            case "DBSCAN":
                parameterLine.getChildren().addAll(new Label("Epsilon: "), new Text(eps), new Label(" Pts. Min. em Cluster/Área: "), new Text(minPts));
                break;

            case "K-Means":
               parameterLine.getChildren().addAll(new Label("N° de Clusters/Áreas: "), new Text(numberOfClusters));
               break;
                
            case "LSDBC":
                parameterLine.getChildren().addAll(new Label("Alpha: "), new Text(alpha), new Label(" N° de Vizinhos: "), new Text(neighbors));
                break;    
                
             case "PAM":
               parameterLine.getChildren().addAll(new Label("N° de Clusters/Áreas: "), new Text(numberOfClusters));
               break;
                 
             default:
                 break;
        }
        
        return parameterLine;       
    }
    
    
    /**Devolve uma linha com com o id do cluster.*/
    public HBox clusterLine(int index)
    {
        HBox clusterLine = new HBox();
        clusterLine.setAlignment(Pos.BASELINE_LEFT);
        clusterLine.getChildren().addAll(new Label("Área/Cluster " + (index+1)  + ""));
        
        return clusterLine;       
    }
    
    
    /**Devolve uma linha com informação referente ao centro do cluster.*/
    public HBox clusterCenterLine(String algorithm, String middle)
    {
       HBox clusterCenterLine = new HBox(5);
       clusterCenterLine.setAlignment(Pos.BASELINE_LEFT);   
        
       switch(algorithm)
       {
           case "K-Means":    
                clusterCenterLine.getChildren().addAll(new Label("Centro da Área/Cluster (Centróide): "), new Text(middle));
                break;
       
           default:    
                clusterCenterLine.getChildren().addAll(new Label("Centro da Área/Cluster (Medóide): "), new Text(middle));
                break;       
      }
        
        return clusterCenterLine;       
    }
    
    
    
    /**Devolve uma linha com o número de pontos contidos.*/
    public HBox elementsInClusterLine(int size, int total)
    {
        HBox elements = new HBox(5);
        elements.setAlignment(Pos.BASELINE_LEFT);
        float part =  (float)size * 100 / (float)total;
        String percent = String.format("%.2f", part); 
        
        elements.getChildren().addAll(new Label("N° de Pontos Contidos: "), new Text(String.valueOf(size)),new Label(" %: "), new Text(percent));
        
        return elements;
    }
    
    
    /**Devolve uma linha com os elementos dentro do clusters e outros fora.*/
    public HBox outlineLine(String elements, String outline)
    {
        HBox outlineLine = new HBox(5);
        outlineLine.setAlignment(Pos.BASELINE_LEFT);
        outlineLine.getChildren().addAll(new Label("Elementos em Cluster: "), new Text(elements), new Label(" Ruídos/Outline: "), new Text(outline));
        
        return outlineLine;
    }
    
}
