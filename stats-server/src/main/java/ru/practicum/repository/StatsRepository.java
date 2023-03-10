package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.StatsNode;
import ru.practicum.model.StatsNodeDTO;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<StatsNode, Long> {

    @Query("SELECT new ru.practicum.model.StatsNodeDTO(sn.app, sn.uri, COUNT(sn.id))" +
            "FROM StatsNode AS sn WHERE sn.timeStamp BETWEEN :start AND :end " +
            "GROUP BY sn.app, sn.uri ")
    List<StatsNodeDTO> findAll(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.model.StatsNodeDTO(sn.app, sn.uri, COUNT(sn.id))" +
            "FROM StatsNode AS sn WHERE sn.timeStamp BETWEEN :start AND :end " +
            "GROUP BY sn.app, sn.uri, sn.ip")
    List<StatsNodeDTO> findAllUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.model.StatsNodeDTO(sn.app, sn.uri, COUNT(sn.id))" +
            "FROM StatsNode AS sn WHERE sn.uri IN :uris AND sn.timeStamp BETWEEN :start AND :end " +
            "GROUP BY sn.app, sn.uri ")
    List<StatsNodeDTO> findAllByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.model.StatsNodeDTO(sn.app, sn.uri, COUNT(sn.id))" +
            "FROM StatsNode AS sn WHERE sn.uri IN :uris AND sn.timeStamp BETWEEN :start AND :end " +
            "GROUP BY sn.app, sn.uri, sn.ip")
    List<StatsNodeDTO> findAllByUrisUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}


