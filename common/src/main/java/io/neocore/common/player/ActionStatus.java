package io.neocore.common.player;

public enum ActionStatus {

	/** Everything went as indended. */
	SUCCESS,

	/** Something bad happened so it couldn't be completed. */
	FAILURE,

	/** Everything was going as expected but we had to stop for some reason. */
	ABORTED,

	/** We haven't finished it yet, apparently. */
	IN_PROGRESS,

	/** We don't know what is going on. :( */
	UNKNOWN;

}
