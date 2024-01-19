package vladislavmaltsev.terranotabot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vladislavmaltsev.terranotabot.enity.MapHeights;

public interface MapHeightsRepository extends MongoRepository<MapHeights, String> {

}
