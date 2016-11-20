package io.neocore.api.player.group;

import java.util.Date;

public interface GroupMembership {
	
	/**
	 * Sets the group that this membership is of.
	 * 
	 * @param group The group.
	 */
	public void setGroup(Group group);
	
	/**
	 * @return The group this membership is of.
	 */
	public Group getGroup();
	
	/**
	 * Sets the date that this membership goes into effect.
	 * 
	 * @param date The date of start.
	 */
	public void setStartDate(Date date);
	
	/**
	 * @return The start date of the membership.
	 */
	public Date getStartDate();
	
	/**
	 * Sets the date that this membership should no longer be valid.
	 * 
	 * @param date The end date of the membership.
	 */
	public void setEndDate(Date date);
	
	/**
	 * @return The end date of this membership.
	 */
	public Date getEndDate();
	
	/**
	 * @return If this membership is currently in effect.
	 */
	public default boolean isCurrentlyValid() {
		
		Date now = new Date();
		return (this.getStartDate() == null || this.getStartDate().before(now)) && (this.getEndDate() == null || this.getEndDate().after(now));
		
	}
	
}
