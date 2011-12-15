package net.citizensnpcs.dpromoter;

import java.util.HashMap;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.dpromoter.data.DMessageStore;
import net.citizensnpcs.dpromoter.data.DNPCjobLookup;
import net.citizensnpcs.dpromoter.data.DNPCjobLookup.DJobInfoValue;
import net.citizensnpcs.dpromoter.data.DStatesStorage;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DPromoter extends CitizensNPC {
	private static DNPCjobLookup npclookup = DNPCjobLookup.INSTANCE;
	private static DStatesStorage stateslookup = DStatesStorage.INSTANCE;
	private static DMessageStore msglookup = DMessageStore.INSTANCE;
	private static Permission permission = Bukkit.getServer()
			.getServicesManager()
			.getRegistration(net.milkbowl.vault.permission.Permission.class)
			.getProvider();
	private static Economy economy = Bukkit.getServer().getServicesManager()
			.getRegistration(net.milkbowl.vault.economy.Economy.class)
			.getProvider();
	private static Material basicItem = Material.SPONGE;
	// events
	public static final int MEMBER = 31;
	public static final int MEMBER_INTRO = 30;
	public static final int TRIAL_NEED = 21;
	public static final int TRIAL_INTRO = 20;
	public static final int USER_ASK = 11;
	public static final int USER_INTRO = 10;
	public static final int NONE = 0;
	// mouse
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	// settings
	public static final int CONFIRM = RIGHT;
	public static final int DENY = LEFT;

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
		action(DPromoter.LEFT, player, npc);
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		action(DPromoter.RIGHT, player, npc);
		// DJobInfoValue v = npclookup.getJobInfoFromNpc(npc);
		// if (v != null) {
		// player.sendMessage("name: " + v.getName() + " rank: "
		// + v.getNeededRank() + " sp: " + v.getNeededSkillpoints()
		// + " bi: " + v.getNeededBasicItems() + " €: "
		// + v.getNeededMoney());
		// } else {
		// player.sendMessage("Bei mir gibt es keinen Job");
		// }
	}

	public void action(int mouse, Player player, HumanNPC npc) {
		// check if NPC has a job for promoting
		int jobid = npclookup.getJobOfNpc(npc);
		if (jobid > 0) {
			// get State + level from DB (if missing insert into db)
			int[] state = stateslookup.getStatOfNpcForPlayer(jobid, player);
			switch (state[0]) {
			case DPromoter.USER_INTRO:
				player.sendMessage(msglookup.getMessageForEventAnJobAndLevel(
						jobid, state[0], state[1]));
				state[0] = DPromoter.TRIAL_INTRO;
				stateslookup.setStateAndLevel(jobid, player, state);
				break;

			case DPromoter.TRIAL_INTRO:
				if (mouse == DPromoter.CONFIRM) {
					DJobInfoValue jobInfo = npclookup.getJobInfoFromNpc(jobid);
					player.sendMessage(msglookup
							.getMessageForEventAnJobAndLevel(jobid, state[0],
									state[1], generateConfig(jobInfo)));
					state[0] = DPromoter.TRIAL_NEED;
					stateslookup.setStateAndLevel(jobid, player, state);
				}
				break;

			case DPromoter.TRIAL_NEED:
				DJobInfoValue jobInfo = npclookup.getJobInfoFromNpc(jobid);
				System.out.println(jobInfo.toString());
				if (hasRequirements(player, jobInfo)) {
					removeRequirements(player, jobInfo);
					player.sendMessage(msglookup
							.getMessageForEventAnJobAndLevel(jobid,
									DPromoter.MEMBER_INTRO, state[1]));
					state[0] = DPromoter.MEMBER;
					permission.playerAddGroup(player, jobInfo.getPex());
					stateslookup.setStateAndLevel(jobid, player, state);
					player.sendMessage(msglookup
							.getMessageForEventAnJobAndLevel(jobid, state[0],
									state[1]));
				} else {
					player.sendMessage(msglookup
							.getMessageForEventAnJobAndLevel(jobid, state[0],
									state[1], generateConfig(jobInfo)));
				}
				break;

			default:
				player.sendMessage(msglookup.getMessageForEventAnJobAndLevel(
						jobid, state[0], state[1]));
				break;
			}
		} else {
			// TODO z_fancy
			player.sendMessage("Sag bitte ein Admin, dass ich keinen Job habe.");
		}
	}

	public String[][] generateConfig(DJobInfoValue jobInfo) {
		String[][] config = new String[2][2];
		config[0][0] = "m";
		config[0][1] = jobInfo.getNeededMoney() + "";
		config[1][0] = "bi";
		config[1][1] = jobInfo.getNeededBasicItems() + "";
		return config;
	}

	public boolean hasRequirements(Player player, DJobInfoValue jobInfo) {
		return hasAmoundOfBasicItem(player, jobInfo)
				&& economy.has(player.getName(), jobInfo.getNeededMoney());
	}

	public void removeRequirements(Player player, DJobInfoValue jobInfo) {
		removeAmountOfItem(player, basicItem, jobInfo.getNeededBasicItems());
		economy.withdrawPlayer(player.getName(), jobInfo.getNeededMoney());
	}

	public boolean hasAmoundOfBasicItem(Player player, DJobInfoValue jobInfo) {
		return player.getInventory().contains(basicItem,
				jobInfo.getNeededBasicItems());
	}

	// don't check amount use Player.getInventory().contains
	public void removeAmountOfItem(Player player, Material material,
			int amount_needed) {
		HashMap<Integer, ? extends ItemStack> stacks = player.getInventory()
				.all(material);
		int max = amount_needed;
		ItemStack tmpStack = null;
		int amount = -1;
		for (Integer slot : stacks.keySet()) {
			tmpStack = stacks.get(slot);
			amount = tmpStack.getAmount() - max;
			if (amount > 0) {
				tmpStack.setAmount(amount);
				break;
			} else if (amount == 0) {
				player.getInventory().clear(slot.intValue());
				break;
			}
			max -= tmpStack.getAmount();
			player.getInventory().clear(slot.intValue());
		}
	}

	@Override
	public CitizensNPCType getType() {
		return new DPromoterType();
	}

	@Override
	public void load(Storage profiles, int UID) {
		// use db not config file
	}

	@Override
	public void save(Storage profiles, int UID) {
		// use db not config file

	}

	// TODO refactor
	public static void agreeAnimation(final HumanNPC npc) {
		npc.getNPCData().setLookClose(false);
		Runnable t = new AgreeAnimation(npc);
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Citizens.plugin, t);
	}

	// TODO refactor
	public static void disagreeAnimation(final HumanNPC npc) {
		npc.getNPCData().setLookClose(false);
		Runnable t = new DisagreeAnimation(npc);
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Citizens.plugin, t);
	}

	public static class DisagreeAnimation implements Runnable {
		private HumanNPC npc;

		public DisagreeAnimation(HumanNPC npc) {
			this.npc = npc;
		}

		@Override
		public void run() {
			float yaw = this.npc.getHandle().yaw;
			int[] v = new int[] { -32, -64, -32, 0, 32, 64, 32, 0 };
			for (int j = 0; j < 3; j++) {
				for (int i = 0; i < v.length; i++) {
					try {
						Thread.sleep(80);
					} catch (Exception e) {
						e.printStackTrace();
					}
					this.npc.getHandle().yaw = yaw + (v[i] / 2f);
				}
			}
			this.npc.getNPCData().setLookClose(true);
		}
	}

	public static class AgreeAnimation implements Runnable {
		private HumanNPC npc;

		public AgreeAnimation(HumanNPC npc) {
			this.npc = npc;
		}

		@Override
		public void run() {
			float pitch = this.npc.getHandle().pitch;
			int[] v = new int[] { -32, -64, -32, 0, 32, 64, 32, 0 };
			for (int j = 0; j < 2; j++) {
				for (int i = 0; i < v.length; i++) {
					try {
						Thread.sleep(80);
					} catch (Exception e) {
						e.printStackTrace();
					}
					this.npc.getHandle().pitch = pitch - (v[i] / 2f);
				}
			}
			this.npc.getHandle().pitch = pitch;
			this.npc.getNPCData().setLookClose(true);
		}
	}

	public static class TurnAroundAnimation implements Runnable {
		private Player player;
		private HumanNPC npc;

		public TurnAroundAnimation(Player player, HumanNPC npc) {
			this.player = player;
			this.npc = npc;
		}

		@Override
		public void run() {
			this.npc.getNPCData().setLookClose(false);
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.npc.getHandle().pitch = 0;
			float yaw = this.player.getEyeLocation().getYaw();
			this.npc.getHandle().yaw = yaw;
			int[] v = new int[] { -32, -64, -32, 0, 32, 64, 32, 0 };
			for (int i = 0; i < v.length; i++) {
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.npc.getHandle().yaw = yaw + v[i];
			}
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.npc.getNPCData().setLookClose(true);
		}
	}

}
