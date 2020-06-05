package com.example.mareu;

import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.DummyMeetingGenerator;
import com.example.mareu.service.MeetingApiService;

import org.junit.Before;
import org.junit.Test;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.graphics.Color.rgb;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class MeetingUnitTest {
    private MeetingApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getMeetingWithSuccess() {
        List<Meeting> meeting =service.getMeetingsList();
        List<Meeting> expectedMeeting = DummyMeetingGenerator.generateMeeting();
        assertThat(meeting, IsIterableContainingInAnyOrder.containsInAnyOrder(Objects.requireNonNull(expectedMeeting.toArray())));

    }

    @Test
    public void createMeetingWithSuccess() {
        Date dateStart = new Date();
        Date dateEnd = new Date();

        List<String> list = new ArrayList<>(Arrays.asList("1@1.1", "2@2.2"));
        Meeting test = new Meeting(rgb(100, 150, 200), "Salle A", dateStart, dateEnd, "test", list);
        service.addMeeting(test);
        assertTrue(service.getMeetingsList().contains(test));
    }

    @Test
    public void deleteMeetingWithSuccess() {
        Meeting meetingToDelete = service.getMeetingsList().get(0);
        service.deleteMeeting(meetingToDelete);
        assertFalse(service.getMeetingsList().contains(meetingToDelete));
    }


    @Test
    public void checkingDateFilter() {
        List<String> list = new ArrayList<>(Arrays.asList("1@1.1", "2@2.2"));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        Meeting test = new Meeting(rgb(100, 150, 200), "Salle A", cal.getTime(), cal.getTime(), "test", list);
        service.addMeeting(test);

        assertTrue(service.meetingsByDate(cal).contains(test));

    }

    @Test
    public void checkingRoomFilter() {
        List<String> list = new ArrayList<>(Arrays.asList("1@1.1", "2@2.2"));
        Calendar cal = Calendar.getInstance();
        Meeting test = new Meeting(rgb(100, 150, 200), "Salle A", cal.getTime(), cal.getTime(), "test", list);
        service.addMeeting(test);

        assertEquals(2, service.meetingsByLocation("Salle A").size());

    }

}
