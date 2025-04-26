package m2.miage.interop.serviceactualite.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class ConsulClientService {

    private static final Logger logger = LoggerFactory.getLogger(ConsulClientService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.cloud.consul.host}")
    private String consulHost;

    @Value("${spring.cloud.consul.port}")
    private String consulPort;

    /**
     * Récupère la clé publique depuis Consul et la convertit en RSAPublicKey.
     */
    public RSAPublicKey getPublicKeyFromConsul() {
        String consulUrl = String.format("http://%s:%s/v1/kv/security/publicKey?raw", consulHost, consulPort);
        try {
            String publicKeyPem = restTemplate.getForObject(consulUrl, String.class);
            logger.info("✅ Clé publique récupérée depuis Consul !");
            return convertPemToRSAPublicKey(publicKeyPem);
        } catch (Exception e) {
            logger.error("❌ Erreur lors de la récupération de la clé publique depuis Consul : {}", e.getMessage());
            throw new RuntimeException("Impossible de récupérer la clé publique !");
        }
    }

    /**
     * Convertit une clé PEM en RSAPublicKey.
     */
    private RSAPublicKey convertPemToRSAPublicKey(String publicKeyPem) {
        try {
            String cleanKey = publicKeyPem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", ""); // Supprime les sauts de ligne et espaces

            byte[] decodedKey = Base64.getDecoder().decode(cleanKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            logger.error("❌ Erreur lors de la conversion de la clé publique en RSAPublicKey : {}", e.getMessage());
            throw new RuntimeException("Erreur de conversion de la clé publique !");
        }
    }
}
