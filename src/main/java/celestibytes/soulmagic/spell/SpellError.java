package celestibytes.soulmagic.spell;

public class SpellError extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public SpellError() {
		super("An error occurred while casting the spell!");
	}
	
	public SpellError(String reason) {
		super("Spell casting errored: " + reason);
	}

}
