package m2.miage.interop.serviceauthentification.service;

import jakarta.annotation.PostConstruct;
import m2.miage.interop.serviceauthentification.config.KeyGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Base64;

@Service
public class ConsulService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final KeyGeneratorService keyGeneratorService;
    private static final Logger logger = LoggerFactory.getLogger(ConsulService.class);

    @Value("${spring.cloud.consul.host}")
    private String consulHost;

    @Value("${spring.cloud.consul.port}")
    private String consulPort;

    public ConsulService(KeyGeneratorService keyGeneratorService) {
        this.keyGeneratorService = keyGeneratorService;
    }

    @PostConstruct
    public void registerPublicKey() {
        String publicKeyPem = getPublicKeyPem();
        String consulUrl = String.format("http://%s:%s/v1/kv/security/publicKey", consulHost, consulPort);

        try {
            restTemplate.put(consulUrl, publicKeyPem);
            logger.info("✅ Clé publique envoyée à Consul !");
        } catch (HttpClientErrorException e) {
            logger.error("❌ Erreur lors de l'envoi de la clé publique à Consul : {}", e.getMessage());
        } catch (Exception e) {
            logger.error("❌ Une erreur inattendue est survenue : {}", e.getMessage());
        }
    }

    private String getPublicKeyPem() {
        return "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getEncoder().encodeToString(keyGeneratorService.getPublicKey().getEncoded()) +
                "\n-----END PUBLIC KEY-----";
    }
}
