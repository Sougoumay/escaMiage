package m2.miage.interop.serviceauthentification.dao;

import m2.miage.interop.serviceauthentification.modele.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByCodeAndEmail(long code, String email);
}
