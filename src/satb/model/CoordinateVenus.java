package satb.model;

import java.util.Date;
import satb.behavior.Util;

/** 
 * Classe representa um ponto GPS.
 * @author Leandro de Jesus.
 */
public class CoordinateVenus
{

    //Atributos
    private Double utcTime = null; // UTC time in hhmmss.sss format (000000.00 ~ 235959.999)
    private Double latitude = null; // Latitude in dddmm.mmmm format   Leading zeros transmitted
    private Character nsIndicator = null; // Latitude hemisphere indicator ‘N’ = North ‘S’ = South
    private Double longitude = null; // Longitude in dddmm.mmmm format   Leading zeros transmitted
    private Character ewIndicator = null; // Longitude hemisphere indicator 'E' = East 'W' = West
    /**
     * GPS quality indicator 0: position fix unavailable 1: valid
     * position fix, SPS mode 2: valid position fix, differential
     * GPS mode 3: GPS PPS Mode, fix valid 4: Real Time Kinematic.
     * System used in RTK mode with fixed integers 5: Float RTK.
     * Satellite system used in RTK mode. Floating integers 6:
     * Estimated (dead reckoning) Mode 7: Manual Input Mode 8:
     * Simulator Mode
                *
     */
    private Character gpsQuality = null;
    private Integer satellitesUsed = null;
    private Double PDOP = null; //Position dilution of precision (00.0 to 99.9)
    private Double HDOP = null; // Horizontal dilution of precision (00.0 to 99.9)
    private Double VDOP = null; // Vertical dilution of precision (00.0 to 99.9)                          
    private Double altitude = null;
    private Integer differencialReferenceStationID = null;
    private Character status = null; // ‘V’ = Navigation receiver warning ‘A’ = Data Valid
    private Double speedGPS = null; //Speed over ground in kilometers per hour (0000.0 ~ 1800.0)
    private Double course = null; //Course over ground in degrees (000.0 ~ 359.9)
    private Integer utcDate = null; // UTC date of position fix, ddmmyy format
    // Mode indicator ‘N’ = Data not valid ‘A’ = Autonomous mode ‘D’ = Differential mode 
    // ‘E’ = Estimated (dead reckoning) mode ‘M’ = Manual input mode ‘S’ = Simulator mode
    private Character modeIndicator = null;
    
    
    private Date date;
    
    
    /**Construtor Default. */
    public CoordinateVenus()
    {
    }

    public Double getUtcTime() {
        return utcTime;
    }

    public void setUtcTime(Double utcTime) {
        this.utcTime = utcTime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Character getNsIndicator() {
        return nsIndicator;
    }

    public void setNsIndicator(Character nsIndicator) {
        this.nsIndicator = nsIndicator;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Character getEwIndicator() {
        return ewIndicator;
    }

    public void setEwIndicator(Character ewIndicator) {
        this.ewIndicator = ewIndicator;
    }

    public Character getGpsQuality() {
        return gpsQuality;
    }

    public void setGpsQuality(Character gpsQuality) {
        this.gpsQuality = gpsQuality;
    }

    public Integer getSatellitesUsed() {
        return satellitesUsed;
    }

    public void setSatellitesUsed(Integer satellitesUsed) {
        this.satellitesUsed = satellitesUsed;
    }

    public Double getPDOP() {
        return PDOP;
    }

    public void setPDOP(Double PDOP) {
        this.PDOP = PDOP;
    }

    public Double getHDOP() {
        return HDOP;
    }

    public void setHDOP(Double HDOP) {
        this.HDOP = HDOP;
    }

    public Double getVDOP() {
        return VDOP;
    }

    public void setVDOP(Double VDOP) {
        this.VDOP = VDOP;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Integer getDifferencialReferenceStationID() {
        return differencialReferenceStationID;
    }

    public void setDifferencialReferenceStationID(Integer differencialReferenceStationID) {
        this.differencialReferenceStationID = differencialReferenceStationID;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Double getSpeedGPS() {
        return speedGPS;
    }

    public void setSpeedGPS(Double speedGPS) {
        this.speedGPS = speedGPS;
    }

    public Double getCourse() {
        return course;
    }

    public void setCourse(Double course) {
        this.course = course;
    }

    public Integer getUtcDate() {
        return utcDate;
    }

    public void setUtcDate(Integer utcDate) {
        this.utcDate = utcDate;
    }

    public Character getModeIndicator() {
        return modeIndicator;
    }

    public void setModeIndicator(Character modeIndicator) {
        this.modeIndicator = modeIndicator;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    
    /**
     * Mostra a distância entre a a coordenada (this) e a recebida pelo parâmetro em metros. 
     * As coordenadas devem estar no padrão Geodésico graus decimais
     */
    public double distance(CoordinateVenus c)
    {
        int EARTH_RADIUS_KM = 6371;
        double dLat = Util.toRad(c.latitude) - Util.toRad(this.latitude);
        double dLong = Util.toRad(c.longitude) - Util.toRad(this.longitude);
	            
	double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Util.toRad(this.latitude)) * Math.cos(Util.toRad(c.latitude)) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double d = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return (EARTH_RADIUS_KM * d * 1000);
    }    
    
    public void print() {
        System.out.println("utcTime                         = " + utcTime);
        System.out.println("latitude                        = " + latitude);
        System.out.println("nsIndicator                     = " + nsIndicator);
        System.out.println("logitude                        = " + longitude);
        System.out.println("ewIndicator                     = " + ewIndicator);
        System.out.println("gpsQuality                      = " + gpsQuality);
        System.out.println("satellitesUsed                  = " + satellitesUsed);
        System.out.println("PDOP                            = " + PDOP);
        System.out.println("HDOP                            = " + HDOP);
        System.out.println("VDOP                            = " + VDOP);
        System.out.println("altitude                        = " + altitude);
        System.out.println("differencialReferenceStationID  = " + differencialReferenceStationID);
        System.out.println("status                          = " + status);
        System.out.println("speedGPS                        = " + speedGPS);
        System.out.println("course                          = " + course);
        System.out.println("utcDate                         = " + utcDate);
        System.out.println("modeIndicator                   = " + modeIndicator);
        System.out.println("data                            = " + date);
        System.out.println("");
    }
}