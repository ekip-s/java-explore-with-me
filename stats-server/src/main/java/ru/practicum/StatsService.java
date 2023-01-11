package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StatsService {

    private final StatsRepository statsRepository;

    @Autowired
    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Transactional
    public StatsNode addStat(StatsNode statsNode) {
        statsNode.setTimeStamp(LocalDateTime.now());

        return statsRepository.save(statsNode);
    }

    public List<StatsNodeDTO> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if(uris == null) {
            if(unique) {
                return statsRepository.findAllUnique(start, end);
            }  else{
                return statsRepository.findAll(start, end);
            }
        } else {
            if(unique) {
                return statsRepository.findAllByUrisUnique(start, end, uris);
            } else {
                return statsRepository.findAllByUris(start, end, uris);
            }
        }
    }
}
