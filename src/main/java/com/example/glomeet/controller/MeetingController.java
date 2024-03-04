package com.example.glomeet.controller;

import com.example.glomeet.entity.Meeting;
import com.example.glomeet.response.Response;
import com.example.glomeet.service.MeetingService;
import com.example.glomeet.service.MeetingService.ExceededMeetingCapacityException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/create")
    public void createMeeting(@RequestBody @Valid Meeting meeting) {
        meetingService.createMeeting(meeting);
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinMeeting(@RequestBody @Valid MemberJoinRequestDTO memberJoinRequestDTO) {
        try {
            meetingService.joinMeeting(memberJoinRequestDTO);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(new Response("이미 속해있는 모임이에요"), HttpStatus.CONFLICT);
        } catch (ExceededMeetingCapacityException e) {
            return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok().build();
    }

    @Getter
    public static class MemberJoinRequestDTO {
        private String meetingId;
    }

}
