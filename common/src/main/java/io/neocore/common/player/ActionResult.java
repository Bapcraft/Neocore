package io.neocore.common.player;

public class ActionResult {

	private final ActionStatus status;

	public ActionResult(ActionStatus status) {
		this.status = status;
	}

	public ActionStatus getStatus() {
		return this.status;
	}

}
