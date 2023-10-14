package ru.practicum.ewm.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.stats.server.model.EndpointHit;
import ru.practicum.ewm.stats.server.model.ViewStatsProjection;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select e.app as app, e.uri as uri, count(distinct e.ip) as hits " +
            "from EndpointHit e " +
            "where e.timestamp between :start and :end " +
            "and ((:uris) is null or e.uri in :uris) " +
            "group by e.app, e.uri " +
            "order by hits desc")
    List<ViewStatsProjection> findUniqueStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                              @Param("uris") List<String> uris);

    @Query("select e.app as app, e.uri as uri, count(e.ip) as hits " +
            "from EndpointHit e " +
            "where e.timestamp between :start and :end " +
            "and ((:uris) is null or e.uri in :uris) " +
            "group by e.app, e.uri " +
            "order by hits desc")
    List<ViewStatsProjection> findNotUniqueStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                                 @Param("uris") List<String> uris);
}
