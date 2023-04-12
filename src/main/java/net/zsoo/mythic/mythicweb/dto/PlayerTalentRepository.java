package net.zsoo.mythic.mythicweb.dto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerTalentRepository extends JpaRepository<PlayerTalent, PlayerTalentId> {

    List<PlayerTalent> findByPlayerRealmAndPlayerName(String realm, String name);

}
