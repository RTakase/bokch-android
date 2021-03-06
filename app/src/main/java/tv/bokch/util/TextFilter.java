package tv.bokch.util;

import android.text.InputFilter;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.widget.TextView;

public class TextFilter implements InputFilter {
	private final TextView view;

	public TextFilter(TextView view) {
		this.view = view;
	}
	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
		TextPaint paint = view.getPaint();
		int w = view.getWidth();
		int wpl = view.getCompoundPaddingLeft();
		int wpr = view.getCompoundPaddingRight();
		int width = w - wpl - wpr;

		SpannableStringBuilder result = new SpannableStringBuilder();
		for (int index = start; index < end; index++) {
			if (Layout.getDesiredWidth(source, start, index + 1, paint) > width) {
				result.append(source.subSequence(start, index));
				result.append("\n");
				start = index;
			} else if (source.charAt(index) == '\n') {
				result.append(source.subSequence(start, index));
				start = index;
			}
		}
		if (start < end) {
			result.append(source.subSequence(start, end));
		}
		return result;
	}
}
