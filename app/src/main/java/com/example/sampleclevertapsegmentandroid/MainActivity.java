package com.example.sampleclevertapsegmentandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import com.example.sampleclevertapsegmentandroid.adapter.ViewAdapter;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;
import com.segment.analytics.Traits;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DisplayUnitListener {
    private static final String TAG = String.format("%s.%s", "CLEVERTAP", MainActivity.class.getName());

    AppCompatEditText etName, etIdentity, etEmail, etMobile;
    AppCompatSpinner spnGender;
    AppCompatTextView tvSelectedDate;
    LinearLayout llBanner;
    ViewPager viewPager;
    SpringDotsIndicator dot1;
    Button btnDatePicker, btnLogin, btnPushProfile, btnReset, btnPushEventNoProperty, btnPushEventStringProperty, btnPushEventIntegerProperty, btnPushEventFloatProperty, btnPushEventBooleanProperty, btnPushEventDateProperty, btnPushChargedEvent,btnScreenEvent,btnAppInbox,btnNativeDisplay;
    CleverTapAPI cleverTapAPI;
    private static final String CLEVERTAP_KEY = "CleverTap";
    ViewAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etIdentity = findViewById(R.id.etIdentity);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        spnGender = findViewById(R.id.spnGender);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnDatePicker = findViewById(R.id.btnDatePicker);
        btnLogin = findViewById(R.id.btnLogin);
        btnPushProfile = findViewById(R.id.btnPushProfile);
        btnReset = findViewById(R.id.btnReset);
        btnScreenEvent = findViewById(R.id.btnScreenEvent);
        btnPushEventNoProperty = findViewById(R.id.btnPushEventNoProperty);
        btnPushEventStringProperty = findViewById(R.id.btnPushEventStringProperty);
        btnPushEventIntegerProperty = findViewById(R.id.btnPushEventIntegerProperty);
        btnPushEventFloatProperty = findViewById(R.id.btnPushEventFloatProperty);
        btnPushEventBooleanProperty = findViewById(R.id.btnPushEventBooleanProperty);
        btnPushEventDateProperty = findViewById(R.id.btnPushEventDateProperty);
        btnPushChargedEvent = findViewById(R.id.btnPushChargedEvent);
        btnAppInbox = findViewById(R.id.btnAppInbox);
        btnNativeDisplay = findViewById(R.id.btnNativeDisplay);
        viewPager=findViewById(R.id.view_pager);
        dot1=findViewById(R.id.dot1);
        llBanner=findViewById(R.id.llBanner);

        btnDatePicker.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnPushProfile.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnScreenEvent.setOnClickListener(this);
        btnPushEventNoProperty.setOnClickListener(this);
        btnPushEventStringProperty.setOnClickListener(this);
        btnPushEventIntegerProperty.setOnClickListener(this);
        btnPushEventFloatProperty.setOnClickListener(this);
        btnPushEventBooleanProperty.setOnClickListener(this);
        btnPushEventDateProperty.setOnClickListener(this);
        btnPushChargedEvent.setOnClickListener(this);
        btnAppInbox.setOnClickListener(this);
        btnNativeDisplay.setOnClickListener(this);

        cleverTapAPI = CleverTapAPI.getDefaultInstance(this);
        cleverTapAPI.initializeInbox();
        cleverTapAPI.setDisplayUnitListener(this);

        viewAdapter=new ViewAdapter(this,cleverTapAPI);
        viewPager.setAdapter(viewAdapter);
        dot1.setViewPager(viewPager);
    }

    @SuppressLint({"SimpleDateFormat", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnDatePicker:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                ((DatePickerFragment) newFragment).setDateSelectionListener(new DatePickerFragment.DateSelectionListener() {
                    @Override
                    public void onDateSelected(String selectedDate) {
                        tvSelectedDate.setText(selectedDate);
                    }
                });
                break;
            case R.id.btnLogin:
                Traits traitsLogin = new Traits();
                StringBuilder stringBuilder  = new StringBuilder();
                if (TextUtils.isEmpty(etIdentity.getText().toString())){
                    etIdentity.setError("Identity is Mandatory");
                    break;
                }else {
                    stringBuilder.append("Identity : "+etIdentity.getText().toString()+"\n");
                }
                if (!TextUtils.isEmpty(etName.getText().toString())){
                    traitsLogin.putName(etName.getText().toString());
                    stringBuilder.append("Name : "+etName.getText().toString()+"\n");
                }
                if (!TextUtils.isEmpty(etEmail.getText().toString())){
                    traitsLogin.putEmail(etEmail.getText().toString());
                    stringBuilder.append("Email : "+etEmail.getText().toString()+"\n");
                }
                if (!TextUtils.isEmpty(etMobile.getText().toString())){
                    if (etMobile.getText().toString().length() == 10){
                        traitsLogin.putPhone("+91"+etMobile.getText().toString());
                        stringBuilder.append("Mobile : +91"+etMobile.getText().toString()+"\n");
                    }else {
                        etMobile.setError("Please enter valid 10 digit mobile number");
                        break;
                    }
                }
                traitsLogin.putGender(spnGender.getSelectedItem().toString());
                stringBuilder.append("Gender : "+spnGender.getSelectedItem().toString()+"\n");

                if (!TextUtils.isEmpty(tvSelectedDate.getText().toString())){
                    Date dob1 = new Date();
                    try {
                        dob1 =new SimpleDateFormat("dd/MM/yyyy").parse(tvSelectedDate.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //Birthday key will not set DOB on CleverTap
                    //traitsLogin.putBirthday(dob1);
                    traitsLogin.putValue("DOB",dob1);
                    stringBuilder.append("DOB : "+tvSelectedDate.getText().toString()+"\n");
                }
                //Avatar will not set the Profile picture on CleverTap
                //traitsLogin.putAvatar("https://i.ibb.co/pJ8D5Z7/woman-g40aa99db5-1920.jpg");
                if (spnGender.getSelectedItem().toString().equalsIgnoreCase("MALE")){
                    traitsLogin.putValue("Photo","https://eu1.dashboard.clevertap.com/images/DemoStoreMales/image-20.png");
                }else {
                    traitsLogin.putValue("Photo","https://eu1.dashboard.clevertap.com/images/DemoStoreFemales/image-1.png");
                }

                Analytics.with(getApplicationContext()).identify(etIdentity.getText().toString(),traitsLogin,null);
                showAlertDialog("Identify",stringBuilder.toString(),"Login");
                break;
            case R.id.btnPushProfile:

                //It will append the identity and will raise identity set event.
                Analytics.with(getApplicationContext()).alias(etIdentity.getText().toString());
                showAlertDialog("Alias","Identity: "+etIdentity.getText().toString(),"Login");
                break;
            case R.id.btnReset:
                //Analytics.with(getApplicationContext()).reset();
                showAlertDialog("Reset","reset() method doesn't work for Clevertap.","");
                break;
            case R.id.btnScreenEvent:
                //Analytics.with(getApplicationContext()).screen("Main Screen");
                showAlertDialog("Screen","Screen Event doesn't work for Clevertap.","");
                break;
            case R.id.btnPushEventNoProperty:
                Analytics.with(getApplicationContext()).track("Product Viewed");
                showAlertDialog("Track","Track event with No Property called","");
                break;
            case R.id.btnPushEventStringProperty:
                Analytics.with(getApplicationContext()).track("Product Viewed",new Properties().putValue("Product Name","Apple Macbook Pro"));
                String productName = "<br><b>Product Name: Apple Macbook Pro </b>";
                showAlertDialog("Track","Track event with String Property raised. "+ productName,"");
                break;
            case R.id.btnPushEventIntegerProperty:
                Analytics.with(getApplicationContext()).track("Product Viewed",new Properties().putValue("Product Id",1001));
                String productId = "<br><b>Product Id: 1001</b>";
                showAlertDialog("Track","Track event with Integer Property raised. "+productId,"");
                break;
            case R.id.btnPushEventFloatProperty:
                Analytics.with(getApplicationContext()).track("Product Viewed",new Properties().putValue("Product Amount",200.15f));
                String productAmount = "<br><b>Product Amount: 200.15</b>";
                showAlertDialog("Track","Track event with Float Property raised. "+productAmount,"");
                break;
            case R.id.btnPushEventBooleanProperty:
                boolean b = true;
                Analytics.with(getApplicationContext()).track("Product Viewed",new Properties().putValue("In-Stock",b));
                String productInStock = "<br><b>In-Stock: TRUE</b>";
                showAlertDialog("Track","Track event with Boolean Property raised. "+productInStock,"");
                break;
            case R.id.btnPushEventDateProperty:
                Analytics.with(getApplicationContext()).track("Product Viewed",new Properties().putValue("Purchase Date",new Date()));
                String purchaseDate = "<br><b>Purchase Date: "+new SimpleDateFormat("dd/MM/yyyy").format(new Date())+"</b>";
                showAlertDialog("Track","Track event with Date Property raised. "+purchaseDate,"");
                break;
            case R.id.btnPushChargedEvent:
                final String orderId = "123456";
                final int revenue = 100;
                Properties properties = new Properties();
                properties.putValue("orderId", orderId).putValue("revenue", revenue);

                Properties.Product product1 = new Properties.Product("id1", "sku1", 100);
                Properties.Product product2 = new Properties.Product("id2", "sku2", 200);
                properties.putProducts(product1, product2);

                Analytics.with(getApplicationContext()).track("Order Completed", properties);

                String builder = "<br><b>Order Id : " + orderId +
                        "<br>Revenue : " + revenue +
                        "<br>Products : " +
                        "<br>Product Id : id1 SKU: sku1 Price: 100" +
                        "<br>Product Id : id2 SKU: sku2 Price: 200</b>";

                showAlertDialog("Track","Charged (Order Completed) called. "+ builder,"");
                break;
            case R.id.btnAppInbox:
                ArrayList<String> tabs = new ArrayList<>();
                tabs.add("Promotions");
                tabs.add("Offers");//We support upto 2 tabs only. Additional tabs will be ignored

                CTInboxStyleConfig styleConfig = new CTInboxStyleConfig();
                styleConfig.setFirstTabTitle("First Tab");
                styleConfig.setTabs(tabs);//Do not use this if you don't want to use tabs
                styleConfig.setTabBackgroundColor("#FF0000");
                styleConfig.setSelectedTabIndicatorColor("#0000FF");
                styleConfig.setSelectedTabColor("#0000FF");
                styleConfig.setUnselectedTabColor("#FFFFFF");
                styleConfig.setBackButtonColor("#FF0000");
                styleConfig.setNavBarTitleColor("#FF0000");
                styleConfig.setNavBarTitle("MY INBOX");
                styleConfig.setNavBarColor("#FFFFFF");
                styleConfig.setInboxBackgroundColor("#ADD8E6");
                if (cleverTapAPI != null) {
                    cleverTapAPI.showAppInbox(styleConfig); //With Tabs
                }
                break;
            case R.id.btnNativeDisplay:
                boolean isNativeDisplay = true;
                Analytics.with(getApplicationContext()).track("Product Viewed",new Properties().putValue("IsNativeDisplay",isNativeDisplay));
                Toast.makeText(this, "Product Viewed raised for Native Display", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }

    }

    @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {

        //showAlertDialog("Native Display Data", units.toString(),"");

        if (!units.isEmpty()){
            llBanner.setVisibility(View.VISIBLE);
            btnNativeDisplay.requestFocus();
            ArrayList<CleverTapDisplayUnit> displayUnits = new ArrayList<>();
            ArrayList<CleverTapDisplayUnitContent> cleverTapDisplayUnitContents = new ArrayList<>();
            for (int i = 0; i <units.size() ; i++) {
                cleverTapDisplayUnitContents.addAll(units.get(i).getContents());
                for (CleverTapDisplayUnitContent cleverTapDisplayUnitContent:
                     cleverTapDisplayUnitContents) {
                    displayUnits.add(units.get(i));
                }
            }
            viewAdapter.setImages(displayUnits,cleverTapDisplayUnitContents);
            viewAdapter.notifyDataSetChanged();
            cleverTapAPI.pushDisplayUnitViewedEventForID(displayUnits.get(0).getUnitID());

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    Toast.makeText(MainActivity.this, "Selected Item: "+units.size(), Toast.LENGTH_SHORT).show();
                    cleverTapAPI.pushDisplayUnitViewedEventForID(displayUnits.get(position).getUnitID());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        DateSelectionListener dateSelectionListener;

        public void setDateSelectionListener(DateSelectionListener dateSelectionListener) {
            this.dateSelectionListener = dateSelectionListener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            dateSelectionListener.onDateSelected(String.format("%02d/%02d/%d",day,month+1,year));
        }

        interface DateSelectionListener{
            void onDateSelected(String selectedDate);
        }
    }

    public void showAlertDialog(String title, String message, String type){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(Html.fromHtml(message,Html.FROM_HTML_MODE_LEGACY))
                //.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        if (type.equals("Login")) {
                            etIdentity.setText("");
                            etMobile.setText("");
                            etName.setText("");
                            etEmail.setText("");
                            spnGender.setSelection(0);
                            tvSelectedDate.setText("");
                        }
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}