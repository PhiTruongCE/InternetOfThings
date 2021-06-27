package com.example.pc.lab4;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private LineGraphSeries<DataPoint> series1,series2,series3,series4;
    TextView light,temp,light1,temp1;
    MQTTHelper mqttHelper;
    Integer x = 0;
    Integer x1 = 0;
    Button sad, happy, no, yes;
    String send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        light = findViewById(R.id.light);
        temp = findViewById(R.id.temp);
        light1 = findViewById(R.id.light1);
        temp1 = findViewById(R.id.temp1);
        sad = findViewById(R.id.sad);
        happy = findViewById(R.id.happy);
        no = findViewById(R.id.no);
        yes = findViewById(R.id.yes);

        startMQTT();
        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send = "#c1-10-*";
                sendDataMQTT("phitruong220675/feeds/midterm", send);
            }
        });
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send = "#c1-11-*";
                sendDataMQTT("phitruong220675/feeds/midterm", send);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send = "#c1-20-*";
                sendDataMQTT("phitruong220675/feeds/midterm", send);
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send = "#c1-21-*";
                sendDataMQTT("phitruong220675/feeds/midterm", send);
            }
        });

    }
    private void startMQTT(){
        mqttHelper = new MQTTHelper(getApplicationContext());
        final GraphView graph1 = (GraphView) findViewById(R.id.graph1);
        final GraphView graph2 = (GraphView) findViewById(R.id.graph2);
        final GraphView graph3 = (GraphView) findViewById(R.id.graph3);
        final GraphView graph4 = (GraphView) findViewById(R.id.graph4);
        series1 = new LineGraphSeries<DataPoint>();
        series1.setColor(Color.BLUE);
        series2 = new LineGraphSeries<DataPoint>();
        series2.setColor(Color.RED);
        series3 = new LineGraphSeries<DataPoint>();
        series3.setColor(Color.BLUE);
        series4 = new LineGraphSeries<DataPoint>();
        series4.setColor(Color.RED);

        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.d("MQTT", mqttMessage.toString());
                String st = mqttMessage.toString();
                String split[] = st.split("-");
                Double y = Double.parseDouble(split[2]);
                Double y2 = Double.parseDouble(split[1]);
                if(split[0].equals("#1")){
                    series1.appendData(new DataPoint(x,y), true, 100);
                    series2.appendData(new DataPoint(x,y2), true, 100);
                    light.setText(y.toString());
                    temp.setText(y2.toString());
                    x = x + 1;
                }
                else{
                    series3.appendData(new DataPoint(x1,y), true, 100);
                    series4.appendData(new DataPoint(x1,y2), true, 100);
                    light1.setText(y.toString());
                    temp1.setText(y2.toString());
                    x1 = x1 + 1;
                }
                graph1.addSeries(series1);
                graph2.addSeries(series2);
                graph3.addSeries(series3);
                graph4.addSeries(series4);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
    private void sendDataMQTT(String topic, String data){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);

        byte[] b = data.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        Log.d("ABC","Publish :" + msg);
        try {
            mqttHelper.mqttAndroidClient.publish(topic , msg);

        }catch (MqttException e){
        }
    }
}
