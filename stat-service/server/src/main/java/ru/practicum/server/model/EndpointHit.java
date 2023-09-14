package ru.practicum.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class EndpointHit {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app", length = 100)
    private String app;

    @Column(name = "uri", length = 512)
    private String uri;

    @Column(name = "ip", length = 50)
    private String ip;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
