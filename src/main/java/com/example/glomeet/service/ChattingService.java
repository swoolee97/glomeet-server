package com.example.glomeet.service;

import com.example.glomeet.dto.ChatInfoDTO;
import com.example.glomeet.dto.MessageListRequestDTO;
import com.example.glomeet.mapper.MatchingMapper;
import com.example.glomeet.mapper.MeetingMapper;
import com.example.glomeet.mongo.model.ChatMessage;
import com.example.glomeet.repository.ChatMessageRepository;
import com.example.glomeet.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChattingService {
    private static final String LAST_MESSAGE_PREFIX = "lastMessage:";
    private static final String MESSAGE_LIST_PREFIX = "chat:";
    private final RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;
    private ListOperations<String, Object> listOperations;
    private final UserDetailsServiceImpl userDetailsService;
    private final MatchingMapper matchingMapper;
    private final MeetingMapper meetingMapper;
    private final ChatMessageRepository chatMessageRepository;
    private final ObjectMapper objectMapper;
    private final MongoTemplate mongoTemplate;

    public List<String> getMyChatList() {
        String email = userDetailsService.getUserNameByAccessToken();
        List<String> matchingIdList = matchingMapper.findMatchingRoomByEmail(email);
        List<String> meetingIdList = meetingMapper.findMyMeetingsIdByEmail(email);
        matchingIdList.addAll(meetingIdList);
        return matchingIdList;
    }

    public List<ChatInfoDTO> findLastMessageByRoomId(List<ChatInfoDTO> list) {
        valueOperations = redisTemplate.opsForValue();
        list.forEach(c -> {
            ChatMessage chatMessage = (ChatMessage) valueOperations.get(LAST_MESSAGE_PREFIX + c.getId());
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

    private int countUnReadMessageInDatabase(String id, Date lastLeftAt) {
        return chatMessageRepository.countMessagesAfterLastLeftAt(id, lastLeftAt);
    }

    private int countUnReadMessageInRedis(String id, String lastReadTime) {
        int count = 0;
        listOperations = redisTemplate.opsForList();
        Date date = DateUtil.parseDate(lastReadTime);
        List<ChatMessage> list = listOperations.range("chat:" + id, 0, -1).stream().map(o -> (ChatMessage) o).collect(
                Collectors.toList());
        for (ChatMessage message : list) {
            if (date.toInstant().isBefore(message.getSendAt().toInstant())) {
                count++;
            }
        }
        return count;
    }

    public List<ChatInfoDTO> findMatchingRoomInfoByEmail(Map<String, String> lastLeftMap) {
        String email = userDetailsService.getUserNameByAccessToken();
        List<ChatInfoDTO> matchingRoomInfos = matchingMapper.findMatchingRoomInfoByEmail(email);
        matchingRoomInfos = addUnReadCount(matchingRoomInfos,
                lastLeftMap);
        matchingRoomInfos = (List<ChatInfoDTO>) findLastMessageByRoomId(
                matchingRoomInfos);
        return matchingRoomInfos;
    }

    public List<ChatInfoDTO> addUnReadCount(List<ChatInfoDTO> infoList,
                                            Map<String, String> lastLeftMap) {
        for (ChatInfoDTO info : infoList) {
            String id = info.getId();
            if (!lastLeftMap.containsKey(id)) {
                continue;
            }
            Date lastLeft = DateUtil.parseDate(lastLeftMap.get(id));
            int unReadCountInDatabase = countUnReadMessageInDatabase(id, lastLeft);
            int unReadCountInRedis = countUnReadMessageInRedis(id, lastLeftMap.get(id));
            info.setUnRead(unReadCountInDatabase + unReadCountInRedis);
        }
        return infoList;
    }

    public List<ChatMessage> MatchingMessageByChatRoomId(MessageListRequestDTO messageListRequestDTO) {
        commitMessagesToDatabase(messageListRequestDTO);
        return chatMessageRepository.findMatchingMessagesByRoomId(
                messageListRequestDTO.getRoomId());
    }

    public void commitMessagesToDatabase(MessageListRequestDTO messageListRequestDTO) {
        listOperations = redisTemplate.opsForList();
        List<Object> list = listOperations.range(MESSAGE_LIST_PREFIX + messageListRequestDTO.getRoomId(), 0,
                -1);
        if (!list.isEmpty()) {
            List<ChatMessage> messages = list.stream()
                    .map(json -> objectMapper.convertValue(json, ChatMessage.class))
                    .collect(Collectors.toList());
            mongoTemplate.insertAll(messages);
            redisTemplate.delete(MESSAGE_LIST_PREFIX + messageListRequestDTO.getRoomId());
        }
    }

    public void saveMessageToRedis(ChatMessage chatMessage) {
        addMessageToRedis(chatMessage);
        saveLastMessage(chatMessage);
    }

    public void saveLastMessage(ChatMessage chatMessage) {
        valueOperations = redisTemplate.opsForValue();
        valueOperations.set(LAST_MESSAGE_PREFIX + chatMessage.getRoomId(), chatMessage);
    }

    public void addMessageToRedis(ChatMessage chatMessage) {
        redisTemplate.opsForList()
                .rightPush(MESSAGE_LIST_PREFIX + chatMessage.getRoomId(), chatMessage);
    }
}
