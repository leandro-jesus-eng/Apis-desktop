package satb.controller;

import java.io.File;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jsat.DataSet;
import jsat.classifiers.DataPoint;
import jsat.clustering.DBSCAN;
import jsat.clustering.LSDBC;
import jsat.clustering.PAM;
import jsat.clustering.dissimilarity.CentroidDissimilarity;
import jsat.clustering.evaluation.DaviesBouldinIndex;
import jsat.clustering.evaluation.DunnIndex;
import jsat.clustering.evaluation.intra.MaxDistance;
import jsat.linear.Vec;
import jsat.linear.VecPaired;
import jsat.linear.distancemetrics.NormalizedEuclideanDistance;
import jsat.linear.vectorcollection.RTree;
import satb.controller.functions.ARFFFunctions;
import satb.controller.functions.KMLFunctions;
import satb.controller.functions.WekaFunctions;
import satb.model.Clustering;
import satb.model.Coordinate;
import satb.model.dao.ClusteringDAO;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

/**Classe que implementa algoritmos de Clustering (Agrupamento).
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class ClusteringController
{
    //Atributos
    private SimpleKMeans kMeans;
    private ObservableList<Coordinate> list;
    private ObservableList<Clustering> clusters;
    private WekaFunctions weka;
    private KMLFunctions kml;
    private ARFFFunctions arff;
    private int numberElements;
    private int outline;
    private List<List<DataPoint>> pam;
    private List<List<DataPoint>> dbscan;
    private List<List<DataPoint>> lsdbc;
    private int [] medoidPam;
    private String path, pathkml;
    
    /**Construtor.*/
    public ClusteringController()
    {
        kml = new KMLFunctions();
        clusters = FXCollections.observableArrayList();
        weka = new WekaFunctions();
        arff = new ARFFFunctions();
        path = "C:/Program Files (x86)/Google/Google Earth/client/googleearth.exe";
        pathkml = "C:/Users/Marcel/Documents/Mestrado/";
        outline = 0;
    }
 
    
    /**Retorna a coleção de objetos do tipo Clustering. */
    public ObservableList<Clustering> getClusters()
    {
        return clusters;
    }
    
    /**Retorna a coleção de objetos do tipo Clustering. */
    public List<List<DataPoint>> getPAM()
    {
        return pam;
    }

    
    /**Retorna a coleção de objetos do tipo Clustering. */
    public List<List<DataPoint>> getDBSCAN()
    {
        return dbscan;
    }
    
    
    /**Retorna a coleção de objetos do tipo Clustering. */
    public List<List<DataPoint>> getLSDBC()
    {
        return lsdbc;
    }
  
    /**Método verifica se o KMeans já foi executado alguma vez.
    @param true se sim ou false se não.*/
    private boolean isKMeansCreated()
    {
        if(kMeans == null)
        {
            return false;
        }       
        return true;
    }
    
    /**Método verifica se o DBSCAN já foi executado alguma vez.
    @param true se sim ou false se não.*/
    private boolean isDBSCANCreated()
    {
        if(dbscan == null)
        {
            return false;
        }       
        return true;
    }
    
  
    /**Método verifica se o PAM já foi executado alguma vez.
    @param true se sim ou false se não.*/
    private boolean isPAMCreated()
    {
        if(pam == null)
        {
            return false;
        }       
        return true;
    }
    
    /**Método verifica se o lsdbc já foi executado alguma vez.
    @param true se sim ou false se não.*/
    private boolean isLSDBCCreated()
    {
        if(lsdbc == null)
        {
            return false;
        }       
        return true;
    }
    
    
    /**Método verifica se algum dos algoritmos já foi executado alguma vez.
    @param true se sim ou false se não.*/
    public boolean isCreated()
    {
        if(isKMeansCreated() || isDBSCANCreated() || isPAMCreated() || isLSDBCCreated())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    /**Retorna o número de elementos que o colar possui.*/
    public int getNumberElements() 
    {
         return numberElements;
    } 
   
    
    /**Retorna o número de ruídos (outlines).*/
    public int getOutline() 
    {
         return outline;
    } 
   
    
    /**Retorna o número de elementos presentes nos clusters.*/
    public int getClustersTotal()
    {
        return (numberElements - outline);
    }
  
    
    /**Método onde o algoritmo K-Means é executado.*/
    public void kMeans(ObservableList<String> collars, String ncluster, String date, String dateBegin, String dateEnd, String hourBefore, String hourAfter, String hourBegin, String hourEnd) throws Exception
    {   
        ClusteringDAO cd = new ClusteringDAO();  
        cd.setHourSQL(hourBefore, hourAfter, hourBegin, hourEnd);
        cd.setDateSQL(date, dateBegin, dateEnd);
        cd.setCollarSQL(collars);
        
        numberElements = cd.getNumberOfElements();
        
        if(!clusters.isEmpty())
        {
            clusters.clear();
        }
        list = cd.selectCoordinates();

        SimpleKMeans cluster = new SimpleKMeans();
        cluster.setPreserveInstancesOrder(true);
        //Seta o numero de clusters a serem criados
        cluster.setNumClusters(Integer.parseInt(ncluster));
        cluster.setDisplayStdDevs(true);
        //Construção dos Clusters
        cluster.buildClusterer(weka.arff(list));
               
        // Mostramos estatísticas sobre o agrupamento
        Instances clusterCenters = cluster.getClusterCentroids();
        int[] clusterSizes = cluster.getClusterSizes();
              
        //Limpando a array de clusters
        clusters.clear();
        
        for(int i=0; i < clusterCenters.numInstances(); i++)
        {
            Clustering c = new Clustering(i+1, clusterSizes[i], clusterCenters.instance(i).toString());
            clusters.add(c);
        } 
        
        //Salva o resultado mais atual do agrupamento para uso futuro
        kMeans = cluster;
        this.outline = 0;    
   }
   
    
   /**Método onde o algoritmo DBSCAN será executado.*/
   public void DBSCAN(ObservableList<String> collars, String e, String min, String date, String dateBegin, String dateEnd, String hourBefore, String hourAfter, String hourBegin, String hourEnd) throws Exception
   {   
        ClusteringDAO cd = new ClusteringDAO();  
        cd.setHourSQL(hourBefore, hourAfter, hourBegin, hourEnd);
        cd.setDateSQL(date, dateBegin, dateEnd);
        cd.setCollarSQL(collars);
        
        numberElements = cd.getNumberOfElements();
        outline = 0;
   
        list = cd.selectCoordinates();
        String name = arff.buildARFFName(collars);
        arff.createARFF(list, name); 
        DataSet data = arff.openARFFJSAT(name);
        /*
        DBSCAN scan = new DBSCAN();
        Transformando os dados para executar algo equivalente ao Normatized
        Euclidean Distance (igual a library JAVA-ML).
        DataTransform transform = new UnitVarianceTransform(data);
        data.applyTransform(transform);
       */
        DBSCAN scan = new DBSCAN(new NormalizedEuclideanDistance(), new RTree.RTreeFactory<VecPaired<Vec,Integer>>());
        List<List<DataPoint>> result = scan.cluster(data, Double.parseDouble(e), Integer.parseInt(min));
       
        //Limpando a array de clusters
        clusters.clear();
        
        int count = 0, i = 0;
        for(List<DataPoint> mean : result)
        {
            count = count + mean.size();
            Clustering c = new Clustering(i+1, mean.size());
            clusters.add(c);
        
            i = i + 1;
        }

        //Salva os resultados do algoritmo
        outline = numberElements - count;
        this.dbscan = result;
        
        //Avalia os clusters gerados
        DaviesBouldinIndex davies = new DaviesBouldinIndex();
        double evaluate = davies.evaluate(result);
        System.out.println("Avaliação: " + evaluate);

        DunnIndex dunn = new DunnIndex(new MaxDistance(), new CentroidDissimilarity());
        evaluate = dunn.evaluate(result);
        System.out.println("Avaliação: " + evaluate);
   }
     

   /**Método onde o algoritmo DBSCAN será executado.*/
   public void LSDBC(ObservableList<String> collars, String alpha, String neighbors, String date, String dateBegin, String dateEnd, String hourBefore, String hourAfter, String hourBegin, String hourEnd) throws Exception
   {   
        ClusteringDAO cd = new ClusteringDAO();  
        cd.setHourSQL(hourBefore, hourAfter, hourBegin, hourEnd);
        cd.setDateSQL(date, dateBegin, dateEnd);
        cd.setCollarSQL(collars);
        
        numberElements = cd.getNumberOfElements();
        outline = 0;

        list = cd.selectCoordinates();
        String name = arff.buildARFFName(collars);
        arff.createARFF(list, name); 
        DataSet data = arff.openARFFJSAT(name);

        LSDBC scan = new LSDBC(); 
        scan.setAlpha(Double.parseDouble(alpha));
        scan.setNeighbors(Integer.parseInt(neighbors));
              
        List<List<DataPoint>> result = scan.cluster(data); 
       
        //Limpando a array de clusters
        clusters.clear();
        
        int count = 0, i = 0;
        for(List<DataPoint> mean : result)
        {
            count = count + mean.size();
            Clustering c = new Clustering(i+1, mean.size());
            clusters.add(c);
        
            i = i + 1;
        }

        //Salva os resultados do algoritmo
        outline = numberElements - count;
        this.lsdbc = result;
        
        //Avalia os clusters gerados
        DaviesBouldinIndex davies = new DaviesBouldinIndex();
        double evaluate = davies.evaluate(result);
        System.out.println("Avaliação: " + evaluate);

        DunnIndex dunn = new DunnIndex(new MaxDistance(), new CentroidDissimilarity());
        evaluate = dunn.evaluate(result);
        System.out.println("Avaliação: " + evaluate);
   }
   
   
    /**Método onde o algoritmo PAM será executado.*/
   public void PAM(ObservableList<String> collars, String ncluster, String date, String dateBegin, String dateEnd, String hourBefore, String hourAfter, String hourBegin, String hourEnd) throws Exception
   {   
        ClusteringDAO cd = new ClusteringDAO();  
        cd.setHourSQL(hourBefore, hourAfter, hourBegin, hourEnd);
        cd.setDateSQL(date, dateBegin, dateEnd);
        cd.setCollarSQL(collars);
        
        numberElements = cd.getNumberOfElements();
        
        list = cd.selectCoordinates();
        String name = arff.buildARFFName(collars);
        arff.createARFF(list, name);     
        
        PAM p = new PAM();
        DataSet data = arff.openARFFJSAT(name);

        p.setStoreMedoids(true);
        List<List<DataPoint>> result = p.cluster(data, Integer.parseInt(ncluster));
        
        int [] medoids = p.getMedoids();

        //Limpando a array de clusters
        clusters.clear();
        
        int i = 0;
        for(List<DataPoint> mean : result)
        {
            Clustering c = new Clustering(i+1, mean.size());
            c.setMedoid(data.getDataPoint(medoids[i]).getNumericalValues().get(0)+","+data.getDataPoint(medoids[i]).getNumericalValues().get(1));
            clusters.add(c);
            
            i++;
        }
        
        //Salva os resultados do algoritmo
        this.pam = result;
        this.medoidPam = medoids;
        this.outline = 0;
        
        //Avalia os clusters gerados
        DaviesBouldinIndex davies = new DaviesBouldinIndex();
        double evaluate = davies.evaluate(result);
        System.out.println("Avaliação: " + evaluate);

        DunnIndex dunn = new DunnIndex(new MaxDistance(), new CentroidDissimilarity());
        evaluate = dunn.evaluate(result);
        System.out.println("Avaliação: " + evaluate);
   } 

   
   /**Mostrar o resultado do DBSCAN no mapa.*/
   public void showDBSCAN(ObservableList<String> collars) throws Exception
   {
        String pathfile = pathkml;
                
        //Invoca o método de construção do KML
        File file = kml.buildDBSCANKML(kml.buildDBSCANKMLName(collars), this.dbscan);
        
        //Executa o google earth com o kml gerado
        Runtime.getRuntime().exec(new String[] {path,pathfile.concat(file.getPath())});   
   }
   
   
   /**Mostrar o resultado do K-means no mapa.*/
   public void showKMeans(ObservableList<String> collars, String ncluster) throws Exception
   {
        String pathfile = pathkml;
                
        //Invoca o método de construção do KML
        File file = kml.buildKMeansKML(kml.buildKMeansKMLName(collars,ncluster), this.kMeans, weka.getInstances());
        
        //Executa o google earth com o kml gerado
        Runtime.getRuntime().exec(new String[] {path,pathfile.concat(file.getPath())});   
   }
   
   
   /**Mostrar o resultado do LSDBC no mapa.*/
   public void showLSDBC(ObservableList<String> collars) throws Exception
   {
        String pathfile = pathkml;
                
        //Invoca o método de construção do KML
        File file = kml.buildLSDBCKML(kml.buildLSDBCKMLName(collars), this.lsdbc);
        
        //Executa o google earth com o kml gerado
        Runtime.getRuntime().exec(new String[] {path,pathfile.concat(file.getPath())});   
   }
   
   
   /**Mostrar o resultado do PAM no mapa.*/
   public void showPAM(ObservableList<String> collars) throws Exception
   {
        String pathfile = pathkml;
                
        //Invoca o método de construção do KML
        File file = kml.buildPAMKML(kml.buildPAMKMLName(collars), this.pam, clusters);
        
        //Executa o google earth com o kml gerado
        Runtime.getRuntime().exec(new String[] {path,pathfile.concat(file.getPath())});   
   }

}