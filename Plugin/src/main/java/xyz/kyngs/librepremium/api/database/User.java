package xyz.kyngs.librepremium.api.database;

import xyz.kyngs.librepremium.api.crypto.HashedPassword;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

/**
 * A user in the database.
 *
 * @author kyngs
 */
public class User {

    private final UUID uuid;
    private UUID premiumUUID;
    private HashedPassword hashedPassword;
    private String lastNickname;
    private Timestamp joinDate;
    private Timestamp lastSeen;
    private String secret;
    private String email;
    private String ip;

    public User(UUID uuid, UUID premiumUUID, HashedPassword hashedPassword, String lastNickname, Timestamp joinDate, Timestamp lastSeen, String secret, String email, String ip) {
        this.uuid = uuid;
        this.premiumUUID = premiumUUID;
        this.hashedPassword = hashedPassword;
        this.lastNickname = lastNickname;
        this.joinDate = joinDate;
        this.lastSeen = lastSeen;
        this.secret = secret;
        this.email = email;
        this.ip = ip;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }

    public Timestamp getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Timestamp lastSeen) {
        this.lastSeen = lastSeen;
    }

    public HashedPassword getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(HashedPassword hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getPremiumUUID() {
        return premiumUUID;
    }

    public void setPremiumUUID(UUID premiumUUID) {
        this.premiumUUID = premiumUUID;
    }

    public String getLastNickname() {
        return lastNickname;
    }

    public void setLastNickname(String lastNickname) {
        this.lastNickname = lastNickname;
    }

    public boolean isRegistered() {
        return hashedPassword != null;
    }

    public boolean autoLoginEnabled() {
        return premiumUUID != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uuid.equals(user.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    public String getSecret() {
        return secret;
    }
    public String getIP() {
        return ip;
    }
    public String getEmail() {
        return email;
    }
    public void setIP(String ip) {
        this.ip = ip;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
