package com.way2android.geofence_part1;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.geofence_part1.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends ActionBarActivity  implements
                                    ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<Status>{

	/**
     * Provides the entry point to Google Play services.
     */
	 private GoogleApiClient mGoogleApiClient;

	/**
	 * The list of geofences used in this sample.
	 */
	protected ArrayList<Geofence> mGeofenceList;
	 

    /**
     * Used when requesting to add or remove geofences.
     */
    private PendingIntent mGeofencePendingIntent;

    
    private Button buttonAddGeofence;
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		int status=GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		
	    /*
	     *  Checking for the availability of Google Play Service.
	     */
        if(!(status==ConnectionResult.SUCCESS)) {
        	
        	Toast.makeText(getApplicationContext(), "Google Play Service is not available on your device !" , 0).show();
        	finish();
        	return;
        	
        }  
        
        Intent intent = new Intent(this, GeofenceReceiver.class);        
        mGeofencePendingIntent=PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		        
        createDummyGeofenceList();
        
        connectWithGooglePlayService();
                
        buttonAddGeofence=(Button) findViewById(R.id.btnRegisterNow);
        buttonAddGeofence.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
 
		        try {
		            LocationServices.GeofencingApi.addGeofences(
		                    mGoogleApiClient,
		                    // The GeofenceRequest object.
		                    getGeofencingRequest(),
		                    // A pending intent that that is reused when calling removeGeofences(). This
		                    // pending intent is used to generate an intent when a matched geofence
		                    // transition is observed.
		                    mGeofencePendingIntent
		            ).setResultCallback(MainActivity.this); // Result processed in onResult().
		            
		        } catch (SecurityException securityException) {
		            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
		        	securityException.printStackTrace();
		        }
				
			}
		});
	}
	
	public void connectWithGooglePlayService(){

		 mGoogleApiClient = new GoogleApiClient.Builder(this)
			.addConnectionCallbacks(this)
			.addOnConnectionFailedListener(this)
			.addApi(LocationServices.API)
			.build();
		 
		 mGoogleApiClient.connect();
	}

    private GeofencingRequest getGeofencingRequest() {
    	
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }

	private void createDummyGeofenceList(){
		
		 mGeofenceList = new ArrayList<Geofence>();
		
		/*
		 * Creating dummy latitude, longitude values 
		 */
		
		/*
		 * GeoCordinates for the location Banglore Center Railway Station
		 * ( Radius: 3KM)
		 */
		Geofence dummyGeofence1=new Geofence.Builder()
	     							.setRequestId("geofence_id_1")
	     							.setCircularRegion(
	     								12.9765063,   
	     								77.5687821,
	     								3000)
	     							.setExpirationDuration(Geofence.NEVER_EXPIRE)
	     							.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |Geofence.GEOFENCE_TRANSITION_EXIT)
	     							.build();
		
		/*
		 * GeoCordinates for the location Delhi (India Gate)
		 * ( Radius: 3KM)
		 */
		Geofence dummyGeofence2=new Geofence.Builder()
	     							.setRequestId("geofence_id_2")
	     							.setCircularRegion(
	     								28.612912,   
	     							    77.2295097,
	     								3000)
	     							.setExpirationDuration(Geofence.NEVER_EXPIRE)
	     							.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |Geofence.GEOFENCE_TRANSITION_EXIT)
	     							.build();
		
		
		  mGeofenceList.add(dummyGeofence1);
		  mGeofenceList.add(dummyGeofence2);
		  
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
	
		 Toast.makeText(getApplicationContext(), "Google Play Service- conncetion failed !",0).show();	 	 
	}

	@Override
	public void onConnected(Bundle arg0) {		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		 Toast.makeText(getApplicationContext(), "Google Play Service- conncetion suspended !",0).show();	 
	 	
	}


	@Override
	public void onResult(Status status) {
		 
		if (status.isSuccess()) {
		   Toast.makeText(getApplicationContext(), "Geofence added successfully !",0).show();	 
		}
		
	}
 
	
	@Override
	protected void onDestroy() {	
		super.onDestroy();
		
		mGoogleApiClient.disconnect();
	
		mGoogleApiClient=null;
		mGeofenceList=null;
		mGeofencePendingIntent=null;
		buttonAddGeofence=null;
	}
}
