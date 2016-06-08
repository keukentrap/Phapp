package haakjeopenen.phapp.util;

/**
 * Interface for object to be notified when an API response update or finishes
 */
public interface Notify {
    void notifyUpdate();

    void notifyFinished();
}
