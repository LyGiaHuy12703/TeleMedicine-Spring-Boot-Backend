package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.TokenRefresh;

@Repository
public interface RefreshTokenRepository extends JpaRepository<TokenRefresh, String> {
    TokenRefresh findByToken(String token);
    TokenRefresh findByStaffId(String userId);
    TokenRefresh findByRefreshToken(String refreshToken);
    void deleteRefreshTokenByStaffId(String refreshToken);
}
