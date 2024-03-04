package com.example.glomeet.service;

import com.example.glomeet.controller.MeetingController.MemberJoinRequestDTO;
import com.example.glomeet.entity.Meeting;
import com.example.glomeet.mapper.MeetingMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
        meeting.setId(UUID.randomUUID().toString());
        meetingMapper.insertMeeting(meeting);
    }

    public boolean joinMeeting(MemberJoinRequestDTO memberJoinRequestDTO) {
        validateMeetingCapacity(memberJoinRequestDTO.getMeetingId());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        try {
            meetingMapper.insertMeetingUser(memberJoinRequestDTO.getMeetingId(), email);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }

        return true;
    }

    private void validateMeetingCapacity(String meetingId) {
        int currentMeetingUserCount = meetingMapper.countMeetingUser(meetingId);
        int meetingCapacity = meetingMapper.findMeetingCapacityById(meetingId);
        if (currentMeetingUserCount == meetingCapacity) {
            throw new ExceededMeetingCapacityException("제한 인원을 초과했어요");
        }
    }

    public static class NotFoundMeetingException extends RuntimeException {
        public NotFoundMeetingException(String message) {
            super(message);
        }
    }

    public static class ExceededMeetingCapacityException extends RuntimeException {
        public ExceededMeetingCapacityException(String message) {
            super(message);
        }
    }

}
