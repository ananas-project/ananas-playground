package ananas.app.ft_iso.func;

import ananas.app.ft_iso.ValueBoxModel;
import ananas.app.ft_iso.ValueBoxModelListener;

public class DataCore implements ValueBoxModelListener {

	public final FValueModel m_a_f;
	public final IsoValueModel m_a_iso;
	public final TimeValueModel m_a_time;

	public final FValueModel m_b_f;
	public final IsoValueModel m_b_iso;
	public final TimeValueModel m_b_time;

	public DataCore() {

		this.m_a_f = new FValueModel();
		this.m_a_iso = new IsoValueModel();
		this.m_a_time = new TimeValueModel();

		this.m_b_f = new FValueModel();
		this.m_b_iso = new IsoValueModel();
		this.m_b_time = new TimeValueModel();

		this.m_a_f.addListener( this  ) ;
		
	}

	@Override
	public void onChanged(ValueBoxModel model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(ValueBoxModel model, int key) {
		// TODO Auto-generated method stub
		
	}
}
