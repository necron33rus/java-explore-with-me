package ru.practicum.ewm.service.compilation.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.service.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;

    @Column(name = "compilation_title", nullable = false, length = 64)
    private String title;

    @Column(name = "compilation_pinned", nullable = false)
    private Boolean pinned;

    @ManyToMany
    @JoinTable(
            name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;
}
