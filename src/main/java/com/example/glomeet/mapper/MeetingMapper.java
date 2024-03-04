package com.example.glomeet.mapper;

import com.example.glomeet.entity.Meeting;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MeetingMapper {
    void insertMeeting(Meeting meeting);

    int countMeetingUser(String meetingId);

    int findMeetingCapacityById(String meetingId);

    int insertMeetingUser(String meetingId, String email);
}
