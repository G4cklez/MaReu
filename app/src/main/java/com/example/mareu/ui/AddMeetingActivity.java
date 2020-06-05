package com.example.mareu.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mareu.R;
import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.DummyMeetingGenerator;
import com.example.mareu.service.MeetingApiService;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddMeetingActivity extends AppCompatActivity {

    private ImageView backBtn;
    private DatePicker datePicker;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private Spinner roomSpinner;
    private EditText editTextParticipants;
    private EditText editTextSubject;
    private Button saveMeetingBtn;
    private List<String> roomList = DummyMeetingGenerator.ROOM_LIST;

    private int color;
    private String roomName;
    private Date dateStart;
    private Date dateEnd;
    private String subject;
    private String participants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
        initViews();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews() {
        backBtn = findViewById(R.id.back_btn);
        datePicker = findViewById(R.id.date_picker);
        startTimePicker = findViewById(R.id.start_time_picker);
        endTimePicker = findViewById(R.id.end_time_picker);
        roomSpinner = findViewById(R.id.room_spinner);
        editTextParticipants = findViewById(R.id.edit_text_participants);
        editTextSubject = findViewById(R.id.edit_text_subject);
        saveMeetingBtn = findViewById(R.id.save_meeting_btn);
        startTimePicker.setIs24HourView(true);
        endTimePicker.setIs24HourView(true);
        setRoomList();
        datePicker.setMinDate(Calendar.getInstance().getTimeInMillis());
        endTimePicker.setMinute(startTimePicker.getMinute()+1);

        saveMeetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMeeting();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addMeeting() {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        initCalendar(startCalendar, endCalendar);
        if (!endCalendar.after(startCalendar)) {
            Toast.makeText(this, "Vous devez sélectionner une heure de fin de réunion différente de celle du début", Toast.LENGTH_SHORT).show();
            return;
        }

        initFields(startCalendar, endCalendar);

        if (validateInputFields(subject, participants)) return;

        if (!validateEmails()) return;

        List<String> participantsList = Arrays.asList(participants.split("\n"));
        Meeting meeting = new Meeting(color, roomName, dateStart, dateEnd, subject, participantsList);
        MeetingApiService meetingApiService = DI.getApiService();
        if(!meetingApiService.addMeeting(meeting)) {
            Toast.makeText(this, "Vous avez selectionné les mêmes dates et heures qu'une autre réunion", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Votre réunion a bien été ajoutée", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void initFields(Calendar startCalendar, Calendar endCalendar) {
        color = DummyMeetingGenerator.generateColor();
        roomName = roomSpinner.getSelectedItem().toString();
        dateStart = startCalendar.getTime();
        dateEnd = endCalendar.getTime();
        subject = editTextSubject.getText().toString();
        participants = editTextParticipants.getText().toString();
    }

    private boolean validateInputFields(String subject, String participants) {
        if (subject.isEmpty()) {
            Toast.makeText(this, "Vous devez renseigner un sujet", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (participants.isEmpty()) {
            Toast.makeText(this, "Vous devez renseigner des participants", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void initCalendar(Calendar startCalendar, Calendar endCalendar) {
        startCalendar.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
        endCalendar.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startCalendar.set(Calendar.HOUR_OF_DAY,startTimePicker.getHour());
            startCalendar.set(Calendar.MINUTE,startTimePicker.getMinute());

            endCalendar.set(Calendar.HOUR_OF_DAY,endTimePicker.getHour());
            endCalendar.set(Calendar.MINUTE,endTimePicker.getMinute());
        }
        else {
            startCalendar.set(Calendar.HOUR_OF_DAY,startTimePicker.getCurrentHour());
            startCalendar.set(Calendar.MINUTE,startTimePicker.getCurrentMinute());

            endCalendar.set(Calendar.HOUR_OF_DAY,endTimePicker.getCurrentHour());
            endCalendar.set(Calendar.MINUTE,endTimePicker.getCurrentMinute());
        }
    }

    private void setRoomList() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, roomList);
        roomSpinner.setAdapter(arrayAdapter);
    }

    private boolean validateEmails() {
        List<String> participantsList = Arrays.asList(editTextParticipants.getText().toString().split("\n"));
        for (String email : participantsList) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Veuillez entrer des e-mails valides", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

}
