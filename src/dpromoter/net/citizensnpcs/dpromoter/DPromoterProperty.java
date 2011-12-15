package net.citizensnpcs.dpromoter;

import java.util.Collection;
import java.util.List;

import net.citizensnpcs.properties.Node;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;

import com.google.common.collect.Lists;

public class DPromoterProperty extends PropertyManager implements Properties {
	public static final DPromoterProperty INSTANCE = new DPromoterProperty();
	private final String isPromoter = "." + DPromoterType.TYPE + ".toggle";

	private DPromoterProperty() {
		// Singleton Pattern
	}

	@Override
	public List<Node> getNodes() {
		return null;
		// List<Node> nodes = new ArrayList<Node>();
		// nodes.add(new Node("TestE", SettingsType.GENERAL, this.type +
		// ".test",
		// new Integer(10)));
		// return Lists.newArrayList();
	}

	@Override
	public Collection<String> getNodesForCopy() {
		return Lists.newArrayList();
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (isEnabled(npc)) {
			if (!npc.isType(DPromoterType.TYPE)) {
				npc.registerType(DPromoterType.TYPE);
			}
			DPromoter promoter = npc.getType(DPromoterType.TYPE);
			promoter.load(profiles, npc.getUID());
		}
		saveState(npc);
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isType(DPromoterType.TYPE));
			DPromoter promoter = npc.getType(DPromoterType.TYPE);
			promoter.save(profiles, npc.getUID());
		}
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		profiles.setBoolean(npc.getUID() + this.isPromoter, value);
	}

	@Override
	public boolean isEnabled(HumanNPC npc) {
		return profiles.getBoolean(npc.getUID() + this.isPromoter);
	}

}
