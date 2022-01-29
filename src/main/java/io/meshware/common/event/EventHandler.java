package io.meshware.common.event;

import java.util.function.Predicate;

@FunctionalInterface
public interface EventHandler<E extends Event> {

    /**
     * Handle event, require events that cannot be blocked
     *
     * @param event event
     */
    void handle(E event);

    /**
     * Wrapper
     *
     * @param predicate predicate
     * @return event handler
     */
    default EventHandler<E> wrap(final Predicate<E> predicate) {
        return e -> {
            if (predicate == null || predicate.test(e)) {
                handle(e);
            }
        };
    }
}
