/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package satb.behavior;

import java.util.LinkedList;

/**
 *
 * @author Leandro
 */
public class SegmentDataStructure {
    
    protected LinkedList<MovementDataStructure> listMDS;
    
    protected Double distributionForward = 0.0;

    protected Double distributionUturn = 0.0;
    
    protected Double distributionLeft = 0.0;
    
    protected Double distributionRight = 0.0;
    
    protected Double distributionNonMoving = 0.0;
    
    protected Double changeRateForwardToNonMoving = 0.0;
    
    protected Double changeRateForwardToUturn = 0.0;
    
    protected Double changeRateForwardToLeft = 0.0;
    
    protected Double changeRateForwardToRight = 0.0;
    
    protected Double changeRateNonMovingToForward = 0.0;
    
    protected Double changeRateNonMovingToUturn = 0.0;
    
    protected Double changeRateNonMovingToLeft = 0.0;
    
    protected Double changeRateNonMovingToRight = 0.0;
    
    protected Double changeRateUturnToForward = 0.0;
    
    protected Double changeRateUturnToNonmoving = 0.0;
    
    protected Double changeRateUturnToLeft = 0.0;
    
    protected Double changeRateUturnToRight = 0.0;
    
    protected Double changeRateLeftToForward = 0.0;
    
    protected Double changeRateLeftToNonmoving = 0.0;
    
    protected Double changeRateLeftToUturn = 0.0;
    
    protected Double changeRateLeftToRight = 0.0;
    
    protected Double changeRateRightToForward = 0.0;
    
    protected Double changeRateRightToNonmoving = 0.0;
    
    protected Double changeRateRightToUturn = 0.0;
    
    protected Double changeRateRightToLeft = 0.0;
    
    protected Double changeRateAccumulated = 0.0;
    
    protected Double changeRate = 0.0;
    
    protected Double maxSpeed = 0.0;
    
    protected Double minSpeed = 0.0;
    
    protected Double meanSpeed = 0.0;
    
    protected Double maxAcceleration = 0.0;
    
    protected Double minAcceleration = 0.0;
    
    protected Double accumulatedAccelerationPositive = 0.0;
    
    protected Double accumulatedAccelerationNegative = 0.0;
    
    protected Double meanAccelerationPositive = 0.0;
    
    protected Double meanAccelerationNegative = 0.0;
    
    protected Integer countAccelerationPositive = 0;                
    
    protected Integer countAccelerationNegative = 0;
    
    protected Integer changeBetweenPositiveAndNegative = 0;
    
    protected Double accumulatedDistance = 0.0;

    protected Double maxDistanceMoving = 0.0;  
    
    protected Double accumulatedTimeMoving = 0.0;
    
    protected Double accumulatedTimeNonMoving = 0.0;
    
            
    public SegmentDataStructure(LinkedList<MovementDataStructure> listMDS) {
        this.listMDS = listMDS;
    }

    public LinkedList<MovementDataStructure> getListMDS() {
        return listMDS;
    }

    public void setListMDS(LinkedList<MovementDataStructure> listMDS) {
        this.listMDS = listMDS;
    }

    public Double getDistributionForward() {
        return distributionForward;
    }

    public void setDistributionForward(Double distributionForward) {
        this.distributionForward = distributionForward;
    }

    public Double getDistributionUturn() {
        return distributionUturn;
    }

    public void setDistributionUturn(Double distributionUturn) {
        this.distributionUturn = distributionUturn;
    }

    public Double getDistributionLeft() {
        return distributionLeft;
    }

    public void setDistributionLeft(Double distributionLeft) {
        this.distributionLeft = distributionLeft;
    }

    public Double getDistributionRight() {
        return distributionRight;
    }

    public void setDistributionRight(Double distributionRight) {
        this.distributionRight = distributionRight;
    }

    public Double getDistributionNonMoving() {
        return distributionNonMoving;
    }

    public void setDistributionNonMoving(Double distributionNonMoving) {
        this.distributionNonMoving = distributionNonMoving;
    }

    public Double getChangeRateForwardToNonMoving() {
        return changeRateForwardToNonMoving;
    }

    public void setChangeRateForwardToNonMoving(Double changeRateForwardToNonMoving) {
        this.changeRateForwardToNonMoving = changeRateForwardToNonMoving;
    }

    public Double getChangeRateForwardToUturn() {
        return changeRateForwardToUturn;
    }

    public void setChangeRateForwardToUturn(Double changeRateForwardToUturn) {
        this.changeRateForwardToUturn = changeRateForwardToUturn;
    }

    public Double getChangeRateForwardToLeft() {
        return changeRateForwardToLeft;
    }

    public void setChangeRateForwardToLeft(Double changeRateForwardToLeft) {
        this.changeRateForwardToLeft = changeRateForwardToLeft;
    }

    public Double getChangeRateForwardToRight() {
        return changeRateForwardToRight;
    }

    public void setChangeRateForwardToRight(Double changeRateForwardToRight) {
        this.changeRateForwardToRight = changeRateForwardToRight;
    }

    public Double getChangeRateNonMovingToForward() {
        return changeRateNonMovingToForward;
    }

    public void setChangeRateNonMovingToForward(Double changeRateNonMovingToForward) {
        this.changeRateNonMovingToForward = changeRateNonMovingToForward;
    }

    public Double getChangeRateNonMovingToUturn() {
        return changeRateNonMovingToUturn;
    }

    public void setChangeRateNonMovingToUturn(Double changeRateNonMovingToUturn) {
        this.changeRateNonMovingToUturn = changeRateNonMovingToUturn;
    }

    public Double getChangeRateNonMovingToLeft() {
        return changeRateNonMovingToLeft;
    }

    public void setChangeRateNonMovingToLeft(Double changeRateNonMovingToLeft) {
        this.changeRateNonMovingToLeft = changeRateNonMovingToLeft;
    }

    public Double getChangeRateNonMovingToRight() {
        return changeRateNonMovingToRight;
    }

    public void setChangeRateNonMovingToRight(Double changeRateNonMovingToRight) {
        this.changeRateNonMovingToRight = changeRateNonMovingToRight;
    }

    public Double getChangeRateUturnToForward() {
        return changeRateUturnToForward;
    }

    public void setChangeRateUturnToForward(Double changeRateUturnToForward) {
        this.changeRateUturnToForward = changeRateUturnToForward;
    }

    public Double getChangeRateUturnToNonmoving() {
        return changeRateUturnToNonmoving;
    }

    public void setChangeRateUturnToNonmoving(Double changeRateUturnToNonmoving) {
        this.changeRateUturnToNonmoving = changeRateUturnToNonmoving;
    }

    public Double getChangeRateUturnToLeft() {
        return changeRateUturnToLeft;
    }

    public void setChangeRateUturnToLeft(Double changeRateUturnToLeft) {
        this.changeRateUturnToLeft = changeRateUturnToLeft;
    }

    public Double getChangeRateUturnToRight() {
        return changeRateUturnToRight;
    }

    public void setChangeRateUturnToRight(Double changeRateUturnToRight) {
        this.changeRateUturnToRight = changeRateUturnToRight;
    }

    public Double getChangeRateLeftToForward() {
        return changeRateLeftToForward;
    }

    public void setChangeRateLeftToForward(Double changeRateLeftToForward) {
        this.changeRateLeftToForward = changeRateLeftToForward;
    }

    public Double getChangeRateLeftToNonmoving() {
        return changeRateLeftToNonmoving;
    }

    public void setChangeRateLeftToNonmoving(Double changeRateLeftToNonmoving) {
        this.changeRateLeftToNonmoving = changeRateLeftToNonmoving;
    }

    public Double getChangeRateLeftToUturn() {
        return changeRateLeftToUturn;
    }

    public void setChangeRateLeftToUturn(Double changeRateLeftToUturn) {
        this.changeRateLeftToUturn = changeRateLeftToUturn;
    }

    public Double getChangeRateLeftToRight() {
        return changeRateLeftToRight;
    }

    public void setChangeRateLeftToRight(Double changeRateLeftToRight) {
        this.changeRateLeftToRight = changeRateLeftToRight;
    }

    public Double getChangeRateRightToForward() {
        return changeRateRightToForward;
    }

    public void setChangeRateRightToForward(Double changeRateRightToForward) {
        this.changeRateRightToForward = changeRateRightToForward;
    }

    public Double getChangeRateRightToNonmoving() {
        return changeRateRightToNonmoving;
    }

    public void setChangeRateRightToNonmoving(Double changeRateRightToNonmoving) {
        this.changeRateRightToNonmoving = changeRateRightToNonmoving;
    }

    public Double getChangeRateRightToUturn() {
        return changeRateRightToUturn;
    }

    public void setChangeRateRightToUturn(Double changeRateRightToUturn) {
        this.changeRateRightToUturn = changeRateRightToUturn;
    }

    public Double getChangeRateRightToLeft() {
        return changeRateRightToLeft;
    }

    public void setChangeRateRightToLeft(Double changeRateRightToLeft) {
        this.changeRateRightToLeft = changeRateRightToLeft;
    }

    public Double getChangeRateAccumulated() {
        return changeRateAccumulated;
    }

    public void setChangeRateAccumulated(Double changeRateAccumulated) {
        this.changeRateAccumulated = changeRateAccumulated;
    }

    public Double getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(Double changeRate) {
        this.changeRate = changeRate;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Double getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(Double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public Double getMeanSpeed() {
        return meanSpeed;
    }

    public void setMeanSpeed(Double meanSpeed) {
        this.meanSpeed = meanSpeed;
    }

    public Double getMaxAcceleration() {
        return maxAcceleration;
    }

    public void setMaxAcceleration(Double maxAcceleration) {
        this.maxAcceleration = maxAcceleration;
    }

    public Double getMinAcceleration() {
        return minAcceleration;
    }

    public void setMinAcceleration(Double minAcceleration) {
        this.minAcceleration = minAcceleration;
    }

    public Double getAccumulatedAccelerationPositive() {
        return accumulatedAccelerationPositive;
    }

    public void setAccumulatedAccelerationPositive(Double accumulatedAccelerationPositive) {
        this.accumulatedAccelerationPositive = accumulatedAccelerationPositive;
    }

    public Double getAccumulatedAccelerationNegative() {
        return accumulatedAccelerationNegative;
    }

    public void setAccumulatedAccelerationNegative(Double accumulatedAccelerationNegative) {
        this.accumulatedAccelerationNegative = accumulatedAccelerationNegative;
    }

    public Double getMeanAccelerationPositive() {
        return meanAccelerationPositive;
    }

    public void setMeanAccelerationPositive(Double meanAccelerationPositive) {
        this.meanAccelerationPositive = meanAccelerationPositive;
    }

    public Double getMeanAccelerationNegative() {
        return meanAccelerationNegative;
    }

    public void setMeanAccelerationNegative(Double meanAccelerationNegative) {
        this.meanAccelerationNegative = meanAccelerationNegative;
    }

    public Integer getCountAccelerationPositive() {
        return countAccelerationPositive;
    }

    public void setCountAccelerationPositive(Integer countAccelerationPositive) {
        this.countAccelerationPositive = countAccelerationPositive;
    }

    public Integer getCountAccelerationNegative() {
        return countAccelerationNegative;
    }

    public void setCountAccelerationNegative(Integer countAccelerationNegative) {
        this.countAccelerationNegative = countAccelerationNegative;
    }

    public Integer getChangeBetweenPositiveAndNegative() {
        return changeBetweenPositiveAndNegative;
    }

    public void setChangeBetweenPositiveAndNegative(Integer changeBetweenPositiveAndNegative) {
        this.changeBetweenPositiveAndNegative = changeBetweenPositiveAndNegative;
    }

    public Double getAccumulatedDistance() {
        return accumulatedDistance;
    }

    public void setAccumulatedDistance(Double accumulatedDistance) {
        this.accumulatedDistance = accumulatedDistance;
    }

    public Double getMaxDistanceMoving() {
        return maxDistanceMoving;
    }

    public void setMaxDistanceMoving(Double maxDistanceMoving) {
        this.maxDistanceMoving = maxDistanceMoving;
    }

    public Double getAccumulatedTimeMoving() {
        return accumulatedTimeMoving;
    }

    public void setAccumulatedTimeMoving(Double accumulatedTimeMoving) {
        this.accumulatedTimeMoving = accumulatedTimeMoving;
    }

    public Double getAccumulatedTimeNonMoving() {
        return accumulatedTimeNonMoving;
    }

    public void setAccumulatedTimeNonMoving(Double accumulatedTimeNonMoving) {
        this.accumulatedTimeNonMoving = accumulatedTimeNonMoving;
    }
    
    
    
    
}
