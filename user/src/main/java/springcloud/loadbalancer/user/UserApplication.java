package springcloud.loadbalancer.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
@SpringBootApplication
public class UserApplication {

	private static Logger log = LoggerFactory.getLogger(UserApplication.class);

	private final WebClient.Builder loadBalancedWebClientBuilder;
	private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

	public UserApplication(WebClient.Builder webClientBuilder, 
		ReactorLoadBalancerExchangeFilterFunction lbFunction) {
			this.loadBalancedWebClientBuilder = webClientBuilder;
			this.lbFunction = lbFunction;
	}

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@RequestMapping("/hi")
	public Mono<String> hi(@RequestParam(value = "name", defaultValue = "Mary") String name) {
		log.info("received name: " + name);
		return loadBalancedWebClientBuilder.build()
			.get()
			.uri("http://say-hello/greeting")
			.retrieve()
			.bodyToMono(String.class)
			.map(greeting -> String.format("%s, %s!", greeting, name));
	}

	@RequestMapping("/hello")
	public Mono<String> hello(@RequestParam(value = "name", defaultValue = "John") String name) {
		log.info("received name: " + name);
		return WebClient.builder()
			.filter(lbFunction)
			.build()
			.get()
			.uri("http://say-hello/greeting")
			.retrieve()
			.bodyToMono(String.class)
			.map(greeting -> String.format("$s, $s!", greeting, name));
	}

}
