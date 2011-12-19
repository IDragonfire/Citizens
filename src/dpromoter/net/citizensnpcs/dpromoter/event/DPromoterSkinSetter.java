package net.citizensnpcs.dpromoter.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import net.citizensnpcs.idragonfire.datamanager.DMySQLManager;
import net.citizensnpcs.resources.npclib.NPCManager;

import org.getspout.spoutapi.SpoutManager;

public class DPromoterSkinSetter implements Runnable {

	public static final DPromoterSkinSetter INSTANCE = new DPromoterSkinSetter();

	private DPromoterSkinSetter() {
		// singleton pattern
	}

	@Override
	// TODO refactor: check type and nullpointer
	public void run() {
		ArrayList<DPromoterIntStingObject> list = getUrl(true);
		for (int i = 0; i < list.size(); i++) {
			try {
				SpoutManager.getPlayer(
						NPCManager.get(list.get(i).getId()).getPlayer())
						.setSkin(list.get(i).getUrl());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		list = getUrl(false);
		for (int i = 0; i < list.size(); i++) {
			try {
				SpoutManager.getPlayer(
						NPCManager.get(list.get(i).getId()).getPlayer())
						.setCape(list.get(i).getUrl());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param skin
	 *            true: skin - false: capes
	 * @return
	 */

	public static ArrayList<DPromoterIntStingObject> getUrl(boolean skin) {
		ArrayList<DPromoterIntStingObject> skins = new ArrayList<DPromoterSkinSetter.DPromoterIntStingObject>();
		try {
			PreparedStatement stmt = DMySQLManager.INSTANCE
					.getCon()
					.prepareStatement(
							(skin) ? "SELECT npcid, skin FROM dp_npc d WHERE LENGTH(skin) > 10"
									: "SELECT npcid, cape FROM dp_npc d WHERE LENGTH(cape) > 10");
			ResultSet res = stmt.executeQuery();
			while (res.next()) {
				skins.add(new DPromoterIntStingObject(res.getInt(1), res
						.getString(2)));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return skins;
	}

	static class DPromoterIntStingObject {
		private int id;
		private String url;

		public DPromoterIntStingObject(int id, String url) {
			super();
			this.id = id;
			this.url = url;
		}

		public int getId() {
			return this.id;
		}

		public String getUrl() {
			return this.url;
		}

	}
}
