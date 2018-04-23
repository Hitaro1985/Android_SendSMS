package com.example.bulksms.sendsms.model;

/**
 * Created by PDC100 on 3/23/2018.
 */

public class InfoRowData {
    public boolean isclicked=false;
    public int index;
    /*public String fanId;
    public String strAmount;*/

    public InfoRowData(boolean isclicked,int index/*,String fanId,String strAmount*/)
    {
        this.index=index;
        this.isclicked=isclicked;
        /*this.fanId=fanId;
        this.strAmount=strAmount;*/
    }
}
