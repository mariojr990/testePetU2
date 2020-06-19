package com.pet2u.pet2u.domain;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.pet2u.pet2u.Petshop.Cad_do_Pet2_Activity;
import com.pet2u.pet2u.Petshop.Cad_do_Pet_Activity;

import java.lang.ref.WeakReference;

public class AddressRequest extends AsyncTask<Void, Void, Address> {
    private WeakReference<Cad_do_Pet2_Activity> activity;

    public AddressRequest( Cad_do_Pet2_Activity activity ){
        this.activity = new WeakReference<>( activity );
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.get().lockFields( true );
    }

    @Override
    protected Address doInBackground(Void... voids) {

        try{
            String jsonString = JsonRequest.request( activity.get().getUriRequest() );
            Gson gson = new Gson();

            return gson.fromJson(jsonString, Address.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Address address) {
        super.onPostExecute(address);

        if( activity.get() != null ){
            activity.get().lockFields( false );

            if( address != null ){
                activity.get().setAddressFields(address);
            }
        }
    }
}