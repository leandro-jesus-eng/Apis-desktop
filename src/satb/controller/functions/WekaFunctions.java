package satb.controller.functions;

import java.io.File;
import javafx.collections.ObservableList;
import satb.Util;
import satb.model.Coordinate;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;

/**Classe com métodos para manipulação e criação de Weka.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class WekaFunctions 
{
    private String path = Util.getCurrentRelativePath() + File.separator + "arff" + File.separator;
    private Instances instances;

    /**Construtor. */
    public WekaFunctions(){}
 
    /**Retorna o valor da variavel instances.
     @return o atributo instances.*/
    public Instances getInstances()
    {
        return instances;
    }
    
 
    //Cria um objeto no formato ARFF  
    public Instances arff(ObservableList<Coordinate> list) throws Exception 
    {
        FastVector            atts;
        Instances			data;
        double[]			vals;

        // 1. Definir os atributos
        atts = new FastVector();
        // - numérico
        atts.addElement(new Attribute("longitude"));
        // - numérico
        atts.addElement(new Attribute("latitude"));

        // 2. Criar o objeto Instances
        data = new Instances("AnimalPosition", atts, 0);
     
        // 3. Preencher com dados
        for(Coordinate point : list)
        {
            vals = new double[data.numAttributes()];
                     
            // - numérico
            vals[0] = point.getLongitudeX();
            
            // - numérico
            vals[1] = point.getLatitudeY();
                
            // Adicionar atributos no ARFF
            //data.add(new Instance(1.0, vals)); // 3.6            
            data.add(new DenseInstance(1.0, vals)); // 3.8
        }
       
        System.out.println(data);
        System.out.println("\n");
        instances = data;
        
        //Retorna o arquivo ARFF pronto
        return data;
    }
}
