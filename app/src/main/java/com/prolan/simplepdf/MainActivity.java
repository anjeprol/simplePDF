package com.prolan.simplepdf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button createPDF, openPDF, sendEmail;
    private static final String TAG_PDF_CREATOR = "PDFCreator";
    private static final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";
    private String PDF_NAME = "Report_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        createPDF = (Button) findViewById(R.id.btnGen);
        createPDF.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             createPDF();
                                         }
                                     }

        );

        openPDF = (Button) findViewById(R.id.btnCte);
        openPDF.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           openPDF();
                                       }
                                   }

        );

        sendEmail = (Button) findViewById(R.id.btnSend);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
    }



    public void fileName(){

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(date);

        PDF_NAME = PDF_NAME+timeStamp+".pdf";
    }

    public void createPDF() {
        Document doc = new Document();
        fileName();
        try {
            File dir = new File(path);
            if(!dir.exists())
                dir.mkdir();
            Log.d(TAG_PDF_CREATOR,"PDF path:"+path);


            File file = new File(dir,PDF_NAME);
            Log.d(TAG_PDF_CREATOR,"NameFile:"+PDF_NAME);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc,fOut);

            //Opening the document
            doc.open();

            //Create paragraph and set the font
            Paragraph p1 = new Paragraph("This is the text that will appear into my pdf!!");

            //Create set font and its size
            Font paraFont =  new Font(Font.HELVETICA);
            paraFont.setSize(16);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);

            //Adding paragraph into the doc

            doc.add(p1);

            Paragraph p2 = new Paragraph("This is the second line that show another way to set the styles, like font and size");

            Font paraFont2 = new Font(Font.COURIER,14.0f, Color.GREEN);
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            p2.setFont(paraFont2);

            doc.add(p2);

            //Inserting Image in PDF
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.zod);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.setAlignment(Image.MIDDLE);

            //Adding img
            doc.add(myImg);

            //Setting footer
            Phrase footText  = new Phrase("This is the footer of the document");
            HeaderFooter pdfFooter = new HeaderFooter(footText,false);
            doc.setFooter(pdfFooter);

            //Letting know the PDF is created
            Toast.makeText(MainActivity.this, R.string.msg_pdfCreated, Toast.LENGTH_LONG).show();


        }catch (DocumentException de){
            Log.e(TAG_PDF_CREATOR,"DocumentException:"+de);
        }catch (IOException err){
            Log.e(TAG_PDF_CREATOR,"ioException"+err);
        }finally {
            doc.close();
        }

    }

    public void openPDF() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(path,PDF_NAME);
        intent.setDataAndType(Uri.fromFile(file),"application/pdf");
        startActivity(intent);
        Log.d(TAG_PDF_CREATOR,"Reading from: "+path+"/"+PDF_NAME);
    }



    public void sendEmail(){

        Intent email = new Intent(Intent.ACTION_SEND);
        File file = new File(path,PDF_NAME);

        email.putExtra(Intent.EXTRA_SUBJECT,"Subject from the email");
        email.putExtra(Intent.EXTRA_TEXT,"Body of the email");
        email.putExtra(Intent.EXTRA_EMAIL, new String[] {"antoniopradoo@gmail.com","anjeprol_prado@hotmail.com","anjeprolprado@gmail.com"});
        Uri uri = Uri.fromFile(file);
        Log.d(TAG_PDF_CREATOR,"File:___"+file.getAbsolutePath());
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("message/rfc822");

        //startActivity(Intent.createChooser(email, "Como on Yiorch!"));
        startActivity(email);
        finish();
    }
}
