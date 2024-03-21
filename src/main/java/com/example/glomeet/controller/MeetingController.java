package com.example.glomeet.controller;

import com.example.glomeet.dto.ChatInfoDTO;
import com.example.glomeet.dto.MeetingInfoDTO;
import com.example.glomeet.entity.Meeting;
import com.example.glomeet.response.Response;
import com.example.glomeet.service.MeetingService;
import com.example.glomeet.service.MeetingService.ExceededMeetingCapacityException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createMeeting(@RequestBody @Valid Meeting meeting) {
        Map<String, String> map = meetingService.createMeeting(meeting);
        return ResponseEntity.ok(map);
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

    // 활성화된 미팅 리스트 전체 불러오는 api
    @GetMapping("/all")
    public ResponseEntity<?> getAllMeetingList() {
        List<MeetingInfoDTO> meetingList = meetingService.getAllMeetingList();
        return ResponseEntity.ok().body(meetingList);
    }

    // 내가 속한 미팅방 ID 불러오기
    @PostMapping("/my")
    public ResponseEntity<?> getMyMeetingList() {
        List<String> meetingList = meetingService.getMyMeetingList();
        return ResponseEntity.ok(meetingList);
    }

    @PostMapping("/list")
    public List<ChatInfoDTO> getMeetingChatList() {
        return meetingService.getMeetingChatList();
    }

    @Getter
    public static class MemberJoinRequestDTO {
        @NotNull
        private String meetingId;
    }

}
