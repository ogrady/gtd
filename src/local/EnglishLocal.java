package local;

import java.util.ListResourceBundle;

import util.Const;

public class EnglishLocal extends ListResourceBundle {
	private final Object[][] contents = {
			{ Const.L_NO_MONEY, "Not enough Money" },
			{ Const.L_NOT_IMPLEMENTED, "Not implemented yet" } };

	@Override
	protected Object[][] getContents() {
		return contents;
	}

}
