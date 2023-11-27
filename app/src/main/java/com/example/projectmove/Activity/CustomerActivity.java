package com.example.projectmove.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.projectmove.Fragment.ListsFragment;
import com.example.projectmove.R;
import com.example.projectmove.Utilis.Adapter.AdapterDriversJobs;
import com.example.projectmove.Utilis.Class.CustomersJobs;
import com.example.projectmove.Utilis.Class.DriversJobs;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {

    ExtendedFloatingActionButton fab;
    FirebaseAuth firebaseAuth;
    List<DriversJobs> driversJobLists;
    AdapterDriversJobs adapterDriversJobs;
    RecyclerView recyclerView;
    AutoCompleteTextView searchDestination,searchLocation,searchVehicleType;

    LinearLayout filter;

    ImageView indication;


    private ImageView back,locationDropdown,destinationDropdown,vehicleTypeDropDown;

    LottieAnimationView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);


        fab= findViewById(R.id.fab);
        firebaseAuth=FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.driversPostsRecyclerView);
        progressBar=findViewById(R.id.progressBarCustomer);
        progressBar.setVisibility(View.VISIBLE);


        //scrollAnimation
        final int THRESHOLD = 20; // set a threshold to control the scroll distance
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fab.getLayoutParams();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDist = 0;
            boolean isVisible = true;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isVisible && scrollDist > THRESHOLD) {
                    // hide the button if it's visible and scroll distance exceeds threshold
                    isVisible = false;
                    params.setMargins(0, 0, 0, -fab.getHeight());
                    fab.setLayoutParams(params);
                    fab.setVisibility(View.GONE);
                } else if (!isVisible && scrollDist < -THRESHOLD) {
                    // show the button if it's hidden and scroll distance is less than negative threshold
                    isVisible = true;
                    params.setMargins(0, 0, 0, 0);
                    fab.setLayoutParams(params);
                    fab.setVisibility(View.VISIBLE);
                }
                if((isVisible && dy > 0) || (!isVisible && dy < 0)) {
                    // calculate the scroll distance based on the scroll direction
                    scrollDist += dy;
                }
            }
        });









        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CustomerActivity.this, PostCustomerActivity.class);
                startActivity(intent);
            }
        });



        indication=findViewById(R.id.indication);

        back=findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        indication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogForSearch();
            }
        });






        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        driversJobLists=new ArrayList<>();

        loadPosts();



    }

    private void showDialogForSearch() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customer_filter_bottom_sheetlayout);


        searchDestination=dialog.findViewById(R.id.searchDestination);
        searchLocation=dialog.findViewById(R.id.searchLocation);
        searchVehicleType=dialog.findViewById(R.id.searchVehicleType);

        ImageView cancel=dialog.findViewById(R.id.cancel);
        TextView search=dialog.findViewById(R.id.search);

        locationDropdown=dialog.findViewById(R.id.locationDropDown);
        destinationDropdown=dialog.findViewById(R.id.destinationDropDown);
        vehicleTypeDropDown=dialog.findViewById(R.id.vehicleTypeDropDown);

        searchLocation.setThreshold(1);
        searchDestination.setThreshold(1);
        searchVehicleType.setThreshold(1);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,location);
        searchLocation.setAdapter(adapter);

        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,destination);
        searchDestination.setAdapter(adapter1);

        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,vehicleType);
        searchVehicleType.setAdapter(adapter2);

        searchDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        searchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterLocation(editable.toString());
            }
        });

        searchVehicleType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterVehicleType(editable.toString());
            }
        });

        vehicleTypeDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchVehicleType.showDropDown();
            }
        });

        locationDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLocation.showDropDown();
            }
        });

        destinationDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDestination.showDropDown();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void filterVehicleType(String text) {

        ArrayList<DriversJobs> filteredList = new ArrayList<>();

        for (DriversJobs item : driversJobLists) {
            if (item.getpDVehicleType().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapterDriversJobs.filterList(filteredList);


    }

    private void filterLocation(String text) {

        ArrayList<DriversJobs> filteredList = new ArrayList<>();

        for (DriversJobs item : driversJobLists) {
            if (item.getpDLocation().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapterDriversJobs.filterList(filteredList);


    }

    private void filter(String text) {

        ArrayList<DriversJobs> filteredList = new ArrayList<>();

        for (DriversJobs item : driversJobLists) {
            if (item.getpDDestination().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapterDriversJobs.filterList(filteredList);


    }

    private void loadPosts() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversPosts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                driversJobLists.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    DriversJobs driversJobs =ds.getValue(DriversJobs.class);

                    driversJobLists.add(driversJobs);

                    adapterDriversJobs=new AdapterDriversJobs(getApplicationContext(),driversJobLists);
                    recyclerView.setAdapter(adapterDriversJobs);

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private static final String[] location= new String[]{"Debidwar", "Barura", "Brahmanpara", "Chandina", "Chauddagram", "Daudkandi", "Homna", "Laksam", "Muradnagar", "Nangalkot", "Cumillasadar", "Meghna", "Monohargonj", "Sadarsouth", "Titas", "Burichang","Lalmai","Chhagalnaiya","Feni Sadar","Sonagazi","Fulgazi","Parshuram","Daganbhuiyan","Brahmanbaria Sadar","Kasba","Nasirnagar","Sarail","Ashuganj","Akhaura","Nabinagar","Bancharampur","Bijoynagar","Rangamati Sadar","Kaptai","Kawkhali","Baghaichari","Barkal","Langadu","Rajasthali","Belaichari","Juraichari","Naniarchar","Noakhali Sadar","Companiganj","Begumganj","Hatia","Subarnachar","Kabirhat","Senbug","Chatkhil","Sonaimuri","Haimchar","Kachua","Shahrasti","Chandpur Sadar","Matlabsouth","Hajiganj","Matlabnorth","Faridgonj","Lakshmipur Sadar","Kamalnagar","Raipur","Ramgati","Ramganj","Rangunia","Sitakunda","Mirsharai","Patiya","Sandwip","Banshkhali","Boalkhali","Anwara","Chandanaish","Satkania","Lohagara","Hathazari","Fatikchhari","Raozan","Karnafuli","Coxsbazar Sadar","Chakaria","Kutubdia","Ukhiya","Moheshkhali","Pekua","Ramu","Teknaf","Khagrachhari Sadar","Dighinala","Panchari","Laxmichhari","Mohalchari","Manikchari","Ramgarh","Matiranga","Guimara","Bandarban Sadar","Alikadam","Naikhongchhari","Rowangchhari","Lama","Ruma","Thanchi","Belkuchi", "Chauhali", "Kamarkhand", "Kazipur", "Raigonj", "Shahjadpur", "Sirajganjsadar", "Tarash", "Ullapara", "Sujanagar", "Ishurdi", "Bhangura", "Pabnasadar", "Bera", "Atghoria", "Chatmohar", "Santhia", "Faridpur", "Kahaloo", "Bogura Sadar", "Shariakandi", "Shajahanpur", "Dupchanchia", "Adamdighi", "Nondigram", "Sonatala", "Dhunot", "Gabtali", "Sherpur", "Shibganj", "Paba", "Durgapur", "Mohonpur", "Charghat", "Puthia", "Bagha", "Godagari", "Tanore", "Bagmara", "Natoresadar", "Singra", "Baraigram", "Bagatipara", "Lalpur", "Gurudaspur", "Naldanga", "Akkelpur", "Kalai", "Khetlal", "Panchbibi", "Joypurhatsadar", "Chapainawabganjsadar", "Gomostapur", "Nachol", "Bholahat", "Shibganj", "Mohadevpur", "Badalgachi", "Patnitala", "Dhamoirhat", "Niamatpur", "Manda", "Atrai", "Raninagar", "Naogaonsadar", "Porsha", "Sapahar","Manirampur", "Abhaynagar", "Bagherpara", "Chougachha", "Jhikargacha", "Keshabpur", "Jashore Sadar", "Sharsha", "Assasuni", "Debhata", "Kalaroa", "Satkhirasadar", "Shyamnagar", "Tala", "Kaliganj", "Mujibnagar", "Meherpursadar", "Gangni", "Narailsadar", "Lohagara", "Kalia", "Chuadangasadar", "Alamdanga", "Damurhuda", "Jibannagar", "Kushtiasadar", "Kumarkhali", "Khoksa", "Mirpurkushtia", "Daulatpur", "Bheramara", "Shalikha", "Sreepur", "Magurasadar", "Mohammadpur", "Paikgasa", "Fultola", "Digholia", "Rupsha", "Terokhada", "Dumuria", "Botiaghata", "Dakop", "Koyra", "Fakirhat", "Bagerhat Sadar", "Mollahat", "Sarankhola", "Rampal", "Morrelganj", "Kachua", "Mongla", "Chitalmar", "Jhenaidah Sadar", "Shailkupa", "Harinakundu", "Kaliganj", "Kotchandpur", "Moheshpur","Jhalakathi Sadar", "Kathalia", "Nalchity", "Rajapur", "Bauphal", "Patuakhali Sadar", "Dumki", "Dashmina", "Kalapara", "Mirzaganj", "Galachipa", "Rangabali", "Pirojpur Sadar", "Nazirpur", "Kawkhali", "Bhandaria", "Mathbaria", "Nesarabad", "Indurkani", "Barishalsadar", "Bakerganj", "Babuganj", "Wazirpur", "Banaripara", "Gournadi", "Agailjhara", "Mehendiganj", "Muladi", "Hizla", "Bhola Sadar", "Borhanuddin", "Charfesson", "Doulatkhan", "Monpura", "Tazumuddin", "Lalmohan", "Amtali", "Barguna Sadar", "Betagi", "Bamna", "Pathorghata", "Taltali", "Balaganj", "Beanibazar", "Bishwanath", "Companiganj", "Fenchuganj", "Golapganj", "Gowainghat", "Jaintiapur", "Kanaighat", "Sylhetsadar", "Zakiganj", "Dakshinsurma", "Osmaninagar", "Barlekha", "Kamolganj", "Kulaura", "Moulvibazarsadar", "Rajnagar", "Sreemangal", "Juri", "Nabiganj", "Bahubal", "Ajmiriganj", "Baniachong", "Lakhai", "Chunarughat", "Habiganjsadar", "Madhabpur", "Shayestaganj", "Sunamganj Sadar", "Southsunamganj", "Bishwambarpur", "Chhatak", "Jagannathpur", "Dowarabazar", "Tahirpur", "Dharmapasha", "Jamalganj", "Shalla", "Derai","Belabo", "Monohardi", "Narsingdisadar", "Palash", "Raipura", "Shibpur", "Kaliganj", "Kaliakair", "Kapasia", "Gazipur Sadar", "Sreepur", "Shariatpur Sadar", "Naria", "Zajira", "Gosairhat", "Bhedarganj", "Damudya", "Araihazar", "Bandar", "Narayanganjsadar", "Rupganj", "Sonargaon", "Basail", "Bhuapur", "Delduar", "Ghatail", "Gopalpur", "Madhupur", "Mirzapur", "Nagarpur", "Sakhipur", "Tangailsadar", "Kalihati", "Dhanbari", "Itna", "Katiadi", "Bhairab", "Tarail", "Hossainpur", "Pakundia", "Kuliarchar", "Kishoreganjsadar", "Karimgonj", "Bajitpur", "Austagram", "Mithamoin", "Nikli", "Harirampur", "Saturia", "Manikganj Sadar", "Gior", "Shibaloy", "Doulatpur", "Singiar", "Savar", "Dhamrai", "Keraniganj", "Nawabganj", "Dohar", "Munshiganj Sadar", "Sreenagar", "Sirajdikhan", "Louhajanj", "Gajaria", "Tongibari", "Rajbari Sadar", "Goalanda", "Pangsa", "Baliakandi", "Kalukhali", "Madaripur Sadar", "Shibchar", "Kalkini", "Rajoir", "Gopalganj Sadar", "Kashiani", "Tungipara", "Kotalipara", "Muksudpur", "Faridpur Sadar", "Alfadanga", "Boalmari", "Sadarpur", "Nagarkanda", "Bhanga", "Charbhadrasan", "Madhukhali", "Saltha","Panchagarh Sadar", "Debiganj", "Boda", "Atwari", "Tetulia", "Nawabganj", "Birganj", "Ghoraghat", "Birampur", "Parbatipur", "Bochaganj", "Kaharol", "Fulbari", "Dinajpursadar", "Hakimpur", "Khansama", "Birol", "Chirirbandar", "Lalmonirhat Sadar", "Kaliganj", "Hatibandha", "Patgram", "Aditmari", "Syedpur", "Domar", "Dimla", "Jaldhaka", "Kishorganj", "Nilphamarisadar", "Sadullapur", "Gaibandhasadar", "Palashbari", "Saghata", "Gobindaganj", "Sundarganj", "Phulchari", "Thakurgaonsadar", "Pirganj", "Ranisankail", "Haripur", "Baliadangi", "Rangpursadar", "Gangachara", "Taragonj", "Badargonj", "Mithapukur", "Pirgonj", "Kaunia", "Pirgacha", "Kurigramsadar", "Nageshwari", "Bhurungamari", "Phulbari", "Rajarhat", "Ulipur", "Chilmari", "Rowmari", "Charrajibpur","Sherpursadar", "Nalitabari", "Sreebordi", "Nokla", "Jhenaigati", "Fulbaria", "Trishal", "Bhaluka", "Muktagacha", "Mymensinghsadar", "Dhobaura", "Phulpur", "Haluaghat", "Gouripur", "Gafargaon", "Iswarganj", "Nandail", "Tarakanda", "Jamalpursadar", "Melandah", "Islampur", "Dewangonj", "Sarishabari", "Madarganj", "Bokshiganj", "Barhatta", "Durgapur", "Kendua", "Atpara", "Madan", "Khaliajuri", "Kalmakanda", "Mohongonj", "Purbadhala", "Netrokona Sadar"};
    private static final String[] destination= new String[]{"Debidwar", "Barura", "Brahmanpara", "Chandina", "Chauddagram", "Daudkandi", "Homna", "Laksam", "Muradnagar", "Nangalkot", "Cumillasadar", "Meghna", "Monohargonj", "Sadarsouth", "Titas", "Burichang","Lalmai","Chhagalnaiya","Feni Sadar","Sonagazi","Fulgazi","Parshuram","Daganbhuiyan","Brahmanbaria Sadar","Kasba","Nasirnagar","Sarail","Ashuganj","Akhaura","Nabinagar","Bancharampur","Bijoynagar","Rangamati Sadar","Kaptai","Kawkhali","Baghaichari","Barkal","Langadu","Rajasthali","Belaichari","Juraichari","Naniarchar","Noakhali Sadar","Companiganj","Begumganj","Hatia","Subarnachar","Kabirhat","Senbug","Chatkhil","Sonaimuri","Haimchar","Kachua","Shahrasti","Chandpur Sadar","Matlabsouth","Hajiganj","Matlabnorth","Faridgonj","Lakshmipur Sadar","Kamalnagar","Raipur","Ramgati","Ramganj","Rangunia","Sitakunda","Mirsharai","Patiya","Sandwip","Banshkhali","Boalkhali","Anwara","Chandanaish","Satkania","Lohagara","Hathazari","Fatikchhari","Raozan","Karnafuli","Coxsbazar Sadar","Chakaria","Kutubdia","Ukhiya","Moheshkhali","Pekua","Ramu","Teknaf","Khagrachhari Sadar","Dighinala","Panchari","Laxmichhari","Mohalchari","Manikchari","Ramgarh","Matiranga","Guimara","Bandarban Sadar","Alikadam","Naikhongchhari","Rowangchhari","Lama","Ruma","Thanchi","Belkuchi", "Chauhali", "Kamarkhand", "Kazipur", "Raigonj", "Shahjadpur", "Sirajganjsadar", "Tarash", "Ullapara", "Sujanagar", "Ishurdi", "Bhangura", "Pabnasadar", "Bera", "Atghoria", "Chatmohar", "Santhia", "Faridpur", "Kahaloo", "Bogura Sadar", "Shariakandi", "Shajahanpur", "Dupchanchia", "Adamdighi", "Nondigram", "Sonatala", "Dhunot", "Gabtali", "Sherpur", "Shibganj", "Paba", "Durgapur", "Mohonpur", "Charghat", "Puthia", "Bagha", "Godagari", "Tanore", "Bagmara", "Natoresadar", "Singra", "Baraigram", "Bagatipara", "Lalpur", "Gurudaspur", "Naldanga", "Akkelpur", "Kalai", "Khetlal", "Panchbibi", "Joypurhatsadar", "Chapainawabganjsadar", "Gomostapur", "Nachol", "Bholahat", "Shibganj", "Mohadevpur", "Badalgachi", "Patnitala", "Dhamoirhat", "Niamatpur", "Manda", "Atrai", "Raninagar", "Naogaonsadar", "Porsha", "Sapahar","Manirampur", "Abhaynagar", "Bagherpara", "Chougachha", "Jhikargacha", "Keshabpur", "Jashore Sadar", "Sharsha", "Assasuni", "Debhata", "Kalaroa", "Satkhirasadar", "Shyamnagar", "Tala", "Kaliganj", "Mujibnagar", "Meherpursadar", "Gangni", "Narailsadar", "Lohagara", "Kalia", "Chuadangasadar", "Alamdanga", "Damurhuda", "Jibannagar", "Kushtiasadar", "Kumarkhali", "Khoksa", "Mirpurkushtia", "Daulatpur", "Bheramara", "Shalikha", "Sreepur", "Magurasadar", "Mohammadpur", "Paikgasa", "Fultola", "Digholia", "Rupsha", "Terokhada", "Dumuria", "Botiaghata", "Dakop", "Koyra", "Fakirhat", "Bagerhat Sadar", "Mollahat", "Sarankhola", "Rampal", "Morrelganj", "Kachua", "Mongla", "Chitalmar", "Jhenaidah Sadar", "Shailkupa", "Harinakundu", "Kaliganj", "Kotchandpur", "Moheshpur","Jhalakathi Sadar", "Kathalia", "Nalchity", "Rajapur", "Bauphal", "Patuakhali Sadar", "Dumki", "Dashmina", "Kalapara", "Mirzaganj", "Galachipa", "Rangabali", "Pirojpur Sadar", "Nazirpur", "Kawkhali", "Bhandaria", "Mathbaria", "Nesarabad", "Indurkani", "Barishalsadar", "Bakerganj", "Babuganj", "Wazirpur", "Banaripara", "Gournadi", "Agailjhara", "Mehendiganj", "Muladi", "Hizla", "Bhola Sadar", "Borhanuddin", "Charfesson", "Doulatkhan", "Monpura", "Tazumuddin", "Lalmohan", "Amtali", "Barguna Sadar", "Betagi", "Bamna", "Pathorghata", "Taltali", "Balaganj", "Beanibazar", "Bishwanath", "Companiganj", "Fenchuganj", "Golapganj", "Gowainghat", "Jaintiapur", "Kanaighat", "Sylhetsadar", "Zakiganj", "Dakshinsurma", "Osmaninagar", "Barlekha", "Kamolganj", "Kulaura", "Moulvibazarsadar", "Rajnagar", "Sreemangal", "Juri", "Nabiganj", "Bahubal", "Ajmiriganj", "Baniachong", "Lakhai", "Chunarughat", "Habiganjsadar", "Madhabpur", "Shayestaganj", "Sunamganj Sadar", "Southsunamganj", "Bishwambarpur", "Chhatak", "Jagannathpur", "Dowarabazar", "Tahirpur", "Dharmapasha", "Jamalganj", "Shalla", "Derai","Belabo", "Monohardi", "Narsingdisadar", "Palash", "Raipura", "Shibpur", "Kaliganj", "Kaliakair", "Kapasia", "Gazipur Sadar", "Sreepur", "Shariatpur Sadar", "Naria", "Zajira", "Gosairhat", "Bhedarganj", "Damudya", "Araihazar", "Bandar", "Narayanganjsadar", "Rupganj", "Sonargaon", "Basail", "Bhuapur", "Delduar", "Ghatail", "Gopalpur", "Madhupur", "Mirzapur", "Nagarpur", "Sakhipur", "Tangailsadar", "Kalihati", "Dhanbari", "Itna", "Katiadi", "Bhairab", "Tarail", "Hossainpur", "Pakundia", "Kuliarchar", "Kishoreganjsadar", "Karimgonj", "Bajitpur", "Austagram", "Mithamoin", "Nikli", "Harirampur", "Saturia", "Manikganj Sadar", "Gior", "Shibaloy", "Doulatpur", "Singiar", "Savar", "Dhamrai", "Keraniganj", "Nawabganj", "Dohar", "Munshiganj Sadar", "Sreenagar", "Sirajdikhan", "Louhajanj", "Gajaria", "Tongibari", "Rajbari Sadar", "Goalanda", "Pangsa", "Baliakandi", "Kalukhali", "Madaripur Sadar", "Shibchar", "Kalkini", "Rajoir", "Gopalganj Sadar", "Kashiani", "Tungipara", "Kotalipara", "Muksudpur", "Faridpur Sadar", "Alfadanga", "Boalmari", "Sadarpur", "Nagarkanda", "Bhanga", "Charbhadrasan", "Madhukhali", "Saltha","Panchagarh Sadar", "Debiganj", "Boda", "Atwari", "Tetulia", "Nawabganj", "Birganj", "Ghoraghat", "Birampur", "Parbatipur", "Bochaganj", "Kaharol", "Fulbari", "Dinajpursadar", "Hakimpur", "Khansama", "Birol", "Chirirbandar", "Lalmonirhat Sadar", "Kaliganj", "Hatibandha", "Patgram", "Aditmari", "Syedpur", "Domar", "Dimla", "Jaldhaka", "Kishorganj", "Nilphamarisadar", "Sadullapur", "Gaibandhasadar", "Palashbari", "Saghata", "Gobindaganj", "Sundarganj", "Phulchari", "Thakurgaonsadar", "Pirganj", "Ranisankail", "Haripur", "Baliadangi", "Rangpursadar", "Gangachara", "Taragonj", "Badargonj", "Mithapukur", "Pirgonj", "Kaunia", "Pirgacha", "Kurigramsadar", "Nageshwari", "Bhurungamari", "Phulbari", "Rajarhat", "Ulipur", "Chilmari", "Rowmari", "Charrajibpur","Sherpursadar", "Nalitabari", "Sreebordi", "Nokla", "Jhenaigati", "Fulbaria", "Trishal", "Bhaluka", "Muktagacha", "Mymensinghsadar", "Dhobaura", "Phulpur", "Haluaghat", "Gouripur", "Gafargaon", "Iswarganj", "Nandail", "Tarakanda", "Jamalpursadar", "Melandah", "Islampur", "Dewangonj", "Sarishabari", "Madarganj", "Bokshiganj", "Barhatta", "Durgapur", "Kendua", "Atpara", "Madan", "Khaliajuri", "Kalmakanda", "Mohongonj", "Purbadhala", "Netrokona Sadar"};
    private static final String[] vehicleType= new String[]{"Truck", "Covered Van", "Mini Truck", "MicroBus", "CNG","Car"};
    }


