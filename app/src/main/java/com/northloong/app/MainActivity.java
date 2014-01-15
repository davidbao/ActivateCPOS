package com.northloong.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.text.*;
import java.util.regex.*;
import android.net.ConnectivityManager;
import android.content.Context;

public class MainActivity extends ActionBarActivity {

    public static String _callResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // for test
//        ((TextView) findViewById(R.id.editTextId)).setText("32550387");
//        ((TextView) findViewById(R.id.editTextCode1)).setText("55952");
//        ((TextView) findViewById(R.id.editTextCode2)).setText("50253");
//        ((TextView) findViewById(R.id.editTextCode3)).setText("42952");
//        ((TextView) findViewById(R.id.editTextCode4)).setText("43816");
        //////////

        final TextView code1EditText = (TextView) findViewById(R.id.editTextCode1);
        TextView code2EditText = (TextView) findViewById(R.id.editTextCode2);
        TextView code3EditText = (TextView) findViewById(R.id.editTextCode3);
        TextView code4EditText = (TextView) findViewById(R.id.editTextCode4);
        final TextView[] codeViews = new TextView[]{code1EditText, code2EditText, code3EditText, code4EditText};
        for(int i=0;i<codeViews.length;i++)
        {
            final int current = i;
             TextView view = codeViews[i];
            view.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length() == 5)
                    {
                        if(codeViews.length-1 != current)
                        {
                            TextView next = codeViews[current+1];
                            next.requestFocus();
                        }
                    }
                }
            });
        }

        TextView idEditText = (TextView) findViewById(R.id.editTextId);
        idEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 8)
                {
                    code1EditText.requestFocus();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void msbox(String title, String message)
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setTitle(title);
        dlgAlert.setMessage(message);
        dlgAlert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //finish();
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void onActivate(View view) {
        TextView idEditText = (TextView) findViewById(R.id.editTextId);
        String idStr = idEditText.getText().toString();
        if(idStr.length() < 8)
        {
            msbox(getString(R.string.error), getString(R.string.stationError));
            return;
        }

        TextView code1EditText = (TextView) findViewById(R.id.editTextCode1);
        TextView code2EditText = (TextView) findViewById(R.id.editTextCode2);
        TextView code3EditText = (TextView) findViewById(R.id.editTextCode3);
        TextView code4EditText = (TextView) findViewById(R.id.editTextCode4);
        TextView[] codeViews = new TextView[]{code1EditText, code2EditText, code3EditText, code4EditText};
        for (TextView codeView : codeViews) {
            if (codeView.length() < 5) {
                msbox(getString(R.string.error), getString(R.string.installCodeError));
                return;
            }
        }

        if(!isNetworkConnected(this))
        {
            // can not find the network connection, then return.
            msbox(getString(R.string.error), getString(R.string.networkDisconnection));
            return;
        }

        TextView codeEditText = (TextView) findViewById(R.id.editTextActivation);
        codeEditText.setText("");

        Caller c=new Caller();
        c.stationNo = idStr;    //"12345678";
        c.installCode = String.format("%s-%s-%s-%s",
                code1EditText.getText().toString(),
                code2EditText.getText().toString(),
                code3EditText.getText().toString(),
                code4EditText.getText().toString());    // "12345-54321-12345-54321";

        //c.join();
        c.start();

        int count = 0;
        MainActivity._callResult = "";
        while(MainActivity._callResult.length() == 0)
        {
            count++;
            if(count > 800)
                break;

            try {
                Thread.sleep(10);
            }catch(Exception ex) {
            }
        }

        if(MainActivity._callResult.length() == 0)
        {
            msbox(getString(R.string.error), getString(R.string.networkError));
            return;
        }

        // "欢迎使用CPOS，油站编码为32550387，激活码为29141-41562-32543-00571-47869"
        //MainActivity._callResult = "欢迎使用CPOS，油站编码为32550387，激活码为29141-41562-32543-00571-47869";

        // Direct use of Pattern:
        Pattern p = Pattern.compile("激活码为(\\d{5}-\\d{5}-\\d{5}-\\d{5}-\\d{5})");
        Matcher m = p.matcher(MainActivity._callResult);
        if(m.find()) { // Find each match in turn; String can't do this.
            String code = m.group(1); // Access a submatch group; String can't do this.
            codeEditText.setText(code);
        }

        msbox(getString(R.string.information), MainActivity._callResult);
    }

    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
