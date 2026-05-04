package com.example.picana_apk.presentation.view.menu;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MenuPagerAdapter extends FragmentStateAdapter {
    private String[] categorias;

    public MenuPagerAdapter(@NonNull FragmentActivity fragmentActivity, String[] categorias) {
        super(fragmentActivity);
        this.categorias = categorias;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        MenuFragment fragment = new MenuFragment();
        Bundle bundle = new Bundle();
        bundle.putString("categoria", categorias[position]);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() { return categorias.length; }
}