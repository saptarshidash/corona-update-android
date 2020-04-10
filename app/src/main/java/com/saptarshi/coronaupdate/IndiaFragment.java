package com.saptarshi.coronaupdate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.anychart.charts.Map;
import com.anychart.core.grids.MapSettings;
import com.anychart.core.map.series.Choropleth;
import com.anychart.core.ui.ColorRange;
import com.anychart.core.ui.markersfactory.Marker;
import com.anychart.core.utils.UnboundRegionsSettings;
import com.anychart.enums.SelectionMode;
import com.anychart.enums.SidePosition;
import com.anychart.graphics.vector.text.HAlign;
import com.anychart.scales.LinearColor;
import com.anychart.scales.OrdinalColor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class IndiaFragment extends Fragment {
   private TextView totalConfrmd,totalRecovrd,totalDeaths;
    TextView gotoList;


    AnyChartView anyChartView;
    Map map;

    IndiaStat indiaStat=new IndiaStat();
    Summary summary=new Summary();
    Data data=new Data();

    List<Regional> regionalList=new ArrayList<>();



    static final String indianDataApi="https://api.rootnet.in/covid19-in/stats/latest";

    public static List<Regional> tempList=new ArrayList<>();

    public IndiaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_india, container, false);

        initTaks(view);
        gotoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI(getActivity(),RegionalRecyclerView.class);
            }
        });




        /* Fetching the json data */

        StringRequest stringRequest=new StringRequest(Request.Method.GET,indianDataApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Endpont response",response);
                GsonBuilder gsonBuilder=new GsonBuilder();
                Gson gson=gsonBuilder.create();
               indiaStat= gson.fromJson(response,IndiaStat.class);
                data=indiaStat.getData();
                summary=data.getSummary(); //getting summary data
                setSummaryData(summary);  // setting summary data

                regionalList=data.getRegional();

                initMap();
                sortRegionalList();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error code", "onErrorResponse: "+error);
                Toast.makeText(getContext(),"Unable to reach host",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);


        return view;
    }

    /* Getting the summary total data stat & setting it in the views*/
    private void setSummaryData(Summary summaryData){
        totalConfrmd.setText(summaryData.getTotal().toString());
        totalRecovrd.setText(summaryData.getDischarged().toString());
        totalDeaths.setText(summaryData.getDeaths().toString());

    }



    /* Sort the regional list acording to cases */
     private void sortRegionalList(){

         tempList=regionalList;
         for(int i=0;i<tempList.size()-1;i++){
             for(int j=0;j<tempList.size()-i-1;j++){
                 if (tempList.get(j).getConfirmedCasesIndian()>tempList.get(j+1).getConfirmedCasesIndian()){
                     Collections.swap(tempList,j,j+1);
                 }
             }
         }
         Collections.reverse(tempList);
         Toast.makeText(getContext(),""+tempList.get(0).getLoc(),Toast.LENGTH_SHORT).show();
     }

    /* Initialize the objs */
    private void initTaks(View view){
        anyChartView = view.findViewById(R.id.any_chart_view_map);
        map = AnyChart.map();

        //textViews
        totalConfrmd=view.findViewById(R.id.cnf_total);
        totalRecovrd=view.findViewById(R.id.recovered_total);
        totalDeaths=view.findViewById(R.id.death_total);
        gotoList=view.findViewById(R.id.goto_Listview);
    }
    /* Initialize the map */
    private void initMap(){
        map.geoData();
        map.geoData("anychart.maps.india");


        ColorRange colorRange = map.colorRange();
        colorRange.enabled(true);
        colorRange.orientation("left");
        colorRange.length(200);
        Marker marker=colorRange.marker();
        marker.type("diamond");

        map.interactivity().selectionMode(SelectionMode.NONE);
        map.selectMarqueeStroke();
        map.overlapMode(true);
        map.padding(0, 0, 0, 0);
        map.unboundRegions("{stroke:'0.2 #212121' ,fill: '#00ff00 0.4'}");


        Choropleth series = map.choropleth(getData());
        LinearColor linearColor = LinearColor.instantiate();
        linearColor.colors(new String[]{ "#e3f2fd", "#64b5f6", "#01579b", "#26418f"});
        // OrdinalColor ordinalColor=OrdinalColor.instantiate();
       // ordinalColor.colors(new String[]{"#e1f5fe", "#81d4fa", "#29b6f6", "#039be5","#01579b"});
        OrdinalColor ordinalColor = OrdinalColor.instantiate();
        ordinalColor.colors(new String[]{"#e1f5fe", "#81d4fa", "#29b6f6", "#039be5","#01579b"});
        ordinalColor.ranges(new String[]{"{from: 1, to: 100}", "{from: 101, to: 200}","{from: 201, to: 300}","{from: 301, to: 400}","{from: 401, to: 2000}"});
        series.colorScale(ordinalColor);
        series.stroke("0.2 #212121");

        series.hovered()
                .fill("#f44336 1")
                .stroke("2 #ffff");
        series.selected()
                .fill("#eee")
                .stroke("#ffff",1,"0 0","bevel","round");
        /*series.selected()
                .fill("#c2185b")
                .stroke("#c2185b"); */
        series.labels().enabled(true);
        series.labels().fontSize(7);
        series.labels().fontColor("#212121");
        series.labels().format("{%Name}");

        series.tooltip()
                .useHtml(true)
                .format("function() {\n" +
                        "            return '<span style=\"font-size: 13px\">CASES : '+this.getData('value')+'<br>RCVRD : '+ this.getData('Value2')+'<br>DEATH : '+this.getData('Value3')+'</span>';\n" + "          }");


        anyChartView.addScript("file:///android_asset/india.js");
        anyChartView.addScript("file:///android_asset/proj4.js");
        anyChartView.setChart(map);
    }

      /* Setting data in the map */
    private List<DataEntry> getData() {
        List<DataEntry> data = new ArrayList<>();

        for (int i=0;i<regionalList.size();i++){

            switch (regionalList.get(i).getLoc()){
           //regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()
                case "Andaman and Nicobar Islands" :
                    data.add(new CustomDataEntry("IN.AN", "Andaman and Nicobar", regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;

                    case "Andhra Pradesh" :
                    data.add(new CustomDataEntry("IN.AP", "Andhra Pradesh", regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Assam"  :
                    data.add(new CustomDataEntry("IN.AS", "Assam", regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Bihar"  :
                    data.add(new CustomDataEntry("IN.BR", "Bihar",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Chandigarh" :
                    data.add(new CustomDataEntry("IN.CH", "Chandigarh",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Chhattisgarh" :
                    data.add(new CustomDataEntry("IN.CT", "Chhattisgarh",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Delhi" :
                    data.add(new CustomDataEntry("IN.DL", "Delhi",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Goa" :
                    data.add(new CustomDataEntry("IN.GA", "Goa",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;

                case "Gujarat" :
                    data.add(new CustomDataEntry("IN.GJ", "Gujarat",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Haryana" :
                    data.add(new CustomDataEntry("IN.HR", "Haryana",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case  "Himachal Pradesh" :
                    data.add(new CustomDataEntry("IN.HP", "Himachal Pradesh",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Jammu and Kashmir" :
                    data.add(new CustomDataEntry("IN.JK", "Jammu and Kashmir",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Jharkhand" :
                    data.add(new CustomDataEntry("IN.JH", "Jharkhand",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Karnataka" :
                    data.add(new CustomDataEntry("IN.KA", "Karnataka",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Kerala" :
                    data.add(new CustomDataEntry("IN.KL", "Kerala",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Madhya Pradesh" :
                    data.add(new CustomDataEntry("IN.MP", "Madhya Pradesh",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Maharashtra" :
                    data.add(new CustomDataEntry("IN.MH", "Maharashtra",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Manipur" :
                    data.add(new CustomDataEntry("IN.MN", "Manipur",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Mizoram" :
                    data.add(new CustomDataEntry("IN.MZ", "Mizoram",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Odisha" :
                    data.add(new CustomDataEntry("IN.OR", "Odisha",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Puducherry" :
                    data.add(new CustomDataEntry("IN.PY", "Puducherry",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Punjab" :
                    data.add(new CustomDataEntry("IN.PB", "Punjab",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Rajasthan" :
                    data.add(new CustomDataEntry("IN.RJ", "Rajasthan",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Tamil Nadu" :
                    data.add(new CustomDataEntry("IN.TN", "Tamil Nadu",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Telengana" :
                    data.add(new CustomDataEntry("IN.TG", "Telengana",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Uttarakhand" :
                    data.add(new CustomDataEntry("IN.UT", "Uttarakhand",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "Uttar Pradesh" :
                    data.add(new CustomDataEntry("IN.UP", "Uttar Pradesh",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;
                case "West Bengal" :
                    data.add(new CustomDataEntry("IN.WB", "West Bengal",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;

                case "Arunachal Pradesh" :

                        data.add(new CustomDataEntry("IN.AR", "Arunachal Pradesh",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;

                case "Tripura" :
                    data.add(new CustomDataEntry("IN.TR", "Tripura",regionalList.get(i).getConfirmedCasesIndian(),regionalList.get(i).getDischarged(),regionalList.get(i).getDeaths()));break;


            }

        }

        return data;
    }

    /* Customize data entry for map */
    class CustomDataEntry extends DataEntry {
        public CustomDataEntry(String id, String name, Number value) {
            setValue("id", id);
            setValue("name", name);
            setValue("value", value);
        }


        public CustomDataEntry(String id, String name, LabelDataEntry label) {
            setValue("id", id);
            setValue("name", name);
            setValue("label", label);
        }

        public CustomDataEntry(String id,String name, Number value,Number value2,Number value3){
            setValue("id",id);
            setValue("name",name);
            setValue("value",value);
            setValue("Value2",value2);
            setValue("Value3",value3);
        }
    }

    class LabelDataEntry extends DataEntry {
        LabelDataEntry(Boolean enabled) {
            setValue("enabled", enabled);
        }
    }

    /* Method to update the activity as per situation */
    public void updateUI(Context context, Class cls){

        Intent intent=new Intent(context,cls);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}





