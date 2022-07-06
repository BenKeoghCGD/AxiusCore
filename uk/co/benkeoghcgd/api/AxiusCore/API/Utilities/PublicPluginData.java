package uk.co.benkeoghcgd.api.AxiusCore.API.Utilities;

import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.Enums.VersionFormat;

public class PublicPluginData {
    private boolean isPublicResource, canRegister, isPremiumResource;
    private VersionFormat versionFormat;
    private String seperator;
    private int SpigotResourceID;

    /**
     * Set PublicPluginData defaults
     */
    public PublicPluginData() {
        isPublicResource = false;
        canRegister = true;
        isPublicResource = false;
        versionFormat = VersionFormat.NULL;
    }


    /**
     * Retrieve public status
     *
     * @return public status
     */
    public boolean getPublicStatus() { return isPublicResource; }

    /**
     * Set public status
     *
     * @param newStatus new status for AxiusPlugin
     */
    public void setPublicStatus(boolean newStatus) { isPublicResource = newStatus; }


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
     * Retrieve premium status
     * @return premium status
     */
    public boolean getPremiumStatus() { return isPremiumResource; }

    /**
     * Set premium status
     *
     * @param newStatus new premium status for AxiusPlugin
     */
    public void setPremiumStatus(boolean newStatus) { isPremiumResource = newStatus; }


    /**
     * Retrieve plugin version format
     *
     * @return VersionFormat type
     */
    public VersionFormat getVersionFormat() { return versionFormat; }

    /**
     * Set plugin version format
     * @param newFormat new VersionFormat type
     */
    public void setVersionFormat(VersionFormat newFormat) { versionFormat = newFormat; }


    /**
     * Retrieve plugin version seperator
     * i.e. 1.0.0 -> plugin seperator is '.'
     *
     * @return current plugin seperator
     */
    public String getVersionSeperator() { return seperator; }

    /**
     * set plugin version seperator
     * i.e. 1.0.0 -> plugin seperator is '.'
     *
     * @param newSeperator new plugin version seperator
     */
    public void setVersionSeperator(String newSeperator) { seperator = newSeperator; }


    /**
     * Retrieve Spigot resource ID
     *
     * @return Spigot Resource ID
     */
    public int getSpigotResourceID() { return SpigotResourceID; }

    /**
     * Set Spigot Resource ID
     *
     * @param SpigotPluginID Spigot Resource ID
     */
    public void setSpigotResourceID(int SpigotPluginID) { SpigotResourceID = SpigotPluginID; }
}
