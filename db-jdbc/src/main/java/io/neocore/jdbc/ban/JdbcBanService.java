package io.neocore.jdbc.ban;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.DatabaseService;
import io.neocore.api.database.ban.BanEntry;
import io.neocore.api.database.ban.BanService;
import io.neocore.api.host.Context;
import io.neocore.jdbc.AbstractJdbcService;

public class JdbcBanService extends AbstractJdbcService implements BanService {

	private Set<JdbcBan> cachedBans;

	private Dao<JdbcBan, UUID> banDao;

	public JdbcBanService(ConnectionSource src) {
		super(src);
	}

	@Override
	public void init() {

		NeocoreAPI.getAgent().getPlayerManager().addService(DatabaseService.BAN);

		try {

			// Get the DAO.
			this.banDao = DaoManager.createDao(this.getSource(), JdbcBan.class);

			// Make sure the table exists.
			TableUtils.createTableIfNotExists(this.getSource(), JdbcBan.class);

		} catch (SQLException e) {
			NeocoreAPI.getLogger().log(Level.SEVERE, "Error initializing ban system!", e);
		}

		this.reloadBans();

	}

	@Override
	public List<BanEntry> getBans(UUID uuid) {

		List<BanEntry> relevant = new ArrayList<>();

		for (BanEntry ban : this.cachedBans) {
			if (ban.getPlayerId().equals(uuid))
				relevant.add(ban);
		}

		return relevant;

	}

	@Override
	public synchronized void reloadBans() {

		// Setup.
		List<Context> contexts = NeocoreAPI.getAgent().getHost().getContexts();

		// Build the query.
		// FIXME Better naming for the column queries.
		try {

			QueryBuilder<JdbcBan, UUID> qb = this.banDao.queryBuilder();
			Where<JdbcBan, UUID> preperation = qb.where().isNull("context");
			for (Context c : contexts) {
				preperation.or().eq("context", c.getName());
			}

			List<JdbcBan> queried = preperation.query();
			queried.forEach(b -> b.setFlushProcedure(() -> this.flushBans()));
			this.cachedBans = new HashSet<>(queried);

			NeocoreAPI.getLogger().info("Loaded " + this.cachedBans.size() + " bans.");

		} catch (SQLException e) {
			NeocoreAPI.getLogger().log(Level.SEVERE, "Error loading bans!", e);
		}

	}

	@Override
	public synchronized void flushBans() {

		for (JdbcBan ban : this.cachedBans) {

			try {

				if (ban.isDirty())
					this.banDao.update(ban);
				ban.setDirty(false);

			} catch (SQLException e) {
				NeocoreAPI.getLogger().log(Level.WARNING, "Problem flushing ban!", e);
			}

		}

	}

	@Override
	public void addBan(BanEntry entry) {

		if (entry instanceof JdbcBan) {

			JdbcBan jdbcEntry = (JdbcBan) entry;

			try {

				JdbcBan there = this.banDao.createIfNotExists(jdbcEntry);

				if (there != jdbcEntry) {
					NeocoreAPI.getLogger().warning(
							"Already a ban entry with ID " + there.getBanId() + "! (" + there.getReason() + ")");
				} else {

					jdbcEntry.setDirty(false);
					this.cachedBans.add(jdbcEntry);

				}

			} catch (SQLException e) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Problem inserting ban!", e);
			}

		} else {

			// Copy all the data from this "magical" thing.
			JdbcBan dup = new JdbcBan();
			dup.setPlayerId(entry.getPlayerId());
			dup.setIssuerId(entry.getIssuerId());
			dup.setDateIssued(entry.getDateIssued());
			dup.setStartDate(entry.getStartDate());
			dup.setExpirationDate(entry.getExpirationDate());
			dup.setReason(entry.getReason());
			dup.setContext(entry.getContext());

			// Then pretend it was that in the first place.
			this.addBan(dup);

		}

	}

	@Override
	public BanEntry createNewBan(UUID uuid) {

		JdbcBan ban = new JdbcBan(uuid);
		ban.setFlushProcedure(() -> this.flushBans());
		return ban;

	}

}
