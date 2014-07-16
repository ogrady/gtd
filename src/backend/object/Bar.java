package backend.object;

import java.awt.Dimension;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import frontend.renderer.BarDefaultRenderer;
import util.IRenderable;
import backend.Config;

/**
 * {@link Bar}s represent the current value in relation to a maximum value of a
 * number (eg: healthpoints).
 * 
 * @author Daniel
 * 
 */
public class Bar extends Positionable implements IRenderable {

	private Color borderColor, fullColor, emptyColor;
	private final Dimension size;
	private Float value, maximum;
	private int borderSize;

	/**
	 * @return the colour used to render the border of the {@link Bar}
	 */
	public Color getBorderColor() {
		return borderColor;
	}

	/**
	 * @param borderColor
	 *            the new colour to render the border of the {@link Bar} with
	 */
	public void setBorderColor(final Color borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * @return the colour to render the filledi portion of the {@link Bar} with
	 */
	public Color getFullColor() {
		return fullColor;
	}

	/**
	 * @param fullColor
	 *            the new colour to render the filled portion of the {@link Bar}
	 *            with
	 */
	public void setFullColor(final Color fullColor) {
		this.fullColor = fullColor;
	}

	/**
	 * @return the colour to render the empty portion of the the {@link Bar}
	 *         with
	 */
	public Color getEmptyColor() {
		return emptyColor;
	}

	/**
	 * @param emptyColor
	 *            the new colour to render the empty portion of the {@link Bar}
	 *            with
	 */
	public void setEmptyColor(final Color emptyColor) {
		this.emptyColor = emptyColor;
	}

	/**
	 * @return the current value of the {@link Bar} (represented by the filled
	 *         portion)
	 */
	public Float getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the new value of the {@link Bar}, which can never be smaller
	 *            than 0 or greater than the bars maximum (asserted by this
	 *            setter)
	 */
	public void setValue(final Float value) {
		this.value = Math.min(this.maximum, Math.max(0, value));
	}

	/**
	 * @return the maximum value of the (which is the value when the bar is
	 *         full)
	 */
	public Float getMaximum() {
		return maximum;
	}

	/**
	 * @param maximum
	 *            the new value for the maximum of the {@link Bar}
	 */
	public void setMaximum(final Float maximum) {
		this.maximum = maximum;
	}

	/**
	 * @return the size of the {@link Bar} in pixel
	 */
	public Dimension getSize() {
		return size;
	}

	/**
	 * @param borderSize
	 *            the new size of the border of the {@link Bar} in pixel
	 */
	public void setBorderSize(final int borderSize) {
		this.borderSize = borderSize;
	}

	/**
	 * @return the size of the border of the {@link Bar} in pixel
	 */
	public int getBorderSize() {
		return borderSize;
	}

	/**
	 * Consutructor
	 * 
	 * @param pos
	 *            the position of the {@link Bar} on the screen
	 * @param size
	 *            the size of the {@link Bar} in pixel
	 * @param borderSize
	 *            the size of the border of the {@link Bar} in pixel
	 * @param borderColor
	 *            the colour of the border
	 * @param fullColor
	 *            the colour for the full portion of the {@link Bar}
	 * @param emptyColor
	 *            the colour for the empty portion of the {@link Bar}
	 * @param currentValue
	 *            the initial current value
	 * @param maxValue
	 *            the max value the {@link Bar} can represent
	 */
	public Bar(final Vector2f pos, final Dimension size, final int borderSize,
			final Color borderColor, final Color fullColor,
			final Color emptyColor, final Float currentValue,
			final Float maxValue) {
		super(pos);
		this.size = size;
		this.borderSize = borderSize;
		this.borderColor = borderColor;
		this.fullColor = fullColor;
		this.emptyColor = emptyColor;
		this.value = currentValue;
		this.maximum = maxValue;
		this.renderer = new BarDefaultRenderer(this);
	}

	/**
	 * Constructor, taking values from the {@link Config}
	 * 
	 * @param pos
	 *            the position of the {@link Bar} on the screen
	 * @param currentValue
	 *            the initial current value of the {@link Bar}
	 * @param maxValue
	 *            the max value the {@link Bar} can represent
	 */
	public Bar(final Vector2f pos, final Float currentValue,
			final Float maxValue) {
		this(pos, Config.BAR_SIZE, Config.BAR_BORDER_SIZE,
				Config.BAR_BORDER_COLOR, Config.BAR_FULL_COLOR,
				Config.BAR_EMPTY_COLOR, currentValue, maxValue);
	}

	/**
	 * Changes the value of the bar by a certain delta (positive or negative).<br>
	 * This is a convienience-method for setValue(getValue() + delta)
	 * 
	 * @param delta
	 *            the delta to change the value by
	 */
	public void changeValue(final float delta) {
		this.setValue(this.value + delta);
	}

	@Override
	public Renderable getRenderableRepresentation() {
		Image img = null;
		try {
			img = new Image(this.size.width, this.size.height);
		} catch (final SlickException e) {
			e.printStackTrace();
		}
		return img;

	}

	@Override
	public int getRepresentationWidth() {
		return this.size.width + this.borderSize * 2;
	}

	@Override
	public int getRepresentationHeight() {
		return this.size.height;
	}
}
