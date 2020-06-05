package com.example.mareu.service;

import com.example.mareu.model.Meeting;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface MeetingApiService {

    List<Meeting> getMeetingsList();

    void deleteMeeting(Meeting meeting);
    boolean addMeeting(Meeting meeting);

    List<Meeting> meetingsByDate(Calendar calendar);
    List<Meeting> meetingsByLocation(String location);
}
