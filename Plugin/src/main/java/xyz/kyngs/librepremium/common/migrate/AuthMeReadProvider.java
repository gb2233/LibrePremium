package xyz.kyngs.librepremium.common.migrate;

import xyz.kyngs.easydb.EasyDB;
import xyz.kyngs.easydb.provider.mysql.MySQL;
import xyz.kyngs.librepremium.api.Logger;
import xyz.kyngs.librepremium.api.crypto.HashedPassword;
import xyz.kyngs.librepremium.api.database.User;
import xyz.kyngs.librepremium.common.util.CryptoUtil;
import xyz.kyngs.librepremium.common.util.GeneralUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class AuthMeReadProvider extends MySQLReadProvider {
    public AuthMeReadProvider(EasyDB<MySQL, Connection, SQLException> easyDB, String tableName, Logger logger) {
        super(easyDB, tableName, logger);
    }

    @Override
    public User getByName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User getByUUID(UUID uuid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User getByPremiumUUID(UUID uuid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> getUsersByIP(String ipAddress) {
        return null;
    }

    @Override
    public Collection<User> getAllUsers() {
        return easyDB.runFunctionSync(connection -> {
            var ps = connection.prepareStatement("SELECT * FROM `%s`".formatted(tableName));

            var rs = ps.executeQuery();

            var users = new HashSet<User>();

            while (rs.next()) {
                try {
                    var nickname = rs.getString("realname");
                    var passwordRaw = rs.getString("password");
                    var lastSeen = rs.getObject("lastlogin", Long.class);
                    var firstSeen = rs.getObject("regdate", Long.class);
                    var ip = rs.getString("ip");
                    var email = rs.getString("email");

                    if (nickname == null) continue;

                    HashedPassword password = null;

                    if (passwordRaw != null) {
                        if (passwordRaw.startsWith("$SHA$")) {
                            var split = passwordRaw.split("\\$");

                            var algo = "SHA-256";
                            var salt = split[2];
                            var hash = split[3];

                            password = new HashedPassword(hash, salt, algo);
                        } else if (passwordRaw.startsWith("$2a$")) {
                            password = CryptoUtil.convertFromBCryptRaw(passwordRaw);
                        } else {
                            logger.error("User " + nickname + " has an invalid password hash");
                        }
                    }

                    users.add(
                            new User(
                                    GeneralUtil.getCrackedUUIDFromName(nickname),
                                    null,
                                    password,
                                    nickname,
                                    firstSeen == null ? null : new Timestamp(firstSeen),
                                    lastSeen == null ? null : new Timestamp(lastSeen),
                                    null,email,ip)
                    );

                } catch (Exception e) {
                    logger.error("Failed to read user from AuthMe db, omitting");
                }
            }

            return users;
        });
    }
}
