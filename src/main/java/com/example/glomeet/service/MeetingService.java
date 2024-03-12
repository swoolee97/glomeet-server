package com.example.glomeet.service;

import com.example.glomeet.controller.MeetingController.MemberJoinRequestDTO;
import com.example.glomeet.dto.MeetingChatInfoDTO;
import com.example.glomeet.dto.MeetingInfoDTO;
import com.example.glomeet.entity.Meeting;
import com.example.glomeet.mapper.MeetingMapper;
import com.example.glomeet.mongo.model.ChatMessage;
import com.example.glomeet.repository.ChatMessageRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingMapper meetingMapper;
    private final RedisTemplate<String, ChatMessage> redisTemplate;
    private ValueOperations<String, ChatMessage> valueOperations;
    private static final String LAST_MESSAGE_PREFIX = "lastMessage:";
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public Map<String, String> createMeeting(Meeting meeting) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        meeting.setMasterEmail(userDetails.getUsername());
        meeting.setId(UUID.randomUUID().toString());
        meetingMapper.insertMeeting(meeting);
        meetingMapper.insertMeetingUser(meeting.getId(), userDetails.getUsername());
        Map<String, String> map = new HashMap<>();
        map.put("id", meeting.getId());
        return map;
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

    public List<MeetingInfoDTO> getAllMeetingList() {
        List<MeetingInfoDTO> meetingList = meetingMapper.findAllMeetings();
        return meetingList;
    }

    public List<String> getMyMeetingList() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return meetingMapper.findMyMeetingsIdByEmail(userDetails.getUsername());
    }

    public List<MeetingChatInfoDTO> getMeetingChatList() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUsername();
        List<MeetingChatInfoDTO> list = meetingMapper.findMeetingChatById(email);
        valueOperations = redisTemplate.opsForValue();
        list.forEach(c -> {
            ChatMessage chatMessage = valueOperations.get(LAST_MESSAGE_PREFIX + c.getId());
            if (!Objects.isNull(chatMessage)) {
                c.setLastMessage(chatMessage.getMessage());
                c.setSendAt(chatMessage.getSendAt());
            } else {
                ChatMessage lastMessage = chatMessageRepository.findTopByRoomId(c.getId());
                if (!Objects.isNull(lastMessage)) {
                    c.setLastMessage(lastMessage.getMessage());
                    c.setSendAt(lastMessage.getSendAt());
                }
            }
        });
        return list;
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
