package com.example.mareu.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.example.mareu.R;
import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.DummyMeetingGenerator;
import com.example.mareu.service.MeetingApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements MeetingAdapter.DeleteMeetingListener {

    RecyclerView mRecyclerView;
    MeetingAdapter mAdapter;
    MeetingApiService apiService;
    FloatingActionButton addMeetingBtn;
    String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addMeetingBtn = findViewById(R.id.add_meeting_btn);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this,R.drawable.ic_filter));
        apiService = DI.getApiService();
        initList();
        addMeetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddMeetingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initList() {
        mAdapter = new MeetingAdapter(apiService.getMeetingsList(), this);
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        apiService.deleteMeeting(meeting);
        initList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter_by_date) {
            filterByDate();
        } else {
            filterByLocation();
        }
        return super.onOptionsItemSelected(item);
    }

    public void filterByDate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final DatePicker filterDatePicker = new DatePicker(this);
        builder.setView(filterDatePicker);
        builder.setNegativeButton("clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initList();
            }
        });
        builder.setPositiveButton("filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, filterDatePicker.getDayOfMonth());
                calendar.set(Calendar.MONTH, filterDatePicker.getMonth());
                calendar.set(Calendar.YEAR, filterDatePicker.getYear());
                initListByDate(calendar);
            }
        });
        builder.show();
    }

    public void initListByDate (Calendar calendar) {
        mRecyclerView.setAdapter(new MeetingAdapter(apiService.meetingsByDate(calendar), this));
    }

    public void filterByLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Room");
        String[] roomList = new String[DummyMeetingGenerator.ROOM_LIST.size()];
        roomList = DummyMeetingGenerator.ROOM_LIST.toArray(roomList);
        roomName = roomList[0];

        final String[] finalRoomList = roomList;
        builder.setSingleChoiceItems(roomList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                roomName = finalRoomList[which];
            }
        });

        builder.setNegativeButton("reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initList();
            }
        });

        builder.setPositiveButton("filter by location", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initListByLocation(roomName);
            }
        });
        builder.show();
    }

    public void initListByLocation (String location) {
        mRecyclerView.setAdapter(new MeetingAdapter(apiService.meetingsByLocation(location), this));
    }

}
