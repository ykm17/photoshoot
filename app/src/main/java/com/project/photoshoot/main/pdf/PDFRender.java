package com.project.photoshoot.main.pdf;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;
import com.project.photoshoot.R;
import com.project.photoshoot.main.user.UserHomeActivity;
import com.project.photoshoot.models.ImageBookModel;

import java.io.File;
import java.util.List;

public class PDFRender {



    private List<ImageBookModel> mImagesForBook;

    public PDFRender(List<ImageBookModel> mImagesForBook) {
        this.mImagesForBook = mImagesForBook;
    }

    public List<ImageBookModel> getmImagesForBook() {
        return mImagesForBook;
    }

    public void setmImagesForBook(List<ImageBookModel> mImagesForBook) {
        this.mImagesForBook = mImagesForBook;
    }




    public void createPDF(Activity activity) {

            //Create the pages
            //Add the pages to PDF


        PdfDocument doc = new PdfDocument(activity.getApplicationContext());


        addPages(doc,activity);

        doc.setRenderWidth(2115);
        doc.setRenderHeight(1500);
        doc.setOrientation(PdfDocument.A4_MODE.LANDSCAPE);
        doc.setProgressTitle(R.string.gen_please_wait);
        doc.setProgressMessage(R.string.gen_pdf_file);
        doc.setFileName("testfile");
        doc.setSaveDirectory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        doc.setInflateOnMainThread(false);
        doc.setListener(new PdfDocument.Callback() {
            @Override
            public void onComplete(File file) {
                Toast.makeText(activity, "PDF Created", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(activity, UserHomeActivity.class);
                activity.startActivity(intent);
                activity.finish();
                //TODO: Send it as mail now Sendmail Class

            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        doc.createPdf(activity);
    }

    private void addPages(PdfDocument doc,Activity ac) {

        for(int i=0;i<mImagesForBook.size();i++) {
            ImageBookModel model = mImagesForBook.get(i);

            AbstractViewRenderer page = new AbstractViewRenderer(ac, R.layout.page_pdf) {

                @Override
                protected void initView(View view) {
                    ImageView iv = view.findViewById(R.id.page_ib_iv);
                    iv.setImageURI(model.getPhotoURI());
                }
            };
            doc.addPage(page);
        }



    }
}
