package satb.model;

import javafx.collections.ObservableList;

/**Classe que representa o objeto Classificação.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class Classifier 
{
    //Atributos
    private String area;
    private int pointsTrue;
    private ObservableList<Coordinate> coordinates;
    
    //Contrutor
    public Classifier(String a, int p)
    {
        this.area = a;
        this.pointsTrue = p;
    }
    
    //Retorna o valor Area
    public String getArea()
    {
        return this.area;
    }

    //Seta o valor de Area
    public void setArea(String a)
    {
        this.area = a;
    }

    //Retorna o valor de pointsTrue
    public int getPointsTrue()
    {
        return this.pointsTrue;
    }

    //Seta o valor de pointsTrue
    public void setPointsTrue(int p)
    {
        this.pointsTrue = p;
    }
    
     //Retorna a coleção coordinates
    public ObservableList<Coordinate> getCoordinates()
    {
        return this.coordinates;
    }

    //Seta a coleção coordinates
    public void setCoordinates(ObservableList<Coordinate> c)
    {
        this.coordinates = c;
    }
    
}
   