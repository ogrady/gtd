package util;

public interface ICopyable<C extends ICopyable<C>> {
	C copy();
}
