package com.n23.foa.travelassistant.memory;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class InMemoryTravelSessionMemory
        implements TravelSessionMemory{

    private final Map<Object,TravelSession> memory =
            new HashMap<>();

    @Override
    public Optional<TravelSession> get(Object memoryId) {
        return Optional.ofNullable(
                memory.get(memoryId)
        );
    }

    @Override
    public void save(Object memoryId, TravelSession session) {
        memory.put(memoryId,session);
    }

    @Override
    public void clear(Object memoryId) {

        memory.remove(memoryId);

    }
}
