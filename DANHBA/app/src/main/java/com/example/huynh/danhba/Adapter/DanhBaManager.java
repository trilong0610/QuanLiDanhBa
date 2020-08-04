package com.example.huynh.danhba.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import com.example.huynh.danhba.Model.DanhBa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DanhBaManager {
    public Context mcontext;
    public List<DanhBa> mdanhBaList= new ArrayList<>();;


    public DanhBaManager(Context context) {
        mcontext = context;
        getContactData();
        Collections.sort(mdanhBaList);

    }
   public void getContactData()
    {
        String[] projections = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI
        };
        Cursor phones = mcontext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,projections,null,null,null);

        int nameIndex = phones.getColumnIndex(projections[0]);
        int numberIndex = phones.getColumnIndex(projections[1]);
        int photoIndex = phones.getColumnIndex(projections[2]);
        phones.moveToFirst();

        while (phones.moveToNext())
        {
            String name = phones.getString(nameIndex);
            String number = phones.getString(numberIndex);
            String photoUri = phones.getString(photoIndex);
            Bitmap photo = getPhotofromUri(photoUri);
            mdanhBaList.add(new DanhBa(name,number,photo));
        }
        phones.close();

    }
    private Bitmap getPhotofromUri(String photoUri)
    {
        if (photoUri!=null)
        {
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mcontext.getContentResolver(), Uri.parse(photoUri));
                return bitmap;

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
    public List<DanhBa> getMdanhBaList()
    {
        return mdanhBaList;
    }
}
