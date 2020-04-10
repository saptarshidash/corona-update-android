package com.saptarshi.coronaupdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class RegionalRecyclerView extends AppCompatActivity {
        RecyclerView recyclerView;
        List<Regional> regionalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regional_recycler_view);
         initTasks();
         recyclerView.setLayoutManager(new LinearLayoutManager(this));
         recyclerView.setAdapter(new RegionalRecylerAdapter(IndiaFragment.tempList));


    }

    private void initTasks(){
        recyclerView=findViewById(R.id.recycler_view);

    }


}
