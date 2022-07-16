package com.magdyradwan.sellonline;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.magdyradwan.sellonline.dto.ProfileOptionDTO;
import com.magdyradwan.sellonline.responsemodels.PhoneNumberResponseModel;
import com.magdyradwan.sellonline.responsemodels.ProfileResponseModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DISPLAY_NAME = "displayName";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_COUNTRY = "country";
    private static final String ARG_CITY = "city";
    private static final String ARG_DISTRICT = "district";
    private static final String ARG_USERID = "userId";
    private static final String ARG_PHONE_NUMBERS = "phoneNumbers";

    // TODO: Rename and change types of parameters
    private ProfileResponseModel mProfileData;
    private ArrayList<String> phoneNumbers;

    public ProfileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(
            String displayName,
            String email,
            String country,
            String city,
            String district,
            String userId,
            ArrayList<String> phoneNumbers
    ) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DISPLAY_NAME, displayName);
        args.putString(ARG_EMAIL, email);
        args.putString(ARG_COUNTRY, country);
        args.putString(ARG_CITY, city);
        args.putString(ARG_DISTRICT, district);
        args.putString(ARG_USERID, userId);
        args.putStringArrayList(ARG_PHONE_NUMBERS, phoneNumbers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProfileData = new ProfileResponseModel();
            mProfileData.setDisplayName(getArguments().getString(ARG_DISPLAY_NAME));
            mProfileData.setEmail(getArguments().getString(ARG_EMAIL));
            mProfileData.setCountry(getArguments().getString(ARG_COUNTRY));
            mProfileData.setCity(getArguments().getString(ARG_CITY));
            mProfileData.setDistrict(getArguments().getString(ARG_DISTRICT));
            mProfileData.setUserId(getArguments().getString(ARG_USERID));
            phoneNumbers = getArguments().getStringArrayList(ARG_PHONE_NUMBERS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_profile, container, false);

        ListView listView = inflatedView.findViewById(R.id.profile_data_list);
        ArrayList<ProfileOptionDTO> data = new ArrayList<>();
        data.add(new ProfileOptionDTO("Display Name", mProfileData.getDisplayName(), false));
        data.add(new ProfileOptionDTO("Email", mProfileData.getEmail(), false));
        data.add(new ProfileOptionDTO("Country", mProfileData.getCountry(), false));
        data.add(new ProfileOptionDTO("City", mProfileData.getCity(), false));
        data.add(new ProfileOptionDTO("District", mProfileData.getDistrict(), false));
        data.add(new ProfileOptionDTO("User ID", mProfileData.getUserId(), false));

        for(int i =0; i < phoneNumbers.size(); i++) {
            data.add(new ProfileOptionDTO("Phone Number " + (i+1), phoneNumbers.get(i), false));
        }

        listView.setAdapter(new ProfileAdapter(getActivity(), R.layout.profile_option_item, data));

        return inflatedView;
    }
}