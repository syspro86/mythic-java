package net.zsoo.mythic.mythicweb.timeline;

public interface TimelineResult {
    int getDungeonId();

    String getDungeonName();

    float getMythicRating();

    int getPeriod();

    int getSeason();

    long getTimestamp();
}
