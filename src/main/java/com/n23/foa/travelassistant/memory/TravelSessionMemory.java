package com.n23.foa.travelassistant.memory;

import java.util.Optional;

public interface TravelSessionMemory {

    Optional<TravelSession> get(Object memoryId);

    void save(
            Object memoryId,
            TravelSession session
    );

    void clear(
            Object memoryId
    );
}
