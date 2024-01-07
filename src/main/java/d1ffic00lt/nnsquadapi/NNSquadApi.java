package d1ffic00lt.nnsquadapi;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;


@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = "d1ffic00lt.nnsquadapi.API")
public class NNSquadApi implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("nnsquad-api");
	public static MinecraftServer server = null;

	@Bean
	public MinecraftServer minecraftServer() {
		ServerLifecycleEvents.SERVER_STARTING.register(server1 -> {
			server = server1;
			LOGGER.info("Commands registered successfully.");
		});
		return server;
	}
	@Bean
	public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
		return factory -> {

			factory.setPort(18577);

            try {
                factory.setAddress(InetAddress.getByName("0.0.0.0"));
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        };
	}
	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTING.register(server1 -> {
			server = server1;
			LOGGER.info("Commands registered successfully.");
		});
		LOGGER.info("MOD STARTED");
		main(null);
	}

	public static void main(String[] args) {
		LOGGER.info("START RUNNING");
		SpringApplication.run(NNSquadApi.class, new String[]{"--server.address=0.0.0.0", "--server.port=18577"});
		LOGGER.info("RUNNING STOPPED");
	}
}