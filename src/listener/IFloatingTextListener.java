package listener;

import frontend.gui.FloatingText;

public interface IFloatingTextListener extends IListener {
	/**
	 * Called when the floating text reaches 0-alpha-component (= is not
	 * rendered anymore)
	 * 
	 * @param text
	 *            the {@link FloatingText} that just faded out
	 */
	public void onFadeOut(FloatingText text);
}
