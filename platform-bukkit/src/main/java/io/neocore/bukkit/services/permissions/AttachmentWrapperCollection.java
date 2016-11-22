package io.neocore.bukkit.services.permissions;

import org.bukkit.permissions.PermissionAttachment;

import io.neocore.api.host.permissions.PermissionCollection;

public class AttachmentWrapperCollection implements PermissionCollection {
	
	private PermissionAttachment attachment;
	
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
	
}
