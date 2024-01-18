package vladislavmaltsev.terranotabot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vladislavmaltsev.terranotabot.enity.UserParameters;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserParametersRepository extends JpaRepository<UserParameters, Integer> {

//    @Query("SELECT u FROM UserParameters u " +
//            "where u.localDateTime = " +
//            "(select MAX(u2.localDateTime) from UserParameters u2)")
//    Optional<UserParameters> findByChatIdAndMaxDate(@Param("chat") long chatId);
//
//    List<UserParameters> findAllByChatId(long chatId);

    @Query("select u from UserParameters u " +
            "where u.chatId = :chat " +
            "and u.localDateTime = " +
            "(select MAX(u2.localDateTime) from UserParameters u2)")
    Optional<UserParameters> findByChatIdAndMaxDate(@Param("chat") long chatId);
}
