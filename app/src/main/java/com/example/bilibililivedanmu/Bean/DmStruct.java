package com.example.bilibililivedanmu.Bean;

/**
 * Created by chazz on 2017/7/13.
 */

public class DmStruct {
    private DmData.DmType type;
    private Object obj;
    public DmStruct(DmData.DmType type,Object obj){
        this.type = type;
        this.obj = obj;
    }
    public DmStruct(){

    }
    public void setType(DmData.DmType type){
        this.type = type;
    }
    public void setObj(Object obj){
        this.obj = obj;
    }
    public DmData.DmType getType(){return type;}
    public Object getObj(){return obj;}
}
