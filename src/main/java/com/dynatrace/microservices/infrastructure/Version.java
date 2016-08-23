package com.dynatrace.microservices.infrastructure;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "version")
@XmlAccessorType(XmlAccessType.FIELD)
public final class Version implements Comparable<Version> {

	@XmlAttribute(name = "major")
	private int major = 0;
	@XmlAttribute(name = "minor")
	private int minor = 0;
	@XmlAttribute(name = "revision")
	private int revision = 0;
	@XmlAttribute(name = "build")
	private int build = 0;
	
	public Version() {
		
	}
	
	public Version(int major, int minor, int revision, int build) {
		this.major = major;
		this.minor = minor;
		this.revision = revision;
		this.build = build;
	}
	
	public int getMajor() {
		return major;
	}
	
	public void setMajor(int major) {
		this.major = major;
	}
	
	public int getMinor() {
		return minor;
	}
	
	public void setMinor(int minor) {
		this.minor = minor;
	}
	
	public int getRevision() {
		return revision;
	}
	
	public void setRevision(int revision) {
		this.revision = revision;
	}
	
	public int getBuild() {
		return build;
	}
	
	public void setBuild(int build) {
		this.build = build;
	}
	
	@Override
	public String toString() {
		return major + "." + minor + "." + revision + "." + build;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + build;
		result = prime * result + major;
		result = prime * result + minor;
		result = prime * result + revision;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Version other = (Version) obj;
		if (build != other.build)
			return false;
		if (major != other.major)
			return false;
		if (minor != other.minor)
			return false;
		if (revision != other.revision)
			return false;
		return true;
	}

	@Override
	public int compareTo(Version o) {
		if (o == null) {
			return 1;
		}
		if (major < o.major) {
			return -1;
		} else if (major > o.major) {
			return 1;
		} else if (minor < o.minor) {
			return -1;
		} else if (minor > o.minor) {
			return 1;
		} else if (revision < o.revision) {
			return -1;
		} else if (revision > o.revision) {
			return 1;
		}
		return 0;
	}
	
	public boolean matchesMajor(Version o) {
		if (o == null) {
			return false;
		}
		return major == o.major;
	}
	
	public boolean includesMajor(Version o) {
		if (o == null) {
			return false;
		}
		return major >= o.major;
	}
	
	public boolean matchesMinor(Version o) {
		if (o == null) {
			return false;
		}
		if (!matchesMajor(o)) {
			return false;
		}
		return minor == o.minor;
	}
	
	public boolean includesMinor(Version o) {
		if (o == null) {
			return false;
		}
		if (!matchesMajor(o)) {
			return false;
		}
		return minor >= o.minor;
	}
	
	public boolean matchesRevision(Version o) {
		if (o == null) {
			return false;
		}
		if (!matchesMinor(o)) {
			return false;
		}
		return revision == o.revision;
	}
	
	public boolean includesRevision(Version o) {
		if (o == null) {
			return false;
		}
		if (!matchesMinor(o)) {
			return false;
		}
		return minor >= o.minor;
	}
	
	
	
}
