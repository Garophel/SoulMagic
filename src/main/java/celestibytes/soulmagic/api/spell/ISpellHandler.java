package celestibytes.soulmagic.api.spell;

public interface ISpellHandler {
	
	public void endSpell(boolean failed, ISpell spell);
	
	/** Use a negative amount to add mana. */
	public boolean consumeMana(int amount);
	
	public boolean checkMana(int amount);
	
	public int getMaxMana();
	
	public int getAvailableMana();
}
