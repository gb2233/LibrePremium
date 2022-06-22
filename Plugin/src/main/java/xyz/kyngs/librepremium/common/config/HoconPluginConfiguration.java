package xyz.kyngs.librepremium.common.config;

import xyz.kyngs.librepremium.api.LibrePremiumPlugin;
import xyz.kyngs.librepremium.api.configuration.CorruptedConfigurationException;
import xyz.kyngs.librepremium.api.configuration.NewUUIDCreator;
import xyz.kyngs.librepremium.api.configuration.PluginConfiguration;
import xyz.kyngs.librepremium.common.config.key.ConfigurationKey;
import xyz.kyngs.librepremium.common.config.migrate.config.FirstConfigurationMigrator;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static xyz.kyngs.librepremium.common.config.DefaultConfiguration.*;

public class HoconPluginConfiguration implements PluginConfiguration {

    private ConfigurateHelper helper;

    @Override
    public boolean reload(LibrePremiumPlugin plugin) throws IOException, CorruptedConfigurationException {
        var adept = new ConfigurateConfiguration(
                plugin.getDataFolder(),
                "config.conf",
                DefaultConfiguration.class,
                """
                          !!THIS FILE IS WRITTEN IN THE HOCON FORMAT!!
                          The hocon format is very similar to JSON, but it has some extra features.
                          You can find more information about the format on the sponge wiki:
                          https://docs.spongepowered.org/stable/en/server/getting-started/configuration/hocon.html
                          ----------------------------------------------------------------------------------------
                          LibrePremium Configuration
                          ----------------------------------------------------------------------------------------
                          This is the configuration file for LibrePremium.
                          You can find more information about LibrePremium on the github page:
                          https://github.com/kyngs/LibrePremium
                        """,
                new FirstConfigurationMigrator()
        );

        var helperAdept = adept.getHelper();

        if (!adept.isNewlyCreated() && plugin.getCryptoProvider(helperAdept.get(DEFAULT_CRYPTO_PROVIDER)) == null) {
            throw new CorruptedConfigurationException("Crypto provider not found");
        }

        helper = helperAdept;

        return adept.isNewlyCreated();
    }

    @Override
    public List<String> getAllowedCommandsWhileUnauthorized() {
        return get(ALLOWED_COMMANDS_WHILE_UNAUTHORIZED);
    }
    
    @Override
    public String getDatabasePassword() {
        return get(DATABASE_PASSWORD);
    }

    @Override
    public String getDatabaseUser() {
        return get(DATABASE_USER);
    }

    @Override
    public String getDatabaseHost() {
        return get(DATABASE_HOST);
    }

    @Override
    public String getDatabaseName() {
        return get(DATABASE_NAME);
    }

    @Override
    public Collection<String> getPassThrough() {
        return get(PASS_THROUGH);
    }

    @Override
    public List<String> getLimbo() {
        return get(LIMBO);
    }

    @Override
    public int getDatabasePort() {
        return get(DATABASE_PORT);
    }

    @Override
    public String getDefaultCryptoProvider() {
        return get(DEFAULT_CRYPTO_PROVIDER);
    }

    @Override
    public boolean kickOnWrongPassword() {
        return get(KICK_ON_WRONG_PASSWORD);
    }

    @Override
    public boolean migrationOnNextStartup() {
        return get(MIGRATION_ON_NEXT_STARTUP);
    }

    @Override
    public String getMigrationType() {
        return get(MIGRATION_TYPE);
    }

    @Override
    public String getMigrationOldDatabaseHost() {
        return get(MIGRATION_OLD_DATABASE_HOST);
    }

    @Override
    public int getMigrationOldDatabasePort() {
        return get(MIGRATION_OLD_DATABASE_PORT);
    }

    @Override
    public String getMigrationOldDatabaseUser() {
        return get(MIGRATION_OLD_DATABASE_USER);
    }

    @Override
    public String getMigrationOldDatabasePassword() {
        return get(MIGRATION_OLD_DATABASE_PASSWORD);
    }

    @Override
    public String getMigrationOldDatabaseName() {
        return get(MIGRATION_OLD_DATABASE_NAME);
    }

    @Override
    public String getMigrationOldDatabaseTable() {
        return get(MIGRATION_OLD_DATABASE_TABLE);
    }

    @Override
    public NewUUIDCreator getNewUUIDCreator() {
        var name = get(NEW_UUID_CREATOR);

        try {
            return NewUUIDCreator.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NewUUIDCreator.RANDOM;
        }
    }

    @Override
    public boolean useTitles() {
        return get(USE_TITLES);
    }

    @Override
    public boolean autoRegister() {
        return get(AUTO_REGISTER);
    }

    @Override
    public int milliSecondsToRefreshNotification() {
        return get(MILLISECONDS_TO_REFRESH_NOTIFICATION);
    }

    @Override
    public int secondsToAuthorize() {
        return get(SECONDS_TO_AUTHORIZE);
    }

    @Override
    public boolean totpEnabled() {
        return get(TOTP_ENABLED);
    }

    @Override
    public String getTotpLabel() {
        return get(TOTP_LABEL);
    }

    @Override
    public int minimumPasswordLength() {
        return get(MINIMUM_PASSWORD_LENGTH);
    }

    @Override
    public int maximumPasswordLength() {
        return get(MAXIMUM_PASSWORD_LENGTH);
    }
    @Override
    public int maxRegPerIP() {
        return get(MAX_REG_PER_IP);
    }
    @Override
    public boolean tempbanEnabled() {
        return get(TEMPBAN_ENABLED);
    }
    @Override
    public String getTempbanCommand() {
        return get(TEMPBAN_COMMAND);
    }
    @Override
    public String getTempbanLength() {
        return get(TEMPBAN_LENGTH);
    }

    @Override
    public int getTempbanMaxTries() {
        return get(TEMPBAN_MAXTRIES);
    }

    @Override
    public int getTempbanCounterReset() {
        return get(TEMPBAN_COUNTER_RESET);
    }

    public <T> T get(ConfigurationKey<T> key) {
        return helper.get(key);
    }
}
