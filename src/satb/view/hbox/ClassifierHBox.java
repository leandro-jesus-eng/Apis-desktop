package satb.view.hbox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**Classe organiza as HBox da ClassifierView.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class ClassifierHBox 
{
    /**Construtor Default.*/
    public ClassifierHBox(){}
    
    /**Linha que devolve opções para hora.*/
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
    public HBox radioDateLine(RadioButton allDate, RadioButton oneDate, RadioButton intervalDate, final TextField dateBeginField, final TextField dateEndField, final ComboBox<String> comboDate)
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
               comboDate.setValue(null); 
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
                radioDateLine.getChildren().addAll(new Label("Dia: "), comboDate);                
            }
        });
       
        //Função que representa o Radio Button intervalDate
        intervalDate.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {               
               comboDate.setValue(null);
               radioDateLine.getChildren().clear();
               radioDateLine.getChildren().addAll(new Label("Intervalo de Dias:"), dateBeginField, new Label(" até "), dateEndField);
            }
        });
       
        allDate.setSelected(true);
        tgDate.getToggles().addAll(allDate, oneDate, intervalDate);
       
        return radioDateLine;
    }
    
    /**Devolve uma linha com os elementos de tempo.*/
    public HBox collarLine(String collar, String pasture)
    {
        HBox collarLine = new HBox();
        
        collarLine.getChildren().addAll(new Label("Colar: "), new Text(collar), new Label(" Pasto: "), new Text(pasture));
        collarLine.setAlignment(Pos.BASELINE_LEFT);
        collarLine.setSpacing(5); 
       
        return collarLine;       
    }
    
    /**Devolve uma linha com os elementos de tempo.*/
    public HBox timeLine(String date, String dateBegin, String dateEnd, String hourBefore, String hourAfter, String hourBegin, String hourEnd)
    {
        HBox timeLine = new HBox();
        timeLine.setAlignment(Pos.BASELINE_LEFT);
              
        timeLine.getChildren().addAll(new Label("Data(s): "), new Text(date(date, dateBegin, dateEnd)), new Label(" Horário: "), new Text(hour(hourBefore, hourAfter, hourBegin, hourEnd)));
   
        return timeLine;       
    }
    
    
    /**Devolve uma linha com os elementos relacionadas a pontos no pasto.*/
    public HBox pointsLine(String pts, String ptsIn, String ptsOut)
    {
        HBox pointsLine = new HBox();
        pointsLine.setAlignment(Pos.BASELINE_LEFT);
        
        pointsLine.getChildren().addAll(new Label("Pontos coletados: "), new Text(pts), new Label(" No Pasto: "), new Text(ptsIn), new Label(" Fora: "), new Text(ptsOut));  
              
        return pointsLine;       
    }
    
    
    /**Retorna o valor da data a ser posicionado na janela de resultados.*/
    public String date(String date, String dateBegin, String dateEnd)
    {
        if(date != null)
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
    
    /**Retorna a linha com o nome da área.*/
    public HBox areaLine(String area)
    {
        HBox areaLine = new HBox();
        areaLine.getChildren().addAll(new Label("Área: "), new Text(area));
        areaLine.setAlignment(Pos.BASELINE_LEFT);
           
        return areaLine;   
    }
    
    
    /**Retorna a linha com os pontos dentro da área.*/
    public HBox pointsInAreaLine(String points)
    {
        HBox pointsInAreaLine = new HBox();
        pointsInAreaLine.getChildren().addAll(new Label("Pontos na Área: "), new Text(points));
        pointsInAreaLine.setAlignment(Pos.BASELINE_LEFT);
           
        return pointsInAreaLine;   
    }
    
    /**Retorna a linha com os pontos dentro da área.*/
    public HBox pointsLine(String date, String hour)
    {
        HBox pointsLine = new HBox();
        pointsLine.getChildren().addAll(new Label("=>> Ponto: "), new Text(date),new Label(" às "), new Text(hour));
        pointsLine.setAlignment(Pos.BASELINE_LEFT);
           
        return pointsLine;   
    }
    
    /**Retorna a linha com os pontos relacionados a sombra,*/
    public HBox shadowLine(String points)
    {
       HBox shadowLine = new HBox();
       shadowLine.setAlignment(Pos.BASELINE_LEFT);
       shadowLine.getChildren().addAll(new Label("Pontos na Sombra: "), new Text(points));  
       
           
        return shadowLine;   
    }
    
}
