package ru.practicum.exploreWithMe.auxiliary_objects;
public enum StatusOfEvent {
    PUBLISHED, // This status starts after positive Moderator's check
    PENDING, // This status became immediately after initiator created event
    CANCELED // This status happen in two case, 1. Initiator rejected it while event has status WAITING,
    // 2. Moderator decided that this event cannot be PUBLISHED
}
