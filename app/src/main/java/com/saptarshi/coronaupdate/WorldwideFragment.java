package com.saptarshi.coronaupdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.anychart.graphics.vector.Fill;
import com.anychart.palettes.DistinctColors;
import com.anychart.palettes.RangeColors;
import com.anychart.scales.OrdinalColor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class WorldwideFragment extends Fragment {
   private static final String endPoint="https://coronavirus-monitor.p.rapidapi.com/coronavirus/worldstat.php";
   private static final String apiKey="0e7092436fmsh19d9d41e84fe511p1c5d3djsn272e898a612d";

   int totalCases,totalDeaths,totalRecovered,newCases,newDeaths;
   WorldwideData worldwideData=new WorldwideData();
   AnyChartView anyChartView;
    Pie pie;
   String response_body;



   List<Integer> countList=new ArrayList<>();

    public WorldwideFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=  inflater.inflate(R.layout.fragment_worldwide, container, false);
        initTasks(view);
        setPieChart();


        StringRequest stringRequest=new StringRequest(Request.Method.GET,endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Endpont response",response);
                response_body=response;
                GsonBuilder gsonBuilder=new GsonBuilder();
                Gson gson=gsonBuilder.create();
                worldwideData= gson.fromJson(response,WorldwideData.class);
                setWorldwideData(worldwideData);
                udatePieChartData();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error code", "onErrorResponse: "+error);
                Toast.makeText(getContext(),"Unable to reach host",Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers =new HashMap<String, String>();
                headers.put("x-rapidapi-key", apiKey);
                return headers;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

        return view;


    }



    private void initTasks(View view) {
        anyChartView=view.findViewById(R.id.any_chart_view);
        pie= AnyChart.pie();
    }

    private void setPieChart(){
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                // Toast.makeText(getContext(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Cases", 1));



        pie.data(data);

        pie.title(" ");

        pie.labels().position("outside");
        pie.labels().format("{%value}");
        pie.labels().fontSize(15);
        //pie.palette(new String[]{"#eeeeee", "#ff1744", "#b2ff59", "#7e57c2","#424242"});

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text(" ")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE)
                .align(Align.CENTER);




    }

    private void udatePieChartData(){


                List<DataEntry> data = new ArrayList<>();
                data.add(new ValueDataEntry("Cases", totalCases));
                data.add(new ValueDataEntry("Deaths",totalDeaths));
                data.add(new ValueDataEntry("Recovered",totalRecovered));
                data.add(new ValueDataEntry("New Cases",newCases));
                data.add(new ValueDataEntry("New Deaths",newDeaths));


                pie.data(data);
                pie.startAngle(240);
                pie.tooltip().format("{%Value} {%x} World wide");
                anyChartView.setChart(pie);








    }


    private void setWorldwideData(WorldwideData worldwideData) {

        totalCases=Integer.parseInt(worldwideData.getTotalCases().replaceAll(",",""));
        totalDeaths=Integer.parseInt(worldwideData.getTotalDeaths().replaceAll(",",""));
        totalRecovered=Integer.parseInt(worldwideData.getTotalRecovered().replaceAll(",",""));
        newCases=Integer.parseInt(worldwideData.getNewCases().replaceAll(",",""));
        newDeaths=Integer.parseInt(worldwideData.getNewDeaths().replaceAll(",",""));


    }

}
