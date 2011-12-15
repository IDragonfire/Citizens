package net.citizensnpcs.dpromoter;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.dpromoter.data.DMessageStore;
import net.citizensnpcs.dpromoter.data.DNPCjobLookup;
import net.citizensnpcs.dpromoter.data.DStatesStorage;
import net.citizensnpcs.dpromoter.data.DNPCjobLookup.DJobInfoValue;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DPromoter extends CitizensNPC {
	private static DNPCjobLookup npclookup = DNPCjobLookup.INSTANCE;
	private static DStatesStorage stateslookup = DStatesStorage.INSTANCE;
	private static DMessageStore msglookup = DMessageStore.INSTANCE;
	// events
	public static final int MEMBER = 31;
	public static final int MEMBER_INTO = 30;
	public static final int TRIAL_NEED = 21;
	public static final int TRIAL_INTRO = 20;
	public static final int USER_NO = 11;
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
		DJobInfoValue v = npclookup.getJobInfoFromNpc(npc);
		// TODO remove
		npclookup.test();
		if (v != null) {
			player.sendMessage("name: " + v.getName() + " rank: "
					+ v.getNeededRank() + " sp: " + v.getNeededSkillpoints()
					+ " bi: " + v.getNeededBasicItems() + " €: "
					+ v.getNeededMoney());
		} else {
			player.sendMessage("Bei mir gibt es keinen Job");
		}
	}

	public void action(int mouse, Player player, HumanNPC npc) {
		int state = stateslookup.getStatOfNpcForPlayer(npc, player);
		if (mouse == LEFT) {
			disagreeAnimation(npc);
		} else {
			agreeAnimation(npc);
		}
		if (state == TRIAL_NEED) {
			player.sendMessage("");
		} else {
			player.sendMessage(msglookup.getMessageForEventAndNpc(npc, state));
		}
		npc.setItemInHand(new ItemStack(org.bukkit.Material.REDSTONE_TORCH_ON));
	}

	@Override
	public CitizensNPCType getType() {
		return new DPromoterType();
	}

	@Override
	public void load(Storage profiles, int UID) {
		// TODO
	}

	@Override
	public void save(Storage profiles, int UID) {
		// TODO Auto-generated method stub

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
