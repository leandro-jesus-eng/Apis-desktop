package satb.controller.functions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.model.ClusterPoints;
import satb.model.Coordinate;
import satb.model.SetOfPoints;
import satb.model.SubTrajectory;
import satb.model.TargetObject;
import satb.model.dao.TrajectoryPatternDAO;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.postgis.LineString;
import org.postgis.Point;
import org.postgis.Polygon;

/**Classe que fornece funções para os algoritmos de trajetórias semântica.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class TrajectoryFuntions 
{
    private GeoFunctions geo;
    
    /**Construtor Default. */
    public TrajectoryFuntions()
    {
        geo = new GeoFunctions();
    }

    /**Retorna true se o tempo de A for igual ou maior que o de B. */
    public boolean compareTwoHours(String hourA, String hourB)
    {
        
        String[] parts = hourA.split(":");
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        cal1.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        cal1.set(Calendar.SECOND, Integer.parseInt(parts[2]));

        parts = hourB.split(":");
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        cal2.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        cal2.set(Calendar.SECOND, Integer.parseInt(parts[2]));
        
        // Add 1 day because you mean 00:16:23 the next day
        cal1.add(Calendar.DATE, 1);
        cal2.add(Calendar.DATE, 1);

               
        if (cal2.before(cal1) || cal2.equals(cal1)) 
        {
            return true;
        }
        
        return false;
    }
    
    /**Método que executa a operação hourA + hourB. */
    public String plusHours(String hourA, String hourB)
    {
        /// A= 11:00:00  B = 11:15:00
        // parse the string
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        DateTime dateTime = formatter.parseDateTime(hourA);
        
        String[] parts = hourB.split(":");
        dateTime = dateTime.plusSeconds(Integer.parseInt(parts[2]));
        dateTime = dateTime.plusMinutes(Integer.parseInt(parts[1]));
        dateTime = dateTime.plusHours(Integer.parseInt(parts[0]));
        
        return dateTime.toString(formatter);        
    }        
    
    
    /**Método que executa a operação hourB - hourA. */
    public String subtractHours(String hourA, String hourB)
    {
        /// A= 11:00:00  B = 11:15:00
        // parse the string
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        DateTime dateTime = formatter.parseDateTime(hourB);
        
        String[] parts = hourA.split(":");
        dateTime = dateTime.minusSeconds(Integer.parseInt(parts[2]));
        dateTime = dateTime.minusMinutes(Integer.parseInt(parts[1]));
        dateTime = dateTime.minusHours(Integer.parseInt(parts[0]));
        
        return dateTime.toString(formatter);        
    }
    
    
    /**Método que retorna um horário em segundos. */
    public int timeInSeconds(String hour) 
    {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        DateTime dateTime = formatter.parseDateTime(hour);
        
        return dateTime.getSecondOfDay();
    }
    
    public boolean limitedNeighborhood(ObservableList<Coordinate> points, Coordinate point, int clusterId, double avgSpeed, int minTime, double speedLimit) 
    {
        if (point.getSpeed() > speedLimit) 
        {
            return false;
        }

        SetOfPoints seeds = new SetOfPoints();
        seeds.addToEnd(point);
        
        slowestNeighborhood(points, seeds, speedLimit, minTime);
        double meanSpeed = seeds.meanSpeed();

        if (meanSpeed > avgSpeed || seeds.durationInSeconds() < minTime)
        {
            return false;
        } 
        else 
        {
            seeds.setClusterId(clusterId);
            while (true) 
            {
                Coordinate addedPoint = addSlowerNeighborToSeeds(points, seeds);
                if (addedPoint != null) 
                {
                    double newMeanSpeed = seeds.meanSpeed();
                    if (newMeanSpeed <= avgSpeed && addedPoint.getSpeed() <= speedLimit) 
                    {
                        if (addedPoint.getClusterId() == Coordinate.NULL_CLUSTER_ID) 
                        {
                            addedPoint.setClusterId(clusterId);
                        } 
                        else 
                        {
                            break;
                        }
                    } 
                    else 
                    {
                        break;
                    }
                } 
                else 
                {
                    break;
                }
            }
        }
        return true;
    }
    
    /**Fills the cluster with such points that its' time do not exceed the limite minTime.*/
    public void slowestNeighborhood(ObservableList<Coordinate> points, SetOfPoints seeds, double speedLimit, int minTime) 
    {
        Coordinate leftPoint;
        Coordinate rightPoint;

        do 
        {
            try 
            {
                leftPoint = points.get(seeds.getFirstPointTimeIndex()-1);
             
            } 
            catch (IndexOutOfBoundsException eLeft) 
            {
                try 
                {
                    rightPoint = points.get(seeds.getLastPointTimeIndex()+1);

                } 
                catch (IndexOutOfBoundsException eRight) 
                {
                    break;
                }
                if (rightPoint.getClusterId() == Coordinate.NULL_CLUSTER_ID && rightPoint.getSpeed() <= speedLimit) 
                {
                    
                    seeds.addToEnd(rightPoint);
                    continue;
                } 
                else 
                {
                    break;
                }
            }
            try 
            {
                rightPoint = points.get(seeds.getLastPointTimeIndex()+1);

            } 
            catch (IndexOutOfBoundsException eRight) 
            {
                
                if (leftPoint.getClusterId() == Coordinate.NULL_CLUSTER_ID && leftPoint.getSpeed() <= speedLimit) 
                {
                    seeds.addToBegin(leftPoint);
                    continue;
                } 
                else 
                {
                    break;
                }
            }
            if ((leftPoint.getSpeed() <= rightPoint.getSpeed()) && leftPoint.getClusterId() == Coordinate.NULL_CLUSTER_ID && leftPoint.getSpeed() <= speedLimit) 
            {
                seeds.addToBegin(leftPoint);
            } 
            else if (rightPoint.getClusterId() == Coordinate.NULL_CLUSTER_ID && rightPoint.getSpeed() <= speedLimit) 
            {
                seeds.addToEnd(rightPoint);
            } 
            else 
            {
                break;
            }

        } while (seeds.durationInSeconds() < minTime);

    }
    
    private Coordinate addSlowerNeighborToSeeds(ObservableList<Coordinate> points, SetOfPoints seeds) 
    {
        Coordinate leftPoint;
        Coordinate rightPoint;
        
        try 
        {
            leftPoint = points.get(seeds.getFirstPointTimeIndex()-1);
        } 
        catch (IndexOutOfBoundsException eLeft) 
        {
            leftPoint = null;
        }
        try 
        {
            rightPoint =  points.get(seeds.getLastPointTimeIndex()+1);
        } 
        catch (IndexOutOfBoundsException eRight) 
        {
            if (leftPoint != null) 
            {
                seeds.addToBegin(leftPoint);
            }
            return leftPoint;
        }
        if ((leftPoint != null) && (leftPoint.getSpeed() <= rightPoint.getSpeed())) 
        {
            seeds.addToBegin(leftPoint);
            return leftPoint;
        } 
        else 
        {
            seeds.addToEnd(rightPoint);
            return rightPoint;
        }
    }

    
    /**Cria os clusters com base nos dados processados pelo CB-SMoT. */
    public ObservableList<ClusterPoints> createClusters(ObservableList<Coordinate> points) 
    {
        ObservableList<Coordinate> clusterPoints = FXCollections.observableArrayList();
        for (Coordinate p: points) 
        {
            if (p.getClusterId() != Coordinate.NULL_CLUSTER_ID) 
            {
                clusterPoints.add(p);
            }
        }

        ObservableList<ClusterPoints> clusters = FXCollections.observableArrayList();
        ClusterPoints cluster = new ClusterPoints();

        if (clusterPoints.size() > 0) 
        {
            Coordinate c = clusterPoints.get(0);
            cluster.points.add(c);
            int lastClusterId = c.getClusterId();
            cluster.setClusterId(lastClusterId);
            int i = 1;
            while (i < clusterPoints.size()) 
            {
                c = clusterPoints.get(i);
                if (c.getClusterId() != lastClusterId) 
                {
                    clusters.add(cluster);
                    cluster = new ClusterPoints();
                    lastClusterId = c.getClusterId();
                    cluster.setClusterId(lastClusterId);
                }
                cluster.points.add(c);
                i++;
            }
            clusters.add(cluster); // adiciona o ultimo cluster
        }
        
        return clusters;
    }
    
    /**Unifica dois clusters que estão próximos. */
    public ObservableList<Coordinate> unifyClusters(ObservableList<Coordinate> points) 
    {
        if (points.size() < 2) 
        {
            return points;
        }
        
        int i = 1;
        while (i < points.size()) 
        {
            Coordinate c = points.get(i); // gets the second point ordered by time
            int lastClusterId = points.get(c.getTimeIndex()-1).getClusterId();
            if (c.getClusterId() != Coordinate.NULL_CLUSTER_ID && lastClusterId != Coordinate.NULL_CLUSTER_ID) 
            {
                if (c.getClusterId() != lastClusterId) 
                {
                    int newClusterId = Math.min(lastClusterId, c.getClusterId());
                    points.get(i).setClusterId(newClusterId);
                }
            }
            i++;
        }
        
        return points;
    }
    

    public ObservableList<SubTrajectory> divideTrajectory(ObservableList<Coordinate> pointsIn)
    {
        ObservableList<SubTrajectory> sub = FXCollections.observableArrayList();
        int id = pointsIn.get(0).getId(), begin = 0, end = 0;
     
        while(end < pointsIn.size())
        {    
           if(id != pointsIn.get(end).getId())
           {
               sub.add(new SubTrajectory(pointsIn.subList(begin, end)));
               id = pointsIn.get(end).getId();
               begin = end;
           }
            
           end = end + 1; 
           id = id + 1;
        }
        
        int count = 0;
        for(SubTrajectory subt : sub)
        {
            count = count + subt.size();
        }
        
        count = pointsIn.size() - count;
        
        if(count > 0)
        {
           sub.add(new SubTrajectory(pointsIn.subList(begin, end))); 
        }
        
        return sub;
    }
    
 
    /**Através de uma sub-trajetória, identifica a maior distância em direção ao objeto alvo. */
    public ObservableList<SubTrajectory> SubtrajDT(ObservableList<Coordinate> pointsIn, TargetObject target) throws Exception
    {
        TrajectoryPatternDAO tpd = new TrajectoryPatternDAO();
        //Retorna o subconjunto presente na Área de Interesse
        ObservableList<SubTrajectory> sub = divideTrajectory(pointsIn);
        Coordinate ini;
        double dist, ap,auxdist;
        int index, nIndex;
 
        for(SubTrajectory subt  : sub)
        {
            dist = 0;
            index = 0;
            
            if(subt.size() > 1)
            {        
                //Recebe o ponto inicial da trajetória.
                ini = subt.get(index);
                nIndex = index + 1;

                while(nIndex < subt.size())
                {
                    // Calcula o azimute
                    ap = tpd.azimute(subt.get(index).getCoordinate(), subt.get(nIndex).getCoordinate());

                    Coordinate cAux = geo.findNextPoint(subt.get(index).getCoordinate(), target.getRadiusInterest(), ap);
                    LineString line = new LineString(tpd.makeLine(subt.get(index).getCoordinate(), cAux.getCoordinate()));

                //    showTargetObject(target, subt.get(index), subt.get(nIndex), cAux, line, target.getAreaInterest());
                //    pause(10);

                    //Se a trajetória passa pela área do objeto
                    if(tpd.intersectSubInTarget(line, target.getAreaObject()))
                    { 
                        auxdist = subt.get(index).distance(subt.get(nIndex));
                        if(auxdist > dist)
                        {
                            dist = auxdist;
                            ini = subt.get(index); // Guarda ponto inicial da maior subtrajetória identificada
                        } 
                        nIndex = nIndex + 1;             
                    }
                    else
                    {
                        index = index + 1;
                        nIndex = index + 1;
                    }    
                }

                subt.setDistance(dist);
                subt.setInitialPosition(ini);
            }
        }       
        return sub;
    }
    
   
    /**Retorna a região de incremento. */
    public Polygon regionOfIncrement(TargetObject target, ObservableList<Coordinate> traj, SubTrajectory subt) throws Exception
    {      
        TrajectoryPatternDAO tpd = new TrajectoryPatternDAO();           
        LineString line;
        Coordinate center, c2;
        Polygon  lineArea, diff;
        double azi;
        
        //Pega o ponto central da área objeto-alvo.
        center = new Coordinate(tpd.centroid(target.getAreaObject()));

        //Pega o azimute entre o ponto inicial e o centroide.
        azi = tpd.azimute(subt.getInitialPoint().getCoordinate(), center.getCoordinate());      
               
        c2 = geo.findNextPoint(center.getCoordinate(), target.getRadiusInterest(), azi);
        
        line = new LineString(tpd.makeLine(center.getCoordinate(), c2.getCoordinate()));
        lineArea = tpd.buildLineArea(line, target.getRadiusInterest());
        
        diff = tpd.diffBetweenAreas(lineArea, target.getAreaObject());
           
     //   showTargetObject(target, subt.getInitialPoint(), center, c2, line, diff);
     //   pause(20);
        return diff;
    }
    
    
    public static void pause(int seconds)
    {
        
        Date start = new Date();
        Date end = new Date();
        while(end.getTime() - start.getTime() < seconds * 1000)
        {
            end = new Date();
        }
    }
    
    
    
     
    public void showTargetObject(TargetObject area, Coordinate c, Coordinate center, Coordinate c2, LineString l, Polygon area2) throws Exception
    {
        String pathfile = "C:/Users/Marcel/Documents/Mestrado/";
        String path = "C:/Program Files (x86)/Google/Google Earth/client/googleearth.exe";
               
        //Invoca o método de construção do KML
        File file = teste("kml/teste.kml", area, c, center, c2, l, area2);
               
        //Executa o google earth com o kml gerado
        Runtime.getRuntime().exec(new String[] {path,pathfile.concat(file.getPath())});  
    }
   
   
   public File teste(String filename, TargetObject target, Coordinate c, Coordinate center, Coordinate c2, LineString l, Polygon area) throws FileNotFoundException, IOException
   {
        //Faz uso da Biblioteca JDOM
        //Passo 1: gerar o stub XML
        Namespace ns = Namespace.getNamespace("", "http://earth.google.com/kml/2.2");
       
        // KML
        Element kml = new Element("kml", ns);
        Document kmlDocument = new Document(kml);
   
        // Document
        Element document = new Element("Document", ns);
        kml.addContent(document);
   
        // name
        Element name = new Element("name", ns);
        name.setText("Testes");
        document.addContent(name);

        //Passo 2: adiciona elementos no Style
        // Style
        Element style = new Element("Style", ns);
        style.setAttribute("id", "default");
        document.addContent(style);

        // LineStyle - Style
        Element lineStyle = new Element("LineStyle", ns);
        style.addContent(lineStyle);

        // color - LineStyle
        Element color = new Element("color", ns);
        color.setText("ff00ffff");
        lineStyle.addContent(color);
       
        //width - - LineStyle
        Element width = new Element("width", ns);
        width.setText("1.5");
        lineStyle.addContent(width);
       
        //IconStyle
        Element iconStyle = new Element("IconStyle", ns);
        style.addContent(iconStyle);

        // color - IconStyle
        Element iconColor = new Element("color", ns);
        iconColor.setText("ff00ffff");
        iconStyle.addContent(iconColor);

        //scale - IconStyle
        Element iconScale = new Element("scale", ns);
        iconScale.setText("0.8");
        iconStyle.addContent(iconScale);
       
         //LabelStyle
        Element labelStyle = new Element("LabelStyle", ns);
        style.addContent(labelStyle);

        //scale - LabelStyle
        Element labelScale = new Element("scale", ns);
        labelScale.setText("0.6");
        labelStyle.addContent(labelScale);
     
         // Style
        Element style2 = new Element("Style", ns);
        style2.setAttribute("id", "polygon");
        document.addContent(style2);

        // LineStyle - Style
        Element lineStyle2 = new Element("LineStyle", ns);
        style2.addContent(lineStyle2);

        // color - LineStyle
        Element colorLineSyle = new Element("color", ns);
        colorLineSyle.setText("ff00ffff");
        lineStyle2.addContent(colorLineSyle);

        // PolyStyle - Style
        Element polyStyle = new Element("PolyStyle", ns);
        style2.addContent(polyStyle);

        // color - PolyStyle
        Element polyStylecolor = new Element("color", ns);
        polyStylecolor.setText("7fffffff");
        polyStyle.addContent(polyStylecolor);
               
        // styleMap
        Element styleMap = new Element("StyleMap", ns);
        styleMap.setAttribute("id", "msn_ylw-pushpin2");
        document.addContent(styleMap);

        //Pair
        Element pair = new Element("Pair", ns);
        styleMap.addContent(pair);
       
        //key
        Element key = new Element("key", ns);
        key.setText("normal");
        pair.addContent(key);
       
        // styleUrl - Pair
        Element styleUrlPair = new Element("styleUrl", ns);
        styleUrlPair.setText("#polygon");
        pair.addContent(styleUrlPair);
       
        //Pair
        Element pair2 = new Element("Pair", ns);
        styleMap.addContent(pair2);
       
        //key
        Element key2 = new Element("key", ns);
        key2.setText("highlight");
        pair2.addContent(key2);
       
        // styleUrl - Pair2
        Element styleUrlPair2 = new Element("styleUrl", ns);
        styleUrlPair2.setText("#polygon");
        pair2.addContent(styleUrlPair2);
       
        //Passo 3: ler os dados da origem e adicionar no Placemark
        // Placemark
        Element placemark = new Element("Placemark", ns);
        document.addContent(placemark);

        // name
        Element polygonName = new Element("name", ns);
        polygonName.setText("Área");
        placemark.addContent(polygonName);

        // styleUrl - Placemark
        Element styleUrl2 = new Element("styleUrl", ns);
        styleUrl2.setText("#msn_ylw-pushpin2");
        placemark.addContent(styleUrl2);

        //polygon
        Element polygon = new Element("Polygon", ns);
        placemark.addContent(polygon);

        //extrude
        Element extrude = new Element("extrude", ns);
        extrude.setText("1");
        polygon.addContent(extrude);    

        //outerBoundaryIs
        Element outerBoundary = new Element("outerBoundaryIs", ns);
        polygon.addContent(outerBoundary);

        //LinearRing
        Element linearRing = new Element("LinearRing", ns);
        outerBoundary.addContent(linearRing);

        //Coordinates
        Element Coordinates = new Element("coordinates", ns);
        for(int i = 0; i < target.getAreaObject().numPoints(); i++)
        {
            Point p = target.getAreaObject().getPoint(i);
            Coordinates.addContent(""+ String.valueOf(p.getX()) +"," + String.valueOf(p.getY())  +",0 ");
        }
        linearRing.addContent(Coordinates);
       
       
       
        // Placemark
        Element secondPlacemark = new Element("Placemark", ns);
        document.addContent(secondPlacemark);

        // name
        Element secondPolygonName = new Element("name", ns);
        secondPolygonName.setText("Área");
        secondPlacemark.addContent(secondPolygonName);

        // styleUrl - Placemark
        Element secondStyleUrl = new Element("styleUrl", ns);
        secondStyleUrl.setText("#msn_ylw-pushpin2");
        secondPlacemark.addContent(secondStyleUrl);

        //polygon
        Element secondPolygon = new Element("Polygon", ns);
        secondPlacemark.addContent(secondPolygon);

        //extrude
        Element extrude2 = new Element("extrude", ns);
        extrude2.setText("1");
        secondPolygon.addContent(extrude2);    

        //outerBoundaryIs
        Element outerBoundary2 = new Element("outerBoundaryIs", ns);
        secondPolygon.addContent(outerBoundary2);

        //LinearRing
        Element linearRing2 = new Element("LinearRing", ns);
        outerBoundary2.addContent(linearRing2);
       
        //Coordinates
        Element Coordinates2 = new Element("coordinates", ns);
        for(int i = 0; i < l.numPoints(); i++)
        {
            Point p = l.getPoint(i);
            Coordinates2.addContent(""+ String.valueOf(p.getX()) +"," + String.valueOf(p.getY())  +",0 ");
        }
        linearRing2.addContent(Coordinates2);
       
       
        // Placemark3
        Element thirdPlacemark = new Element("Placemark", ns);
        document.addContent(thirdPlacemark);

        // name  - Placemark3
        Element pName = new Element("name", ns);
   //     pName.setText("Ponto Inicial");
        thirdPlacemark.addContent(pName);

        // styleUrl2 - Placemark3
        Element styleUrl3 = new Element("styleUrl", ns);
        styleUrl3.setText("#default");
        thirdPlacemark.addContent(styleUrl3);

        // Point2D  - Placemark3
        Element pPoint = new Element("Point", ns);
        thirdPlacemark.addContent(pPoint);

        // coordinates
        Element pCoordinates = new Element("coordinates", ns);            
        pCoordinates.addContent(""+ String.valueOf(c.getLongitudeX()) +"," + String.valueOf(c.getLatitudeY())  +",0 ");            
        pPoint.addContent(pCoordinates);
           
        
        // Placemark3
        Element forthPlacemark = new Element("Placemark", ns);
        document.addContent(forthPlacemark);

        // name  - Placemark3
        Element p4Name = new Element("name", ns);
   //     p4Name.setText("Ponto Final");
        forthPlacemark.addContent(p4Name);

        // styleUrl2 - Placemark3
        Element styleUrl4 = new Element("styleUrl", ns);
        styleUrl4.setText("#default");
        forthPlacemark.addContent(styleUrl4);

        // Point2D  - Placemark3
        Element p4Point = new Element("Point", ns);
        forthPlacemark.addContent(p4Point);

        // coordinates
        Element p4Coordinates = new Element("coordinates", ns);            
        p4Coordinates.addContent(""+ String.valueOf(c2.getLongitudeX()) +"," + String.valueOf(c2.getLatitudeY())  +",0 ");            
        p4Point.addContent(p4Coordinates);
        
        
        // Placemark3
        Element sixtyPlacemark = new Element("Placemark", ns);
        document.addContent(sixtyPlacemark);

        // name  - Placemark3
        Element pName6 = new Element("name", ns);
   //     pName6.setText("Centro");
        sixtyPlacemark.addContent(pName6);

        // styleUrl2 - Placemark3
        Element styleUrl6 = new Element("styleUrl", ns);
        styleUrl6.setText("#default");
        sixtyPlacemark.addContent(styleUrl6);

        // Point2D  - Placemark3
        Element pPoint6 = new Element("Point", ns);
        sixtyPlacemark.addContent(pPoint6);

        // coordinates
        Element pCoordinates6 = new Element("coordinates", ns);            
        pCoordinates6.addContent(""+ String.valueOf(center.getLongitudeX()) +"," + String.valueOf(center.getLatitudeY())  +",0 ");            
        pPoint6.addContent(pCoordinates6);
        
        
        //Passo 3: ler os dados da origem e adicionar no Placemark
        // Placemark
        Element fifthPlacemark = new Element("Placemark", ns);
        document.addContent(fifthPlacemark);

        // name
        Element polygonName5 = new Element("name", ns);
        polygonName5.setText("Área de Incremento");
        fifthPlacemark.addContent(polygonName5);

        // styleUrl - Placemark
        Element styleUrl5 = new Element("styleUrl", ns);
        styleUrl5.setText("#msn_ylw-pushpin2");
        fifthPlacemark.addContent(styleUrl5);

        //polygon
        Element polygon5 = new Element("Polygon", ns);
        fifthPlacemark.addContent(polygon5);

        //extrude
        Element extrude5 = new Element("extrude", ns);
        extrude5.setText("1");
        polygon5.addContent(extrude5);    

        //outerBoundaryIs
        Element outerBoundary5 = new Element("outerBoundaryIs", ns);
        polygon5.addContent(outerBoundary5);

        //LinearRing
        Element linearRing5 = new Element("LinearRing", ns);
        outerBoundary5.addContent(linearRing5);

        //Coordinates
        Element Coordinates5 = new Element("coordinates", ns);
        for(int i = 0; i < area.numPoints(); i++)
        {
            Point p = area.getPoint(i);
            Coordinates5.addContent(""+ String.valueOf(p.getX()) +"," + String.valueOf(p.getY())  +",0 ");
        }
        linearRing5.addContent(Coordinates5);
        
       
        // Passo 5: escrever o arquivo XML
        File file = new File(filename);
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        try (FileOutputStream writer = new FileOutputStream(file))
        {
            outputter.output(kmlDocument, writer);
        }
       return file;
  }
    
}
