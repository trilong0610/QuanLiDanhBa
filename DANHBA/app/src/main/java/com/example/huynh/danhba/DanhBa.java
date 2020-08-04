package com.example.huynh.danhba;

import android.content.ContentProvider;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public class DanhBa implements Comparable<DanhBa> {
    private String Ten;
    private String Sdt;
    private Bitmap Hinh;


    public DanhBa(String ten, String sdt, Bitmap hinh) {
        Ten = ten;
        Sdt = sdt;
        Hinh = hinh;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getSdt() {
        return Sdt;
    }

    public void setSdt(String sdt) {
        Sdt = sdt;
    }

    public Bitmap getHinh() {
        return Hinh;
    }

    public void setHinh(Bitmap hinh) {
        Hinh = hinh;
    }

    @Override
    public int compareTo(@NonNull DanhBa o) {
        return this.Ten.compareTo(o.Ten);
    }
}
