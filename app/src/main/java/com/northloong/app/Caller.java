package com.northloong.app;

/**
 * Created by baowei on 14-1-10.
 */
public class Caller extends Thread
{
    public CallSoap cs;
    public String stationNo, installCode;

    public void run(){
        try{
            cs=new CallSoap();
            String resp = cs.Call(stationNo, installCode);
            MainActivity._callResult=resp;
        }catch(Exception ex)
        {MainActivity._callResult=ex.toString();}
    }
}
