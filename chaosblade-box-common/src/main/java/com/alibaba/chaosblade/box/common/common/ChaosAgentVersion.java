package com.alibaba.chaosblade.box.common.common;

/**
 * @author haibin
 *
 *
 */
public class ChaosAgentVersion implements Comparable<ChaosAgentVersion>, java.io.Serializable {

    private final static ChaosAgentVersion UNKNOWN_VERSION = new ChaosAgentVersion(0, 0, 0, 0);

    protected final int _majorVersion;

    protected final int _minorVersion;

    protected final int _secondMinorVersion;

    protected final int _thirdMinorVersion;

    private int MIN_VERSION_LENGTH = 3;

    public ChaosAgentVersion(int major, int minor, int secondMinor, int thirdMinorVersion) {
        this._majorVersion = major;
        this._minorVersion = minor;
        this._secondMinorVersion = secondMinor;
        this._thirdMinorVersion = thirdMinorVersion;
    }

    public ChaosAgentVersion(String versionString) {
        String[] versionStr = versionString.split("\\.");
        if (versionStr.length < MIN_VERSION_LENGTH) {
            throw new IllegalArgumentException("illegal version");
        }
        _majorVersion = Integer.valueOf(versionStr[0]);
        _minorVersion = Integer.valueOf(versionStr[1]);
        _secondMinorVersion = Integer.valueOf(versionStr[2]);
        if (versionStr.length > MIN_VERSION_LENGTH) {
            _thirdMinorVersion = Integer.valueOf(versionStr[3]);
        } else {
            _thirdMinorVersion = 0;
        }
    }

    public String toFullString() {
        return _majorVersion + '.' + _minorVersion + '.' + _secondMinorVersion + "." + _thirdMinorVersion + toString();
    }

    @Override
    public int compareTo(ChaosAgentVersion o) {
        if (o == this) { return 0; }
        int diff = _majorVersion - o._majorVersion;
        if (diff == 0) {
            diff = _minorVersion - o._minorVersion;
            if (diff == 0) {
                diff = _secondMinorVersion - o._secondMinorVersion;
                if (diff == 0) {
                    diff = _thirdMinorVersion - o._thirdMinorVersion;

                }
            }
        }
        return diff;
    }

    public static void main(String[] args) {
        ChaosAgentVersion chaosAgentVersion = new ChaosAgentVersion("1.4.3.0");
        ChaosAgentVersion chaosAgentVersion2 = new ChaosAgentVersion("1.4.3");
        System.out.println(chaosAgentVersion.compareTo(chaosAgentVersion2));
    }
}
