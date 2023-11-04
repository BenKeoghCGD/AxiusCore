package uk.co.benkeoghcgd.api.AxiusCore.API.PluginData;

import uk.co.benkeoghcgd.api.AxiusCore.API.Enums.UpdaterMethod;

@SuppressWarnings("unused")
public class PublicPluginData {
    private boolean canRegister, isPublicResource;
    private String separator;
    private UpdaterMethod method;

    // METHOD: SPIGOT
    private int spigot_PluginID = Integer.MAX_VALUE;

    // METHOD: MAVEN
    private String maven_Repository;

    /**
     * Set PublicPluginData defaults
     */
    public PublicPluginData() {
        canRegister = true;
        isPublicResource = false;
        separator = null;
        maven_Repository = null;
    }

    /**
     * Retrieve current maven repository URL
     *
     * @return maven repository URL as string
     */
    public String getMavenRepository() { return maven_Repository; }

    /**
     * Set new repository URL
     *
     * @param newRepository repository URL
     */
    public void setMavenRepository(String newRepository) { maven_Repository = newRepository; }

    /**
     * Retrieve current spigot resource ID
     *
     * @return current spigot resource id
     */
    public int getSpigotID() { return spigot_PluginID; }

    /**
     * set new spigot plugin ID
     *
     * @param newId new spigot resource id
     */
    public void setSpigotID(int newId) {spigot_PluginID = newId;}

    /**
     * Retrieve the method by which AxiusCore attempts to auto-update your plugin
     *
     * @return UpdaterMethod object
     */
    public UpdaterMethod getUpdaterMethod() { return method; }

    /**
     * Set the updater method
     *
     * @param newMethod new updater method
     */
    public void setUpdaterMethod(UpdaterMethod newMethod) { method = newMethod; }

    /**
     * Retrieve register status
     *
     * @return register status
     */
    public boolean getRegisterStatus() { return canRegister; }

    /**
     * Set register status
     *
     * @param newStatus new Register Status for AxiusPlugin
     */
    public void setRegisterStatus(boolean newStatus) { canRegister = newStatus; }

    /**
     * Retrieve plugin public status
     *
     * @return current plugin public status
     */
    public boolean getPublicStatus() { return isPublicResource; }

    /**
     * set plugin public status
     *
     * @param value The new value to be set
     */
    public void setPublicStatus(boolean value) { isPublicResource = value; }

    /**
     * Retrieve plugin version separator
     * i.e. 1.0.0 -> plugin separator is '.'
     *
     * @return current plugin separator
     */
    public String getVersionSeparator() { return separator; }

    /**
     * set plugin version separator
     * i.e. 1.0.0 -> plugin separator is '.'
     *
     * @param newSeparator new plugin version separator
     */
    public void setVersionSeparator(String newSeparator) { separator = newSeparator; }

    @Deprecated(forRemoval = true, since = "1.3")
    public void setSpigotResourceID(int spigotResourceID) {
    }

    @Deprecated(forRemoval = true, since = "1.3")
    public void setVersionFormat(Object versionFormat) {
    }
}
