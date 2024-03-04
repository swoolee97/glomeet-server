package com.example.glomeet.controller;

import com.example.glomeet.entity.Meeting;
import com.example.glomeet.service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/create")
    public void createMeeting(@RequestBody @Valid Meeting meeting) {
        meetingService.createMeeting(meeting);
    }

}
