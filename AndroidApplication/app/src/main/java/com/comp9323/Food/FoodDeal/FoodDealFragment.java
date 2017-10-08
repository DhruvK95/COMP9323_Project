package com.comp9323.Food.FoodDeal;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comp9323.AsycnTask.FoodDealAsycn;
import com.comp9323.RestAPI.Beans.FoodDeal;
import com.comp9323.RestAPI.DataHolder.SingletonDataHolder;
import com.comp9323.myapplication.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFooDealInteractionListener}
 * interface.
 */
public class FoodDealFragment extends Fragment{

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    public static int mPage = 1;

    protected static OnListFooDealInteractionListener mListener;
    protected static MyFoodDealRecyclerViewAdapter mAdapter;
    protected SwipeRefreshLayout mSwipeRefreshLayout;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FoodDealFragment() {
    }

    @SuppressWarnings("unused")
    public static FoodDealFragment newInstance(int columnCount) {
        FoodDealFragment fragment = new FoodDealFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        mListener = new OnListFooDealInteractionListener() {
            @Override
            public void onListFragmentInteraction(FoodDeal item) {

                String link = item.getEventLink();
                Log.d("Length", link.length()+"");
                if (link.length()>0){
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setPackage("com.android.chrome");
                    try {
                        getContext().startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        getContext().startActivity(intent);
                    }
                }else {
                    Toast.makeText(getActivity().getApplicationContext(), "There are no Home Page of Deal Yet", Toast.LENGTH_LONG).show();
                }
            }
        };
        mAdapter = new MyFoodDealRecyclerViewAdapter(mListener);


    }
    private void pullList(int newpage){
        mPage = newpage;
        new FoodDealAsycn(mAdapter).execute(FoodDealAsycn.GET_LIST, mPage+"");
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fooddeal_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.fooddeal_list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        recyclerView.setAdapter(mAdapter);

        //set onLoadListener for checking if list end
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!MyFoodDealRecyclerViewAdapter.ifReachEnd() && !mAdapter.ifLoading()) {
                    if (linearLayoutManager.getItemCount() <= (linearLayoutManager.findLastVisibleItemPosition() + 1)) {
                        mPage++;
                        mAdapter.setIsLoading(true);
                        new FoodDealAsycn(mAdapter).execute(FoodDealAsycn.GET_LIST, mPage + "");
                    }
                }
            }
        });

        //set refresh listener
        mSwipeRefreshLayout = view.findViewById(R.id.fooddeal_swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            SingletonDataHolder.getInstance().clearFoodDealList();
            MyFoodDealRecyclerViewAdapter.setIsReachEnd(false);
            pullList(1);
            }
        });

        //pull item from server after launch if no item in the list
        if (SingletonDataHolder.getInstance().getFoodDealList().size() ==0){
            pullList(1);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        SingletonDataHolder.getInstance().clearFoodDealList();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFooDealInteractionListener {
        void onListFragmentInteraction(FoodDeal item);
    }
}
