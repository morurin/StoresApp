package com.neko.storesapp.ui.home;



import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.neko.storesapp.CustomViewModel;
import com.neko.storesapp.R;
import com.neko.storesapp.databinding.FragmentHomeBinding;




public class HomeFragment extends Fragment implements View.OnLongClickListener{

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private CustomViewModel customViewModel;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();



        customViewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);




        TextView displayNameH = root.findViewById(R.id.displayUsernameH);
        TextView cardDescription = root.findViewById(R.id.bio);
        cardDescription.setText(customViewModel.getUserDescription());


        CardView cardView = root.findViewById(R.id.cardViewDescription);
        cardView.setOnLongClickListener(this);

        String username = customViewModel.getUsername();
        displayNameH.setText(username);



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onLongClick(View v) {
        Navigation.findNavController(v).navigate(R.id.userDescriptionFragment);

        return false;
    }
}