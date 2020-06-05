package com.example.mareu.service;

import android.widget.Toast;

import com.example.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DummyMeetingApiService implements MeetingApiService  {

    private List<Meeting> mMeetingList = DummyMeetingGenerator.generateMeeting();

    @Override
    public List<Meeting> getMeetingsList() {
        return mMeetingList;
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        mMeetingList.remove(meeting);
    }

    @Override
    public boolean addMeeting(Meeting meeting) {
        List<Meeting> roomList = new ArrayList<>();
        for (Meeting someMeeting : mMeetingList) {
            if(someMeeting.getRoomName().equals(meeting.getRoomName())) {
                roomList.add(someMeeting);
            }
        }
        if (roomList.size() == 0) {
            mMeetingList.add(meeting);
            return true;
        }

        boolean sameTimeExists = false;
        for (Meeting someMeeting : roomList) {
            if (meeting.getDateStart().before(someMeeting.getDateStart()) &&
                meeting.getDateEnd().before(someMeeting.getDateStart()) ||
            meeting.getDateStart().after(someMeeting.getDateEnd()) &&
            meeting.getDateEnd().after(someMeeting.getDateEnd())) {
                sameTimeExists = false;
            } else {
                sameTimeExists = true;
                break;
            }
        }
         if (!sameTimeExists){
             mMeetingList.add(meeting);
            return true; }
         else {
             return false;
         }

    }

    @Override
    public List<Meeting> meetingsByDate(Calendar selectedCalendar) {
        List<Meeting> meetingsByDateList = new ArrayList<>();
        for (Meeting meeting : mMeetingList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(meeting.getDateStart());
            if (selectedCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                    selectedCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                    selectedCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                meetingsByDateList.add(meeting);
            }
        }
        return meetingsByDateList;
    }

    @Override
    public List<Meeting> meetingsByLocation(String location) {
        List<Meeting> meetingsByLocationList = new ArrayList<>();
        for (Meeting meeting : mMeetingList) {
            if (meeting.getRoomName().equals(location)) {
                meetingsByLocationList.add(meeting);
            }
        }
        return meetingsByLocationList;
    }

}
