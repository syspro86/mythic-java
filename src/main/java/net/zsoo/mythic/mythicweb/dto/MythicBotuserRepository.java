package net.zsoo.mythic.mythicweb.dto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MythicBotuserRepository extends JpaRepository<MythicBotuser, String> {

  @Query("""
      SELECT MB
        FROM MythicBotuser MB JOIN MB.players MBP
       WHERE MBP.playerName = :playerName
         AND MBP.playerRealm = :playerRealm
      """)
  List<MythicBotuser> findByPlayerRealmAndPlayerName(String playerRealm, String playerName);
}
