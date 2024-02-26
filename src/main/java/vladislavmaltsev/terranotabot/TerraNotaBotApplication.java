package vladislavmaltsev.terranotabot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.GenericApplicationContext;
import vladislavmaltsev.terranotabot.service.MapService;

import java.util.Observer;

@SpringBootApplication
public class TerraNotaBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TerraNotaBotApplication.class, args);
    }

}
