package com.example.glomeet.controller;

import com.example.glomeet.dto.MeetingChatInfoDTO;
import com.example.glomeet.dto.MeetingInfoDTO;
import com.example.glomeet.entity.Meeting;
import com.example.glomeet.response.Response;
import com.example.glomeet.service.MeetingService;
import com.example.glomeet.service.MeetingService.ExceededMeetingCapacityException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/list")
    public ResponseEntity<?> getMeetingList() {
        List<MeetingInfoDTO> list = meetingService.getMeetingList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/chat")
    public List<MeetingChatInfoDTO> getMeetingChatList() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUsername();
        List<MeetingChatInfoDTO> list = meetingService.getMeetingChatList(email);
        return list;
    }

    @Getter
    public static class MemberJoinRequestDTO {
        @NotNull
        private String meetingId;
    }

}
