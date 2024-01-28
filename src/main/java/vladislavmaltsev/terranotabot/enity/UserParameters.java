package vladislavmaltsev.terranotabot.enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_parameters")
public class UserParameters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int userParametersId;
    @Column(name = "updateid")
    private int updateId;
    @Column(name = "messageid")
    private int messageId;
    @Column(name = "chatid")
    private long chatId;
    @Column(name = "mapsize")
    private int mapSize;
    @Column(name = "scale")
    private int scale;
    @Column(name = "heightdifference")
    private int heightDifference;
    @Column(name = "islandsmodifier")
    private int islandsModifier;
    @Column(name = "username")
    private String username;
    @Column(name = "date")
    private LocalDateTime localDateTime;
    @Column(name = "mapid")
    private String mapid;
    @Column(name = "maphash")
    private int mapHash;
    public static UserParameters getDefaultWithUpdate(Update update, int messageId, long chatId){
        return UserParameters.builder()
                .updateId(update.getUpdateId())
                .messageId(messageId)
                .chatId(chatId)
                .heightDifference(4)
                .mapSize(513)
                .scale(1)
                .islandsModifier(10)
                .localDateTime(LocalDateTime.now())
                .username(update.getCallbackQuery().getFrom().getUserName())
                .build();
    }

}
