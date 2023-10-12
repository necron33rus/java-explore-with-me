package ru.practicum.ewm.service.event.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.category.model.Category;
import ru.practicum.ewm.service.category.repository.CategoryRepository;
import ru.practicum.ewm.service.constant.EventState;
import ru.practicum.ewm.service.constant.ParticipationRequestStatus;
import ru.practicum.ewm.service.event.dto.*;
import ru.practicum.ewm.service.event.model.Event;
import ru.practicum.ewm.service.event.model.Location;
import ru.practicum.ewm.service.event.repository.EventRepository;
import ru.practicum.ewm.service.event.repository.LocationRepository;
import ru.practicum.ewm.service.exception.ConflictException;
import ru.practicum.ewm.service.exception.NotFoundException;
import ru.practicum.ewm.service.participation.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.participation.model.ParticipationRequest;
import ru.practicum.ewm.service.participation.repository.ParticipationRequestRepository;
import ru.practicum.ewm.service.user.model.User;
import ru.practicum.ewm.service.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.constant.EventState.CANCELED;
import static ru.practicum.ewm.service.constant.EventState.PENDING;
import static ru.practicum.ewm.service.constant.ParticipationRequestStatus.CONFIRMED;
import static ru.practicum.ewm.service.constant.ParticipationRequestStatus.REJECTED;
import static ru.practicum.ewm.service.event.mapper.EventMapper.EVENT_MAPPER;
import static ru.practicum.ewm.service.event.mapper.LocationMapper.LOCATION_MAPPER;
import static ru.practicum.ewm.service.participation.mapper.ParticipationRequestMapper.REQUEST_MAPPER;

@Service
@RequiredArgsConstructor
public class EventPrivateServiceImpl implements EventPrivateService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    @Transactional
    @Override
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        if (LocalDateTime.now().plusHours(2).isAfter(newEventDto.getEventTimestamp())) {
            throw new ConflictException("The date and time for which the event is scheduled cannot be earlier than " +
                    "two hours from the current moment.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id=" + userId + " not found."));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category id=" + newEventDto.getCategory() + " not found."));
        Location location = getLocation(newEventDto.getLocation());
        Event event = EVENT_MAPPER.fromDto(newEventDto, category, location);

        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        event.setPaid(newEventDto.getPaid() != null && newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit() == null ? 0 : newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration() == null || newEventDto.getRequestModeration());
        event.setState(PENDING);
        return EVENT_MAPPER.toFullDto(eventRepository.save(event));
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getAllPrivate(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        return events.stream()
                .map(EVENT_MAPPER::toShortDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getByIdPrivate(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event id=" + eventId + " not found."));
        if (!userId.equals(event.getInitiator().getId())) {
            throw new NotFoundException("Event id=" + eventId + " not found");
        }
        return EVENT_MAPPER.toFullDto(event);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getParticipationRequests(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User id=" + userId + " not found."));
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event id=" + eventId + " not found."));
        return participationRequestRepository.findAllByEventId(eventId).stream()
                .map(REQUEST_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto updatePrivate(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event id=" + eventId + " not found."));
        if (!userId.equals(event.getInitiator().getId())) {
            throw new NotFoundException("Event id=" + eventId + " not found");
        }
        if (updateEventUserRequest.getEventTimestamp() != null
                && LocalDateTime.now().plusHours(2).isAfter(updateEventUserRequest.getEventTimestamp())) {
            throw new ConflictException("The date and time for which the event is scheduled cannot be earlier than " +
                    "two hours from the current moment.");
        }
        if (!(event.getState().equals(CANCELED) ||
                event.getState().equals(PENDING))) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }
        if (updateEventUserRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventUserRequest.getCategory()).orElseThrow(
                    () -> new NotFoundException("Category id=" + updateEventUserRequest.getCategory() + " not found.")));
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLocation(getLocation(updateEventUserRequest.getLocation()));
        }

        Optional.ofNullable(updateEventUserRequest.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(updateEventUserRequest.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(updateEventUserRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updateEventUserRequest.getEventTimestamp()).ifPresent(event::setEventDate);
        Optional.ofNullable(updateEventUserRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(updateEventUserRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updateEventUserRequest.getRequestModeration()).ifPresent(event::setRequestModeration);

        if (updateEventUserRequest.getStateAction() != null) {
            switch (updateEventUserRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }
        return EVENT_MAPPER.toFullDto(eventRepository.save(event));
    }

    @Override
    public EventRequestStatusUpdateResult updateParticipationRequests(
            Long userId, Long eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User id=" + userId + " not found.");
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event id=" + eventId + " not found."));
        if (BooleanUtils.isFalse(event.getRequestModeration()) || event.getParticipantLimit() == 0) {
            throw new ConflictException("Event participant limit is 0 or request moderation is disabled.");
        }

        long confirmedReq = participationRequestRepository.countByEventIdAndStatus(eventId, CONFIRMED);

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= confirmedReq) {
            throw new ConflictException("The number of requests for participation has exceeded the limit.");
        }

        List<ParticipationRequest> requestList = participationRequestRepository
                .findAllByIdIn(eventRequestStatusUpdateRequest.getRequestIds());

        List<Long> notFoundIds = eventRequestStatusUpdateRequest.getRequestIds().stream()
                .filter(requestId -> requestList.stream().noneMatch(request -> request.getId().equals(requestId)))
                .collect(Collectors.toList());

        if (!notFoundIds.isEmpty()) {
            throw new NotFoundException("Participation request ids=" + notFoundIds + " not found");
        }

        EventRequestStatusUpdateResult result = EventRequestStatusUpdateResult.builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>())
                .build();

        for (ParticipationRequest request : requestList) {
            if (!request.getStatus().equals(ParticipationRequestStatus.PENDING)) {
                throw new ConflictException("Unable to accept/reject request that not is in pending state.");
            }
            if (!request.getEvent().getId().equals(eventId)) {
                result.getRejectedRequests().add(REQUEST_MAPPER.toDto(request));
                continue;
            }
            switch (eventRequestStatusUpdateRequest.getStatus()) {
                case CONFIRMED:
                    if (confirmedReq < event.getParticipantLimit()) {
                        request.setStatus(CONFIRMED);
                        confirmedReq++;
                        result.getConfirmedRequests().add(REQUEST_MAPPER.toDto(request));
                    } else {
                        request.setStatus(REJECTED);
                        result.getRejectedRequests().add(REQUEST_MAPPER.toDto(request));
                        throw new ConflictException("Participant limit has reached.");
                    }
                    break;
                case REJECTED:
                    request.setStatus(REJECTED);
                    result.getRejectedRequests().add(REQUEST_MAPPER.toDto(request));
                    break;
            }
        }
        participationRequestRepository.saveAll(requestList);
        return result;
    }

    private Location getLocation(LocationDto locationDto) {
        Location location = locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon());
        return location != null ? location : locationRepository.save(LOCATION_MAPPER.fromDto(locationDto));
    }
}
