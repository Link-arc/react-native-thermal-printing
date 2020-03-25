package com.loop;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.RemoteException;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vfi.smartpos.deviceservice.aidl.PrinterConfig;
import com.vfi.smartpos.deviceservice.aidl.PrinterListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class HelloWorldModule extends ReactContextBaseJavaModule {
    private ImageView mImageView;
    private Bitmap bmp;
    private Transaction transaction;
    static ObjectMapper mapper = new ObjectMapper();


    public HelloWorldModule(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);

    }

    @NonNull
    @Override
    public String getName() {
        return "HelloWorld";
    }

    @ReactMethod
    public void printStrukBCA(String data) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @ReactMethod
    public void printJson(String taction, Callback errorCallback, Callback successCallback) throws IOException {
        Toast.makeText(getReactApplicationContext(), "Start", Toast.LENGTH_LONG).show();
        try {
            if (MainApplication.printer == null) {
                Toast.makeText(getReactApplicationContext(), "Printer connection error", Toast.LENGTH_LONG).show();
            } else {
                doPrintJson(taction);
            }
            doPrintJson(taction);
            successCallback.invoke("Callback : Greetings from Java");
        } catch (IllegalViewOperationException | FileNotFoundException | ParseException e) {
            errorCallback.invoke(e.getMessage());
        }
    }

    @ReactMethod
    public void printInvoice(String taction, Callback errorCallback, Callback successCallback) throws IOException {
        Toast.makeText(getReactApplicationContext(), "Start", Toast.LENGTH_LONG).show();
        try {
            if (MainApplication.printer == null) {
                Toast.makeText(getReactApplicationContext(), "Printer connection error", Toast.LENGTH_LONG).show();
            } else {
                doPrintInvoice(taction, getReactApplicationContext());
            }
            successCallback.invoke("Callback : Greetings from Java");
        } catch (IllegalViewOperationException | FileNotFoundException e) {
            errorCallback.invoke(e.getMessage());
        }
    }

    @ReactMethod
    public void printCash(Callback errorCallback, Callback successCallback) {
        Toast.makeText(getReactApplicationContext(), "Start", Toast.LENGTH_LONG).show();
        try {
            if (MainApplication.printer == null) {
                Toast.makeText(getReactApplicationContext(), "Printer connection error", Toast.LENGTH_LONG).show();
            } else {
                doPrintCash(getReactApplicationContext());
            }
            successCallback.invoke("Callback : Greetings from Java");
        } catch (IllegalViewOperationException | FileNotFoundException e) {
            errorCallback.invoke(e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void doPrintJson(String taction) throws IOException, ParseException {
        transaction = mapper.readValue(taction, Transaction.class);
//        if (transaction.getProduct().length() > 18) {
//            product.append(transaction.getProduct().substring(0,18));
//        } else {
//            product.append(transaction.getProduct());
//            while (product.length() < 18) {
//                product.append(" ");
//                System.out.println("bug");
//            }
//            System.out.println("product : "+product.toString()+"1");
//        }

        List<Detail> value = transaction.getDetail();
        SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");

        Date date = in.parse(transaction.getDate());
        String result = out.format(date);
        System.out.println("Date : "+result);

        value.iterator().forEachRemaining(v -> {
            System.out.println("program name: "+v.getProgram_name());
            int number = (int) Double.parseDouble(v.getQuantity());
            System.out.println("QTY : "+number);
        });
        System.out.println("transaction : "+transaction.toString());
    }

    public String product(String product) {
        StringBuilder prod = new StringBuilder();
        if (product.length() > 18) {
            prod.append(product.substring(0,18));
        } else {
            int remain = 18 - product.length();
            prod.append(product);
            for (int i = 0; i<remain; i++) {
                prod.append(" ");
            }
        }
        return prod.toString();
    }

    public byte[] getImage(Context context) {
        byte[] buffer = null;
        try {
            InputStream is = context.getAssets().open("bwaicon.png");
            // get the size
            int size = is.available();
            // crete the array of byte
            buffer = new byte[size];
            is.read(buffer);
            // close the stream
            is.close();
        } catch (IOException e) {
            // Should never happen!
            throw new RuntimeException(e);
        }

        return buffer;
    }

    public void doPrintInvoice(String taction, Context context) throws IOException {
        transaction = mapper.readValue(taction, Transaction.class);

        try {
            // bundle format for addText
            Bundle format = new Bundle();
            // bundle fmtImage for addImage
            Bundle  fmtImage = new Bundle();
            fmtImage.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.CENTER);
            fmtImage.putInt("offset", (384 - 129) / 2);
            fmtImage.putInt("width", 129);  // bigger then actual, will print the actual
            fmtImage.putInt("height", 60);

            MainApplication.printer.addImage(fmtImage, getImage(context));
            MainApplication.printer.feedLine(2);
            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.NORMAL_24_24);
            MainApplication.printer.addText(format, String.format("%s %s: %s", "Tgl", "  ", "24/01/2020"));
            MainApplication.printer.addText(format, String.format("%s %s: %s", "No", "   ", "010.S0200100032"));
            MainApplication.printer.addText(format, String.format("%s %s: %s", "Cab", "  ", "DKI Jakarta"));
            MainApplication.printer.addText(format, String.format("%s %s: %s", "Wakif", "", "Bpk. Gatut Gantoro"));
            MainApplication.printer.addText(format, "================================");
            MainApplication.printer.addText(format, "Prog Peruntukan       Qty  harga");
            MainApplication.printer.addText(format, "Proj");
            MainApplication.printer.addText(format,"--------------------------------");
            List<Detail> value = transaction.getDetail();

            for(Detail v : value) {
                MainApplication.printer.addText(format, String.format("%s %s", v.getProgram_code().length() < 4 ? v.getProgram_code()+" ": v.getProgram_code(), v.getProgram_name().length() > 27 ? v.getProgram_name().substring (0,27) : v.getProgram_name() ));
                String product = product(v.getProduct_name());
                MainApplication.printer.addText(format, String.format("%s %s %s %s", v.getProduct_code(), product, v.getQuantity(), v.getTotal() ));
            }
//            MainApplication.printer.addText(format, String.format("%s %s", "WAP ",transaction.getName().length() > 27 ? transaction.getName().substring (0,27) : transaction.getName() ));
//            String product = product(transaction.getProduct());
//            MainApplication.printer.addText(format, String.format("%s %s %s %s", "0360", product,"1","100000" ));
//            MainApplication.printer.addText(format, String.format("%s %s", "AB  ","Wakaf Air Bersih"));
//            MainApplication.printer.addText(format, String.format("%s %s %s %s", "0410","Jepitu            ","1","100000" ));
            MainApplication.printer.addText(format, "================================");
            format.putBoolean(PrinterConfig.addText.StyleBold.BundleName, PrinterConfig.addText.StyleBold.BOLD);
            MainApplication.printer.addText(format, String.format("%s %s %s", "TOTAL","(CASH/GPN)         ","200000" ));
            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.NORMAL_24_24);
            MainApplication.printer.addText(format, String.format("%s %s: %s", "Reference","","71200045000000000003" ));
            format.putBoolean(PrinterConfig.addText.StyleBold.BundleName, PrinterConfig.addText.StyleBold.NotBOLD);
            MainApplication.printer.feedLine(2);
            MainApplication.printer.addText(format, "Terima kasih atas Donasi Anda & Semoga menjadi Pahala yang mengalir abadi selamanya.");
            MainApplication.printer.feedLine(2);
            MainApplication.printer.addText(format, "Jazakallah Khairan.");
            MainApplication.printer.feedLine(2);
            MainApplication.printer.addText(format, String.format("%s", "Icha" ));
            MainApplication.printer.addText(format, String.format("%s", "18120258" ));
            MainApplication.printer.addText(format, "-----------Wakif Copy-----------");
            MainApplication.printer.feedLine(4);
//            MainApplication.printer.feedLine(3);

            //printer.addText(format, String.format("%-15s: %s", "yutuyqtweqw", "weqweqwe"));


//            MainApplication.printer.feedLine(2);
//            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.NORMAL_DH_24_48_IN_BOLD);
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.CENTER);
//            printer.addText(format, "Hello!");

//            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.LARGE_DH_32_64_IN_BOLD);
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.CENTER);
//            printer.addText(format, "Hello!");

//            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.HUGE_48);
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.CENTER);
//            MainApplication.printer.addText(format, "Hello!");
//
//            //
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.LARGE_32_32);
//            //fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_FORTE );
//            MainApplication.printer.addTextInLine(fmtAddTextInLine, "Verifone X9-Series", "", "", 0);
//            //
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.NORMAL_24_24);
//            // fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_segoesc );
//            MainApplication.printer.addTextInLine(fmtAddTextInLine, "", "", "This is the Print Demo", 0);
//

//            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.NORMAL_24_24);
//            MainApplication.printer.addText(format, "Hello Verifone in font NORMAL_24_24!");
//            // left
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.LEFT);
//            MainApplication.printer.addText(format, "Left Alignment long string here: PrinterConfig.addText.Alignment.LEFT ");
//
//            // right
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.RIGHT);
//            MainApplication.printer.addText(format, "Right Alignment  long  string with wrapper here");
//
//            MainApplication.printer.addText(format, "--------------------------------");
//            Bundle fmtAddBarCode = new Bundle();
//            fmtAddBarCode.putInt(PrinterConfig.addBarCode.Alignment.BundleName, PrinterConfig.addBarCode.Alignment.RIGHT);
//            fmtAddBarCode.putInt(PrinterConfig.addBarCode.Height.BundleName, 64);
//            MainApplication.printer.addBarCode(fmtAddBarCode, "123456 Verifone");
//
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.LARGE_32_32);
//            // fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.FONT_AGENCYB);
//            MainApplication.printer.addTextInLine(fmtAddTextInLine, "", "123456 Verifone", "", 0);
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterConfig.addTextInLine.GlobalFont.English);    // set to the default
//
//            MainApplication.printer.addText(format, "--------------------------------");
//
//
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.LARGE_32_32);
//            // fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_ALGER );
//            MainApplication.printer.addTextInLine(fmtAddTextInLine, "Left", "Center", "right", 0);
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.LARGE_32_32);
//            // fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_BROADW );
//            MainApplication.printer.addTextInLine(fmtAddTextInLine, "L & R", "", "Divide Equally", 0);
//            MainApplication.printer.addTextInLine(fmtAddTextInLine, "L & R", "", "Divide flexible", PrinterConfig.addTextInLine.mode.Devide_flexible);
//            // left
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.LEFT);
//            MainApplication.printer.addText(format, "--------------------------------");
//
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.LARGE_32_32);
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterConfig.addTextInLine.GlobalFont.English);
//            //fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_segoesc );
//            MainApplication.printer.addTextInLine(fmtAddTextInLine,
//                    "", "",
//                    "Right long string here call addTextInLine ONLY give the right string",
//                    0);
//
//            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.LEFT);
//            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.NORMAL_24_24);
//            MainApplication.printer.addText(format, "--------------------------------");
//
//            fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterConfig.addTextInLine.GlobalFont.English);  // this the default
//            MainApplication.printer.addTextInLine(fmtAddTextInLine, "", "#",
//                    "Right long string with the center string",
//                    0);
//            MainApplication.printer.addText(format, "--------------------------------");
//            fmtAddTextInLine.putInt(PrinterConfig.addTextInLine.FontSize.BundleName, PrinterConfig.addTextInLine.FontSize.SMALL_16_16);
//            // fmtAddTextInLine.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.FONT_AGENCYB);
//            MainApplication.printer.addTextInLine(fmtAddTextInLine, "Print the QR code far from the barcode to avoid scanner found both of them", "",
//                    "",
//                    PrinterConfig.addTextInLine.mode.Devide_flexible);
//
//            Bundle fmtAddQRCode = new Bundle();
//            fmtAddQRCode.putInt(PrinterConfig.addQrCode.Offset.BundleName, 128);
//            fmtAddQRCode.putInt(PrinterConfig.addQrCode.Height.BundleName, 128);
//            MainApplication.printer.addQrCode(fmtAddQRCode, "www.verifone.cn");
//
//            MainApplication.printer.addTextInLine(fmtAddTextInLine, "", "try to scan it",
//                    "",
//                    0);
//
//
//            MainApplication.printer.addText(format, "---------X-----------X----------");
//       MainApplication.printer.feedLine(3);

            // start print here
            MainApplication.printer.startPrint(new MyListener());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void doPrintCash(Context context) throws FileNotFoundException {

        String imageUrl = "android/app/src/main/res/drawable/bwa.png";

        Resources res = context.getResources();

        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(R.drawable.img)
                + '/' + context.getResources().getResourceTypeName(R.drawable.img) + '/' + context.getResources().getResourceEntryName(R.drawable.img) );
//        Uri otherPath = Uri.parse(imageUrl);
//        context.getResources().getDrawable(R.drawable.bwa);

        InputStream fis = context.getContentResolver().openInputStream(imageUri);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.bwa);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                //Writes to this byte array output stream
                baos.write(buf, 0, readNum);
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
//            Logger.getLogger(ConvertImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte [] b=baos.toByteArray();

        try {
            // bundle format for addText
            Bundle format = new Bundle();

            // bundle formate for AddTextInLine
            Bundle fmtAddTextInLine = new Bundle();

            MainApplication.printer.addImage(format, b);

            format.putInt(PrinterConfig.addText.FontSize.BundleName, PrinterConfig.addText.FontSize.NORMAL_24_24);
            // format.putString(PrinterConfig.addTextInLine.GlobalFont.BundleName, PrinterFonts.path + PrinterFonts.FONT_ABEL);

            MainApplication.printer.addText(format, String.format("%s %s: %s", "Tgl       ", "", "24/01/2020"));
            MainApplication.printer.addText(format, String.format("%s %s: %s", "No Agent  ", "", "18120258"));
            MainApplication.printer.addText(format, String.format("%s %s: %s", "Nama Agent", "", "Icha"));
            MainApplication.printer.addText(format, String.format("%s %s: %s", "Cab       ", "", "DKI Jakarta"));
            format.putBoolean(PrinterConfig.addText.StyleBold.BundleName, PrinterConfig.addText.StyleBold.BOLD);
            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.CENTER);
            MainApplication.printer.addText(format, "DAFTAR TRANSAKSI SETOR CASH");
            MainApplication.printer.addText(format, "================================");
            format.putBoolean(PrinterConfig.addText.StyleBold.BundleName, PrinterConfig.addText.StyleBold.NotBOLD);
            format.putInt(PrinterConfig.addText.Alignment.BundleName, PrinterConfig.addText.Alignment.LEFT);
            MainApplication.printer.addText(format, "Tgl Transaksi");
            MainApplication.printer.addText(format, "No. So                    Jumlah");
            MainApplication.printer.addText(format, "--------------------------------");
            MainApplication.printer.addText(format, String.format("%s", "24/01/2020"));
            MainApplication.printer.addText(format, String.format("%s %s", "010.S0200100032          ","200000"));
            MainApplication.printer.addText(format, String.format("%s", "24/01/2020"));
            MainApplication.printer.addText(format, String.format("%s %s", "010.S0200100034          ","100000"));
            MainApplication.printer.addText(format, "================================");
            format.putBoolean(PrinterConfig.addText.StyleBold.BundleName, PrinterConfig.addText.StyleBold.BOLD);
            MainApplication.printer.addText(format, String.format("%s %s", "TOTAL (CASH)             ","300000"));
            MainApplication.printer.feedLine(2);
            format.putBoolean(PrinterConfig.addText.StyleBold.BundleName, PrinterConfig.addText.StyleBold.NotBOLD);
            MainApplication.printer.addText(format, "Tanggal Setor :_________________");
            MainApplication.printer.addText(format, "-----------Agent Copy-----------");
            MainApplication.printer.feedLine(2);
            MainApplication.printer.feedLine(3);

            // start print here
            MainApplication.printer.startPrint(new MyListener());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    class MyListener extends PrinterListener.Stub {
        @Override
        public void onError(int error) throws RemoteException {
            Toast.makeText(getReactApplicationContext(), "Error printing", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFinish() throws RemoteException {

        }
    }
}
