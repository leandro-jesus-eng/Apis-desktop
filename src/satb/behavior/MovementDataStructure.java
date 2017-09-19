package satb.behavior;

import java.util.LinkedList;
import satb.model.CoordinateVenus;
import satb.model.MeteorologicalData;

/**
 *
 * @author Leandro de Jesus
 */
public class MovementDataStructure {
    
    /*
     * a discrete representation of the latest type of movement performed
     *  left, right, forward, uturn, nonmoving
     */
    protected String movimentType;
    
    /*
     * angle relative to the previous movement
     * graus
     */
    protected Double angle;
    
    /*
     * distance traveled between the two measurements
     * meters
     */
    protected Double magnitude;
    
    /*
     * estimated speed
     * metros/segundo
     */
    protected Double speed;
    
    /*
     * absolute heading - orientação
     * degrees
     * 
     * TODO verificar a necessidade desse atributo
     */    
    protected Double heading;
    
    /*
     * based on estimated speed of the last two measurements
     * m/s2
     */
    protected Double acceleration;
    
    /*
    * 
    */
    private Double ldr;
    
    /*
    * difference of the last two measurements
    */
    private Double deltaAx;
    private Double deltaAy;
    private Double deltaAz;
    private Double deltaGx;
    private Double deltaGy;
    private Double deltaGz;
    private Double deltaMx;
    private Double deltaMy;
    private Double deltaMz;
        
    private String classification;
       
    /*
     * 
     */
    protected CoordinateVenus coordinatePoint;

    
    private LinkedList<MeteorologicalData> listMeteorologicalData = new LinkedList<>();
    
    /*
     * Construtor
     */
    public MovementDataStructure (CoordinateVenus coordinatePoint) {
        this.coordinatePoint = coordinatePoint;
        
        movimentType = null;
        angle = null;
        magnitude = null;
        speed = coordinatePoint.getSpeedGPS() / 3.6;
        heading = coordinatePoint.getCourse();
        acceleration = null;
    }
    
    public String getMovimentType() {
        return movimentType;
    }

    public void setMovimentType(String movimentType) {
        this.movimentType = movimentType;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public Double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Double acceleration) {
        this.acceleration = acceleration;
    }

    public CoordinateVenus getCoordinatePoint() {
        return coordinatePoint;
    }

    public void setCoordinatePoint(CoordinateVenus coordinatePoint) {
        this.coordinatePoint = coordinatePoint;
    }

    public Double getLdr() {
        return ldr;
    }

    public void setLdr(Double ldr) {
        this.ldr = ldr;
    }

    public Double getDeltaAx() {
        return deltaAx;
    }

    public void setDeltaAx(Double deltaAx) {
        this.deltaAx = deltaAx;
    }

    public Double getDeltaAy() {
        return deltaAy;
    }

    public void setDeltaAy(Double deltaAy) {
        this.deltaAy = deltaAy;
    }

    public Double getDeltaAz() {
        return deltaAz;
    }

    public void setDeltaAz(Double deltaAz) {
        this.deltaAz = deltaAz;
    }

    public Double getDeltaGx() {
        return deltaGx;
    }

    public void setDeltaGx(Double deltaGx) {
        this.deltaGx = deltaGx;
    }

    public Double getDeltaGy() {
        return deltaGy;
    }

    public void setDeltaGy(Double deltaGy) {
        this.deltaGy = deltaGy;
    }

    public Double getDeltaGz() {
        return deltaGz;
    }

    public void setDeltaGz(Double deltaGz) {
        this.deltaGz = deltaGz;
    }

    public Double getDeltaMx() {
        return deltaMx;
    }

    public void setDeltaMx(Double deltaMx) {
        this.deltaMx = deltaMx;
    }

    public Double getDeltaMy() {
        return deltaMy;
    }

    public void setDeltaMy(Double deltaMy) {
        this.deltaMy = deltaMy;
    }

    public Double getDeltaMz() {
        return deltaMz;
    }

    public void setDeltaMz(Double deltaMz) {
        this.deltaMz = deltaMz;
    }
    
    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public String toString() {
        String s = "";
        
        s += "movimentType = "+movimentType+"\n";
        s += "angle = "+angle+"\n";
        s += "magnitude = "+magnitude+"\n";
        s += "speed = "+speed+"\n";
        s += "heading = "+heading+"\n";
        s += "acceleration = "+acceleration+"\n";
        
        return s; //To change body of generated methods, choose Tools | Templates.
    }

    public LinkedList<MeteorologicalData> getListMeteorologicalData() {
        return listMeteorologicalData;
    }

    public void setListMeteorologicalData(LinkedList<MeteorologicalData> listMeteorologicalData) {
        this.listMeteorologicalData = listMeteorologicalData;
    }
    
    
}
