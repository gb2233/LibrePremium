package xyz.kyngs.librepremium.api.configuration;

import xyz.kyngs.librepremium.api.LibrePremiumPlugin;

import java.io.IOException;
import java.util.Collection;

/**
 * This interface manages the plugin configuration.
 *
 * @author kyngs
 */
public interface PluginConfiguration {

    /**
     * Reloads the configuration.
     * @param plugin The plugin.
     * @return True if new configuration was generated, false otherwise.
     * @throws IOException If an I/O error occurs.
     * @throws CorruptedConfigurationException If the configuration is corrupted.
     */
    boolean reload(LibrePremiumPlugin<?, ?> plugin) throws IOException, CorruptedConfigurationException;

    Collection<String> getAllowedCommandsWhileUnauthorized();

    String getDatabasePassword();

    String getDatabaseUser();

    String getDatabaseHost();

    String getDatabaseName();

    Collection<String> getPassThrough();

    Collection<String> getLimbo();

    int getDatabasePort();

    String getDefaultCryptoProvider();

    boolean kickOnWrongPassword();

    boolean migrationOnNextStartup();

    String getMigrationType();

    String getMigrationOldDatabaseHost();

    int getMigrationOldDatabasePort();

    String getMigrationOldDatabaseUser();

    String getMigrationOldDatabasePassword();

    String getMigrationOldDatabaseName();

    String getMigrationOldDatabaseTable();

    NewUUIDCreator getNewUUIDCreator();

    boolean useTitles();

    boolean autoRegister();

    int milliSecondsToRefreshNotification();

    int secondsToAuthorize();

    boolean totpEnabled();

    String getTotpLabel();

    int minimumPasswordLength();

    int maximumPasswordLength();

    int maxRegPerIP();

    boolean tempbanEnabled();

    String getTempbanCommand();

    String getTempbanLength();

    int getTempbanMaxTries();

    int getTempbanCounterReset();
}
