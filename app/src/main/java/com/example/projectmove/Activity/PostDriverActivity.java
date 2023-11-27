package com.example.projectmove.Activity;

import static com.example.projectmove.Utilis.Class.Constants.TOPIC;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectmove.Model.NotificationData;
import com.example.projectmove.Model.PushNotification;
import com.example.projectmove.R;
import com.example.projectmove.api.ApiUtilities;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class PostDriverActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] country = { "Truck", "Covered Van", "Mini Truck", "MicroBus", "CNG","Car"};

    //String[] psList=new String[]{"Mirpur","Dhanmondi"};

    Spinner spin;
    String spinnerPosition;

    String myUid;

    EditText dLocationET,dDestinationET;
    AutoCompleteTextView ps;

    //datePicker

    private DatePickerDialog datePickerDialog;
    private Button dStartingDateET;

    //datePicker

    //timePicker
    Button dStartingTimeET;
    int hour, minute;
    //timePicker


    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=200;

    private static final int IMAGE_PICK_CAMERA_CODE=300;
    private static final int IMAGE_PICK_GALLERY_CODE=400;

    String[] cameraPermissions;
    String[] storagePermissions;

    FirebaseAuth firebaseAuth;
    DatabaseReference userDbRef;

    FirebaseUser user;


    EditText dNameET,dExpectedPriceET,dTruckNumberPlate;
    CircleImageView dPickImage;
    LinearLayout dAddPostBTN;

    Uri image_uri=null;
    ProgressDialog pd;

    String username,nationalIdCardNo,uid,dp,phone,pComments="0";
    String policeStation;

    List<String> policeS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_driver);

        ps=findViewById(R.id.et_PS_driver);
        policeS=new ArrayList<>();


        policeS.add("Debidwar");
        policeS.add("Barura");
        policeS.add("Brahmanpara");
        policeS.add("Chandina");
        policeS.add("Chauddagram");
        policeS.add("Daudkandi");
        policeS.add("Homna");
        policeS.add("Laksam");
        policeS.add("Muradnagar");
        policeS.add("Nangalkot");
        policeS.add("Cumillasadar");
        policeS.add("Meghna");
        policeS.add("Monohargonj");
        policeS.add("Sadarsouth");
        policeS.add("Titas");
        policeS.add("Burichang");
        policeS.add("Lalmai");
        policeS.add("Chhagalnaiya");
        policeS.add("Feni Sadar");
        policeS.add("Sonagazi");
        policeS.add("Fulgazi");
        policeS.add("Parshuram");
        policeS.add("Daganbhuiyan");
        policeS.add("Brahmanbaria Sadar");
        policeS.add("Kasba");
        policeS.add("Nasirnagar");
        policeS.add("Sarail");
        policeS.add("Ashuganj");
        policeS.add("Akhaura");
        policeS.add("Nabinagar");
        policeS.add("Bancharampur");
        policeS.add("Bijoynagar");
        policeS.add("Rangamati Sadar");
        policeS.add("Kaptai");
        policeS.add("Kawkhali");
        policeS.add("Baghaichari");
        policeS.add("Barkal");
        policeS.add("Langadu");
        policeS.add("Rajasthali");
        policeS.add("Belaichari");
        policeS.add("Juraichari");
        policeS.add("Naniarchar");
        policeS.add("Noakhali Sadar");
        policeS.add("Companiganj");
        policeS.add("Begumganj");
        policeS.add("Hatia");
        policeS.add("Subarnachar");
        policeS.add("Kabirhat");
        policeS.add("Senbug");
        policeS.add("Chatkhil");
        policeS.add("Sonaimuri");
        policeS.add("Haimchar");
        policeS.add("Kachua");
        policeS.add("Shahrasti");
        policeS.add("Chandpur Sadar");
        policeS.add("Matlabsouth");
        policeS.add("Hajiganj");
        policeS.add("Matlabnorth");
        policeS.add("Faridgonj");
        policeS.add("Lakshmipur Sadar");
        policeS.add("Kamalnagar");
        policeS.add("Raipur");
        policeS.add("Ramgati");
        policeS.add("Ramganj");
        policeS.add("Rangunia");
        policeS.add("Sitakunda");
        policeS.add("Mirsharai");
        policeS.add("Patiya");
        policeS.add("Sandwip");
        policeS.add("Banshkhali");
        policeS.add("Boalkhali");
        policeS.add("Anwara");
        policeS.add("Chandanaish");
        policeS.add("Satkania");
        policeS.add("Lohagara");
        policeS.add("Hathazari");
        policeS.add("Fatikchhari");
        policeS.add("Raozan");
        policeS.add("Karnafuli");
        policeS.add("Coxsbazar Sadar");
        policeS.add("Chakaria");
        policeS.add("Kutubdia");
        policeS.add("Ukhiya");
        policeS.add("Moheshkhali");
        policeS.add("Pekua");
        policeS.add("Ramu");
        policeS.add("Teknaf");
        policeS.add("Khagrachhari Sadar");
        policeS.add("Dighinala");
        policeS.add("Panchari");
        policeS.add("Laxmichhari");
        policeS.add("Mohalchari");
        policeS.add("Manikchari");
        policeS.add("Ramgarh");
        policeS.add("Matiranga");
        policeS.add("Guimara");
        policeS.add("Bandarban Sadar");
        policeS.add("Alikadam");
        policeS.add("Naikhongchhari");
        policeS.add("Rowangchhari");
        policeS.add("Lama");
        policeS.add("Ruma");
        policeS.add("Thanchi");
        policeS.add("Belkuchi");
        policeS.add("Chauhali");
        policeS.add("Kamarkhand");
        policeS.add("Kazipur");
        policeS.add("Raigonj");
        policeS.add("Shahjadpur");
        policeS.add("Sirajganjsadar");
        policeS.add("Tarash");
        policeS.add("Ullapara");
        policeS.add("Sujanagar");
        policeS.add("Ishurdi");
        policeS.add("Bhangura");
        policeS.add("Pabnasadar");
        policeS.add("Bera");
        policeS.add("Atghoria");
        policeS.add("Chatmohar");
        policeS.add("Santhia");
        policeS.add("Faridpur");
        policeS.add("Kahaloo");
        policeS.add("Bogura Sadar");
        policeS.add("Shariakandi");
        policeS.add("Shajahanpur");
        policeS.add("Dupchanchia");
        policeS.add("Adamdighi");
        policeS.add("Nondigram");
        policeS.add("Sonatala");
        policeS.add("Dhunot");
        policeS.add("Gabtali");
        policeS.add("Sherpur");
        policeS.add("Shibganj");
        policeS.add("Paba");
        policeS.add("Durgapur");
        policeS.add("Mohonpur");
        policeS.add("Charghat");
        policeS.add("Puthia");
        policeS.add("Bagha");
        policeS.add("Godagari");
        policeS.add("Tanore");
        policeS.add("Bagmara");
        policeS.add("Natoresadar");
        policeS.add("Singra");
        policeS.add("Baraigram");
        policeS.add("Bagatipara");
        policeS.add("Lalpur");
        policeS.add("Gurudaspur");
        policeS.add("Naldanga");
        policeS.add("Akkelpur");
        policeS.add("Kalai");
        policeS.add("Khetlal");
        policeS.add("Panchbibi");
        policeS.add("Joypurhatsadar");
        policeS.add("Chapainawabganjsadar");
        policeS.add("Gomostapur");
        policeS.add("Nachol");
        policeS.add("Bholahat");
        policeS.add("Shibganj");
        policeS.add("Mohadevpur");
        policeS.add("Badalgachi");
        policeS.add("Patnitala");
        policeS.add("Dhamoirhat");
        policeS.add("Niamatpur");
        policeS.add("Manda");
        policeS.add("Atrai");
        policeS.add("Raninagar");
        policeS.add("Naogaonsadar");
        policeS.add("Porsha");
        policeS.add("Sapahar");
        policeS.add("Manirampur");
        policeS.add("Abhaynagar");
        policeS.add("Bagherpara");
        policeS.add("Chougachha");
        policeS.add("Jhikargacha");
        policeS.add("Keshabpur");
        policeS.add("Jashore Sadar");
        policeS.add("Sharsha");
        policeS.add("Assasuni");
        policeS.add("Debhata");
        policeS.add("Kalaroa");
        policeS.add("Satkhirasadar");
        policeS.add("Shyamnagar");
        policeS.add("Tala");
        policeS.add("Kaliganj");
        policeS.add("Mujibnagar");
        policeS.add("Meherpursadar");
        policeS.add("Gangni");
        policeS.add("Narailsadar");
        policeS.add("Lohagara");
        policeS.add("Kalia");
        policeS.add("Chuadangasadar");
        policeS.add("Alamdanga");
        policeS.add("Damurhuda");
        policeS.add("Jibannagar");
        policeS.add("Kushtiasadar");
        policeS.add("Kumarkhali");
        policeS.add("Khoksa");
        policeS.add("Mirpurkushtia");
        policeS.add("Daulatpur");
        policeS.add("Bheramara");
        policeS.add("Shalikha");
        policeS.add("Sreepur");
        policeS.add("Magura sadar");
        policeS.add("Mohammadpur");
        policeS.add("Paikgasa");
        policeS.add("Fultola");
        policeS.add("Digholia");
        policeS.add("Rupsha");
        policeS.add("Terokhada");
        policeS.add("Dumuria");
        policeS.add("Botiaghata");
        policeS.add("Dakop");
        policeS.add("Koyra");
        policeS.add("Fakirhat");
        policeS.add("Bagerhat Sadar");
        policeS.add("Mollahat");
        policeS.add("Sarankhola");
        policeS.add("Rampal");
        policeS.add("Morrelganj");
        policeS.add("Kachua");
        policeS.add("Mongla");
        policeS.add("Chitalmar");
        policeS.add("Jhenaidah Sadar");
        policeS.add("Shailkupa");
        policeS.add("Harinakundu");
        policeS.add("Kaliganj");
        policeS.add("Kotchandpur");
        policeS.add("Moheshpur");
        policeS.add("Jhalakathi Sadar");
        policeS.add("Kathalia");
        policeS.add("Nalchity");
        policeS.add("Rajapur");
        policeS.add("Bauphal");
        policeS.add("Patuakhali Sadar");
        policeS.add("Dumki");
        policeS.add("Dashmina");
        policeS.add("Kalapara");
        policeS.add("Mirzaganj");
        policeS.add("Galachipa");
        policeS.add("Rangabali");
        policeS.add("Pirojpur Sadar");
        policeS.add("Nazirpur");
        policeS.add("Kawkhali");
        policeS.add("Bhandaria");
        policeS.add("Mathbaria");
        policeS.add("Nesarabad");
        policeS.add("Indurkani");
        policeS.add("Barishalsadar");
        policeS.add("Bakerganj");
        policeS.add("Babuganj");
        policeS.add("Wazirpur");
        policeS.add("Banaripara");
        policeS.add("Gournadi");
        policeS.add("Agailjhara");
        policeS.add("Mehendiganj");
        policeS.add("Muladi");
        policeS.add("Hizla");
        policeS.add("Bhola Sadar");
        policeS.add("Borhanuddin");
        policeS.add("Charfesson");
        policeS.add("Doulatkhan");
        policeS.add("Monpura");
        policeS.add("Tazumuddin");
        policeS.add("Lalmohan");
        policeS.add("Amtali");
        policeS.add("Barguna Sadar");
        policeS.add("Betagi");
        policeS.add("Bamna");
        policeS.add("Pathorghata");
        policeS.add("Taltali");
        policeS.add("Balaganj");
        policeS.add("Beanibazar");
        policeS.add("Bishwanath");
        policeS.add("Companiganj");
        policeS.add("Fenchuganj");
        policeS.add("Golapganj");
        policeS.add("Gowainghat");
        policeS.add("Jaintiapur");
        policeS.add("Kanaighat");
        policeS.add("Sylhetsadar");
        policeS.add("Zakiganj");
        policeS.add("Dakshinsurma");
        policeS.add("Osmaninagar");
        policeS.add("Barlekha");
        policeS.add("Kamolganj");
        policeS.add("Kulaura");
        policeS.add("Moulvibazarsadar");
        policeS.add("Rajnagar");
        policeS.add("Sreemangal");
        policeS.add("Juri");
        policeS.add("Nabiganj");
        policeS.add("Bahubal");
        policeS.add("Ajmiriganj");
        policeS.add("Baniachong");
        policeS.add("Lakhai");
        policeS.add("Chunarughat");
        policeS.add("Habiganjsadar");
        policeS.add("Madhabpur");
        policeS.add("Shayestaganj");
        policeS.add("Sunamganj Sadar");
        policeS.add("Southsunamganj");
        policeS.add("Bishwambarpur");
        policeS.add("Chhatak");
        policeS.add("Jagannathpur");
        policeS.add("Dowarabazar");
        policeS.add("Tahirpur");
        policeS.add("Dharmapasha");
        policeS.add("Jamalganj");
        policeS.add("Shalla");
        policeS.add("Derai");
        policeS.add("Belabo");
        policeS.add("Monohardi");
        policeS.add("Narsingdisadar");
        policeS.add("Palash");
        policeS.add("Raipura");
        policeS.add("Shibpur");
        policeS.add("Kaliganj");
        policeS.add("Kaliakair");
        policeS.add("Kapasia");
        policeS.add("Gazipur Sadar");
        policeS.add("Sreepur");
        policeS.add("Shariatpur Sadar");
        policeS.add("Naria");
        policeS.add("Zajira");
        policeS.add("Gosairhat");
        policeS.add("Bhedarganj");
        policeS.add("Damudya");
        policeS.add("Araihazar");
        policeS.add("Bandar");
        policeS.add("Narayanganjsadar");
        policeS.add("Rupganj");
        policeS.add("Sonargaon");
        policeS.add("Basail");
        policeS.add("Bhuapur");
        policeS.add("Delduar");
        policeS.add("Ghatail");
        policeS.add("Gopalpur");
        policeS.add("Madhupur");
        policeS.add("Mirzapur");
        policeS.add("Nagarpur");
        policeS.add("Sakhipur");
        policeS.add("Tangailsadar");
        policeS.add("Kalihati");
        policeS.add("Dhanbari");
        policeS.add("Itna");
        policeS.add("Katiadi");
        policeS.add("Bhairab");
        policeS.add("Tarail");
        policeS.add("Hossainpur");
        policeS.add("Pakundia");
        policeS.add("Kuliarchar");
        policeS.add("Kishoreganjsadar");
        policeS.add("Karimgonj");
        policeS.add("Bajitpur");
        policeS.add("Austagram");
        policeS.add("Mithamoin");
        policeS.add("Nikli");
        policeS.add("Harirampur");
        policeS.add("Saturia");
        policeS.add("Manikganj Sadar");
        policeS.add("Gior");
        policeS.add("Shibaloy");
        policeS.add("Doulatpur");
        policeS.add("Singiar");
        policeS.add("Savar");
        policeS.add("Dhamrai");
        policeS.add("Keraniganj");
        policeS.add("Nawabganj");
        policeS.add("Dohar");
        policeS.add("Munshiganj Sadar");
        policeS.add("Sreenagar");
        policeS.add("Sirajdikhan");
        policeS.add("Louhajanj");
        policeS.add("Gajaria");
        policeS.add("Tongibari");
        policeS.add("Rajbari Sadar");
        policeS.add("Goalanda");
        policeS.add("Pangsa");
        policeS.add("Baliakandi");
        policeS.add("Kalukhali");
        policeS.add("Madaripur Sadar");
        policeS.add("Shibchar");
        policeS.add("Kalkini");
        policeS.add("Rajoir");
        policeS.add("Gopalganj Sadar");
        policeS.add("Kashiani");
        policeS.add("Tungipara");
        policeS.add("Kotalipara");
        policeS.add("Muksudpur");
        policeS.add("Faridpur Sadar");
        policeS.add("Alfadanga");
        policeS.add("Boalmari");
        policeS.add("Sadarpur");
        policeS.add("Nagarkanda");
        policeS.add("Bhanga");
        policeS.add("Charbhadrasan");
        policeS.add("Madhukhali");
        policeS.add("Saltha");
        policeS.add("Panchagarh Sadar");
        policeS.add("Debiganj");
        policeS.add("Boda");
        policeS.add("Atwari");
        policeS.add("Tetulia");
        policeS.add("Nawabganj");
        policeS.add("Birganj");
        policeS.add("Ghoraghat");
        policeS.add("Birampur");
        policeS.add("Parbatipur");
        policeS.add("Bochaganj");
        policeS.add("Kaharol");
        policeS.add("Fulbari");
        policeS.add("Dinajpursadar");
        policeS.add("Hakimpur");
        policeS.add("Khansama");
        policeS.add("Birol");
        policeS.add("Chirirbandar");
        policeS.add("Lalmonirhat Sadar");
        policeS.add("Kaliganj");
        policeS.add("Hatibandha");
        policeS.add("Patgram");
        policeS.add("Aditmari");
        policeS.add("Syedpur");
        policeS.add("Domar");
        policeS.add("Dimla");
        policeS.add("Jaldhaka");
        policeS.add("Kishorganj");
        policeS.add("Nilphamarisadar");
        policeS.add("Sadullapur");
        policeS.add("Gaibandhasadar");
        policeS.add("Palashbari");
        policeS.add("Saghata");
        policeS.add("Gobindaganj");
        policeS.add("Sundarganj");
        policeS.add("Phulchari");
        policeS.add("Thakurgaonsadar");
        policeS.add("Pirganj");
        policeS.add("Ranisankail");
        policeS.add("Haripur");
        policeS.add("Baliadangi");
        policeS.add("Rangpursadar");
        policeS.add("Gangachara");
        policeS.add("Taragonj");
        policeS.add("Badargonj");
        policeS.add("Mithapukur");
        policeS.add("Pirgonj");
        policeS.add("Kaunia");
        policeS.add("Pirgacha");
        policeS.add("Kurigramsadar");
        policeS.add("Nageshwari");
        policeS.add("Bhurungamari");
        policeS.add("Phulbari");
        policeS.add("Rajarhat");
        policeS.add("Ulipur");
        policeS.add("Chilmari");
        policeS.add("Rowmari");
        policeS.add("Charrajibpur");
        policeS.add("Sherpursadar");
        policeS.add("Nalitabari");
        policeS.add("Sreebordi");
        policeS.add("Nokla");
        policeS.add("Jhenaigati");
        policeS.add("Fulbaria");
        policeS.add("Trishal");
        policeS.add("Bhaluka");
        policeS.add("Muktagacha");
        policeS.add("Mymensinghsadar");
        policeS.add("Dhobaura");
        policeS.add("Phulpur");
        policeS.add("Haluaghat");
        policeS.add("Gouripur");
        policeS.add("Gafargaon");
        policeS.add("Iswarganj");
        policeS.add("Nandail");
        policeS.add("Tarakanda");
        policeS.add("Jamalpursadar");
        policeS.add("Melandah");
        policeS.add("Islampur");
        policeS.add("Dewangonj");
        policeS.add("Sarishabari");
        policeS.add("Madarganj");
        policeS.add("Bokshiganj");
        policeS.add("Barhatta");
        policeS.add("Durgapur");
        policeS.add("Kendua");
        policeS.add("Atpara");
        policeS.add("Madan");
        policeS.add("Khaliajuri");
        policeS.add("Kalmakanda");
        policeS.add("Mohongonj");
        policeS.add("Purbadhala");
        policeS.add("Netrokona Sadar");


        ps.setThreshold(1);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,policeS);
        ps.setAdapter(arrayAdapter);



        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        pd=new ProgressDialog(this);

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        assert user != null;
        myUid=user.getUid();

        checkUserStatus();

        userDbRef=FirebaseDatabase.getInstance().getReference("Users");
        Query query=userDbRef.orderByChild("phone").equalTo(phone);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for(DataSnapshot ds:snapshot.getChildren()){
                     username= ""+ds.child("username").getValue();
                     phone= ""+ds.child("phone").getValue();
                     dp= ""+ds.child("imageUrl").getValue();
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dNameET=findViewById(R.id.dNameET);
        dLocationET=findViewById(R.id.dLocationET);
        dDestinationET=findViewById(R.id.dDestinationET);
        dExpectedPriceET=findViewById(R.id.dExpectedPriceET);
        dStartingTimeET=findViewById(R.id.dStartingTimeET);
        dTruckNumberPlate=findViewById(R.id.dTruckNumberPlate);



        //datePicker

        initDatePicker();
        dStartingDateET=findViewById(R.id.dStartingDateET);


        dStartingDateET.setText(getTodaysDate());

        //datePicker

        dPickImage=findViewById(R.id.dPickImage);

        dAddPostBTN=findViewById(R.id.dAddPostBTN);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spin = (Spinner) findViewById(R.id.spinner_Vehicle_type);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);




        dAddPostBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost();
            }
        });

        dPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

    }


    //timePicker


    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                dStartingTimeET.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    //timePicker


    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dStartingDateET.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    private void checkUserStatus() {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
            phone=user.getPhoneNumber();
            uid=user.getUid();
        }
        else{
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    private void pickImage() {
        showImagePickDialog();
    }

    private void showImagePickDialog() {
        String[] options={"Camera","Gallery"};

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    if(!checkCameraPermissions()){
                        requestCameraPermissions();
                    }
                    else{
                        pickFromCamera();
                    }
                }
                if(i==1){
                    if(!checkStoragePermissions()){
                        requestStoragePermissions();
                    }
                    else{
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void pickFromGallery() {
        Intent intent=new Intent((Intent.ACTION_PICK));
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues cv=new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermissions(){
        boolean result= ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermissions(){
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions(){
        boolean result= ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean resultl= ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && resultl;
    }

    private void requestCameraPermissions(){
        ActivityCompat.requestPermissions(this,storagePermissions,CAMERA_REQUEST_CODE);
    }

    private void prepareNotification(String pId,String title,String description,String notificationType, String notificationTopic){
        String NOTIFICATION_TOPIC="topics"+notificationTopic;
        String NOTIFICATION_TITLE=title;
        String NOTIFICATION_MESSAGE=description;
        String NOTIFICATION_TYPE=notificationType;

        JSONObject notificationJo=new JSONObject();
        JSONObject notificationBodyJo=new JSONObject();

        try {
            notificationBodyJo.put("notificationType",NOTIFICATION_TYPE);
            notificationBodyJo.put("sender",uid);
            notificationBodyJo.put("pId",pId);
            notificationBodyJo.put("pTitle",NOTIFICATION_TITLE);
            notificationBodyJo.put("pDescription",NOTIFICATION_MESSAGE);

            notificationJo.put("to",NOTIFICATION_TOPIC);
            notificationJo.put("data",notificationBodyJo);
        } catch (JSONException e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        sendPostNotification(notificationJo);
    }

    private void sendPostNotification(JSONObject notificationJo) {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("FCM_RESPONSE","onResponse:  "+response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PostDriverActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers=new HashMap<>();

                headers.put("Content-Type","application/json");
                headers.put("Authorization","key=AAAAh8LDDMM:APA91bFEYr5iq3fcIY1qo_DxQRjzfXHBnCLUigPGdeotTO4BhsCzc16r2adAVYrppSLDxEnVhtJDh7e0Xensd5VBfTiPp-XJCIUcccXfLNqRYaiDYs08N_85h35s8VKhkpwLoL3aNrvb");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void addPost() {
        pd.setMessage("Adding Post...");
        pd.show();

        String name=dNameET.getText().toString().trim();
        String location=dLocationET.getText().toString().trim();
        String destination=dDestinationET.getText().toString().trim();
        String expectedPrice=dExpectedPriceET.getText().toString().trim();
        String startingTime=dStartingTimeET.getText().toString().trim();
        String startingDate=dStartingDateET.getText().toString().trim();
        String vehicleNumberPlate= dTruckNumberPlate.getText().toString().trim();
        String vehicleType= spinnerPosition;
        String isAccepted="Not Accepted";
        String uid=user.getUid();
        String policeStation=ps.getText().toString().trim();

        Toast.makeText(this, policeStation, Toast.LENGTH_SHORT).show();



        String timeStamp=String .valueOf(System.currentTimeMillis());

        //subscribe
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+timeStamp+"postDriver").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //addUserIdForNotificationFragment
                String subscribedTopic=timeStamp;
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+timeStamp+"acceptDriver").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //addUserIdForNotificationFragment
                String subscribedTopic=timeStamp+"Accept";
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
            }
        });

        //subscribe
        //FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+vehicleType+"postDriver");
        String subscribeTopics="/topics/"+vehicleType+policeStation+"postDriver";
        FirebaseMessaging.getInstance().subscribeToTopic(subscribeTopics).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //addUserIdForNotificationFragment
                String subscribedTopic=vehicleType+policeStation;
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
            }
        });

        String sSubscribeTopics="/topics/"+vehicleType+policeStation+"postCustomer";



        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter a name", Toast.LENGTH_SHORT).show();
        }


        //Notification
            PushNotification pushNotification = new PushNotification(new NotificationData(name, startingDate + " তারিখ " + startingTime + " টায় একটি গাড়ি বুক হয়েছে"), sSubscribeTopics);
            sendNotification(pushNotification);
























        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversPosts");

        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("uid",uid);
        hashMap.put("uName",username);
        hashMap.put("uPhone",phone);
        hashMap.put("uDp",dp);
        hashMap.put("pId",timeStamp);
        hashMap.put("pDName",name);
        hashMap.put("pDLocation",location);
        hashMap.put("pDDestination",destination);
        hashMap.put("pDExpectedPrice",expectedPrice);
        hashMap.put("pDStartingTime",startingTime);
        hashMap.put("pDStartingDate",startingDate);
        hashMap.put("pTime",timeStamp);
        hashMap.put("pComments",pComments);
        hashMap.put("pDVehicleType",vehicleType);
        hashMap.put("pDNumberPlate",vehicleNumberPlate);
        hashMap.put("isAccepted",isAccepted);
        hashMap.put("psOrThana",policeStation);


        ref.child(timeStamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pd.dismiss();
                        Toast.makeText(PostDriverActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(PostDriverActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });







        // Retrieve a list of user IDs subscribed to the topic
        String subscribedTopic=vehicleType+policeStation;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child("Topics").child(subscribedTopic).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> userIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.getKey();
                    userIds.add(userId);

                }

                for (String userId : userIds) {
                    // TODO: Use the user ID as needed
                    String timestamp=String.valueOf(System.currentTimeMillis());
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Notifications").child(userId).child(timestamp);


                    HashMap<String,Object> hashMap=new HashMap<>();


                    hashMap.put("timeStamp",timestamp);
                    hashMap.put("nId",timestamp);
                    hashMap.put("message",startingDate + " তারিখ " + startingTime + " টায় একটি গাড়ি বুক হয়েছে");
                    hashMap.put("postId",timeStamp);
                    hashMap.put("uName",username);
                    hashMap.put("uDp",dp);
                    hashMap.put("userId",userId);
                    hashMap.put("uPhone",phone);


                    ref.setValue(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(PostDriverActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PostDriverActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });















    }

    private void sendNotification(PushNotification pushNotification) {
        ApiUtilities.getClient().sendNotification(pushNotification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, retrofit2.Response<PushNotification> response) {
                if(response.isSuccessful()){
                    Toast.makeText(PostDriverActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(PostDriverActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(PostDriverActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch ((requestCode)){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted&& storageAccepted){
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(this, "Camera and Storage both permissions are neccessary", Toast.LENGTH_SHORT).show();
                    }
                }
                else{

                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean storageAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromGallery();
                    }
                    else{
                        Toast.makeText(this, "Camera and Storage both permissions are neccessary", Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //image is picked from gallery, get "uri" of image
                image_uri = data.getData();

                //set to imageview
                dPickImage.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image is picked from camera, get uri of image
                dPickImage.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        spinnerPosition=arg0.getItemAtPosition(position).toString().trim();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
