package de.bund.zrb.msdosgames.domain;

public final class LicenseNotice {

    private final String licenseUrl;
    private final String rights;
    private final String sourceUrl;

    public LicenseNotice(String licenseUrl, String rights, String sourceUrl) {
        this.licenseUrl = textOrEmpty(licenseUrl);
        this.rights = textOrEmpty(rights);
        this.sourceUrl = textOrEmpty(sourceUrl);
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public String getRights() {
        return rights;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public boolean hasLicenseInformation() {
        return licenseUrl.length() > 0 || rights.length() > 0;
    }

    public String toSignatureSource() {
        return licenseUrl + "\n" + rights + "\n" + sourceUrl;
    }

    private static String textOrEmpty(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }
}
