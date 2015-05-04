package satb.controller.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import satb.model.Coordinate;
import org.postgis.Point;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;

/**Classe com métodos para manipulação de coordenadas geograficas.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class GeoFunctions 
{
    /**Construtor Default. */
    public GeoFunctions(){}
    
    //Raio da Terra em KM
    public static int EARTH_RADIUS_KM = 6371;
           
    /**Recebe uma string com coordenadas e retorna a longitude. */
    public double returnLongitude (String line)
    {
        StringTokenizer st = new StringTokenizer(line);

        return Double.parseDouble(st.nextToken(","));
    }

    
   /**Recebe uma string com coordenadas e retorna a latitude. */ 
    public double returnLatitude (String line)
    {
        StringTokenizer st = new StringTokenizer(line); 
        st.nextToken(",");

        return Double.parseDouble(st.nextToken());
    }
      
    
   /**Converter o radianos para grau. 
    @param value - o valor em radianos.
    @return o valor convertido em graus.*/
   public double toDeg(double value) 
   {
        return value * 180/Math.PI;
   }
   
   
   /**Converter o grau para radianos. 
    @param value - o valor em graus.
    @return o valor convertido em radianos.*/
   public double toRad(double value) 
   {
       return value * Math.PI/180;
   }

   /**Encontra o segundo ponto, tendo o ponto inicial, a distância e a direção. */
   public Coordinate findNextPoint(Point p, double radius, double degree)
   {
       double dx = radius*Math.sin(degree);  // theta measured clockwise from due north
       double dy = radius*Math.cos(degree);  // dx, dy same units as R
       
       double delta_latitude = dy/110540; //result in degrees long/lat
       double delta_longitude = dx/(111320*Math.cos(toRad(p.getY()))); // dx, dy in meters

       double finalLat = p.getY() + delta_latitude;
       double finalLon = p.getX() + delta_longitude;
       
       return new Coordinate(finalLon,finalLat);  
   }
   

   /**Retorna a distância em Kilometros. */
   public double geoDistanceInKm(double firstLatitude, double firstLongitude, double secondLatitude, double secondLongitude) 
    {	        
	firstLatitude = toRad(firstLatitude);
	firstLongitude = toRad(firstLongitude);
	secondLatitude = toRad(secondLatitude);
	secondLongitude =  toRad(secondLongitude);
	     
        double dLat = secondLatitude - firstLatitude;
        double dLong = secondLongitude - firstLongitude;
	            
	double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(firstLatitude) * Math.cos(secondLatitude) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            
        return (EARTH_RADIUS_KM * c);
   }
   
    
   /**Retorna a maior distância entre o centroide e os seus pontos. */ 
   public double greatestDistance (int index, Instance centroid, SimpleKMeans kMeans, Instances instances) throws Exception
   {
       double latitude, longitude, distance, greatDistance = 0;
       //Inicializa os valores de latitude e longitude do centroide
       latitude = returnLatitude(centroid.toString());
       longitude = returnLongitude(centroid.toString()); 
       
       int[] assignments = kMeans.getAssignments();

       int i = 0;
       for(int clusterNum : assignments) 
       {
            if(clusterNum == index)
            {
                distance = geoDistanceInKm(latitude, longitude, returnLatitude(instances.instance(i).toString()), returnLongitude(instances.instance(i).toString()));

                if(distance >= greatDistance)
                {
                    greatDistance = distance;
                }
            }
            i++;
       }    
       return greatDistance;
   }
   
   /**Através de um ponto origem, a distância e o sentido, retorna o ponto destino. */
   public String discoverCoordinates (double lon, double lat, double dist, char sense)
   {
        // given a VELatLon point latLon, a distance (in km),
        // and a direction (‘N’, ‘S’, ‘E’, or ‘W’),
        // return the VELatLong
        // see http://mathforum.org/library/drmath/view/51816.html for the math
       
       double d = dist/EARTH_RADIUS_KM; // convert dist to arc radians
       double tc = 0;  // heading in radians
       double lat2, lon2;
       
       lat = toRad(lat); 
       lon = toRad(lon);
        
       if(sense == 'N') 
       {
           tc = 0;
       }
       else if(sense == 'S') 
       {
           tc = Math.PI;
       }
       else if(sense == 'E') 
       {
           tc = Math.PI/2;
       }
       else if(sense == 'W') 
       {
           tc = 3*Math.PI/2;
       }
        
       lat2 = Math.asin(Math.sin(lat) * Math.cos(d) + Math.cos(lat) * Math.sin(d) * Math.cos(tc));
       double dlon = Math.atan2(Math.sin(tc) * Math.sin(d) * Math.cos(lat), Math.cos(d) - Math.sin(lat) * Math.sin(lat));
       lon2 =((lon + dlon + Math.PI) % (2 * Math.PI)) - Math.PI;
        
       lat2 = toDeg(lat2);
       lon2 = toDeg(lon2);         
      // lat1 = toDeg(lat1); 
      // lon1 = toDeg(lon1);
          
       String result = "" + lon2 + " " + lat2 +"";
       
       return result;
   }
   
  
   //Cria um hexagono regular tendo o centroide do cluster como ponto central do poligono 
   public List<String> createHexagon (String line, double radius)
   {		
        List<String> hexagon = new ArrayList<>();
        double longitude, latitude, apotema;
        String result, result2;
        
        longitude = returnLongitude(line);   //eixo x
        latitude = returnLatitude(line);    //eixo y
        apotema = (radius * Math.sqrt(3.0))/2;  //apotema do hexagono
       
      
        //ponto 1               
        result = discoverCoordinates (latitude, longitude, radius, 'W');
        hexagon.add(""+ returnLongitude(result) +","+ returnLatitude(result) +"");
        
        //ponto 2
        result = discoverCoordinates (latitude, longitude, radius/2, 'W');
        result2 = discoverCoordinates (returnLatitude(result), returnLongitude(result), apotema, 'N');
        hexagon.add(""+ returnLongitude(result2) +","+ returnLatitude(result2) +"");
       
        //ponto 3
        result = discoverCoordinates (latitude, longitude, radius/2, 'E');
        result2 = discoverCoordinates (returnLatitude(result), returnLongitude(result), apotema, 'N');
        hexagon.add(""+ returnLongitude(result2) +","+ returnLatitude(result2) +"");
        
        //ponto 4
        result = discoverCoordinates (latitude, longitude, radius, 'E');
        hexagon.add(""+ returnLongitude(result) +","+ latitude +"");
        
        
        //ponto 5
        result = discoverCoordinates (latitude, longitude, radius/2, 'E');
        result2 = discoverCoordinates (returnLatitude(result), returnLongitude(result), apotema, 'S');
        hexagon.add(""+ returnLongitude(result2) +","+ returnLatitude(result2) +"");
        
        //ponto 6
	result = discoverCoordinates (latitude, longitude, radius/2, 'W');
        result2 = discoverCoordinates (returnLatitude(result), returnLongitude(result), apotema, 'S');
        hexagon.add(""+ returnLongitude(result2) +","+ returnLatitude(result2) +"");
        
        //ponto 7
        result = discoverCoordinates (latitude, longitude, radius, 'W');
        hexagon.add(""+ returnLongitude(result) +","+ latitude +"");
        
        System.out.println();
        System.out.printf("Maior Distância:");
        System.out.println(radius);
        System.out.printf("Apotema:");
        System.out.println(apotema);

        System.out.println("Hexágono");
        for(String s : hexagon)
        {
            System.out.println(s);
        }
        System.out.println();
        	
        return hexagon;
    }   
   
   
} 
    
    
 
 
   
 
