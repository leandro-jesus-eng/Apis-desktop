package satb.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import satb.Util;
import satb.controller.functions.KMLFunctions;
import satb.controller.functions.TrajectoryFuntions;
import satb.model.Area;
import satb.model.AreaShadow;
import satb.model.ClusterPoints;
import satb.model.Coordinate;
import satb.model.Move;
import satb.model.Stop;
import satb.model.Trajectory;
import satb.model.dao.AreaDAO;
import satb.model.dao.AreaShadowDAO;
import satb.model.dao.PastureDAO;
import satb.model.dao.TrajectoryDAO;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**Classe que representa a camada de controle para Trajetórias.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class TrajectoryController 
{
    //Atributos
    private ObservableList<Coordinate> trajectory;
    private TrajectoryFuntions functions;
    private KMLFunctions kml;
    private String gapTime;
    private String collar;
    private FileChooser fileChooser;
    private File fileARFF;
    private String path, pathkml;
    
    /**Construtor Default. */
    public TrajectoryController()
    {
        functions = new TrajectoryFuntions();
        fileChooser = new FileChooser();
        kml = new KMLFunctions();
        path = "C:/Program Files (x86)/Google/Google Earth/client/googleearth.exe";
        pathkml = Util.getCurrentRelativePath();
    }
    
    
    /**Seta o atributo trajectory a partir das informações de um arquivo ARFF. */
    public void createTrajectory() throws Exception
    {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(fileARFF.getPath());
        Instances datas = source.getDataSet();
        ObservableList<Coordinate> traj = FXCollections.observableArrayList();
       
        int timeIndex = 0;       
        
        for(int k=0 ; k < datas.numInstances(); k++) {
            Instance data = datas.instance(k);        
        //for(Instance data : datas) {
            Coordinate coord = new Coordinate(data.value(1), data.value(2), data.stringValue(3), data.stringValue(4));
            coord.setTimeIndex(timeIndex);
            traj.add(coord);
            timeIndex++;
        }
        
        this.gapTime = functions.subtractHours(traj.get(0).getHour(), traj.get(1).getHour());
        this.trajectory = traj;
        this.collar = datas.instance(0).stringValue(0);
    }
    
    
    /**Invoca um método do TrajectoryDAO onde todas as trajetórias semânticas 
     * relacionadas ao colar são coletadas.*/
    public ObservableList<Trajectory> selectAll(String collar) throws Exception
    {
        TrajectoryDAO td = new TrajectoryDAO();
        return td.selectAll(collar);     
    }
    
    
    /**Invoca um método do TrajectoryDAO que seleciona as datas de trajetórias cadastradas. */
    public  ObservableList<String> selectDate(String collar) throws Exception
    {
        TrajectoryDAO td = new TrajectoryDAO();
        return td.selectDate(collar);       
    }
    
    
    /**Invoca um método do TrajectoryDAO que seleciona as datas de trajetórias cadastradas. */
    public void removeTrajectory (String collar, String date) throws Exception
    {
        TrajectoryDAO td = new TrajectoryDAO();
        int id = td.selectId(collar, date);
        
        td.removeMoves(id);
        td.removeStops(id);
        td.removeShadow(id);
    }
    
    
    /**Método que instância a trajetória escolhida pelo usuário. */
    public void selectTrajectory(String collar, String data) throws Exception
    {
        TrajectoryDAO td = new TrajectoryDAO();
        this.trajectory = td.selectTrajectory(collar, data);        
        this.gapTime = functions.subtractHours(this.trajectory.get(0).getHour(), this.trajectory.get(1).getHour());
        this.collar = collar;
    }
    

    /**Abre um diretório para pegar o caminho de um arquivo arff. */
    public String createFile() throws Exception 
    {        
        if(fileARFF != null)
        {
               File existDirectory = fileARFF.getParentFile();
               fileChooser.setInitialDirectory(existDirectory);
        }
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ARFF files (*.arff)", "*.arff");
        fileChooser.getExtensionFilters().add(extFilter);
        
        //Abrir e mostrar a janela de diretórios
        fileARFF = fileChooser.showOpenDialog(null); 
        
        if(fileARFF != null)
        {    
            try
            {
                return fileARFF.getPath();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                
            }
        }
        return null;
    }
   
    
    /**Implementa a função SMoT. */
    public void SMoT(String pasture, String collar, String date, boolean record) throws Exception 
    {
        TrajectoryDAO td = new TrajectoryDAO();
        PastureDAO pd = new PastureDAO();
        int pastureid = pd.selectId(pasture);
        
        ObservableList<Move> moves = FXCollections.observableArrayList();
        ObservableList<Stop> stops = FXCollections.observableArrayList();
        
        Stop previousStop = null;
        String minTime = td.getMinimunTimeValue(pastureid);
        String startTime;
        String endTime;
    
        int i = 0;
        while(i < this.trajectory.size())
        {
            //Se existe uma tupla de CandidateStop(a área cadastrada) onde o ponto tenha interseção com a área
            Area candStop = td.intersectArea(this.trajectory.get(i).getCoordinate(), pastureid);
            if(candStop != null)                    
            {
                startTime = this.trajectory.get(i).getHour();
                i = i + 1;

                while(i < this.trajectory.size() && td.intersects(this.trajectory.get(i).getCoordinate(), candStop.getId()))
                {
                   i = i + 1;                        
                }
                
                //Go one step back (went outside RC)
                if(i == this.trajectory.size())
                {
                    i = i - 1;
                    endTime = functions.plusHours(this.trajectory.get(i).getHour(), this.gapTime);
                    
                }    
                else
                {
                    endTime = this.trajectory.get(i).getHour();
                    i = i - 1;
                }
                
                if(functions.compareTwoHours(functions.subtractHours(startTime, endTime), candStop.getMinimumTime()))
                {

                    Stop s = new Stop(candStop.getId(), candStop.getName(), startTime, endTime);
                    stops.add(s);
                    if(previousStop == null)
                    {
                        //no primeiro movimento, o tempo inicial será o tempo do primeiro ponto menos
                        //o espaço temporal de coleta do GPS.
                        Move m = new Move(null, s, functions.subtractHours(this.gapTime, startTime), startTime);
                        moves.add(m);
                    }
                    else
                    {    
                        Move m = new Move(previousStop, s, previousStop.getEndTime(), startTime);
                        moves.add(m);
                    }
                    
                    previousStop = s;
                }
            }
            
            i = i + 1;
            int j = 0;

            while((i + j) < this.trajectory.size() && !functions.compareTwoHours(functions.subtractHours(this.trajectory.get(i).getHour(), this.trajectory.get(i+j).getHour()), minTime))
            {
                j = j + 1;
            }
            
            if(((i + j) - 1) < this.trajectory.size())
            {
                candStop = td.intersectArea(this.trajectory.get(i + j - 1).getCoordinate(), pastureid);
                if(candStop == null)                    
                {
                    i = i + j;
                }

            }            
        }
        
        //Caso não haja nenhuma parada na trajetória
        if(previousStop == null)
        {
            Move m = new Move(previousStop, null, this.trajectory.get(0).getHour(), this.trajectory.get(i - 1).getHour());
            moves.add(m);
        }
        else
        {
            //Uma trajetória não pode terminar com uma parada,
            AreaDAO ad = new AreaDAO();
            Area candStop = ad.selectArea(previousStop.getAreaName());
            
            if(!td.intersects(this.trajectory.get(i - 1).getCoordinate(), candStop.getId()))
            {
                Move m = new Move(previousStop, null, previousStop.getEndTime(), this.trajectory.get(i - 1).getHour());
                moves.add(m);
            }
            
        }
        
        int size = this.trajectory.size();
        Trajectory t = new Trajectory(collar, trajectory.get(0).getHour(), trajectory.get(size - 1).getHour(), date, this.gapTime);
        
        //Se a gravação de dados é true
        if(record)
        {
            System.out.println("Record Data ON");            
            insertSMoT(t, moves, stops);
        }
        else
        {
            System.out.println("Record Data OFF");
            Iterator<Move> it = moves.iterator();
            System.out.println("Movimentos");
            while(it.hasNext())
            {           
                Move m = it.next();
                System.out.println("Inicio: " + m.getStartTime());
                System.out.println("Fim: " + m.getEndTime() + "\n");
            }

            Iterator<Stop> its = stops.iterator();
            System.out.println("Paradas");
            while(its.hasNext())
            {
                Stop s = its.next();
                System.out.println("Área de Parada: " + s.getAreaName());
                System.out.println("Inicio: " + s.getStartTime());
                System.out.println("Fim: " + s.getEndTime() + "\n");
            } 
        }
    
   }
    
    
    /**Insere as saídas geradas pelo algoritmo SMoT no Banco de Dados. */     
    public void insertSMoT(Trajectory t, ObservableList<Move> moves, ObservableList<Stop> stops) throws Exception
    {
        TrajectoryDAO td = new TrajectoryDAO(); 
        int trajectoryId = td.selectId(collar, t.getDate());

        //Remove todos os stops e moves para adicionar objetos atualizados.
        td.removeMoves(trajectoryId);
        td.removeStops(trajectoryId);
        
        //Insere Moves e Stops da Trajetória no Banco de Dados.
        Iterator<Move> it = moves.iterator();
        System.out.println("Movimentos");
        while(it.hasNext())
        {           
            Move m = it.next();
            System.out.println("Inicio: " + m.getStartTime());
            System.out.println("Fim: " + m.getEndTime() + "\n");
            td.insertMove(m, trajectoryId);
        }

        Iterator<Stop> its = stops.iterator();
        System.out.println("Paradas");
        while(its.hasNext())
        {
            Stop s = its.next();
            System.out.println("Área de Parada: " + s.getAreaName());
            System.out.println("Inicio: " + s.getStartTime());
            System.out.println("Fim: " + s.getEndTime() + "\n");
            td.insertStop(s, trajectoryId);
        } 

        System.out.println("");

    }        

   
    /**Implementa a função SMoT para áreas de sombra. */
    public void SMoTInShadow(String pasture, String collar, String date, boolean record) throws Exception 
    {
        TrajectoryDAO td = new TrajectoryDAO();
        PastureDAO pd = new PastureDAO();
        int pastureid = pd.selectId(pasture);

        ObservableList<Move> moves = FXCollections.observableArrayList();
        ObservableList<Stop> stops = FXCollections.observableArrayList();

        Stop previousStop = null;
        String minTime = td.getMinimunTimeValue(pastureid);
        String startTime;
        String endTime;

        int i = 0;
        while(i < this.trajectory.size())
        {
            //Se existe uma tupla de CandidateStop(a área cadastrada) onde o ponto tenha interseção com a área
            AreaShadow candStop = td.intersectAreaShadow(this.trajectory.get(i), pastureid);
            
            if(candStop != null)                    
            {
                System.out.println(candStop.getName());
                startTime = this.trajectory.get(i).getHour();
                i = i + 1;

                while(i < this.trajectory.size() && td.intersectShadow(this.trajectory.get(i), candStop))
                {
                   i = i + 1;                        
                }

                //Go one step back (went outside RC)
                if(i == this.trajectory.size())
                {
                    i = i - 1;
                    endTime = functions.plusHours(this.trajectory.get(i).getHour(), this.gapTime);

                }    
                else
                {
                    endTime = this.trajectory.get(i).getHour();
                    i = i - 1;
                }
                
                if(functions.compareTwoHours(functions.subtractHours(startTime, endTime), candStop.getMinTime()))
                {
                    Stop s = new Stop(candStop.getId(), candStop.getName(), startTime, endTime);
                    stops.add(s);
                    if(previousStop == null)
                    {
                        //no primeiro movimento, o tempo inicial será o tempo do primeiro ponto menos
                        //o espaço temporal de coleta do GPS.
                        Move m = new Move(null, s, functions.subtractHours(this.gapTime, startTime), startTime);
                        moves.add(m);
                    }
                    else
                    {    
                        Move m = new Move(previousStop, s, previousStop.getEndTime(), startTime);
                        moves.add(m);
                    }

                    previousStop = s;
                }
            }

            i = i + 1;
            int j = 0;

            while((i + j) < this.trajectory.size() && !functions.compareTwoHours(functions.subtractHours(this.trajectory.get(i).getHour(), this.trajectory.get(i+j).getHour()), minTime))
            {
                j = j + 1;
            }

            if(((i + j) - 1) < this.trajectory.size())
            {
                candStop = td.intersectAreaShadow(this.trajectory.get(i + j - 1), pastureid);
                if(candStop == null)                    
                {
                    i = i + j;
                }

            }            
        }

        //Caso não haja nenhuma parada na trajetória
        if(previousStop == null)
        {
            Move m = new Move(previousStop, null, this.trajectory.get(0).getHour(), this.trajectory.get(i - 1).getHour());
            moves.add(m);
        }
        else
        {
            //Uma trajetória não pode terminar com uma parada,
            AreaShadowDAO ad = new AreaShadowDAO();
            AreaShadow candStop = ad.selectAreaShadow(previousStop.getAreaName());

            if(!td.intersectShadow(this.trajectory.get(i - 1), candStop))
            {
                Move m = new Move(previousStop, null, previousStop.getEndTime(), this.trajectory.get(i - 1).getHour());
                moves.add(m);
            }

        }

        int size = this.trajectory.size();
        Trajectory t = new Trajectory(collar, trajectory.get(0).getHour(), trajectory.get(size - 1).getHour(), date, this.gapTime);

        //Se a gravação de dados é true
        if(record)
        {
            System.out.println("Record Data ON");            
            insertSMoTShadow(t, stops);
        }
        else
        {
            System.out.println("Record Data OFF");
            Iterator<Stop> its = stops.iterator();
            System.out.println("Paradas na Sombra");
            while(its.hasNext())
            {
                Stop s = its.next();
                System.out.println("Sombra: " + s.getAreaName());
                System.out.println("Inicio: " + s.getStartTime());
                System.out.println("Fim: " + s.getEndTime() + "\n");
            } 
        }  
    }

    
    /**Insere as saídas geradas pelo algoritmo SMoT no Banco de Dados. */     
    public void insertSMoTShadow(Trajectory t, ObservableList<Stop> stops) throws Exception
    {
        TrajectoryDAO td = new TrajectoryDAO(); 
        int trajectoryId = td.selectId(collar, t.getDate());

        //Remove todos os shadows para adicionar objetos atualizados.
        td.removeShadow(trajectoryId);
        
        Iterator<Stop> its = stops.iterator();
        System.out.println("Paradas na Sombra");
        while(its.hasNext())
        {
            Stop s = its.next();
            System.out.println("Sombra: " + s.getAreaName());
            System.out.println("Inicio: " + s.getStartTime());
            System.out.println("Fim: " + s.getEndTime() + "\n");
            td.insertShadowStop(s, trajectoryId);
        } 
        System.out.println("");

    } 
   

    /**Implementa a função CB-SMoT. */
    public void cbSMoT(String avg, String minTime, String speedL, boolean record) throws FileNotFoundException, IOException, Exception
    {     
        System.out.println("Número de Pontos de Trajetória: " + trajectory.size());
        Trajectory t = new Trajectory(trajectory);  
        t.calculatePointsSpeed();
        ObservableList<Coordinate> points = t.getGPSPoints();
        double avgSpeedOfTrajectory = t.meanSpeed();
        int time = functions.timeInSeconds(minTime);
        double avgSpeed = avgSpeedOfTrajectory * Double.parseDouble(avg);
        double speedLimit = avgSpeedOfTrajectory * Double.parseDouble(speedL);

        int clusterId = 1;

        ObservableList<Coordinate> pointsSorted = t.sortBySpeed();

        for (Coordinate point: pointsSorted) 
        {
            // point is unprocessed
            if (point.getClusterId() == -1) 
            { 
                 if (functions.limitedNeighborhood(points, point, clusterId, avgSpeed, time, speedLimit)) 
                 {
                    clusterId++;
                 }
            }
        }

        points = functions.unifyClusters(points); 
        ObservableList<ClusterPoints> cluster = functions.createClusters(points);
        System.out.println("Número de Clusters: " + cluster.size()); 

        for(ClusterPoints c : cluster)
        {
            System.out.println("Número de Elementos: " + c.points.size());
        }

        System.out.println("Velocidade Média: " + t.meanSpeed()); 
        System.out.println(avgSpeed);
        System.out.println(speedLimit);

        for(Coordinate c : t.getGPSPoints())
        {
            System.out.println("Velocidade: " + c.getSpeed());
        }

        //Se a gravação de dados é true
        if(record)
        {
            System.out.println("Iniciando a inserção do resultado no Banco.");
            insertCBSMoT(t, cluster);
        }    

        //Invoca o método de construção do KML
        File file = kml.buildCBSMOT("teste.kml", cluster);

        //Executa o google earth com o kml gerado
        Runtime.getRuntime().exec(new String[] {path, pathkml.concat(file.getPath())});         
    }
    
    
    /**Insere as saídas geradas pelo algoritmo CBSMoT no Banco de Dados. */
    public void insertCBSMoT(Trajectory t, ObservableList<ClusterPoints> cluster) throws Exception
    {
        TrajectoryDAO td = new TrajectoryDAO();         
        String date = t.getGPSPoints().get(0).getDate();
        int trajectoryId = td.selectId(collar, date);

        //Remove todos os hotspots para adicionar objetos atualizados.
        td.removeHotSpots(trajectoryId);
        
        for(ClusterPoints c : cluster)
        {
            td.insertHotSpot(c, trajectoryId);
        }             
    }

}
