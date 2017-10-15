package com.comp9323.event;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.comp9323.data.beans.Event;
import com.comp9323.main.R;
import com.comp9323.restclient.api.EventService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventFragment extends Fragment {
    private static final String TAG = "EventFragment";

    private EventRvAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_rv, container, false);
        setHasOptionsMenu(true);

        Toolbar toolbar = getActivity().findViewById(R.id.main_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        adapter = new EventRvAdapter();

        RecyclerView recyclerView = view.findViewById(R.id.event_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        getEvents();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_events, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_events:

                return true;
            case R.id.add_event:
                // Do stuff for adding events
                NewEventFormFragment eventFormFragment = new NewEventFormFragment();
                eventFormFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
                eventFormFragment.show(getFragmentManager(), "Event Dialog Fragment");
                return true;
            default:
                // Let the superclass handle it since the item is not recognised
                return super.onOptionsItemSelected(item);
        }
    }

    private void getEvents() {
        EventService.getEvents(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    updateAdapter(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void updateAdapter(List<Event> events) {
        adapter.setEvents(events);
        adapter.notifyDataSetChanged();
    }
}
