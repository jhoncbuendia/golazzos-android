package games.buendia.jhon.golazzos.npaysdkdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.view.View;

//Mandatory imports to use NPay Object
import games.buendia.jhon.golazzos.R;
import io.npay.activity.NPay;

//Mandatory imports to use NPayCatalogItem
import java.util.List;
import io.npay.catalog.NPayCatalogItem;
import io.npay.catalog.OnCatalogReceivedListener;

//Mandatory imports to use NPayResponseData
import io.npay.purchase_payment.NPayResponseData;
import io.npay.purchase_payment.OnPurchaseDataListener;
import io.npay.resources.NPayConstants;

//Mandatory imports to get Subscription status
import io.npay.subscriptions.OnSubscriptionStatusReceivedListener;


public class MainActivity extends Activity {
	
	//global variables for Demo
	public String sku;
	public int items_qty = 0;
	
	//Instance of NPay Object
	NPay npay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Initialize NPay Object
  		npay = new NPay(this);
  		
  		//Implementation of the Listener that will get the items
		OnCatalogReceivedListener listen = new OnCatalogReceivedListener(){
			
			//Method that has to be overridden in order to use the received items
			@Override
			public void OnCatalogReceived(int responseType, List<NPayCatalogItem> result){
				
				//local variable for Demo 
				String msg;
				
				/* Code to process the received items using List<NPayCatalogItem>, for example: */
				switch(responseType){
					case NPayConstants.CATALOG_ITEMS:
						
						//Items received when calling getCatalog();
						Log.d("CATALOG_ITEMS", "Catalog Received");
						
						//Store the quantity of items gotten
						items_qty = result.size();
						
						//set message
						msg = "Catalog with "+Integer.toString(items_qty)+" items received!";
						
						//set first item's SKU received
						sku = result.get(0).getSku();
				  		
						break;
						
					case NPayConstants.PURCHASED_ITEMS:
						
						//Items received when calling getPurchasedNonConsumableItems();
						Log.d("PURCHASED_ITEMS", "Previous Purchases Received");
						
						//Set message
						msg = Integer.toString(result.size())+" previously Purchased Non Consumable Items received!";
						
						break;
						
					default:
						//Set message
						msg = "";
						break;
				}
				
				//print message on screen
				Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
		  		toast.show();
				
				Log.e("ITEMS_TYPE", Integer.toString(responseType));
				
				Log.e("NUMBER_OF_ITEMS", Integer.toString(result.size()));
				
				//We iterate all the received items
				for (NPayCatalogItem item : result) {
					
					// Use of public functions from NPayCatalogItem to get the item information
					Log.d("ITEM", "----------------------------------------------");
					Log.e("ID_ITEM", item.getId_item());
					Log.e("SKU", item.getSku());
					Log.e("TYPE", Integer.toString(item.getI_type()));
					Log.e("NAME", item.getName());
					Log.e("DESCRIPTION", item.getDescription());
					Log.e("BASE_PRICE", item.getformattedBasePrice());
					Log.e("LOCAL_PRICE", item.getformattedLocalPrice());
					Log.e("IS_PURCHASED", Boolean.toString(item.isPurchased()));
					Log.e("IS_CONSUMABLE", Boolean.toString(item.isConsumable()));
					Log.e("OBJECT_STRING", item.toString());
					
				}
				/*  */
				
			}
			
		};
		
		//Implementation of the Listener that will get Checkout response
  		OnPurchaseDataListener listen2 = new OnPurchaseDataListener(){

  			//Method that has to be overridden in order to get the checkout information 
			@Override
			public void onPurchaseDataReceive(NPayResponseData checkout) {
				// Use of public functions from NPayResponseData to get the checkout information
				Log.d("TRANSACTION", "----------------------------------------------");
				Log.e("ID_CHECKOUT", checkout.getId_checkout());
				Log.e("ID_TRANSACTION", checkout.getId_trx());
				Log.e("SKU", checkout.getSku());
				Log.e("ID_CUSTOMER", checkout.getId_customer());
				Log.e("STATUS", checkout.getStatus());
				
				//print message on screen
				Toast toast = Toast.makeText(getApplicationContext(), "Checkout complete! Status: "+checkout.getStatus(), Toast.LENGTH_LONG);
		  		toast.show();
				
			}
  			
  		};
  	
  		//Implementation of the Listener that will get the subscription status
  		OnSubscriptionStatusReceivedListener listen3 = new OnSubscriptionStatusReceivedListener(){

			@Override
			public void onSubscriptionStatusReceive(boolean subscriptionStatus) {
				String msg;
				
				if(subscriptionStatus){
					Log.d("SUBSCRIPTION_STATUS", "Grant Access!");
					msg = "Access Granted!";
				}else{
					Log.d("SUBSCRIPTION_STATUS", "Deny Access!");
					msg = "Access Denied!";
				}
				
				//print message on screen
				Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
		  		toast.show();
			}
  			
  		};
		
		//Set listener to receive the items
  		npay.setOnCatalogReceivedListener(listen);
  		
  		//Set listener to receive the checkout response
  		npay.setOnOnPurchaseDataListener(listen2);
  		
  		//Set listener to receive subscription_status response
  		npay.setSubscriptionStatusReceivedListener(listen3);
	}
	
	//Action when button is clicked
		public void getCatalogButtonClick(View view){
			
			//print message on screen
			Toast toast = Toast.makeText(getApplicationContext(), "Getting Catalog...", Toast.LENGTH_LONG);
	  		toast.show();
	  		
			//Get the configured catalog
	  		npay.getNpayCatalog().getCatalog();
	  		
		}
		
		//Action when button is clicked
		public void requestPurchaseButtonClick(View view){
			//local variable for Demo
			String msg;
			
			//Check if catalog is empty 
	  		if(this.items_qty == 0){
	  			//set message
	  			msg = "Empty Catalog";
	  		}else{
	  			
	  			//Request purchase of an item using its SKU. Set manually or gotten using NPayCatalogItem.getSku(); from Catalog
	  			
	  			//Uncomment this line if you want to set a specific SKU
	  	  		//this.sku = "";
	  			
	  			//Check if SKU is empty
	  			if(sku == ""){
	  				//set message
	  	  	  		msg = "Item's SKU is not specified!";
	  			}else{ 
	  				
		  	  		// Put this line of code within a button onClick event or the desired action that triggers the payment request
		  	  		npay.requestPurchase(sku);
		  	  		
		  	  		//set message
		  	  		msg = "R equesting Purchase of item whith SKU: "+this.sku+" ...";
		  	  		
	  			}
	  		}
	  		
	  		//print message on screen
			Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
	  		toast.show();
			
		}
		
		//Action when button is clicked
		public void restorePurchasedItemsButtonClick(View view){
			//print message on screen
			Toast toast = Toast.makeText(getApplicationContext(), "Restoring Previously Purchased Non Consumable Items...", Toast.LENGTH_LONG);
	  		toast.show();
			  		
			//Get the previously purchased items (Non Consumable Items only)
	  		// Put this line of code within a button onClick event or the desired action that triggers this action
	  		npay.getNpayCatalog().getPurchasedNonConsumableItems();
		}
		
		//Action when button is clicked
		public void getSubscriptionStatusButtonClick(View view){
			//print message on screen
			Toast toast = Toast.makeText(getApplicationContext(), "Getting Subscription Status...", Toast.LENGTH_LONG);
	  		toast.show();
			
	  		npay.getSubscriptionStatus();
		}
}
