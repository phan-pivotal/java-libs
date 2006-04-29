/*
 * component:   "openEHR Reference Implementation"
 * description: "Class VersionTreeID"
 * keywords:    "common"
 *
 * author:      ""
 * support:     ""
 * copyright:   ""
 * license:     "See notice at bottom of class"
 *
 * file:        "$URL: http://svn.openehr.org/ref_impl_java/TRUNK/libraries/src/java/org/openehr/rm/support/identification/VersionTreeID.java $"
 * revision:    "$LastChangedRevision: 1 $"
 * last_change: "$LastChangedDate: 2005-10-12 22:20:08 +0100 (Wed, 12 Oct 2005) $"
 */
 

package org.openehr.rm.support.identification;

import org.apache.commons.lang.StringUtils;
import org.openehr.rm.RMObject;

/**
 * Version tree identifier for one version
 * The format of the identifier is:
 * <trunk_version>[.<branch_number>.<branch_version>]
 * 
 * @author Yin Su Lim
 *
 */
public class VersionTreeID extends RMObject {

	/**
	 * Contruct VersionTreeID by string value
	 * 
	 * @param value
     * @throws IllegalArgumentException if value is null or empty
	 */
	public VersionTreeID(String value) {
		if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("empty value");
        }
        loadValue(value);
        this.value = value;
	}

	/**
	 * Contruct VersionTreeID by value of trunkVersion, branchNumber 
	 * and branchVersion
	 * 
	 * @param trunkVersion
	 * @param branchNumber
	 * @param branchVersion
     * @throws IllegalArgumentException if value is null or empty??
	 */
	public VersionTreeID(String trunkVersion, String branchNumber, 
				String branchVersion) {
		if (StringUtils.isEmpty(trunkVersion)) {
            throw new IllegalArgumentException("empty trunk version");
        }
		if (StringUtils.isEmpty(branchVersion) != StringUtils.isEmpty(branchNumber)) {
			throw new IllegalArgumentException("breach of branch_validity");
		}
		if(StringUtils.isNotEmpty(branchNumber)) {
			String value = trunkVersion + "." + branchNumber + "." + branchVersion + "";
			loadValue(value);
			this.value = value;
		} else {
			loadValue(trunkVersion);
			this.value = trunkVersion;
		}	
	}
	
	/**
	 * Contruct VersionTreeID by value of trunkVersion, branchNumber 
	 * and branchVersion. All valid integers must be >= 1. 
	 * Special case branchNumber == 0 && branchVersion == 0 indicates no branch.
	 * 
	 * @param trunkVersion 
	 * @param branchVersion
	 * @param branchNumber
     * @throws IllegalArgumentException 
	 */
	public VersionTreeID(int trunkVersion, int branchNumber, 
				int branchVersion) {
		if (trunkVersion < 1 || branchNumber < 0 || branchNumber < 0) {
            throw new IllegalArgumentException("version number smaller than 0");
        }
		if (branchNumber == 0 || branchVersion == 0) {
			if (branchVersion != branchNumber) {
				throw new IllegalArgumentException("breach of branch_validity");
			}
		}
		if(branchNumber > 0 ) {
			String value = Integer.toString(trunkVersion) + "." + Integer.toString(branchNumber) 
							+  "." + Integer.toString(branchVersion);
			this.value = value;
		} else {
			this.value = Integer.toString(trunkVersion);
		}	
	}
	
	private void loadValue(String value) {
		if (!value.matches(PATTERN)) {
			throw new IllegalArgumentException("wrong format");
		}
		int branch = value.indexOf(".");
		if (branch < 0) { // no branch, just trunk
				this.trunkVersion = value; 			
		} else {
			String[] entries = value.split(".");
			this.trunkVersion = entries[0];
			this.branchNumber = entries[1];
			this.branchVersion = entries[2];
		}
	}

	public String getValue() {
		return value;
	}
	
	/**
	 * Trunk version number
	 *
	 *@return trunkVersion
	 */
	public String trunkVersion() {
		return trunkVersion;
	}
	
	/**
	 * Number of brnach from the trunk point
	 * 
	 *@return
	 */
	public String branchNumber() {
		return branchNumber;
	}
	
	/**
	 * Version of the branch
	 * 
	 *@return branchVersion
	 */
	public String branchVersion() {
		return branchVersion;
	}
	
	/**
	 * True if this version identifier represents a branch
	 *
	 */
	public boolean isBranch() {
		return branchVersion != null;
	}

	/**
	 * True if this version is the first copy in a version tree
	 * i.e. versionTreeId is 1.
	 */
	public boolean isFirst() {
		return trunkVersion.equals("1") && !isBranch();
	}
	
	/**
	 * Return the next logical versionTreeId
	 * i.e. next logical versionTreeId for 1.2.1 is 1.2.2
	 */
	public VersionTreeID next() {
		if (isBranch()) {
			String newBranchVersion = Integer.toString(Integer.valueOf(branchVersion) + 1);
			return new VersionTreeID(trunkVersion, branchNumber, newBranchVersion);
			
		} else {
			
			return new VersionTreeID(Integer.toString(Integer.valueOf(trunkVersion) + 1));
		}
	}
	
    /**
     * Return true if both have same value and
     *
     * @param o The object to which this object is to be compared
     * @return true if equals
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!( o instanceof VersionTreeID )) return false;

        final VersionTreeID vtId = (VersionTreeID) o;

        return value.equals(vtId.value);
    }

    /**
     * Return a hash code value of this objectID
     *
     * @return hash code
     */
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Return value as string presentation of this objectID
     *
     * @return string presentation
     */
    public String toString() {
        return value;
    }

	//POJO start
	VersionTreeID() {
	}

	void setValue(String value) {
		this.value = value;
		loadValue(value);
	}
	
	//POJO end

	private String PATTERN = "(\\d)+(\\.(\\d)+\\.(\\d)+)?";
	
	/* field */
	private String value;
	private String trunkVersion;
	private String branchNumber;
	private String branchVersion;
}