package io.neocore.api.player.group;

import java.util.ArrayList;
import java.util.List;
import io.neocore.api.host.Context;

/**
 * A placeholder group used when an actual Group reference cannot be provided.
 * 
 * @author treyzania
 */
public class PlaceholderGroup implements Group {

	@Override
	public void setName(String name) {
		
	}

	@Override
	public String getName() {
		return "[placeholder]";
	}

	@Override
	public void setDisplayName(String displayName) {
		
	}

	@Override
	public String getDisplayName() {
		return "[PLACEHOLDER]";
	}

	@Override
	public void setParent(Group parent) {
		
	}

	@Override
	public Group getParent() {
		return null;
	}

	@Override
	public void addFlair(Flair flair) {
		
	}

	@Override
	public boolean removeFlair(Flair flair) {
		return true;
	}

	@Override
	public List<Flair> getFlairs() {
		return new ArrayList<>();
	}

	@Override
	public void setPermission(Context context, String node, boolean state) {
		
	}

	@Override
	public void unsetPermission(Context context, String node) {
		
	}

	@Override
	public List<PermissionEntry> getPermissions() {
		return new ArrayList<>();
	}

	@Override
	public void setPriority(int priority) {
		
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void setRestrictionLevel(int restriction) {
		
	}

	@Override
	public int getRestrictionLevel() {
		return 0;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isGloballyValid() {
		return true;
	}

	@Override
	public void setFlushProcedure(Runnable callback) {
		
	}

	@Override
	public Runnable getFlushProcedure() {
		return null;
	}

	@Override
	public void setSecret(boolean secret) {
		
	}

	@Override
	public boolean isSecret() {
		return false;
	}
	
}
