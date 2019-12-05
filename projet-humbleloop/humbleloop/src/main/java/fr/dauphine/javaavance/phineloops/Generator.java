package fr.dauphine.javaavance.phineloops;

import java.util.Random;

public class Generator<D extends Shape> {

	private Random generator;
	private String file;

	public Generator(String file) {
		generator = new Random();
		this.file = file;
	}

	public <G extends Game<D>> void generate(G game, int height, int width) {

		game.getConstraints().stream().filter(c -> this.generator.nextInt(10) > 3).forEach(c -> {
			c.getX().getValue().addConnection(c.getXside());
			c.getY().getValue().addConnection(c.getYside());
		});

		game.getCells().forEach(c -> {
			c.getValue().findType();
			c.getValue().setRandomOffset();
		});

		game.export(file);
	}
}