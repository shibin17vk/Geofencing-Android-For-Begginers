package com.way2android.geofence_part1;

import java.util.List;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.geofence_part1.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceReceiver extends IntentService{
 
	public GeofenceReceiver(String name) {
		super(name);		
	}
	
	
	public GeofenceReceiver() {
		super("GeofenceReceiver");		
	} 
	
	@Override
	protected void onHandleIntent(Intent intent) {
		
		 GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
	        if (geofencingEvent.hasError()) {
	        	
	            String errorMessage = geofencingEvent.toString();
	            Log.e("Geofence", errorMessage);
	            return;
	            
	        }
		

	        // Get the transition type.
	        int geofenceTransition = geofencingEvent.getGeofenceTransition();
	        // Test that the reported transition was of interest.
	        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||  geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
	        	
	        	List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
	        	
	        	for (Geofence geofence : triggeringGeofences) {
	        		
	        		String trigeredGeofenceID=geofence.getRequestId();
	        		sendNotification("request_id:"+trigeredGeofenceID);
	        		 
				}
	        	 
	        	
	        }

	}
	 private void sendNotification(String msg) {
	        // Create an explicit content Intent that starts the main Activity.
	        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

	        // Construct a task stack.
	        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

	        // Add the main Activity to the task stack as the parent.
	        stackBuilder.addParentStack(MainActivity.class);

	        // Push the content Intent onto the stack.
	        stackBuilder.addNextIntent(notificationIntent);

	        // Get a PendingIntent containing the entire back stack.
	        PendingIntent notificationPendingIntent =
	                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

	        // Get a notification builder that's compatible with platform versions >= 4
	        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

	        // Define the notification settings.
	        builder.setSmallIcon(R.drawable.ic_launcher)
	                // In a real app, you may want to use a library like Volley
	                // to decode the Bitmap.
	                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
	                        R.drawable.ic_launcher))
	                .setColor(Color.RED)
	                .setContentTitle("Geofence Triggered !")
	                .setContentText(msg)
	                .setContentIntent(notificationPendingIntent);

	        // Dismiss notification once the user touches it.
	        builder.setAutoCancel(true);

	        // Get an instance of the Notification manager
	        NotificationManager mNotificationManager =
	                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

	        // Issue the notification
	        mNotificationManager.notify(0, builder.build());
	    }
}
