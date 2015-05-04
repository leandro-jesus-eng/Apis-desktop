/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package satb.behavior;

/**
 *
 * @author Leandro
 */
public class ActivityDataStructure {
    
    private SegmentDataStructure segmentDataStructure;
    
    private String classification;
    
    private Long timeObservation;

    
    public ActivityDataStructure(SegmentDataStructure segmentDataStructure, String classification) {
        this.segmentDataStructure = segmentDataStructure;
        this.classification = classification;
    }

    
    public SegmentDataStructure getSegmentDataStructure() {
        return segmentDataStructure;
    }

    public void setSegmentDataStructure(SegmentDataStructure segmentDataStructure) {
        this.segmentDataStructure = segmentDataStructure;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public Long getTimeObservation() {
        return timeObservation;
    }

    public void setTimeObservation(Long timeObservation) {
        this.timeObservation = timeObservation;
    }
}
