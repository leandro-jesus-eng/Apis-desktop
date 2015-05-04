package satb.controller.functions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javafx.collections.ObservableList;
import jsat.classifiers.DataPoint;
import satb.model.ClusterPoints;
import satb.model.Clustering;
import satb.model.Coordinate;
import satb.model.TargetObject;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.postgis.Point;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;

/**Classe com métodos para manipulação e criação de arquivos kml.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class KMLFunctions 
{
    /**Contrutor Default. */
    public KMLFunctions(){}
    
    /**Elabora o nome de um arquivo para uma área. */
    public String buildAreaKmlName(String name)
    {       
        String kmlname = "kml/";
        kmlname = kmlname.concat(name);
        kmlname = kmlname.concat(".kml");        
        return kmlname;
    }
    
     /**Elabora o nome de um arquivo de uma trajetória pegando o nome do colar e data. */
    public String buildKmlName(String collar, String date)
    {       
        String kmlname = "kml/";
        kmlname = kmlname.concat(collar.concat(date.substring(0, 2)));
        kmlname = kmlname.concat(date.substring(3, 5));
        kmlname = kmlname.concat(date.substring(6, 8));
        kmlname = kmlname.concat("tracking.kml");        
        return kmlname;
    }
    
    
   /**Elabora o nome de um arquivo de duas trajétriaspegando os nomes dos colares e datas. */
    public String build2KmlName(String collar, String date, String secondCollar, String secondDate)
    {       
        String kmlname = "kml/";
        //Primeira trajetória
        kmlname = kmlname.concat(collar.concat(date.substring(0, 2)));
        kmlname = kmlname.concat(date.substring(3, 5));
        kmlname = kmlname.concat(date.substring(6, 8));
        //Segunda trajetória
        kmlname = kmlname.concat(secondCollar.concat(secondDate.substring(0, 2)));
        kmlname = kmlname.concat(secondDate.substring(3, 5));
        kmlname = kmlname.concat(secondDate.substring(6, 8));
        
        kmlname = kmlname.concat("tracking.kml");        
        
        return kmlname;
    }
    
    
    /**Elabora o nome do arquivo pegando o nome.
    * @param collar - identificação do colar.
    * @return String  - nome do arquivo kml com o resultado do DBSCAN.*/
    public String buildDBSCANKMLName(String collar)
    {       
        String kmlname = "kml/";
        kmlname = kmlname.concat(collar);
        kmlname = kmlname.concat("lsdbc.kml");
        
        return kmlname;
    }
    
    
    /**Elabora o nome do arquivo pegando o nome.
    * @param collar - identificação do colar.
    * @return String  - nome do arquivo kml com o resultado do DBSCAN.*/
    public String buildDBSCANKMLName(ObservableList<String> collars)
    {                     
        String kmlname = "kml/";
        Iterator<String> iterator = collars.iterator();
        
        while(iterator.hasNext())
        {
            kmlname = kmlname.concat(iterator.next());
            if(iterator.hasNext())
            {
                kmlname = kmlname.concat("_");
            }           
        }   
        
        kmlname = kmlname.concat("dbscan.kml");
        
        return kmlname;
    }
    
    
    /**Elabora o nome do arquivo pegando o nome do colar a quantidade de clusters.
     * @param collar - identificação do colar.
     * @param  ncluster - número de clusters.
     * @return String  - nome do arquivo kml com o resultado do k-means.*/
    public String buildKMeansKMLName(ObservableList<String> collars, String ncluster)
    {                     
        String kmlname = "kml/";
        Iterator<String> iterator = collars.iterator();
        
        while(iterator.hasNext())
        {
            kmlname = kmlname.concat(iterator.next());
            if(iterator.hasNext())
            {
                kmlname = kmlname.concat("_");
            }           
        }   
        
        kmlname = kmlname.concat(ncluster);
        kmlname = kmlname.concat("kmeans.kml");
        
        return kmlname;
    }
    
    
    /**Elabora o nome do arquivo pegando o nome.
    * @param collar - identificação do colar.
    * @return String  - nome do arquivo kml com o resultado do DBSCAN.*/
    public String buildLSDBCKMLName(ObservableList<String> collars)
    {       
        String kmlname = "kml/";
        Iterator<String> iterator = collars.iterator();
        
        while(iterator.hasNext())
        {
            kmlname = kmlname.concat(iterator.next());
            if(iterator.hasNext())
            {
                kmlname = kmlname.concat("_");
            }           
        }
        kmlname = kmlname.concat("lsdbc.kml");
        
        return kmlname;
    }
    
    /**Elabora o nome do arquivo pegando o nome.
    * @param collar - identificação do colar.
    * @return String  - nome do arquivo kml com o resultado do DBSCAN.*/
    public String buildLSDBCKMLName(String collar)
    {       
        String kmlname = "kml/";
        kmlname = kmlname.concat(collar);
        kmlname = kmlname.concat("lsdbc.kml");
        
        return kmlname;
    }
    
    
    /**Elabora o nome do arquivo pegando o nome.
    * @param collar - identificação do colar.
    * @return String  - nome do arquivo kml com o resultado do PAM.*/
    public String buildPAMKMLName(ObservableList<String> collars)
    {       
        String kmlname = "kml/";
        Iterator<String> iterator = collars.iterator();
        
        while(iterator.hasNext())
        {
            kmlname = kmlname.concat(iterator.next());
            if(iterator.hasNext())
            {
                kmlname = kmlname.concat("_");
            }           
        }
        kmlname = kmlname.concat("pam.kml");
        
        return kmlname;
   }
   
    
   /**Cria e retorna o Arquivo KML contendo as coordenadas do rastro coletado pelo colar.
     @param filename - o nome do arquivo a ser criado.
     @param list - contem as coordenadas coletadas pelo colar.
     @return retorna o arquivo (FILE) kml formatado.*/
    public File buildTrackKML(String filename,  ObservableList<Coordinate> list ) throws FileNotFoundException, IOException
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
        name.setText("Trajetória Animal");
        document.addContent(name);
 
        //Passo 2: adiciona elementos no Style
        // Style
        Element style = new Element("Style", ns);
        style.setAttribute("id", "default");
        document.addContent(style);

        // LineStyle - Style
        Element lineStyle = new Element("LineStyle", ns);
        style.addContent(lineStyle);
        
        //width - - LineStyle
        Element width = new Element("width", ns);
        width.setText("0.8");
        lineStyle.addContent(width);
       
        //IconStyle
        Element iconStyle = new Element("IconStyle", ns);
        style.addContent(iconStyle);

        // color - IconStyle
        Element iconColor = new Element("color", ns);
        iconColor.setText("ff0000ff");
        iconStyle.addContent(iconColor);

        //scale - IconStyle
        Element iconScale = new Element("scale", ns);
        iconScale.setText("0.8");
        iconStyle.addContent(iconScale);
        
        //Icon
        Element icon = new Element("Icon", ns);
        iconStyle.addContent(icon);

        //href
        Element href = new Element("href", ns);
        href.setText("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
        icon.addContent(href);
        
         //LabelStyle
        Element labelStyle = new Element("LabelStyle", ns);
        style.addContent(labelStyle);

        //scale - LabelStyle
        Element labelScale = new Element("scale", ns);
        labelScale.setText("0.6");
        labelStyle.addContent(labelScale);
        
        //Passo 3: ler os dados da origem e adicionar no Placemark   
        //Placemark do Rastro Animal
        Element placemark = new Element("Placemark", ns);
        document.addContent(placemark);
            
        // name
        Element pmName = new Element("name", ns);
        pmName.setText("Trajetória");
        placemark.addContent(pmName);

        // styleUrl - Placemark2
        Element styleUrl = new Element("styleUrl", ns);
        styleUrl.setText("#default");
        placemark.addContent(styleUrl);
        
        // lineString
        Element lineString = new Element("LineString", ns);
        placemark.addContent(lineString);
        
        // coordinates
        Element lsCoordinates = new Element("coordinates", ns);
        for(Coordinate c : list)
        {          
            lsCoordinates.addContent(""+ String.valueOf(c.getLongitudeX()) +"," + String.valueOf(c.getLatitudeY())  +",0 ");
        }   
        lineString.addContent(lsCoordinates);
        
         //Passo 4: ler os dados da origem e adicionar no Placemark
         int i = 0;
         for(Coordinate c : list)
         {
            // Placemark3
            Element placemark2 = new Element("Placemark", ns);
            document.addContent(placemark2);
            
            // name  - Placemark3
            Element pName = new Element("name", ns);
            pName.setText("#"+(i + 1)+ "("+c.getHour()+")");
            placemark2.addContent(pName);
            
            // styleUrl2 - Placemark3
            Element styleUrl2 = new Element("styleUrl", ns);
            styleUrl2.setText("#default");
            placemark2.addContent(styleUrl2);
                 
            // Point2D  - Placemark3
            Element pPoint = new Element("Point", ns);
            placemark2.addContent(pPoint);

            // coordinates
            Element pCoordinates = new Element("coordinates", ns);            
            pCoordinates.addContent(""+ String.valueOf(c.getLongitudeX()) +"," + String.valueOf(c.getLatitudeY())  +",0 ");            
            pPoint.addContent(pCoordinates);
            
            i = i + 1;
        }
        
        // Passo 5: escrever o arquivo XML
        File file = new File(filename);
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        try (FileOutputStream writer = new FileOutputStream(file)) 
        {
            outputter.output(kmlDocument, writer);
            
        }
        return file;
    }
    
    
    /**Cria e retorna o Arquivo KML contendo as coordenadas de dois rastros coletados pelos colares.
     @param filename - o nome do arquivo a ser criado.
     @param list - contem as coordenadas do primeiro rastro coletados por um colar.
     @param list - contem as coordenadas do segundo rastro coletados por um colar.
     * @return retorna o arquivo (FILE) kml formatado.*/
    public File build2TracksKML(String filename,  ObservableList<Coordinate> list, ObservableList<Coordinate> secondList ) throws FileNotFoundException, IOException
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
        name.setText("Trajetória Animal");
        document.addContent(name);
 
        //Passo 2: adiciona elementos no Style
        // Style
        Element style = new Element("Style", ns);
        style.setAttribute("id", "default");
        document.addContent(style);

        // LineStyle - Style
        Element lineStyle = new Element("LineStyle", ns);
        style.addContent(lineStyle);
        
        //width - LineStyle
        Element width = new Element("width", ns);
        width.setText("0.8");
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
        
        //Icon
        Element icon = new Element("Icon", ns);
        iconStyle.addContent(icon);

        //href
        Element href = new Element("href", ns);
        href.setText("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
        icon.addContent(href);
        
         //LabelStyle
        Element labelStyle = new Element("LabelStyle", ns);
        style.addContent(labelStyle);

        //scale - LabelStyle
        Element labelScale = new Element("scale", ns);
        labelScale.setText("0.6");
        labelStyle.addContent(labelScale);
        
        // SECOND STYLE
        Element secondStyle = new Element("Style", ns);
        secondStyle.setAttribute("id", "default2");
        document.addContent(secondStyle);

        // LineStyle - Second Style
        Element secondLineStyle = new Element("LineStyle", ns);
        secondStyle.addContent(secondLineStyle);

        //secondWidth - LineStyle
        Element secondWidth = new Element("width", ns);
        secondWidth.setText("0.8");
        secondLineStyle.addContent(secondWidth);
       
        //IconStyle
        Element secondIconStyle = new Element("IconStyle", ns);
        secondStyle.addContent(secondIconStyle);

        //Icon
        Element secondIcon = new Element("Icon", ns);
        secondIconStyle.addContent(secondIcon);

        //href
        Element secondHref = new Element("href", ns);
        secondHref.setText("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
        secondIcon.addContent(secondHref);
        
        // color - IconStyle
        Element secondIconColor = new Element("color", ns);
        secondIconColor.setText("ff0000ff");
        secondIconStyle.addContent(secondIconColor);

        //scale - IconStyle
        Element secondIconScale = new Element("scale", ns);
        secondIconScale.setText("0.8");
        secondIconStyle.addContent(secondIconScale);
        
         //LabelStyle
        Element secondLabelStyle = new Element("LabelStyle", ns);
        secondStyle.addContent(secondLabelStyle);

        //scale - LabelStyle
        Element secondLabelScale = new Element("scale", ns);
        secondLabelScale.setText("0.6");
        secondLabelStyle.addContent(secondLabelScale);
  
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
        styleUrlPair.setText("#style2");
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
        styleUrlPair2.setText("#default2");
        pair2.addContent(styleUrlPair2);
       
        //Passo 3: ler os dados da origem e adicionar no Placemark  
        // Placemark
        Element placemark = new Element("Placemark", ns);
        document.addContent(placemark);
            
        // name
        Element pmName = new Element("name", ns);
        pmName.setText("1º Trajetória");
        placemark.addContent(pmName);
      
        // styleUrl - Placemark2
        Element styleUrl = new Element("styleUrl", ns);
        styleUrl.setText("#default");
        placemark.addContent(styleUrl);
        
        // lineString
        Element lineString = new Element("LineString", ns);
        placemark.addContent(lineString);
        
        // coordinates
        Element lsCoordinates = new Element("coordinates", ns);
        for(Coordinate c : list)
        {
            lsCoordinates.addContent(""+ String.valueOf(c.getLongitudeX()) +"," + String.valueOf(c.getLatitudeY()) +",0 ");            
        }   
        lineString.addContent(lsCoordinates);

         int i = 0;
         for(Coordinate p : list)
         {
            // Placemark3
            Element secondPlacemark = new Element("Placemark", ns);
            document.addContent(secondPlacemark);
            
            // name  - Placemark3
            Element pName = new Element("name", ns);
            pName.setText("#"+(i + 1)+ "("+p.getHour()+")");
            secondPlacemark.addContent(pName);

            // styleUrl2 - Placemark3
            Element styleUrl2 = new Element("styleUrl", ns);
            styleUrl2.setText("#default");
            secondPlacemark.addContent(styleUrl2);
                 
            // Point2D  - Placemark3
            Element pPoint = new Element("Point", ns);
            secondPlacemark.addContent(pPoint);

            // coordinates
            Element pCoordinates = new Element("coordinates", ns);
            String line;
            
            line = String.valueOf(p.getLongitudeX());
            line = line.concat(",");
           
            line = line.concat(String.valueOf(p.getLatitudeY()));
            line = line.concat(",");
                        
            line = line.concat("0");
            pCoordinates.setText(""+line+"");
            pPoint.addContent(pCoordinates);
            
            i = i + 1;
        }
         
        // Terceiro Placemark do Rastro Animal
        Element thirdPlacemark = new Element("Placemark", ns);
        document.addContent(thirdPlacemark);
            
        // name
        Element thirdName = new Element("name", ns);
        thirdName.setText("2º Trajetória");
        thirdPlacemark.addContent(thirdName);

        // styleUrl - Placemark2
        Element thirdStyleUrl = new Element("styleUrl", ns);
        thirdStyleUrl.setText("#default2");
        thirdPlacemark.addContent(thirdStyleUrl);
        
        // lineString
        Element thirdLineString = new Element("LineString", ns);
        thirdPlacemark.addContent(thirdLineString);
        
        // coordinates
        Element thirdCoordinates = new Element("coordinates", ns);
        for(Coordinate c : secondList)
        {
            thirdCoordinates.addContent(""+ String.valueOf(c.getLongitudeX()) +"," + String.valueOf(c.getLatitudeY()) +",0 ");                        
        }   
        thirdLineString.addContent(thirdCoordinates);
        
        int j = 0;
        for(Coordinate c : secondList)
         {
            // Placemark3
            Element secondPlacemark = new Element("Placemark", ns);
            document.addContent(secondPlacemark);
            
            // name  - Placemark3
            Element pName = new Element("name", ns);
            pName.setText("#"+(j + 1)+ "("+c.getHour()+")");
            secondPlacemark.addContent(pName);

            // styleUrl2 - Placemark3
            Element styleUrl2 = new Element("styleUrl", ns);
            styleUrl2.setText("#default2");
            secondPlacemark.addContent(styleUrl2);
                 
            // Point2D  - Placemark3
            Element pPoint = new Element("Point", ns);
            secondPlacemark.addContent(pPoint);

            // coordinates
            Element pCoordinates = new Element("coordinates", ns);
            pCoordinates.addContent(""+ String.valueOf(c.getLongitudeX()) +"," + String.valueOf(c.getLatitudeY()) +",0 ");
            pPoint.addContent(pCoordinates);
            
            j = j + 1;
        } 
      
        // Passo 5: escrever o arquivo XML
        File file = new File(filename);
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        try (FileOutputStream writer = new FileOutputStream(file)) 
        {
            outputter.output(kmlDocument, writer);
            
        }
       return file;
   } 
    
    
   /**Cria um arquivo KML com as coordenadas de uma área pertencente a uma pastagem. */
   public File buildArea(String filename, String areaName, ObservableList<Coordinate> area) throws FileNotFoundException, IOException, Exception
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
        name.setText(areaName);
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

        // hexaCoordinates
        Element Coordinates = new Element("coordinates", ns);
        for(Coordinate c : area)
        {
             Coordinates.addContent(""+ String.valueOf(c.getLongitudeX()) +"," + String.valueOf(c.getLatitudeY())  +",0 ");
        }
        linearRing.addContent(Coordinates);
        
        // Passo 4: escrever o arquivo XML
        File file = new File(filename);
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        try (FileOutputStream writer = new FileOutputStream(file)) 
        {
            outputter.output(kmlDocument, writer); 
        }

       return file;
   } 
   

  /**Cria e retorna o Arquivo KML contendo as posições dos clusters K-Means. */
  public File buildKMeansKML(String filename, SimpleKMeans kMeans, Instances inst) throws FileNotFoundException, IOException, Exception
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
        name.setText("Regiões mais Frequentes");
        document.addContent(name);
       
        //Passo 2 - Styles
        
        /*
         * STYLE MAP 
         */     

        for(int i=0; i < kMeans.getNumClusters(); i++)
        {
            Element styleMap = new Element("StyleMap", ns);
            styleMap.setAttribute("id", "default"+i+"");
            document.addContent(styleMap);

            //Pair
            Element pair = new Element("Pair", ns);
            styleMap.addContent(pair);

            //key
            Element key = new Element("key", ns);
            key.setText("normal");
            pair.addContent(key);

            // styleUrl - Pair
            Element styleUrl = new Element("styleUrl", ns);
            styleUrl.setText("#default"+i+"_n");
            pair.addContent(styleUrl);

            //Pair
            Element secondPair = new Element("Pair", ns);
            styleMap.addContent(secondPair);

            //key
            Element secondKey = new Element("key", ns);
            secondKey.setText("highlight");
            secondPair.addContent(secondKey);

            // styleUrl - Pair2
            Element secondStyleUrl = new Element("styleUrl", ns);
            secondStyleUrl.setText("#default"+i+"_hl");
            secondPair.addContent(secondStyleUrl);

            /*
             * STYLE - NORMAL
             */
        
            Element style = new Element("Style", ns);
            style.setAttribute("id", "default"+i+"_n");
            document.addContent(style);

            //IconStyle
            Element iconStyle = new Element("IconStyle", ns);
            style.addContent(iconStyle);

            // color - IconStyle
            Element iconColor = new Element("color", ns);
            iconColor.setText(getColor(i)); //ff00ffff yellow
            iconStyle.addContent(iconColor);

            //scale - IconStyle
            Element iconScale = new Element("scale", ns);
            iconScale.setText("0.5");
            iconStyle.addContent(iconScale);

            //Icon
            Element icon = new Element("Icon", ns);
            style.addContent(icon);

            //href
            Element href = new Element("href", ns);
            href.setText("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
            icon.addContent(href);
      
            //LabelStyle
            Element labelStyle = new Element("LabelStyle", ns);
            style.addContent(labelStyle);

            //scale - LabelStyle
            Element labelScale = new Element("scale", ns);
            labelScale.setText("0");
            labelStyle.addContent(labelScale);

             /*
             * STYLES - HIGHLIGHT
             */

            Element secondStyle = new Element("Style", ns);
            secondStyle.setAttribute("id", "default"+i+"_hl");
            document.addContent(secondStyle);

            //IconStyle
            Element iconStyle2 = new Element("IconStyle", ns);
            secondStyle.addContent(iconStyle2);

            // color - IconStyle
            Element iconColor2 = new Element("color", ns);
            iconColor2.setText(getColor(i)); //ff00ffff yellow
            iconStyle2.addContent(iconColor2);

            //scale - IconStyle
            Element iconScale2 = new Element("scale", ns);
            iconScale2.setText("0.65");
            iconStyle2.addContent(iconScale2);

            //Icon
            Element icon2 = new Element("Icon", ns);
            secondStyle.addContent(icon2);

            //href
            Element href2 = new Element("href", ns);
            href2.setText("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
            icon2.addContent(href2);
                        
            //LabelStyle
            Element labelStyle2 = new Element("LabelStyle", ns);
            secondStyle.addContent(labelStyle2);

            //scale - LabelStyle
            Element labelScale2 = new Element("scale", ns);
            labelScale2.setText("0");
            labelStyle2.addContent(labelScale2);
   
       }

        
       for(int i = 0; i < kMeans.getNumClusters(); i++)
       {
            Element styleMap = new Element("StyleMap", ns);
            styleMap.setAttribute("id", "centroid"+i+"");
            document.addContent(styleMap);

            //Pair
            Element pair = new Element("Pair", ns);
            styleMap.addContent(pair);

            //key
            Element key = new Element("key", ns);
            key.setText("normal");
            pair.addContent(key);

            // styleUrl - Pair
            Element styleUrl = new Element("styleUrl", ns);
            styleUrl.setText("#centroid"+i+"_n");
            pair.addContent(styleUrl);

            //Pair
            Element secondPair = new Element("Pair", ns);
            styleMap.addContent(secondPair);

            //key
            Element secondKey = new Element("key", ns);
            secondKey.setText("highlight");
            secondPair.addContent(secondKey);

            // styleUrl - Pair2
            Element secondStyleUrl = new Element("styleUrl", ns);
            secondStyleUrl.setText("#centroid"+i+"_hl");
            secondPair.addContent(secondStyleUrl);

            /*
             * STYLE - NORMAL
             */
        
            Element style = new Element("Style", ns);
            style.setAttribute("id", "centroid"+i+"_n");
            document.addContent(style);

            //IconStyle
            Element iconStyle = new Element("IconStyle", ns);
            style.addContent(iconStyle);

            // color - IconStyle
            Element iconColor = new Element("color", ns);
            iconColor.setText(getColor(i)); //ff00ffff yellow
            iconStyle.addContent(iconColor);

            //scale - IconStyle
            Element iconScale = new Element("scale", ns);
            iconScale.setText("0.8");
            iconStyle.addContent(iconScale);

            //Icon
            Element icon = new Element("Icon", ns);
            style.addContent(icon);

            //href
            Element href = new Element("href", ns);
            href.setText("http://maps.google.com/mapfiles/kml/shapes/placemark_circle.png");
            icon.addContent(href);
      
            //LabelStyle
            Element labelStyle = new Element("LabelStyle", ns);
            style.addContent(labelStyle);

            //scale - LabelStyle
            Element labelScale = new Element("scale", ns);
            labelScale.setText("0.6");
            labelStyle.addContent(labelScale);

             /*
             * STYLES - HIGHLIGHT
             */

            Element secondStyle = new Element("Style", ns);
            secondStyle.setAttribute("id", "centroid"+i+"_hl");
            document.addContent(secondStyle);

            //IconStyle
            Element iconStyle2 = new Element("IconStyle", ns);
            secondStyle.addContent(iconStyle2);

            // color - IconStyle
            Element iconColor2 = new Element("color", ns);
            iconColor2.setText(getColor(i)); //ff00ffff yellow
            iconStyle2.addContent(iconColor2);

            //scale - IconStyle
            Element iconScale2 = new Element("scale", ns);
            iconScale2.setText("1");
            iconStyle2.addContent(iconScale2);

            //Icon
            Element icon2 = new Element("Icon", ns);
            secondStyle.addContent(icon2);

            //href
            Element href2 = new Element("href", ns);
            href2.setText("http://maps.google.com/mapfiles/kml/shapes/placemark_circle.png");
            icon2.addContent(href2);
                        
            //LabelStyle
            Element labelStyle2 = new Element("LabelStyle", ns);
            secondStyle.addContent(labelStyle2);

            //scale - LabelStyle
            Element labelScale2 = new Element("scale", ns);
            labelScale2.setText("1");
            labelStyle2.addContent(labelScale2);
       } 
 
       //Passo 3: ler os dados da origem e adicionar no Placemark
       int[] assignments = kMeans.getAssignments();
       Instances centroids = kMeans.getClusterCentroids();
       int[] clusterSizes = kMeans.getClusterSizes();
       
       int total = 0; 
       for(int i = 0; i < kMeans.getNumClusters(); i++)
       { 
           total = total + clusterSizes[i];
       }
       
       for(int i = 0; i < kMeans.getNumClusters(); i++)
       {     
            // Placemark
            Element placemark = new Element("Placemark", ns);
            document.addContent(placemark);
            
            float part =  (float)clusterSizes[i] * 100 / (float)total;
            String percent = String.format("%.2f", part);
            
            // name
            Element Name = new Element("name", ns);
            Name.setText("Área #"+(i + 1)+ " [" + percent + "%]");
            placemark.addContent(Name);
            
            // styleUrl - Placemark
            Element styleU = new Element("styleUrl", ns);
            styleU.setText("default"+i+"");
            placemark.addContent(styleU);
            
            //MultiGeometry - Placemark
            Element Multigeo = new Element ("MultiGeometry", ns);
            placemark.addContent(Multigeo);
            
            int j = 0;
            for(int clusterNum : assignments)
            {
               if(clusterNum == i)
               {    
                    // Point2D
                   Element Point = new Element("Point", ns);
                   Multigeo.addContent(Point);

                    // coordinates
                    Element Coordinates = new Element("coordinates", ns);
                    Coordinates.setText(""+ inst.instance(j) + ",0");
                    Point.addContent(Coordinates);
               } 
               
               j = j + 1;
            }
       }
       
       int j = 0;
       for(int k=0 ; k < centroids.numInstances(); k++) {
            Instance instance = centroids.instance(k);        
       //for(Instance instance : centroids)
       //{
           // Placemark
            Element secondPlacemark = new Element("Placemark", ns);
            document.addContent(secondPlacemark);

            // name
            Element secondName = new Element("name", ns);
            secondName.setText("Centroíde #"+(j + 1));
            secondPlacemark.addContent(secondName);
        
            // styleUrl - Placemark
            Element styleUM = new Element("styleUrl", ns);
            styleUM.setText("centroid"+j+"");
            secondPlacemark.addContent(styleUM);
            
            // Point2D
            Element Point = new Element("Point", ns);
            secondPlacemark.addContent(Point);
                
            // coordinates
            Element Coordinates = new Element("coordinates", ns);
            Coordinates.setText(instance.toString() + ",0");
            Point.addContent(Coordinates);
            
            j = j + 1;
       }
              
       // Passo 4: escrever o arquivo XML
       File file = new File(filename);
       XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
       try (FileOutputStream writer = new FileOutputStream(file)) 
       {
           outputter.output(kmlDocument, writer); 
       }

       return file;
    }   

   /**Cria e retorna o Arquivo KML contendo as posições dos clusters dbscan. */
   public File buildDBSCANKML(String filename, List<List<DataPoint>> dbscan) throws FileNotFoundException, IOException, Exception
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
        name.setText("Regiões mais Frequentes");
        document.addContent(name);
       
        //Passo 2 - Styles
        
        /*
         * STYLE MAP 
         */     

        int i = 0;
        for(List<DataPoint> mean : dbscan)
        {
            Element styleMap = new Element("StyleMap", ns);
            styleMap.setAttribute("id", "default"+i+"");
            document.addContent(styleMap);

            //Pair
            Element pair = new Element("Pair", ns);
            styleMap.addContent(pair);

            //key
            Element key = new Element("key", ns);
            key.setText("normal");
            pair.addContent(key);

            // styleUrl - Pair
            Element styleUrl = new Element("styleUrl", ns);
            styleUrl.setText("#default"+i+"_n");
            pair.addContent(styleUrl);

            //Pair
            Element secondPair = new Element("Pair", ns);
            styleMap.addContent(secondPair);

            //key
            Element secondKey = new Element("key", ns);
            secondKey.setText("highlight");
            secondPair.addContent(secondKey);

            // styleUrl - Pair2
            Element secondStyleUrl = new Element("styleUrl", ns);
            secondStyleUrl.setText("#default"+i+"_hl");
            secondPair.addContent(secondStyleUrl);

            /*
             * STYLE - NORMAL
             */
        
            Element style = new Element("Style", ns);
            style.setAttribute("id", "default"+i+"_n");
            document.addContent(style);

            //IconStyle
            Element iconStyle = new Element("IconStyle", ns);
            style.addContent(iconStyle);

            // color - IconStyle
            Element iconColor = new Element("color", ns);
            iconColor.setText(getColor(i)); //ff00ffff yellow
            iconStyle.addContent(iconColor);

            //scale - IconStyle
            Element iconScale = new Element("scale", ns);
            iconScale.setText("0.5");
            iconStyle.addContent(iconScale);

            //Icon
            Element icon = new Element("Icon", ns);
            style.addContent(icon);

            //href
            Element href = new Element("href", ns);
            href.setText("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
            icon.addContent(href);
            
            //LabelStyle
            Element labelStyle = new Element("LabelStyle", ns);
            style.addContent(labelStyle);

            //scale - LabelStyle
            Element labelScale = new Element("scale", ns);
            labelScale.setText("0");
            labelStyle.addContent(labelScale);

             /*
             * STYLES - HIGHLIGHT
             */

            Element secondStyle = new Element("Style", ns);
            secondStyle.setAttribute("id", "default"+i+"_hl");
            document.addContent(secondStyle);

            //IconStyle
            Element iconStyle2 = new Element("IconStyle", ns);
            secondStyle.addContent(iconStyle2);

            // color - IconStyle
            Element iconColor2 = new Element("color", ns);
            iconColor2.setText(getColor(i)); //ff00ffff yellow
            iconStyle2.addContent(iconColor2);

            //scale - IconStyle
            Element iconScale2 = new Element("scale", ns);
            iconScale2.setText("0.65");
            iconStyle2.addContent(iconScale2);

            //Icon
            Element icon2 = new Element("Icon", ns);
            secondStyle.addContent(icon2);

            //href
            Element href2 = new Element("href", ns);
            href2.setText("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
            icon2.addContent(href2);
            
            //LabelStyle
            Element labelStyle2 = new Element("LabelStyle", ns);
            secondStyle.addContent(labelStyle2);

            //scale - LabelStyle
            Element labelScale2 = new Element("scale", ns);
            labelScale2.setText("0");
            labelStyle2.addContent(labelScale2);
            
            
            i = i + 1;
       }
  
       int total = 0; 
       for(List<DataPoint> mean : dbscan)
       {
           total = total + mean.size();
       }
        
       //Passo 3: ler os dados da origem e adicionar no Placemark
       i = 0;
       for(List<DataPoint> mean : dbscan)
       {          
            // Placemark
            Element placemark = new Element("Placemark", ns);
            document.addContent(placemark);

            float part =  (float)mean.size() * 100 / (float)total;
            String percent = String.format("%.2f", part);
            
            // name
            Element Name = new Element("name", ns);
            Name.setText("Área #"+(i + 1)+ " [" + percent + "%]");
            placemark.addContent(Name);
            
            // styleUrl - Placemark
            Element styleU = new Element("styleUrl", ns);
            styleU.setText("default"+i+"");
            placemark.addContent(styleU);
            
            //MultiGeometry - Placemark
            Element Multigeo = new Element ("MultiGeometry", ns);
            placemark.addContent(Multigeo);
            
            
            for(DataPoint point : mean)
            {   
               // Point2D
                Element Point = new Element("Point", ns);
                Multigeo.addContent(Point);
                
                // coordinates
                Element Coordinates = new Element("coordinates", ns);
                Coordinates.setText(""+ point.getNumericalValues().get(0) +"," + point.getNumericalValues().get(1) + ",0");
                Point.addContent(Coordinates);
            } 
             
            i = i + 1;
       }       
              
       // Passo 4: escrever o arquivo XML
       File file = new File(filename);
       XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
       try (FileOutputStream writer = new FileOutputStream(file)) 
       {
           outputter.output(kmlDocument, writer); 
       }

       return file;
   }   
   
   
   
   /**Cria e retorna o Arquivo KML contendo as posições dos clusters lsdbc. */
   public File buildLSDBCKML(String filename, List<List<DataPoint>> lsdbc) throws FileNotFoundException, IOException, Exception
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
        name.setText("Regiões mais Frequentes");
        document.addContent(name);
       
        //Passo 2 - Styles
        
        /*
         * STYLE MAP 
         */     

        int i = 0;
        for(List<DataPoint> mean : lsdbc)
        {
            Element styleMap = new Element("StyleMap", ns);
            styleMap.setAttribute("id", "default"+i+"");
            document.addContent(styleMap);

            //Pair
            Element pair = new Element("Pair", ns);
            styleMap.addContent(pair);

            //key
            Element key = new Element("key", ns);
            key.setText("normal");
            pair.addContent(key);

            // styleUrl - Pair
            Element styleUrl = new Element("styleUrl", ns);
            styleUrl.setText("#default"+i+"_n");
            pair.addContent(styleUrl);

            //Pair
            Element secondPair = new Element("Pair", ns);
            styleMap.addContent(secondPair);

            //key
            Element secondKey = new Element("key", ns);
            secondKey.setText("highlight");
            secondPair.addContent(secondKey);

            // styleUrl - Pair2
            Element secondStyleUrl = new Element("styleUrl", ns);
            secondStyleUrl.setText("#default"+i+"_hl");
            secondPair.addContent(secondStyleUrl);

            /*
             * STYLE - NORMAL
             */
        
            Element style = new Element("Style", ns);
            style.setAttribute("id", "default"+i+"_n");
            document.addContent(style);

            //IconStyle
            Element iconStyle = new Element("IconStyle", ns);
            style.addContent(iconStyle);

            // color - IconStyle
            Element iconColor = new Element("color", ns);
            iconColor.setText(getColor(i)); //ff00ffff yellow
            iconStyle.addContent(iconColor);

            //scale - IconStyle
            Element iconScale = new Element("scale", ns);
            iconScale.setText("0.5");
            iconStyle.addContent(iconScale);

            //Icon
            Element icon = new Element("Icon", ns);
            style.addContent(icon);

            //href
            Element href = new Element("href", ns);
            href.setText("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
            icon.addContent(href);
            
            //LabelStyle
            Element labelStyle = new Element("LabelStyle", ns);
            style.addContent(labelStyle);

            //scale - LabelStyle
            Element labelScale = new Element("scale", ns);
            labelScale.setText("0");
            labelStyle.addContent(labelScale);

             /*
             * STYLES - HIGHLIGHT
             */

            Element secondStyle = new Element("Style", ns);
            secondStyle.setAttribute("id", "default"+i+"_hl");
            document.addContent(secondStyle);

            //IconStyle
            Element iconStyle2 = new Element("IconStyle", ns);
            secondStyle.addContent(iconStyle2);

            // color - IconStyle
            Element iconColor2 = new Element("color", ns);
            iconColor2.setText(getColor(i)); //ff00ffff yellow
            iconStyle2.addContent(iconColor2);

            //scale - IconStyle
            Element iconScale2 = new Element("scale", ns);
            iconScale2.setText("0.65");
            iconStyle2.addContent(iconScale2);

            //Icon
            Element icon2 = new Element("Icon", ns);
            secondStyle.addContent(icon2);

            //href
            Element href2 = new Element("href", ns);
            href2.setText("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
            icon2.addContent(href2);
            
            //LabelStyle
            Element labelStyle2 = new Element("LabelStyle", ns);
            secondStyle.addContent(labelStyle2);

            //scale - LabelStyle
            Element labelScale2 = new Element("scale", ns);
            labelScale2.setText("0");
            labelStyle2.addContent(labelScale2);
            
            
            i = i + 1;
       }

       int total = 0; 
       for(List<DataPoint> mean : lsdbc)
       {
           total = total + mean.size();
       }
                
       //Passo 3: ler os dados da origem e adicionar no Placemark
       i = 0;
       for(List<DataPoint> mean : lsdbc)
       {          
            // Placemark
            Element placemark = new Element("Placemark", ns);
            document.addContent(placemark);

            float part =  (float)mean.size() * 100 / (float)total;
            String percent = String.format("%.2f", part); 
            
            // name
            Element Name = new Element("name", ns);
            Name.setText("Área #"+(i + 1)+ " [" + percent + "%]");
            placemark.addContent(Name);
            
            // styleUrl - Placemark
            Element styleU = new Element("styleUrl", ns);
            styleU.setText("default"+i+"");
            placemark.addContent(styleU);
            
            //MultiGeometry - Placemark
            Element Multigeo = new Element ("MultiGeometry", ns);
            placemark.addContent(Multigeo);
            
            
            for(DataPoint point : mean)
            {   
               // Point2D
                Element Point = new Element("Point", ns);
                Multigeo.addContent(Point);
                
                // coordinates
                Element Coordinates = new Element("coordinates", ns);
                Coordinates.setText(""+ point.getNumericalValues().get(0) +"," + point.getNumericalValues().get(1) + ",0");
                Point.addContent(Coordinates);
            } 
             
            i = i + 1;
       }       
              
       // Passo 4: escrever o arquivo XML
       File file = new File(filename);
       XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
       try (FileOutputStream writer = new FileOutputStream(file)) 
       {
           outputter.output(kmlDocument, writer); 
       }

       return file;
    }   
   
   
   /**Cria e retorna o Arquivo KML contendo as posições dos clusters PAM. */
   public File buildPAMKML(String filename, List<List<DataPoint>> pam, ObservableList<Clustering> clusters) throws FileNotFoundException, IOException, Exception
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
        name.setText("Regiões mais Frequentes");
        document.addContent(name);
       
        //Passo 2 - Styles
        
        /*
         * STYLE MAP 
         */     

        int i = 0;
        for(List<DataPoint> mean : pam)
        {
            Element styleMap = new Element("StyleMap", ns);
            styleMap.setAttribute("id", "default"+i+"");
            document.addContent(styleMap);

            //Pair
            Element pair = new Element("Pair", ns);
            styleMap.addContent(pair);

            //key
            Element key = new Element("key", ns);
            key.setText("normal");
            pair.addContent(key);

            // styleUrl - Pair
            Element styleUrl = new Element("styleUrl", ns);
            styleUrl.setText("#default"+i+"_n");
            pair.addContent(styleUrl);

            //Pair
            Element secondPair = new Element("Pair", ns);
            styleMap.addContent(secondPair);

            //key
            Element secondKey = new Element("key", ns);
            secondKey.setText("highlight");
            secondPair.addContent(secondKey);

            // styleUrl - Pair2
            Element secondStyleUrl = new Element("styleUrl", ns);
            secondStyleUrl.setText("#default"+i+"_hl");
            secondPair.addContent(secondStyleUrl);

            /*
             * STYLE - NORMAL
             */
        
            Element style = new Element("Style", ns);
            style.setAttribute("id", "default"+i+"_n");
            document.addContent(style);

            //IconStyle
            Element iconStyle = new Element("IconStyle", ns);
            style.addContent(iconStyle);

            // color - IconStyle
            Element iconColor = new Element("color", ns);
            iconColor.setText(getColor(i)); //ff00ffff yellow
            iconStyle.addContent(iconColor);

            //scale - IconStyle
            Element iconScale = new Element("scale", ns);
            iconScale.setText("0.5");
            iconStyle.addContent(iconScale);

            //Icon
            Element icon = new Element("Icon", ns);
            style.addContent(icon);

            //href
            Element href = new Element("href", ns);
            href.setText("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
            icon.addContent(href);
      
            //LabelStyle
            Element labelStyle = new Element("LabelStyle", ns);
            style.addContent(labelStyle);

            //scale - LabelStyle
            Element labelScale = new Element("scale", ns);
            labelScale.setText("0");
            labelStyle.addContent(labelScale);

             /*
             * STYLES - HIGHLIGHT
             */

            Element secondStyle = new Element("Style", ns);
            secondStyle.setAttribute("id", "default"+i+"_hl");
            document.addContent(secondStyle);

            //IconStyle
            Element iconStyle2 = new Element("IconStyle", ns);
            secondStyle.addContent(iconStyle2);

            // color - IconStyle
            Element iconColor2 = new Element("color", ns);
            iconColor2.setText(getColor(i)); //ff00ffff yellow
            iconStyle2.addContent(iconColor2);

            //scale - IconStyle
            Element iconScale2 = new Element("scale", ns);
            iconScale2.setText("0.65");
            iconStyle2.addContent(iconScale2);

            //Icon
            Element icon2 = new Element("Icon", ns);
            secondStyle.addContent(icon2);

            //href
            Element href2 = new Element("href", ns);
            href2.setText("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
            icon2.addContent(href2);
                        
            //LabelStyle
            Element labelStyle2 = new Element("LabelStyle", ns);
            secondStyle.addContent(labelStyle2);

            //scale - LabelStyle
            Element labelScale2 = new Element("scale", ns);
            labelScale2.setText("0");
            labelStyle2.addContent(labelScale2);
   
            i = i + 1;
       }

        
       i = 0;
       for(List<DataPoint> mean : pam)
       {
            Element styleMap = new Element("StyleMap", ns);
            styleMap.setAttribute("id", "medoid"+i+"");
            document.addContent(styleMap);

            //Pair
            Element pair = new Element("Pair", ns);
            styleMap.addContent(pair);

            //key
            Element key = new Element("key", ns);
            key.setText("normal");
            pair.addContent(key);

            // styleUrl - Pair
            Element styleUrl = new Element("styleUrl", ns);
            styleUrl.setText("#medoid"+i+"_n");
            pair.addContent(styleUrl);

            //Pair
            Element secondPair = new Element("Pair", ns);
            styleMap.addContent(secondPair);

            //key
            Element secondKey = new Element("key", ns);
            secondKey.setText("highlight");
            secondPair.addContent(secondKey);

            // styleUrl - Pair2
            Element secondStyleUrl = new Element("styleUrl", ns);
            secondStyleUrl.setText("#medoid"+i+"_hl");
            secondPair.addContent(secondStyleUrl);

            /*
             * STYLE - NORMAL
             */
        
            Element style = new Element("Style", ns);
            style.setAttribute("id", "medoid"+i+"_n");
            document.addContent(style);

            //IconStyle
            Element iconStyle = new Element("IconStyle", ns);
            style.addContent(iconStyle);

            // color - IconStyle
            Element iconColor = new Element("color", ns);
            iconColor.setText(getColor(i)); //ff00ffff yellow
            iconStyle.addContent(iconColor);

            //scale - IconStyle
            Element iconScale = new Element("scale", ns);
            iconScale.setText("0.8");
            iconStyle.addContent(iconScale);

            //Icon
            Element icon = new Element("Icon", ns);
            style.addContent(icon);

            //href
            Element href = new Element("href", ns);
            href.setText("http://maps.google.com/mapfiles/kml/shapes/placemark_circle.png");
            icon.addContent(href);
      
            //LabelStyle
            Element labelStyle = new Element("LabelStyle", ns);
            style.addContent(labelStyle);

            //scale - LabelStyle
            Element labelScale = new Element("scale", ns);
            labelScale.setText("0.8");
            labelStyle.addContent(labelScale);

             /*
             * STYLES - HIGHLIGHT
             */

            Element secondStyle = new Element("Style", ns);
            secondStyle.setAttribute("id", "medoid"+i+"_hl");
            document.addContent(secondStyle);

            //IconStyle
            Element iconStyle2 = new Element("IconStyle", ns);
            secondStyle.addContent(iconStyle2);

            // color - IconStyle
            Element iconColor2 = new Element("color", ns);
            iconColor2.setText(getColor(i)); //ff00ffff yellow
            iconStyle2.addContent(iconColor2);

            //scale - IconStyle
            Element iconScale2 = new Element("scale", ns);
            iconScale2.setText("1");
            iconStyle2.addContent(iconScale2);

            //Icon
            Element icon2 = new Element("Icon", ns);
            secondStyle.addContent(icon2);

            //href
            Element href2 = new Element("href", ns);
            href2.setText("http://maps.google.com/mapfiles/kml/shapes/placemark_circle.png");
            icon2.addContent(href2);
                        
            //LabelStyle
            Element labelStyle2 = new Element("LabelStyle", ns);
            secondStyle.addContent(labelStyle2);

            //scale - LabelStyle
            Element labelScale2 = new Element("scale", ns);
            labelScale2.setText("1");
            labelStyle2.addContent(labelScale2);
   
            i = i + 1;
       } 
     
       int total = 0; 
       for(List<DataPoint> mean : pam)
       {
           total = total + mean.size();
       }
        
       //Passo 3: ler os dados da origem e adicionar no Placemark
       i = 0;
       for(List<DataPoint> mean : pam)
       {          
            // Placemark
            Element placemark = new Element("Placemark", ns);
            document.addContent(placemark);
            
            float part =  (float)mean.size() * 100 / (float)total;
            String percent = String.format("%.2f", part); 

            // name
            Element Name = new Element("name", ns);
            Name.setText("Área #"+(i + 1)+ " [" + percent + "%]");
            placemark.addContent(Name);
            
            // styleUrl - Placemark
            Element styleU = new Element("styleUrl", ns);
            styleU.setText("default"+i+"");
            placemark.addContent(styleU);
            
            //MultiGeometry - Placemark
            Element Multigeo = new Element ("MultiGeometry", ns);
            placemark.addContent(Multigeo);
            
            
            for(DataPoint point : mean)
            {   
               // Point2D
                Element Point = new Element("Point", ns);
                Multigeo.addContent(Point);
                
                // coordinates
                Element Coordinates = new Element("coordinates", ns);
                Coordinates.setText(""+ point.getNumericalValues().get(0) +"," + point.getNumericalValues().get(1) + ",0");
                Point.addContent(Coordinates);
            } 
             
            i = i + 1;
       }
       
       int j = 0;
       for(Clustering c : clusters)
       {
           // Placemark
            Element secondPlacemark = new Element("Placemark", ns);
            document.addContent(secondPlacemark);

            // name
            Element secondName = new Element("name", ns);
            secondName.setText("Medoíde #"+(j + 1));
            secondPlacemark.addContent(secondName);
        
            // styleUrl - Placemark
            Element styleUM = new Element("styleUrl", ns);
            styleUM.setText("medoid"+j+"");
            secondPlacemark.addContent(styleUM);
            
            // Point2D
            Element Point = new Element("Point", ns);
            secondPlacemark.addContent(Point);
                
            // coordinates
            Element Coordinates = new Element("coordinates", ns);
            Coordinates.setText(c.getMedoid() + ",0");
            Point.addContent(Coordinates);
            
            j = j + 1;
       }
              
       // Passo 4: escrever o arquivo XML
       File file = new File(filename);
       XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
       try (FileOutputStream writer = new FileOutputStream(file)) 
       {
           outputter.output(kmlDocument, writer); 
       }

       return file;
    }   
   

   /**Cria e retorna o Arquivo KML contendo as posições dos clusters lsdbc. */
   public File buildCBSMOT(String filename, ObservableList<ClusterPoints> cluster) throws FileNotFoundException, IOException, Exception
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
        name.setText("Regiões mais Frequentes");
        document.addContent(name);
       
        //Passo 2 - Styles
        
        /*
         * STYLE MAP 
         */     

        int i = 0;
        for(ClusterPoints c : cluster)
        {
            Element styleMap = new Element("StyleMap", ns);
            styleMap.setAttribute("id", "default"+i+"");
            document.addContent(styleMap);

            //Pair
            Element pair = new Element("Pair", ns);
            styleMap.addContent(pair);

            //key
            Element key = new Element("key", ns);
            key.setText("normal");
            pair.addContent(key);

            // styleUrl - Pair
            Element styleUrl = new Element("styleUrl", ns);
            styleUrl.setText("#default"+i+"_n");
            pair.addContent(styleUrl);

            //Pair
            Element secondPair = new Element("Pair", ns);
            styleMap.addContent(secondPair);

            //key
            Element secondKey = new Element("key", ns);
            secondKey.setText("highlight");
            secondPair.addContent(secondKey);

            // styleUrl - Pair2
            Element secondStyleUrl = new Element("styleUrl", ns);
            secondStyleUrl.setText("#default"+i+"_hl");
            secondPair.addContent(secondStyleUrl);

            /*
             * STYLE - NORMAL
             */
        
            Element style = new Element("Style", ns);
            style.setAttribute("id", "default"+i+"_n");
            document.addContent(style);

            //IconStyle
            Element iconStyle = new Element("IconStyle", ns);
            style.addContent(iconStyle);

            // color - IconStyle
            Element iconColor = new Element("color", ns);
            iconColor.setText(getColor(i)); //ff00ffff yellow
            iconStyle.addContent(iconColor);

            //scale - IconStyle
            Element iconScale = new Element("scale", ns);
            iconScale.setText("0.5");
            iconStyle.addContent(iconScale);

            //Icon
            Element icon = new Element("Icon", ns);
            style.addContent(icon);

            //href
            Element href = new Element("href", ns);
            href.setText("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
            icon.addContent(href);
            
            //LabelStyle
            Element labelStyle = new Element("LabelStyle", ns);
            style.addContent(labelStyle);

            //scale - LabelStyle
            Element labelScale = new Element("scale", ns);
            labelScale.setText("0");
            labelStyle.addContent(labelScale);

             /*
             * STYLES - HIGHLIGHT
             */

            Element secondStyle = new Element("Style", ns);
            secondStyle.setAttribute("id", "default"+i+"_hl");
            document.addContent(secondStyle);

            //IconStyle
            Element iconStyle2 = new Element("IconStyle", ns);
            secondStyle.addContent(iconStyle2);

            // color - IconStyle
            Element iconColor2 = new Element("color", ns);
            iconColor2.setText(getColor(i)); //ff00ffff yellow
            iconStyle2.addContent(iconColor2);

            //scale - IconStyle
            Element iconScale2 = new Element("scale", ns);
            iconScale2.setText("0.65");
            iconStyle2.addContent(iconScale2);

            //Icon
            Element icon2 = new Element("Icon", ns);
            secondStyle.addContent(icon2);

            //href
            Element href2 = new Element("href", ns);
            href2.setText("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
            icon2.addContent(href2);
            
            //LabelStyle
            Element labelStyle2 = new Element("LabelStyle", ns);
            secondStyle.addContent(labelStyle2);

            //scale - LabelStyle
            Element labelScale2 = new Element("scale", ns);
            labelScale2.setText("0");
            labelStyle2.addContent(labelScale2);
            
            
            i = i + 1;
       }

       int total = 0; 
       for(ClusterPoints c : cluster)
       {
           total = total + c.points.size();
       }
                
       //Passo 3: ler os dados da origem e adicionar no Placemark
       i = 0;
       for(ClusterPoints mean : cluster)
       {          
            // Placemark
            Element placemark = new Element("Placemark", ns);
            document.addContent(placemark);

            float part = (float)mean.points.size() * 100 / (float)total;
            String percent = String.format("%.2f", part); 
            
            // name
            Element Name = new Element("name", ns);
            Name.setText("Área #"+(i + 1)+ " [" + percent + "%]");
            placemark.addContent(Name);
            
            // styleUrl - Placemark
            Element styleU = new Element("styleUrl", ns);
            styleU.setText("default"+i+"");
            placemark.addContent(styleU);
            
            //MultiGeometry - Placemark
            Element Multigeo = new Element ("MultiGeometry", ns);
            placemark.addContent(Multigeo);
            
            
            for(Coordinate c : mean.points)
            {   
               // Point2D
                Element Point = new Element("Point", ns);
                Multigeo.addContent(Point);
                
                // coordinates
                Element Coordinates = new Element("coordinates", ns);
                Coordinates.setText(""+ c.getLongitudeX() +"," + c.getLatitudeY() + ",0");
                Point.addContent(Coordinates);
            } 
             
            i = i + 1;
       }       
              
       // Passo 4: escrever o arquivo XML
       File file = new File(filename);
       XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
       try (FileOutputStream writer = new FileOutputStream(file)) 
       {
           outputter.output(kmlDocument, writer); 
       }

       return file;
    }   


   
    /**Método retorna uma cor diferente de acordo com o index.
    @param index - o indice qualquer.
    @return uma string com o código hexadecimal de uma cor.*/
    public String getColor(int index)
    {       
        //KML colors used the format ARGB
        if(index == 0)
        {
            //red 
            return "ff0000ff";
        }
        else if (index == 1)
        {
            //solid blue
            return "ffff0000";
        }
        else if (index == 2)
        {
            //solid green
            return "ff005500";
        }
        else if (index == 3)
        {
            //write
            return "ffffffff";
        }
        else if (index == 4)
        {
            //orange
            return "ff0055ff";
        } 
        else if (index == 5)
        {
            //grey
            return "ff737373";
        }
        else if (index == 6)
        {
            //brown
            return "ff0055aa";
        }
        else if (index == 7)
        {
            //cian blue
            return "ffffff00";
        }
        else if (index == 8)
        {
           //pink
            return "ffff00ff";
        }
        else if (index == 9)
        {
            //mediumblue
            return "ffff5500";
        }
        else if (index == 10)
        {
            //lime
            return "ff00ff00";
        }

        // solid yellow 
        return "ff00ffff";
    }


    
    /**Método retorna uma cor diferente de acordo com o index.
    @param color - o nome da cor em inglês.
    @return uma string com o código hexadecimal de uma cor.*/
    public String getBorderColor(String color)
    {       
        switch(color)
        {
            case "white": //Branco
                return "ffffffff";
                
            case "green": //Verde
                return "ff005500";

            case "red": //Vermelho
                return "ff0000ff";
                
            case "yellow": //Amarelo
                return "ff00ffff";
        }

        // solid yellow 
        return "ff00ffff";

    }

    /**Cria um StyleMap para buildTargetArea. */
    public Element styleMapToTargetArea(Namespace ns, String nameElement)
    {
         // styleMap
        Element styleMap = new Element("StyleMap", ns);
        styleMap.setAttribute("id", nameElement);

        //Pair
        Element pair = new Element("Pair", ns);
        styleMap.addContent(pair);

        //key
        Element key = new Element("key", ns);
        key.setText("normal");
        pair.addContent(key);

        // styleUrl - Pair
        Element styleUrlPair = new Element("styleUrl", ns);
        styleUrlPair.setText("#" + nameElement + "_n");
        pair.addContent(styleUrlPair);

        //Pair
        Element secondPair = new Element("Pair", ns);
        styleMap.addContent(secondPair);

        //key
        Element secondKey = new Element("key", ns);
        secondKey.setText("highlight");
        secondPair.addContent(secondKey);

        // styleUrl - Pair2
        Element styleUrlPair2 = new Element("styleUrl", ns);
        styleUrlPair2.setText("#" + nameElement + "_hl");
        secondPair.addContent(styleUrlPair2);

        return styleMap;
    }    
       
    /**Cria um Style para buildTargetArea. */
    public Element styleToTargetArea(Namespace ns, String nameElement, String color)
    {                        
         // Style
        Element style = new Element("Style", ns);
        style.setAttribute("id", nameElement);

        // LineStyle - Style
        Element secondLineStyle = new Element("LineStyle", ns);
        style.addContent(secondLineStyle);

        // color - LineStyle
        Element colorLineSyle = new Element("color", ns);
        colorLineSyle.setText(getBorderColor(color));
        secondLineStyle.addContent(colorLineSyle);
        
        Element secondWidth = new Element("width", ns);
        secondWidth.setText("3");
        secondLineStyle.addContent(secondWidth);

        // PolyStyle - Style
        Element polyStyle = new Element("PolyStyle", ns);
        style.addContent(polyStyle);

        // color - PolyStyle
        Element polyStylecolor = new Element("color", ns);
        polyStylecolor.setText("7fffffff");
        polyStyle.addContent(polyStylecolor);
        
        return style;
        
    }        
    
    /**Cria um kml com as informações de TargetArea. */
    public File buildTargetArea(String filename, ObservableList<TargetObject> targetList) throws FileNotFoundException, IOException
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
        name.setText("Áreas Avoidance");
        document.addContent(name);

        //Passo 2: adiciona elementos no Style
        document.addContent(styleMapToTargetArea(ns, "style"));
        document.addContent(styleToTargetArea(ns, "style", "white"));
        
        document.addContent(styleMapToTargetArea(ns, "style2"));
        document.addContent(styleToTargetArea(ns, "style2", "green"));
        
        document.addContent(styleMapToTargetArea(ns, "style3"));
        document.addContent(styleToTargetArea(ns, "style3", "yellow"));
        
        document.addContent(styleMapToTargetArea(ns, "style4"));
        document.addContent(styleToTargetArea(ns, "style4", "red"));

        //Passo 3: ler os dados da origem e adicionar no Placemark
        for(TargetObject target : targetList)
        {           
            // Placemark
            Element placemark = new Element("Placemark", ns);
            document.addContent(placemark);

            // name
            Element polygonName = new Element("name", ns);
            polygonName.setText("Área " + target.getTargetId());
            placemark.addContent(polygonName);

            // styleUrl - Placemark
            Element styleUrl = new Element("styleUrl", ns);
            if(target.getPercentAvoidance() == -1)
            {
                styleUrl.setText("style");
            }
            else if(target.getPercentAvoidance() == 0)
            {
                styleUrl.setText("style2");
            }
            else if(target.getPercentAvoidance() > 0 && target.getPercentAvoidance() <= 0.5)
            {
                styleUrl.setText("style3");
            }
            else if(target.getPercentAvoidance() > 0.5)
            {
                styleUrl.setText("style4");
            }
            placemark.addContent(styleUrl);

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
        }

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
