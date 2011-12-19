package net.citizensnpcs.dpromoter.event;

import net.citizensnpcs.api.event.NPCListener;
import net.citizensnpcs.api.event.NPCToggleTypeEvent;
import net.citizensnpcs.dpromoter.DPromoterType;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DPromoterListener extends NPCListener {

	@Override
	public void onNPCToggleType(NPCToggleTypeEvent event) {
		super.onNPCToggleType(event);
		// normaly equals, but my String is static ;)
		if (event.getToggledType() == DPromoterType.TYPE) {
			if (event.isToggledOn()) {
				event.getNPC().setItemInHand(new ItemStack(Material.GOLD_AXE));
				DPromoterType.startSkinSetter();
			} else {
				event.getNPC().setItemInHand(null);
			}
		}
	}
}
