package com.reactnative.wheelpicker;

import android.view.Gravity;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.reactnative.wheelpicker.wheel.OnItemSelectedListener;
import com.reactnative.wheelpicker.wheel.WheelAdapter;
import com.reactnative.wheelpicker.wheel.WheelView;

import java.util.Collections;
import java.util.List;

public class PickerView extends FrameLayout implements WheelAdapter, OnItemSelectedListener {

	final WheelView wheelView;
	private final ReactContext reactContext;

	public PickerView(@NonNull ReactContext context) {
		super(context);
		reactContext = context;
		wheelView = new WheelView(context);
		LayoutParams layoutParams = new LayoutParams(-1, -1);
		layoutParams.gravity = Gravity.CENTER;
		addView(wheelView, layoutParams);
		wheelView.setTextXOffset(1);
		wheelView.setAdapter(this);
		wheelView.setOnItemSelectedListener(this);
		wheelView.setCyclic(false);

		setClipChildren(true);
	}

	List<String> items = Collections.emptyList();

	public void setItems(List<String> items) {
		this.items = items;
		wheelView.setAdapter(this);
		if (items.size() > selectedIndex) {
			wheelView.setCurrentItem(selectedIndex);
		}
	}

	int selectedIndex = 0;

	public void setSelectedItem(int index) {
		selectedIndex = index;
		if (items.size() > index) {
			wheelView.setCurrentItem(index);
		}
	}

	public void setCyclic(boolean cyclic) {
		wheelView.setCyclic(cyclic);
	}

	public void setTextSize(int size) {
		wheelView.setTextSize(size);
	}

	public void setItemHeight(float height) {
		wheelView.setItemHeight(height);
	}

	public void setTextColorOut(int textColorOut) {
		wheelView.setTextColorOut(textColorOut);
	}

	public void setTextColorCenter(int textColorCenter) {
		wheelView.setTextColorCenter(textColorCenter);
	}

	@Override
	public int getItemsCount() {
		return items.size();
	}

	@Override
	public Object getItem(int index) {
		return items.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return items.indexOf(o);
	}

	@Override
	public void onItemSelected(int index) {
		if (getItemsCount() > index) {
			sentEvent(new OnItemSelectedEvent(UIManagerHelper.getSurfaceId(reactContext), getId(), index));
		}
	}

	void sentEvent(Event<?> event) {
		int viewId = getId();
		EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, viewId);
		if (eventDispatcher != null) {
			eventDispatcher.dispatchEvent(event);
		}
	}
}
