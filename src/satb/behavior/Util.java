/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package satb.behavior;

import satb.model.Coordinate;
import satb.model.CoordinateVenus;

/**
 *
 * @author Leandro
 */
public class Util {
    
    
    /**Converter o grau para radianos. 
    @param value - o valor em graus.
    @return o valor convertido em radianos.*/
    public static double toRad(double value) 
    {
       return value * Math.PI/180;
    }   
    
    
    
    /**
     * Recebe a coordenada tranforma os minitos decimais
     * para graus decimais
     * retorna a nova coordenada completa
     */
    public static Double coordinateMinToDegree(Double coordinate) {
    	Double coordDegreeInt = coordinate/100;
    	Double frac = coordDegreeInt - coordDegreeInt.intValue();    	
    	return frac / 60 * 100 + coordDegreeInt.intValue();
    }
}
