package io.neocore.bukkit.services.permissions;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.permissions.PermissionAttachment;

import io.neocore.api.host.permissions.PermissionCollection;

public class AttachmentWrapperCollection implements PermissionCollection {
	
	private PermissionAttachment attachment;
	private Set<String> tags = new HashSet<>();
	
	public AttachmentWrapperCollection(PermissionAttachment atta) {
		this.attachment = atta;
	}
	
	public PermissionAttachment getAttachment() {
		return this.attachment;
	}
	
	@Override
	public boolean isPermissionSet(String perm) {
		return this.attachment.getPermissible().isPermissionSet(perm); // Not strictly speaking compliant, but close enough.
	}

	@Override
	public void setPermission(String perm, boolean value) {
		this.attachment.setPermission(perm, value);
	}

	@Override
	public void unsetPermission(String perm) {
		this.attachment.unsetPermission(perm);
	}

	@Override
	public void addTag(String tag) {
		this.tags.add(tag);
	}

	@Override
	public boolean hasTag(String tag) {
		return this.tags.contains(tag);
	}

	@Override
	public Set<String> getTags() {
		return new HashSet<>(this.tags);
	}
	
}
