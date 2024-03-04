package com.example.glomeet.service;

import com.example.glomeet.entity.Meeting;
import com.example.glomeet.mapper.MeetingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingMapper meetingMapper;

    public void createMeeting(Meeting meeting) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        meeting.setMasterEmail(userDetails.getUsername());
        meetingMapper.insertMeeting(meeting);
    }
}
