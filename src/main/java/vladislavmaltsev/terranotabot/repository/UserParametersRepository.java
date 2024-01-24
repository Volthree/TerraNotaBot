package vladislavmaltsev.terranotabot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vladislavmaltsev.terranotabot.enity.UserParameters;
import java.util.Optional;

public interface UserParametersRepository extends JpaRepository<UserParameters, Integer> {

    @Query("select u from UserParameters u " +
            "where u.chatId = :chat " +
            "and u.localDateTime = " +
            "(select MAX(u2.localDateTime) from UserParameters u2)")
    Optional<UserParameters> findByChatIdAndMaxDate(@Param("chat") long chatId);

    Optional<UserParameters> findByMapid(String mapId);
    Optional<UserParameters> findByMapHash(int mapId);
    Optional<UserParameters> findByChatId(long chatId);

}
