package theChillys.chillys_radio.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfiguration {

    @Bean
    public ModelMapper modelMapper(){
      return new ModelMapper();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}


