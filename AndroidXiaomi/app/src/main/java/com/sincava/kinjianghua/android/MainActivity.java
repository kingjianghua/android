package com.sincava.kinjianghua.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void count(View v)
    {
        EditText e1 =(EditText)findViewById(R.id.editText);
        EditText e2 =(EditText)findViewById(R.id.editText2);
        TextView result = (TextView)findViewById(R.id.textView2);

        int num1 =Integer.parseInt(e1.getText().toString()) ;
        int num2 =Integer.parseInt(e2.getText().toString());
        int res = num1+num2;
        result.setText(Integer.toString(res));

        Toast.makeText(MainActivity.this, "Demo", Toast.LENGTH_SHORT).show();

    }

}
