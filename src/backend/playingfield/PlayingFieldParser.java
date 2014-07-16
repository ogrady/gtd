package backend.playingfield;

import java.awt.Dimension;

import org.newdawn.slick.tiled.TiledMap;

import exception.MapParserException;

/**
 * Parser to transform a {@link TiledMap} into a {@link PlayingField}
 * 
 * @author Daniel
 * 
 */
public class PlayingFieldParser {
	public static final String BLOCKED = "blocked";
	public static final String WALKABLE = "walkable";
	public static final String GROUNDTYPE = "gtype";

	public PlayingFieldParser() {
	}

	/**
	 * Parses a {@link TiledMap} into a {@link PlayingField}.
	 * 
	 * @param mapFile
	 *            the file to create the {@link TiledMap} from
	 * @return the parsed {@link PlayingField}
	 * @throws MapParserException
	 */
	public PlayingField parse(final String path, final String tileset)
			throws MapParserException {
		PlayingField field = null;
		try {
			final TiledMap tiledMap = new TiledMap(this.getClass()
					.getClassLoader().getResourceAsStream(path), tileset);
			final Dimension fieldSize = new Dimension(tiledMap.getWidth()
					* tiledMap.getTileWidth(), tiledMap.getHeight()
					* tiledMap.getTileHeight());
			field = new PlayingField(fieldSize, tiledMap.getWidth(),
					tiledMap.getHeight());
			field.setTiledMap(tiledMap);
			// just inject properties from all layers to the cells
			for (int l = 0; l < tiledMap.getLayerCount(); l++) {
				injectLayerProperties(field, tiledMap, l);
			}
			// for convienience. Those lines throw all types of exceptions...
		} catch (final Exception ex) {
			throw new MapParserException(ex.getMessage());
		}
		return field;
	}

	/**
	 * Runs through the whole layer with the given id and injects the "walkable"
	 * and "playable" properties stored in that layer into each cell at the
	 * corresponding {@link PlayingFieldMetaCell} on the {@link PlayingField}.
	 * <p>
	 * Those properties could be distributed over several layers (e.g. there
	 * could be multiple layers for obstacles, where every tile in an
	 * obstacle-layer that is set would cause the cell to be unwalkable.
	 * </p>
	 * Let's assume we have two layers in a 2x2-map:
	 * <p>
	 * 1<br>
	 * 
	 * <pre>
	 * |X|X|
	 * |W|W|
	 * </pre>
	 * 
	 * <br>
	 * 
	 * and<br>
	 * <br>
	 * 2<br>
	 * 
	 * <pre>
	 * |W|X|
	 * |X|X|
	 * </pre>
	 * 
	 * <br>
	 * Where "W" is walkable and "X" just stands for "no property" Injecting the
	 * properties of both layers will cause all but the upper right cell to
	 * become walkable, as it lacks the "W"-property in both layers.
	 * </p>
	 * <p>
	 * Note that properties are OR-conjuncted. That means: when a cell becomes
	 * walkable from a layer once, another layer can't make it unwalkable again.
	 * Same goes for buildable.
	 * </p>
	 * 
	 * @param field
	 *            the {@link PlayingField} to the the
	 *            {@link PlayingFieldMetaCell}s to inject the properties into
	 * @param tm
	 *            the {@link TiledMap} to read the layer-properties from
	 * @param layerId
	 *            the index of the layer to inject the properties from
	 */
	private void injectLayerProperties(final PlayingField field,
			final TiledMap tm, final int layerId) {
		// determine whether the layer holds the "walkable" or "buildable"
		// properties
		final boolean layerIsWalkable = Boolean.parseBoolean(tm
				.getLayerProperty(layerId, WALKABLE, "false"));
		final boolean layerIsBuildBlocked = Boolean.parseBoolean(tm
				.getLayerProperty(layerId, BLOCKED, "false"));
		for (int x = 0; x < tm.getWidth(); x++) {
			for (int y = 0; y < tm.getHeight(); y++) {
				final PlayingFieldMetaCell cell = field.getCellAt(x, y);
				// inject the found properties into all cells that are painted
				// on that layer
				final int tileId = tm.getTileId(x, y, layerId);
				if (tileId != 0) {
					cell.setWalkable(layerIsWalkable || cell.isWalkable());
					cell.setBuildable(!layerIsBuildBlocked);
					final String groundtype = tm.getTileProperty(tileId,
							GROUNDTYPE, null);
					if (groundtype != null) {
						cell.setGroundType(GroundType.getByName(groundtype));
					}
				}

			}
		}
	}
}
