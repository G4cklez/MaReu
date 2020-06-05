package com.example.mareu.di;

import com.example.mareu.service.DummyMeetingApiService;
import com.example.mareu.service.MeetingApiService;

public class DI {
    private static MeetingApiService getService = new DummyMeetingApiService();

    public static MeetingApiService getApiService() {
        return getService;
    }

    public static MeetingApiService getNewInstanceApiService() {
        return new DummyMeetingApiService();
    }

}
