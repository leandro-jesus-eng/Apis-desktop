package satb.behavior;

import satb.model.CoordinateVenus;

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
    protected CoordinateVenus coordinatePoint;

    
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
}
