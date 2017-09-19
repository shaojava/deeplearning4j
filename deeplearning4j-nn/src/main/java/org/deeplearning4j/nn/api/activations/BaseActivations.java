package org.deeplearning4j.nn.api.activations;

import org.deeplearning4j.nn.api.MaskState;
import org.nd4j.linalg.api.ndarray.INDArray;

public abstract class BaseActivations implements Activations {

    protected void assertIndex(int idx){
        if(idx < 0 || idx >= size()){
            throw new IllegalArgumentException("Invalid index: cannot get/set index " + idx + " from activations of " +
                    "size " + size());
        }
    }

    @Override
    public void clear() {
        for( int i=0; i<size(); i++ ){
            set(i, null);
            setMask(i, null);
            setMaskState(i, null);
        }
    }

    @Override
    public INDArray[] getAsArray(){
        INDArray[] out = new INDArray[size()];
        for( int i=0; i<size(); i++ ){
            out[i] = get(i);
        }
        return out;
    }

    @Override
    public INDArray[] getMaskAsArray(){
        INDArray[] out = new INDArray[size()];
        for( int i=0; i<size(); i++ ){
            out[i] = getMask(i);
        }
        return out;
    }

    @Override
    public MaskState[] getMaskStateAsArray(){
        MaskState[] out = new MaskState[size()];
        for( int i=0; i<size(); i++ ){
            out[i] = getMaskState(i);
        }
        return out;
    }

    @Override
    public void setFromArray(INDArray[] activations){
        if(activations == null){
            for( int i=0; i<size(); i++ ){
                set(i, null);
            }
        } else {
            if(activations.length != size()){
                throw new IllegalArgumentException("Cannot set activations from array: activations size is " + size()
                        + " but got " + activations.length + " arrays as input");
            }
            for( int i=0; i<size(); i++ ){
                set(i, activations[i]);
            }
        }
    }

    @Override
    public void setMaskFromArray(INDArray[] masks, MaskState[] maskStates){
        if(masks == null){
            for( int i=0; i<size(); i++ ){
                setMask(i, null);
                setMaskState(i, null);
            }
        } else {
            if(masks.length != size()){
                throw new IllegalArgumentException("Cannot set masks from array: activations size is " + size()
                        + " but got " + masks.length + " arrays as input");
            }
            for( int i=0; i<size(); i++ ){
                setMask(i, masks[i]);
                setMaskState(i, maskStates == null ? MaskState.Active : maskStates[i]);
            }
        }
    }

    @Override
    public Activations leverageTo(String workspaceId){
        for( int i=0; i<size(); i++ ){
            if(get(i) != null && get(i).isAttached()){
                set(i, get(i).leverageTo(workspaceId));
            }
            if(getMask(i) != null && getMask(i).isAttached()){
                setMask(i, getMask(i).leverageTo(workspaceId));
            }
        }
        return this;
    }

    @Override
    public Activations migrate(){
        for( int i=0; i<size(); i++ ){
            if(get(i) != null ){
                set(i, get(i).migrate());
            }
            if(getMask(i) != null){
                setMask(i, getMask(i).migrate());
            }
        }
        return this;
    }
}
