package com.example.panoramasdcard;

import java.io.File;

import com.example.panoramasdcard.utility.PanoramaSDCardUtility;
import com.panoramagl.PLCubicPanorama;
import com.panoramagl.PLCylindricalPanorama;
import com.panoramagl.PLICamera;
import com.panoramagl.PLIPanorama;
import com.panoramagl.PLIView;
import com.panoramagl.PLImage;
import com.panoramagl.PLSpherical2Panorama;
import com.panoramagl.PLSphericalPanorama;
import com.panoramagl.PLView;
import com.panoramagl.PLViewListener;
import com.panoramagl.enumerations.PLCubeFaceOrientation;
import com.panoramagl.hotspots.PLHotspot;
import com.panoramagl.hotspots.PLIHotspot;
import com.panoramagl.ios.structs.CGPoint;
import com.panoramagl.loaders.PLILoader;
import com.panoramagl.loaders.PLJSONLoader;
import com.panoramagl.structs.PLPosition;
import com.panoramagl.transitions.PLITransition;
import com.panoramagl.transitions.PLTransitionBlend;
import com.panoramagl.utils.PLUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends PLView
{
	/**member variables*/
	
	private Spinner mPanoramaTypeSpinner;
	private ZoomControls mZoomControls;
	
	PanoramaSDCardUtility psdUtil;
	
	/**init methods*/
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        
        psdUtil = new PanoramaSDCardUtility(this);
        psdUtil.prepareRequiredDataToSDCard(); 
        
        this.setListener(new PLViewListener()
        {
        	@Override
    		public void onDidClickHotspot(PLIView view, PLIHotspot hotspot, CGPoint screenPoint, PLPosition scene3DPoint)
        	{
        		Toast.makeText(view.getActivity().getApplicationContext(), String.format("You select the hotspot with ID %d", hotspot.getIdentifier()), Toast.LENGTH_SHORT).show();
        	}
        	
        	@Override
        	public void onDidBeginLoader(PLIView view, PLILoader loader)
        	{
        		setControlsEnabled(false);
        	}
        	
        	@Override
        	public void onDidCompleteLoader(PLIView view, PLILoader loader)
        	{
        		setControlsEnabled(true);
        	}
        	
        	@Override
        	public void onDidErrorLoader(PLIView view, PLILoader loader, String error)
        	{
        		setControlsEnabled(true);
        	}
        	
        	@Override
        	public void onDidBeginTransition(PLIView view, PLITransition transition)
        	{
        		setControlsEnabled(false);
        	}
        	
        	@Override
        	public void onDidStopTransition(PLIView view, PLITransition transition, int progressPercentage)
        	{
        		setControlsEnabled(true);
        	}
        	
        	@Override
        	public void onDidEndTransition(PLIView view, PLITransition transition)
        	{
        		setControlsEnabled(true);
        	}
		});
	}
	
	/**
     * This event is fired when root content view is created
     * @param contentView current root content view
     * @return root content view that Activity will use
     */
	@Override
	protected View onContentViewCreated(View contentView)
	{
		//Load layout
		ViewGroup mainView = (ViewGroup)this.getLayoutInflater().inflate(R.layout.activity_main, null);
		//Add 360 view
    	mainView.addView(contentView, 0);
        //Spinner control
        mPanoramaTypeSpinner = (Spinner)mainView.findViewById(R.id.spinner_panorama_type);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.panorama_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPanoramaTypeSpinner.setAdapter(adapter);
        mPanoramaTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
			{
				loadPanoramaFromJSON(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView)
			{
			}
		});
        //Zoom controls
        mZoomControls = (ZoomControls)mainView.findViewById(R.id.zoom_controls);
        mZoomControls.setOnZoomInClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View view)
			{
				PLICamera camera = getCamera();
				if(camera != null)
					camera.zoomIn(true);
			}
		});
        mZoomControls.setOnZoomOutClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View view)
			{
				PLICamera camera = getCamera();
				if(camera != null)
					camera.zoomOut(true);
			}
		});
    	//Return root content view
		return super.onContentViewCreated(mainView);
	}
	
	/**utility methods*/
	
	private void setControlsEnabled(final boolean isEnabled)
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if(mPanoramaTypeSpinner != null)
				{
					mPanoramaTypeSpinner.setEnabled(isEnabled);
					mZoomControls.setIsZoomInEnabled(isEnabled);
					mZoomControls.setIsZoomOutEnabled(isEnabled);
				}
			}
		});
	}
    
    /**load methods*/
    
    /**
     * Load panorama image using JSON protocol
     * @param index Spinner position where 0 = cubic, 1 = spherical2, 2 = spherical, 3 = cylindrical
     */
    private void loadPanoramaFromJSON(int index)
    {
		try
    	{
    		PLILoader loader = null;
    		switch(index)
    		{
	    		case 0:
	    			 //"urlBase": "res://raw",
	    		    //"urlBase": "file:./",
	    		    //"urlBase": "file:///sdcard/Android/data/com.example.panoramasdcard/files",
	    			loader = new PLJSONLoader("file:///sdcard/Android/data/com.example.panoramasdcard/files"+"/json_cubic.data");
	    			//Toast.makeText(this, loader.toString(), Toast.LENGTH_SHORT).show();
//	    			try {
//						File a = new File("file:///sdcard/Android/data/com.example.panoramasdcard/files"+"/json_cubic.data");
//						Toast.makeText(this, "success", Toast.LENGTH_LONG).show();
//					} catch (Exception e) {
//						Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//					}
	    			break;
	    		case 1:
	    			loader = new PLJSONLoader("res://raw/json_spherical2");
	    			break;
	    		case 2:
	    			loader = new PLJSONLoader("res://raw/json_spherical");
	    			break;
	    		case 3:
	    			loader = new PLJSONLoader("res://raw/json_cylindrical");
	    			break;
	    		default:
	    			break;
    		}
    		if(loader != null)
    			this.load(loader, true, new PLTransitionBlend(2.0f));
    	}
    	catch(Throwable e)
    	{
    		Toast.makeText(this.getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
    	}
    }
}
