package bots;

public abstract class ColoredActor extends Actor {
	protected ActorType type;
	protected final Faction color;
	protected int healthpoints;
	protected int hp = 100;

	public ColoredActor(ActorType kind, Faction color) {
		super();
		this.type = kind;
		this.color = color;
	}

	public int getHP() {
		return hp;
	}

	protected void damage(int damage) {
		hp -= damage * getArmorModificator();
	}

	protected abstract float getArmorModificator();
}
