package ru.practicum.ewm.service.participation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.service.constant.ParticipationRequestStatus;
import ru.practicum.ewm.service.participation.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Long countByEventIdAndStatus(Long eventId, ParticipationRequestStatus status);

    List<ParticipationRequest> findAllByRequesterId(Long userId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    List<ParticipationRequest> findAllByIdIn(List<Long> ids);
}
