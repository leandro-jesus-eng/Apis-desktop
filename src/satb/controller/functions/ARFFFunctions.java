package satb.controller.functions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import jsat.ARFFLoader;
import jsat.DataSet;
import jsat.classifiers.DataPoint;
import satb.Util;
import satb.model.Coordinate;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**Classe que fornece funções para a manipulação do arquivo arff.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class ARFFFunctions 
{
    //Atributos
    private String path = Util.getCurrentRelativePath()+ File.separator  + "arff" + File.separator;

    /**Contrutor Default. */
    public ARFFFunctions(){}  
    
    /**Elabora o nome do arquivo pegando o nome do colar.
    * @param collars - uma lista com a identificação dos colares.
    * @return String  - nome do arquivo arff.*/
    public String buildARFFName(String collar)
    {                     
        String arffname = collar;
        arffname = arffname.concat(".arff");
        
        return arffname;
    }
    
    
    /**Elabora o nome do arquivo pegando o nome do colar.
    * @param collars - uma lista com a identificação dos colares.
    * @return String  - nome do arquivo arff.*/
    public String buildARFFName(ObservableList<String> collars)
    {                     
        String arffname = "";
        Iterator<String> iterator = collars.iterator();
        
        while(iterator.hasNext())
        {
            arffname = arffname.concat(iterator.next());
            if(iterator.hasNext())
            {
                arffname = arffname.concat("_");
            }           
        }   
        
        arffname = arffname.concat(".arff");
        
        return arffname;
    }
    
    
    /**Cria um arquivo no formato ARFF.*/  
    public void createARFF(ObservableList<Coordinate> list, String filename) throws Exception 
    {
        FastVector                      atts;
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
        
        //Criação e escrita de um arquivo
        FileWriter fileWriter = null;
        try 
        {
             File file = new File(path.concat(filename));
             fileWriter = new FileWriter(file);
             fileWriter.write(data.toString());
             fileWriter.close();
        } 
        catch (IOException ex) 
        {
             Logger.getLogger(ARFFFunctions.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally 
        {
             try 
             {
                 fileWriter.close();
             } 
             catch (IOException ex) 
             {
                 Logger.getLogger(ARFFFunctions.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
    }
   
    
    /**Abre um arquivo ARFF para o uso de algoritmos JSAT.
     @param filename - o nome do arquivo a ser usado.
     @return um DataSet com os dados lidos do arquivo.*/
    public DataSet openARFFJSAT(String filename)
    {

        File file = new File(path + filename);
        DataSet dataSet = ARFFLoader.loadArffFile(file);
        System.out.println("\nThere are " + dataSet.getNumFeatures() + " features for this data set.");
        System.out.println(dataSet.getNumCategoricalVars() + " categorical features");
        System.out.println("They are:");
        for(int i = 0; i <  dataSet.getNumCategoricalVars(); i++) 
        {
            System.out.println("\t" + dataSet.getCategoryName(i));
        }
        System.out.println(dataSet.getNumNumericalVars() + " numerical features");
        System.out.println("They are:");
        for(int i = 0; i <  dataSet.getNumNumericalVars(); i++) 
        {
            System.out.println("\t" + dataSet.getNumericName(i));
        }
        
        System.out.println("\nThe whole data set");
        for(int i = 0; i < dataSet.getSampleSize(); i++)
        {
            DataPoint dataPoint = dataSet.getDataPoint(i);
            System.out.println(dataPoint);
        }
        
        return dataSet;
    }
    
    
    /**Abre um arquivo ARFF para o uso de algoritmos Weka.
     @param filename - o nome do arquivo a ser usado.
     @return um Instaces com os dados lidos do arquivo.*/
    public Instances openARFFWeka(String filename) throws Exception
    {     
        DataSource source = new DataSource(path + filename);
        Instances data = source.getDataSet();
        
        // setting class attribute if the data format does not provide this information
        // For example, the XRFF format saves the class attribute information as well
        if (data.classIndex() == -1) 
        {
            data.setClassIndex(data.numAttributes() - 1);
        }
    
        return data;
    }
    
    /*
    public DataSet createDataSet(ObservableList<Coordinate> list)
    {
        
        //We create a new data set. This data set will have 2 dimensions so we can visualize it, and 4 target class values
        ClassificationDataSet data = new ClassificationDataSet(2, new CategoricalData[0], new CategoricalData(4));

        double[] att = new double[2];        

        for(Coordinate c : list)
        {
            att[0] = c.getLongitudeX();
            att[1] = c.getLatitudeY();
            Vec mean = new DenseVector(att);
            data.addDataPoint(mean, new int[0], 1);
     
        }

        System.out.println(data);
        System.out.println(data.getDataPoints());
        
        return data;
    }
    */
}
