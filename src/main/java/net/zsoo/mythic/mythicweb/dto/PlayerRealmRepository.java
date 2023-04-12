package net.zsoo.mythic.mythicweb.dto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRealmRepository extends JpaRepository<PlayerRealm, String> {
    Optional<PlayerRealm> findByRealmSlug(String realmSlug);

    Optional<PlayerRealm> findByRealmName(String realmName);
}
