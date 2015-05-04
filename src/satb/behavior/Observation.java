/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package satb.behavior;

import java.util.Date;

/**
 *
 * @author Leandro
 */
public class Observation {
     
    private Long time;        
    
    private Date data;
    
    private String observation;

    public Observation(Long time, Date data, String observation) {
        this.time = time;
        this.data = data;
        this.observation = observation;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
