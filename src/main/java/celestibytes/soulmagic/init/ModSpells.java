package celestibytes.soulmagic.init;

import java.util.HashMap;
import java.util.Map;

import celestibytes.soulmagic.SoulMagic;
import celestibytes.soulmagic.api.spell.IContinuousSpell;
import celestibytes.soulmagic.api.spell.IInstantSpell;
import celestibytes.soulmagic.api.spell.ISpell;
import celestibytes.soulmagic.init.spells.SpellFeed;
import celestibytes.soulmagic.init.spells.TestSpellMove;

public class ModSpells {
	
	private static final Map<String, Class<? extends ISpell>> spellRegistry = new HashMap<String, Class<? extends ISpell>>();
	
	public static void init() {
		registerSpell(SoulMagic.MODID + ":testmove", TestSpellMove.class);
		registerSpell(SoulMagic.MODID + ":feed", SpellFeed.class);
	}
	
	public static boolean registerSpell(String spellId, Class<? extends ISpell> spellClass) {
		if(!spellRegistry.containsKey(spellId)) {
			spellRegistry.put(spellId, spellClass);
			return true;
		}
		
		return false;
	}
	
	public static Class<? extends ISpell> getSpell(String spellId) {
		return spellId.isEmpty() ? null : spellRegistry.get(spellId);
	}
	
	@SuppressWarnings("unchecked")
	public static Class<? extends IInstantSpell> getInstantSpell(String spellId) {
		if(spellId.isEmpty()) return null;
		
		Class<?> ret = spellRegistry.get(spellId);
		if(ret != null && IInstantSpell.class.isAssignableFrom(ret)) {
			return (Class<? extends IInstantSpell>) ret;
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Class<? extends IContinuousSpell> getContinuousSpell(String spellId) {
		if(spellId.isEmpty()) return null;
		
		Class<?> ret = spellRegistry.get(spellId);
		if(ret != null && IContinuousSpell.class.isAssignableFrom(ret)) {
			
			return (Class<? extends IContinuousSpell>) ret;
		}
		
		return null;
	}
}
