package bots;

public class ColoredActor extends Actor {
	protected ActorType type;
	protected final Faction color;

	public ColoredActor(ActorType kind, Faction color) {
		super();
		this.type = kind;
		this.color = color;
	}
}
