package com.example.eden62.GENSMobile.Adapters.RNFAdapter.TextWatchers;

import com.example.eden62.GENSMobile.Database.RNFDatabase.RNFInventaire;

import java.util.List;

public class NombreTW extends BaseTW {

    @Override
    protected void setItemGoodField(int input) {
        item.setNombre(input);
    }
}
