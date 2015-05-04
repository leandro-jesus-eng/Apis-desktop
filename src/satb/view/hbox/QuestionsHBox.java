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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**Classe organiza as HBox da QuestionsView.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class QuestionsHBox 
{
    /**Construtor Default. */
    public QuestionsHBox(){}
    
    /**Método que retorna um GridPane construído. */
    public GridPane createGrid()
    {
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #EEEEEE;");
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
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
    
    
    /**Método que retorna o botão principal. */
    public HBox onebuttonLine(Button one)
    {
        HBox onebuttonLine = new HBox(5);
        onebuttonLine.getChildren().addAll(one);
    
        return onebuttonLine;
    }
    
    /**Devolve uma linha com um ComboBox de colar. */
    public HBox collarLine(ComboBox<String> comboCollar)
    {
        HBox collarLine = new HBox(5);
        collarLine.getChildren().addAll(new Label("Colar: "), comboCollar);
        collarLine.setAlignment(Pos.BASELINE_LEFT);

        return collarLine;       
     }
    
    
    /**Linha que devolve opções para data. */
    public HBox dateLine(RadioButton allDate, RadioButton oneDate, RadioButton intervalDate)
    {
        HBox dateLine = new HBox(5);
        dateLine.setAlignment(Pos.BASELINE_LEFT);
        
        dateLine.getChildren().addAll(new Label("Data: "), allDate, oneDate, intervalDate);
                
        return dateLine;
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

    /**Devolve uma linha com uma ComboBox de questões. */
    public HBox askLine(ComboBox<String> comboQuestions)
    {
        HBox askLine = new HBox(5);
        askLine.getChildren().addAll(new Label("Pergunta: "), comboQuestions);
        askLine.setAlignment(Pos.BASELINE_LEFT);

        return askLine;       
     }
    
    /**Devolve uma linha com os elementos de tempo. */
    public HBox timeLine(String date, String dateBegin, String dateEnd, String collar)
    {
        HBox timeLine = new HBox(5);
        timeLine.setAlignment(Pos.BASELINE_LEFT);
        
        if(date != null)
        {
            timeLine.getChildren().addAll(new Label("Colar: "), new Text(collar), new Label(" Data: "), new Text(date));
        }
        else if(!dateBegin.isEmpty() && !dateEnd.isEmpty())
        {
            timeLine.getChildren().addAll(new Label("Colar: "), new Text(collar), new Label(" Datas: "), new Text("Entre " + dateBegin + " e " + dateEnd + ""));
        }
        else
        {
            timeLine.getChildren().addAll(new Label("Colar: "), new Text(collar), new Label(" Data: "), new Text("TODAS"));
        }

        return timeLine;       
     }
    
    
    /**Devolve uma linha com a pergunta em questão. */
    public HBox questionLine(String question)
    {
        HBox questionLine = new HBox(5);
        questionLine.getChildren().addAll(new Label("Pergunta: "), new Text(question));
        questionLine.setAlignment(Pos.BASELINE_LEFT);

        return questionLine;       
     }
    
    
    /**Devolve uma linha com a resposta. */
    public HBox answerLine(String date, String hour)
    {
        HBox answerLine = new HBox(5);
        answerLine.getChildren().addAll(new Label("Dia: "), new Text(date), new Label("  Horário: "), new Text(hour));
        answerLine.setAlignment(Pos.BASELINE_LEFT);

        return answerLine;       
    }
    
    
    /**Devolve uma linha com o dia da trajetória e sua duração. */
    public HBox answerTimeLine(String date, String duration)
    {
        HBox answerTimeLine = new HBox(5);
        answerTimeLine.getChildren().addAll(new Label("Dia: "), new Text(date), new Label("  Duração da Trajetória: "), new Text(duration));
        answerTimeLine.setAlignment(Pos.BASELINE_LEFT);

        return answerTimeLine;       
    }
    
    /**Devolve uma linha com a resposta. */
    public HBox answerLocationLine(String shadowName)
    {
        HBox answerLocationLine = new HBox(5);
        answerLocationLine.getChildren().addAll(new Label("Sombra: "), new Text(shadowName));
        answerLocationLine.setAlignment(Pos.BASELINE_LEFT);

        return answerLocationLine;       
    }
    
    
    /**Devolve uma linha com a resposta. */
    public HBox answerIntervalLine(String begin, String end)
    {
        HBox answerIntervalLine = new HBox(5);
        answerIntervalLine.getChildren().addAll(new Label("Esteve entre  "), new Text(begin), new Label(" e "), new Text(end));
        answerIntervalLine.setAlignment(Pos.BASELINE_LEFT);

        return answerIntervalLine;       
    }
    
    
    /**Devolve uma linha com o tempo aproximado do animal na região. */
    public HBox durationLine(String area, String time, long duration)
    {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        DateTime dateTime = formatter.parseDateTime(time);
        
        double part = ((double)dateTime.getSecondOfDay()/(double)duration) * 100;      
        String percent = String.format("%.2f", part);       
        
        HBox durationLine = new HBox(5);
        durationLine.getChildren().addAll(new Label("Tempo aproximado na "+ area +": "), new Text(time + " / "), new Text(percent + "% da duração da trajetória."));
        durationLine.setAlignment(Pos.BASELINE_LEFT);

        return durationLine;       
    }
    
    
    /**Devolve uma linha com o tempo aproximado do animal na região. */
    public HBox durationTotalLine(String time)
    {
        HBox durationTotalLine = new HBox(5);
        durationTotalLine.getChildren().addAll(new Label("Tempo total aproximado :"), new Text(time));
        durationTotalLine.setAlignment(Pos.BASELINE_LEFT);

        return durationTotalLine;       
    }
    
    /**Devolve a subtrajetória de pts. de baixa velocidade e a área de sombra que está intercepta. */
    public HBox densityInShadowLine(String hourBegin, String hourEnd, String areaShadow)
    {
        HBox densityInShadowLine = new HBox(5);
        densityInShadowLine.getChildren().addAll(new Label("Subtraj. de pts. de baixa vel. :"), new Text(hourBegin), new Label(" - "), new Text(hourEnd), new Label(" Sombra Interceptada: "), new Text(areaShadow));
        densityInShadowLine.setAlignment(Pos.BASELINE_LEFT);

        return densityInShadowLine;       
    }
    
    
    /**Devolve uma linha com a resposta da pergunta a respeito da distância. */
    public HBox distanceLine(String date, double dist, double meanSpeed, String duration)
    {                       
         HBox distanceLine = new HBox(10);
         String speed = String.format("%.3f", meanSpeed);
         
         distanceLine.getChildren().addAll(new Label("Dia: "), new Text(date), new Label("Distância: "), new Text(String.valueOf(dist)),  new Label("Vel. Média: "), new Text(speed), new Label("Duração: "), new Text(duration));
         distanceLine.setAlignment(Pos.BASELINE_LEFT);

         return distanceLine;
    }
    
}
