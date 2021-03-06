package com.comp9323.event;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.comp9323.data.DataHolder;
import com.comp9323.data.DateTimeConverter;
import com.comp9323.data.beans.Event;
import com.comp9323.main.R;
import com.comp9323.restclient.service.EventService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventNewFormFragment extends DialogFragment {

    private static final String TAG = "EventNewFormFragment";
    private final String DAY_START = "00:00AM";
    private final String DAY_END = "11:59PM";
    private View rootView;
    private TextInputEditText name, loc, desc, startDate, endDate, startTime, endTime;
    private TextInputLayout nameLayout, locLayout, startDateLayout, startTimeLayout,
            endDateLayout, endTimeLayout;
    private CheckBox dateCheckbox;
    private Toolbar toolbar;
    private Calendar mDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mma");
    private int year, month, day, hour, minute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event_new_form, container, false);

        // linking the variables to the layout items
        name = rootView.findViewById(R.id.new_event_name);
        nameLayout = rootView.findViewById(R.id.new_event_name_layout);
        loc = rootView.findViewById(R.id.new_event_location);
        locLayout = rootView.findViewById(R.id.new_event_location_layout);
        desc = rootView.findViewById(R.id.new_event_desc);
        dateCheckbox = rootView.findViewById(R.id.checkBox);
        startDate = rootView.findViewById(R.id.new_event_startdate);
        startDateLayout = rootView.findViewById(R.id.new_event_startdate_layout);
        endDate = rootView.findViewById(R.id.new_event_enddate);
        endDateLayout = rootView.findViewById(R.id.new_event_enddate_layout);
        startTime = rootView.findViewById(R.id.new_event_starttime);
        startTimeLayout = rootView.findViewById(R.id.new_event_starttime_layout);
        endTime = rootView.findViewById(R.id.new_event_endtime);
        endTimeLayout = rootView.findViewById(R.id.new_event_endtime_layout);
        toolbar = rootView.findViewById(R.id.toolbar);

        setToolbar();
        initDates();
        setDateListeners();
        setTimeListeners();
        setCheckboxClickListener();

        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.new_event_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            if (validateFields()) callPostEvent();
            return true;
        } else if (id == android.R.id.home) {
            // handle close button click here
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Setup listeners for the date fields so that a datepickerdialog pops up when clicked
     */
    private void setDateListeners() {
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog newDatePicker = new DatePickerDialog(getActivity(),
                        getOnDateSetListener(startDate), year, month, day);
                newDatePicker.show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog newDatePicker = new DatePickerDialog(getActivity(),
                        getOnDateSetListener(endDate), year, month, day);
                newDatePicker.show();
            }
        });
    }

    /**
     * Setup listeners for the time fields so that a timepickerdialog pops up when clicked
     */
    private void setTimeListeners() {
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog newTimePicker = new TimePickerDialog(getActivity(),
                        getOnTimeSetListener(startTime), hour, minute, false);
                newTimePicker.show();
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog newTimePicker = new TimePickerDialog(getActivity(),
                        getOnTimeSetListener(endTime), hour, minute, false);
                newTimePicker.show();
            }
        });
    }

    /**
     * Setup the datepickerdialog listener for a textfield, initialises it at the current time
     * @param dateText
     * @return
     */
    private DatePickerDialog.OnDateSetListener getOnDateSetListener(final TextInputEditText dateText) {
        return (new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                mDate.set(year, month, day);
                dateText.setText(dateFormat.format(mDate.getTime()));
            }
        });
    }

    /**
     * Setup a timepickerdialog listener for a textfield, initialises it at the current time
     * @param dateText
     * @return
     */
    private TimePickerDialog.OnTimeSetListener getOnTimeSetListener(final TextInputEditText dateText) {
        return (new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                mDate.set(Calendar.HOUR_OF_DAY, hour);
                mDate.set(Calendar.MINUTE, minute);
                dateText.setText(timeFormat.format(mDate.getTime()));
            }
        });
    }

    /**
     * Setup the toolbar for the form
     */
    private void setToolbar() {
        toolbar.setTitle("New Event");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
        setHasOptionsMenu(true);
    }

    /**
     * Initialise the date fields for the form
     */
    private void initDates() {
        mDate = Calendar.getInstance();
        year = mDate.get(Calendar.YEAR);
        month = mDate.get(Calendar.MONTH);
        day = mDate.get(Calendar.DAY_OF_MONTH);
        hour = mDate.get(Calendar.HOUR_OF_DAY);
        minute = mDate.get(Calendar.MINUTE);

        startDate.setText(dateFormat.format(mDate.getTime()));
        endDate.setText(dateFormat.format(mDate.getTime()));
        startTime.setText(timeFormat.format(mDate.getTime()));
        endTime.setText(timeFormat.format(mDate.getTime()));
    }

    /**
     * Validation check for the new event form
     * @return
     */
    private boolean validateFields() {
        return (validateName() && validateDateRange());
    }

    /**
     * Validation check for name
     * @return
     */
    private boolean validateName() {
        if (name.getText().toString().trim().isEmpty()) {
            nameLayout.setError(getString(R.string.err_msg_name));
            requestFocus(name);
            return false;
        } else {
            nameLayout.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * Checks if the user has input a location, if not, the user will be notified with error text
     *
     * @return
     */
    private boolean validateLocation() {
        if (loc.getText().toString().trim().isEmpty()) {
            locLayout.setError(getString(R.string.err_msg_location));
            requestFocus(loc);
            return false;
        } else {
            locLayout.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * Validation check for date
     * Checks if the end date is before the start date, if start date is before the current date
     * @return
     */
    private boolean validateDateRange() {
        String eventStart = startDate.getText().toString() + " " + startTime.getText().toString();
        String eventEnd = endDate.getText().toString() + " " + endTime.getText().toString();
        Date today = DateTimeConverter.getToday();
        Date starting = DateTimeConverter.getToday();

        startDateLayout.setErrorEnabled(false);
        startTimeLayout.setErrorEnabled(false);
        endDateLayout.setErrorEnabled(false);
        endTimeLayout.setErrorEnabled(false);

        try {
            starting = dateFormat.parse(startDate.getText().toString());
        } catch (ParseException e) {
            Log.d(TAG, e.getMessage());
        }

        if (starting.before(today)) {
            startDateLayout.setError(getString(R.string.err_msg_start_date));
            requestFocus(startDate);
            return false;
        } else if (DateTimeConverter.checkDateBefore(eventStart, eventEnd) || eventStart.equals(eventEnd)) {
            return true;
        } else {
            startDateLayout.setError(getString(R.string.err_msg_date));
            requestFocus(startDate);
            startTimeLayout.setError(" ");
            requestFocus(startTime);
            endDateLayout.setError(getString(R.string.err_msg_date));
            requestFocus(endDate);
            endTimeLayout.setError(" ");
            requestFocus(endTime);
            return false;
        }
    }

    /**
     * If the user inputs data that is invalid, this makes the error message visible
     * @param view
     */
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * Adds a Listener which checks if the user has selected "all-day" event and will disable
     * date/time selecting functionality until unselected. Will also modify endDate to be the same
     * day as startDate and set start and end times to be start and end of the day
     */
    private void setCheckboxClickListener() {
        dateCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateCheckbox.isChecked()) {
                    startDate.setEnabled(false);
                    startTime.setEnabled(false);
                    endDate.setEnabled(false);
                    endTime.setEnabled(false);

                    // set fields on the screen to reflect all-day selection
                    endDate.setText(startDate.getText().toString());
                    startTime.setText(DAY_START);
                    endTime.setText(DAY_END);
                } else {
                    startDate.setEnabled(true);
                    startTime.setEnabled(true);
                    endDate.setEnabled(true);
                    endTime.setEnabled(true);
                }
            }
        });
    }

    /**
     * Post the event to the database
     */
    private void callPostEvent() {
        EventService.postEvent(createEventBean(), new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful()) {
                    Snackbar successMessage = Snackbar.make(getView(), "Event created.",
                            Snackbar.LENGTH_SHORT);
                    successMessage.show();
                    dismiss();
                } else {
                    Snackbar failMessage = Snackbar.make(rootView, "Could not create event.",
                            Snackbar.LENGTH_SHORT);
                    failMessage.show();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private Event createEventBean() {
        String eventName = name.getText().toString();
        String eventLoc = loc.getText().toString();
        String eventDesc = desc.getText().toString();
        String eventStartD = startDate.getText().toString();
        String eventEndD = endDate.getText().toString();
        String eventStartT = startTime.getText().toString();
        String eventEndT = endTime.getText().toString();
        String eventUser = DataHolder.getInstance().getUser().getUsername();

        eventStartD = DateTimeConverter.convertA2SDate(eventStartD);
        eventEndD = DateTimeConverter.convertA2SDate(eventEndD);
        eventStartT = DateTimeConverter.convertA2STime(eventStartT);
        eventEndT = DateTimeConverter.convertA2STime(eventEndT);

        Event newEvent = new Event(eventName, eventLoc, eventStartD, eventEndD, eventStartT,
                eventEndT, eventDesc, eventUser);

        return newEvent;
    }
}
