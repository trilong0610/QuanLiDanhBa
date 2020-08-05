package com.example.huynh.danhba;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.huynh.danhba.Adapter.DanhBaAdapter;
import com.example.huynh.danhba.Adapter.DanhBaManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private DanhBaManager danhBaManager;
    private ListView listviewdanhba;
    //  private List<DanhBa> danhBaList;
    private DanhBaAdapter danhBaAdapter;
    private EditText edittim;
    private Cursor cursor;
    private Button btthem1, btngoi, btnnhantin,btncall;
    private Uri contactsListUri = ContactsContract.Contacts.CONTENT_URI;
    private String upNumber;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions();
        HaiActivity hai = new HaiActivity(this);

        //checkPer();
        AnhXa();

        listviewdanhba.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_xoasua, null);
                dialog.setView(dialogView);
                dialog.setTitle("        BẠN HÃY CHỌN CHỨC NĂNG");
                Button btnsua = dialogView.findViewById(R.id.btn_sua1);
                Button btnxoa = dialogView.findViewById(R.id.btn_xoa1);
                final AlertDialog adialog = dialog.create();
                btnsua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialogsua = new Dialog(MainActivity.this);
                        dialog.setTitle("        THAY ĐỔI THÔNG TIN USER");
                        dialogsua.setContentView(R.layout.dialog_sua);
                        Button btnok = dialogsua.findViewById(R.id.btn_ok);
                        final EditText edttensua = dialogsua.findViewById(R.id.ed_tensua);
                        final EditText edtsdtsua = dialogsua.findViewById(R.id.ed_sdtsua);
                        edttensua.setText(danhBaAdapter.getItem(position).getTen().toString());
                        edtsdtsua.setText(danhBaAdapter.getItem(position).getSdt().toString());
                        Button btnhuy = dialogsua.findViewById(R.id.btn_huy1);
                        btnok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String ten, tenUp, soUp;
                                tenUp = edttensua.getText().toString();
                                soUp = edtsdtsua.getText().toString();
                                ten = danhBaAdapter.getItem(position).getTen().toString();

                                //so=danhBaAdapter.getItem(position).getSdt().toString();
                                xoacontact(MainActivity.this, ten);
                                addcontact(tenUp, soUp);

                                // Log.d("data", "onClick: "+tenUp+"...."+soUp);//da set dc ten + sdt moi

                                setadapter();
                                dialogsua.cancel();
                                adialog.cancel();
                            }
                        });

                        btnhuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogsua.cancel();
                            }
                        });
                        dialogsua.show();
                    }
                });
                btnxoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        xoacontact(MainActivity.this, danhBaAdapter.getItem(position).getTen().toString());
                        Toast.makeText(getBaseContext(), "Đã Xóa Thành Công Số Liên Lạc", Toast.LENGTH_SHORT).show();
                        setadapter();
                        adialog.cancel();
                    }
                });
                adialog.show();
                return true;
            }
        });


        listviewdanhba.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog,null);
                dialog.setView(dialogView);
                dialog.setTitle("        BẠN HÃY CHỌN CHỨC NĂNG");
                dialog.show();
                Button btngoi = dialogView.findViewById(R.id.btn_goi);
                Button btnnhantin = dialogView.findViewById(R.id.btn_nhantin);
                final AlertDialog adialog = dialog.create();
                btngoi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intentGoi(position);
                    }
                });
                btnnhantin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intentNhantin(position);
                    }
                });
                return;
            }
        });
        btthem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog̣̣̣̣̣̣̣̣̣̣̣̣̣̣̣̣̣̣̣̣̣();

            }
        });
        btncall.setOnClickListener(new View.OnClickListener() { // Sai ở đây nè
            // goi activity ban phim
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Call.class);
                startActivity(intent);
            }
        });

        setadapter();


        cursor = managedQuery(contactsListUri, null, null, null, null);
        edittim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = edittim.getText().toString().toLowerCase(Locale.getDefault());
                danhBaAdapter.filter(text);
            }
        });


    }

    public void setadapter() {
        danhBaManager = new DanhBaManager(MainActivity.this);
        danhBaAdapter = new DanhBaAdapter(MainActivity.this, R.layout.dong_danh_ba, danhBaManager.getMdanhBaList());
        listviewdanhba.setAdapter(danhBaAdapter);
        danhBaAdapter.notifyDataSetChanged();
    }

    private void showDialog̣̣̣̣̣̣̣̣̣̣̣̣̣̣̣̣̣̣̣̣̣() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_dialog, null);
        dialog.setView(dialogView);
        dialog.setTitle("        BẠN HÃY NHẬP THÔNG TIN");
        final EditText edit_ten = dialogView.findViewById(R.id.ed_ten);
        final EditText edit_sdt = dialogView.findViewById(R.id.ed_sdt);
        Button them = (Button) dialogView.findViewById(R.id.btn_them);
        Button huy = (Button) dialogView.findViewById(R.id.btn_huy);
        final AlertDialog adialog = dialog.create();
        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edit_ten.getText().toString();
                String sdt = edit_sdt.getText().toString();
                if (ten.length() <= 0){ // check ten
                    Toast.makeText(getBaseContext(), "        Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
                }
                else
                    if (sdt.length() <= 0){ // check sdt
                        Toast.makeText(getBaseContext(), "        Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (checkFormatPhone(sdt) == -1){ // check do dai
                            Toast.makeText(getBaseContext(), "        Độ dài số không đúng", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (checkFormatPhone(sdt) == 0){
                                Toast.makeText(getBaseContext(), "       Sai đầu số\nĐầu số đúng là 0 hoặc 84", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (checkTrung(sdt,danhBaAdapter)){
                                    addcontact(edit_ten.getText().toString(), sdt);
                                    setadapter();
                                    adialog.cancel();
                                }
                                else
                                    Toast.makeText(getBaseContext(), "       Số đã tồn tại", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
            }
        });
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adialog.cancel();
            }
        });
        adialog.show();
    }
    public  boolean checkTrung(String sdt, DanhBaAdapter danhBaAdapter){
        for (int i = 0; i <= danhBaAdapter.getCount(); i++ ){
            if (sdt.compareToIgnoreCase(danhBaAdapter.getItem(i).getSdt()) == 0)
                return false;
        }
        return true;

    }
    public int checkFormatPhone(String sdt) {
        if (sdt.length() == 11) { // dau so 84
            String dauSo = String.valueOf(sdt.charAt(0)) + String.valueOf(sdt.charAt(1)) + String.valueOf(sdt.charAt(2));
            Toast.makeText(getBaseContext(),dauSo,Toast.LENGTH_LONG).show();
            if (dauSo.compareToIgnoreCase("848") == 0 ||
                    dauSo.compareToIgnoreCase("849") == 0 ||
                    dauSo.compareToIgnoreCase("843") == 0 ||
                    dauSo.compareToIgnoreCase("847") == 0 ||
                    dauSo.compareToIgnoreCase("845") == 0
            )
                return 1;
            else
                return 0;


        } else {
            if (sdt.length() == 10) { // dau so 0
                String dauSo = String.valueOf(sdt.charAt(0) + String.valueOf(sdt.charAt(1)));
                if (dauSo.compareToIgnoreCase("08") == 0 ||
                        dauSo.compareToIgnoreCase("09") == 0 ||
                        dauSo.compareToIgnoreCase("03") == 0 ||
                        dauSo.compareToIgnoreCase("07") == 0 ||
                        dauSo.compareToIgnoreCase("05") == 0)
                {
                    return 1;
                } else
                    return 0;
            }

        }
        return -1;
    }

    public void intentGoi(int i)
    {

        Intent intentCall=new Intent();
        intentCall.setAction(Intent.ACTION_CALL);
        intentCall.setData(Uri.parse("tel:"+danhBaAdapter.getItem(i).getSdt()));
        startActivity(intentCall);
    }
    public void intentNhantin(int i)
    {
        Intent intentCall=new Intent();
        intentCall.setAction(Intent.ACTION_VIEW);
        intentCall.setData(Uri.parse("sms:"+danhBaAdapter.getItem(i).getSdt()));
        startActivity(intentCall);
    }
    private void addcontact(String name, String phone) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactID = ops.size();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name).build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(getBaseContext(), "Đã Thêm Thành Công Số Liên Lạc", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
        danhBaManager.getMdanhBaList();
        //ops.clear();
        //danhBaList.clear();
        //listviewdanhba.invalidate();

        danhBaAdapter.notifyDataSetChanged();

    }

    private void xoacontact(Context context, String name) {
        ContentResolver cr = getContentResolver();
        String where = ContactsContract.Data.DISPLAY_NAME + " = ? ";
        String[] params = new String[]{name};

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(where, params)
                .build());
        try {
            cr.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {

        }
    }

    public void AnhXa() {

        listviewdanhba = (ListView) findViewById(R.id.listview);

        btthem1 = findViewById(R.id.btnthem1);
        edittim = findViewById(R.id.editsearch);
        btncall = findViewById(R.id.btncall);

        //danhBaList=new ArrayList<>();


    }
    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }

    /*public void checkPer() {
        String[] per = new String[]{Manifest.permission_group.PHONE,Manifest.permission_group.SMS,Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS,Manifest.permission_group.STORAGE};
        List<String> listPer = new ArrayList<>();
        for (String permission : per) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPer.add(permission);
            }
            else
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        1);

            }
        }
        if (!listPer.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPer.toArray(new String[listPer.size()]), 1);

        }
    }*/
    public class HaiActivity extends BroadcastReceiver {
        Context ct1;

        public HaiActivity(Context ct1) {
            this.ct1 = ct1;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            ct1 = context;
            // lay TELEPHONY MANAGER de dang ki lang nghe su kien
            TelephonyManager tmgr = (TelephonyManager) ct1
                    .getSystemService(Context.TELEPHONY_SERVICE);

//Tao Listner
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();


// Dang ki listener cho LISTEN_CALL_STATE
            tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        }
        private class MyPhoneStateListener extends PhoneStateListener {
            public void onCallStateChanged(int state, String incomingNumber) {

                // state = 1 khi chuong reng
                if (state == 1) {

                    String msg = " CÓ CUỘC GỌI ĐẾN VỚI SỐ ĐIỆN THOẠI : " + incomingNumber;
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(ct1, msg, duration);
                    toast.show();

                }
            }
        }}




}

