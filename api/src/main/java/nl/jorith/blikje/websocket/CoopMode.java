package nl.jorith.blikje.websocket;

import org.apache.commons.lang.Validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CoopMode {
    INSTANCE;

    private static Map<String, List<Blikje>> sessions = Collections.synchronizedMap(new HashMap<>());

    public void startNewCoop(String key, List<Blikje> state) {
        Validate.isTrue(!sessions.containsKey(key), "Co-op session with key " + key + " already started");

        sessions.put(key, state);
    }

    public List<Blikje> joinCoop(String key) {
        Validate.isTrue(sessions.containsKey(key), "Unknown co-op session with key " + key);
        return sessions.get(key);
    }

    public void updateSession(String key, String blikjeId, long amount) {
        Validate.isTrue(sessions.containsKey(key), "Unknown co-op session with key " + key);

        sessions.get(key).stream()
                .filter(b -> b.getId().equals(blikjeId))
                .findFirst()
                .ifPresent(b -> b.setAmount(b.getAmount() + amount));


    }
}
