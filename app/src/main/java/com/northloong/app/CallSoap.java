package com.northloong.app;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class CallSoap
{
    public final String SOAP_ACTION = "http://tempuri.org/Activate";
    public final String OPERATION_NAME = "Activate";
    public final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    public final String SOAP_ADDRESS = "http://northloong.com:8733/ActivateService.asmx";

    public CallSoap()
    {
    }
    public String Call(String stationNo, String installCode)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("stationNo");
        pi.setValue(stationNo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("installCode");
        pi.setValue(installCode);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }
}
