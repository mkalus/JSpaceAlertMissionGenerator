package de.beimax.spacealert.mission;

public class ThreatGroup {
	private Threat internal;
	private Threat external;
	
	public ThreatGroup() {
	}

	public Threat getInternal() {
		return internal;
	}

	public Threat getExternal() {
		return external;
	}
	
	public boolean hasInternal() {
		return internal != null;
	}
	
	public boolean hasExternal() {
		return external != null;
	}
	
	public boolean addInternal(Threat i) {
		if (internal == null) {
			internal = i;
			return true;
		}
		return false;
	}
	
	public boolean addExternal(Threat e) {
		if (external == null) {
			external = e;
			return true;
		}
		return false;
	}

	public boolean setExternal(Threat e) {
		external = e;
		return true;
	}
	
	public boolean setInternal(Threat e) {
		internal = e;
		return true;
	}

	public Threat removeInternal() {
		Threat ret = internal;
		internal = null;
		return ret;
	}
	
	public Threat removeExternal() {
		Threat ret = external;
		external = null;
		return ret;
	}
}
