package entelect.training.spring.booking.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfiguration {
//    @Autowired
//    CloseableHttpClient httpClient;

    @Bean
    public CloseableHttpClient httpClient(){
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient httpClient = builder.build();
        return httpClient;
    }

    @Bean
    public RestTemplate restTemplate(CloseableHttpClient httpClient) {

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory(httpClient));
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("admin","is_a_lie"));
        return restTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory(CloseableHttpClient httpClient) {

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
    }
}
