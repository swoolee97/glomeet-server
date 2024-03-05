package com.example.glomeet.mapper;

import com.example.glomeet.dto.MeetingChatInfoDTO;
import com.example.glomeet.dto.MeetingInfoDTO;
import com.example.glomeet.entity.Meeting;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MeetingMapper {
    void insertMeeting(Meeting meeting);

    int countMeetingUser(String meetingId);

    int findMeetingCapacityById(String meetingId);

    int insertMeetingUser(String meetingId, String email);

    List<MeetingInfoDTO> findAllMeetings();

    List<MeetingChatInfoDTO> findMeetingChatById(String email);
}
