package tv.bokch.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class MyBook implements Parcelable {
	public Book book;
	public Review review;
	public History history;
	public Stack stack;
	public Rental rental;
	
	public MyBook(JSONObject obj) throws JSONException {
		JSONObject _book = obj.optJSONObject("book");
		book = new Book(_book);
		setStatus(_book);
	}
	
	public MyBook(Parcel in) {
		book = in.readParcelable(Book.class.getClassLoader());
		history = in.readParcelable(History.class.getClassLoader());
		review = in.readParcelable(Review.class.getClassLoader());
		stack = in.readParcelable(Stack.class.getClassLoader());
		rental = in.readParcelable(Rental.class.getClassLoader());		
	}
	
	public void setStatus(JSONObject obj) throws JSONException {
		JSONObject status = obj.optJSONObject("my_status");
		if (status == null) {
			return;
		}

		JSONObject _history = status.optJSONObject("history");
		this.history = _history == null ? null : new History(_history);
		
		JSONObject _review = status.optJSONObject("review");
		this.review = _review == null ? null : new Review(_review);
		
		JSONObject _stack = status.optJSONObject("stack");
		this.stack = _stack == null ? null : new Stack(_stack);
		
		JSONObject _rental = status.optJSONObject("rental");
		this.rental = _rental == null ? null : new Rental(_rental);		
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(book, flags);
		dest.writeParcelable(history, flags);
		dest.writeParcelable(review, flags);
		dest.writeParcelable(stack, flags);
		dest.writeParcelable(rental, flags);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<MyBook> CREATOR = new Parcelable.Creator<MyBook>() {
		public MyBook createFromParcel(Parcel in) {
			return new MyBook(in);
		}
		public MyBook[] newArray(int size) {
			return new MyBook[size];
		}
	};
}
