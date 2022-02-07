package de.dustplanet.pickuparrows;

import org.bukkit.entity.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PickupArrowsPAPI extends PlaceholderExpansion {
	
	private final PickupArrows plugin;
	
	public PickupArrowsPAPI(final PickupArrows instance) {
		plugin = instance;
	}
	
    @Override
    public boolean persist(){
        return true;
    }  

   @Override
   public boolean canRegister(){
       return true;
   }

   @Override
   public String getAuthor(){
       return plugin.getDescription().getAuthors().toString();
   }

	@Override
	public String getIdentifier(){
		return "PickupArrows";
	}

	@Override
	public String getVersion(){
		return plugin.getDescription().getVersion();
	}

	@Override
	public String onPlaceholderRequest(Player player, String identifier){	
		if(identifier.equals("is_pickup_on")){
			if (this.plugin.getDisabledPlayers().contains(player.getUniqueId()))
				return Boolean.FALSE.toString();
			return Boolean.TRUE.toString();
		}
		return null;
	}
	
}
