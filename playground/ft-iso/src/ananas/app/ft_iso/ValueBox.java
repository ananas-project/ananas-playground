package ananas.app.ft_iso;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ValueBox extends LinearLayout implements ValueBoxModelListener {

	private Button mButtonMinus;
	private Button mButtonPlus;
	private Button mButtonTitle;
	private TextView mTextValue;
	private ValueBoxModel mModel;

	public ValueBox(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater.from(context).inflate(R.layout.value_box, this, true);
		this.mButtonMinus = (Button) this.findViewById(R.id.button_minus);
		this.mButtonPlus = (Button) this.findViewById(R.id.button_plus);
		this.mButtonTitle = (Button) this.findViewById(R.id.button_title);
		this.mTextValue = (TextView) this.findViewById(R.id.text_value);

	}

	public void setModel(ValueBoxModel model) {
		ValueBoxModel pold;
		synchronized (this) {
			pold = this.mModel;
			this.mModel = model;
		}
		if (pold != null) {
			pold.removeListener(this);
		}
		if (model != null) {
			model.addListener(this);
		}
	}

	public ValueBoxModel getModel() {
		return this.mModel;
	}

	@Override
	public void onChanged(ValueBoxModel model) {
		this.mButtonTitle.setText(model.getTitle());
		this.mTextValue.setText(model.getValue());
	}

	@Override
	public void onClick(ValueBoxModel model, int key) {
		// TODO Auto-generated method stub
		
	}

}
