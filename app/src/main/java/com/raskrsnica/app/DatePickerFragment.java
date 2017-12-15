package com.raskrsnica.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by sandr on 12/15/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int yy = calendar.get(Calendar.YEAR);
        int mm=calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    @Override
    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

        TextView mesec_tekst = (TextView) getActivity().findViewById(R.id.mesec);
        TextView dan_tekst=(TextView)getActivity().findViewById(R.id.datum);
        Calendar c=Calendar.getInstance();
        c.set(Calendar.MONTH, selectedMonth);
        SimpleDateFormat dateformat1 = new SimpleDateFormat("MMMM");
        String monthString= dateformat1.format(c.getTime());
        mesec_tekst.setText(monthString);
        dan_tekst.setText(selectedDay+"");
    }
}

