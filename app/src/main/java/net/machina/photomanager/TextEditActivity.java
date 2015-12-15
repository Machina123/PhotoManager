package net.machina.photomanager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.machina.photomanager.common.ColorPickerDialog;
import net.machina.photomanager.common.Constants;
import net.machina.photomanager.common.TextPreview;

import java.io.IOException;
import java.util.ArrayList;

public class TextEditActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    protected ArrayList<Typeface> tfList = new ArrayList<>();
    protected RelativeLayout layoutTextPrev;
    protected LinearLayout viewListFonts;
    protected EditText editTextPreview;
    protected ImageView btnFillColor, btnStrokeColor, btnAcceptText, btnRejectText;
    protected TextPreview preview;
    protected int fillColor = 0xffffffff, strokeColor = 0xff000000;
    protected int selectedTypeface = 0;
    protected RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_text_edit);
        layoutTextPrev = (RelativeLayout) findViewById(R.id.layoutTextPreview);
        viewListFonts = (LinearLayout) findViewById(R.id.viewListFonts);
        editTextPreview = (EditText) findViewById(R.id.txtPreview);
        btnFillColor = (ImageView) findViewById(R.id.btnFillColor);
        btnFillColor.setOnClickListener(this);
        btnStrokeColor = (ImageView) findViewById(R.id.btnStrokeColor);
        btnStrokeColor.setOnClickListener(this);

        btnAcceptText = (ImageView) findViewById(R.id.btnAcceptText);
        btnAcceptText.setOnClickListener(this);

        btnRejectText = (ImageView) findViewById(R.id.btnRejectText);
        btnRejectText.setOnClickListener(this);

        generateTypefaces();
        TextPreview preview = new TextPreview(TextEditActivity.this, "Podgląd", fillColor, strokeColor, tfList.get(selectedTypeface));
        layoutTextPrev.addView(preview);
        editTextPreview.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFillColor:
                ColorPickerDialog pickerFill = new ColorPickerDialog(TextEditActivity.this, fillColor, new ColorPickerDialog.OnColorSelectedListener() {

                    @Override
                    public void onColorSelected(int color) {
                        fillColor = color;
                        regeneratePreview(selectedTypeface);
                    }

                });
                pickerFill.setTitle("Kolor wypełnienia");
                pickerFill.show();
                break;

            case R.id.btnStrokeColor:
                ColorPickerDialog pickerStroke = new ColorPickerDialog(TextEditActivity.this, strokeColor, new ColorPickerDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        strokeColor = color;
                        regeneratePreview(selectedTypeface);
                    }
                });
                pickerStroke.setTitle("Kolor obramowania");
                pickerStroke.show();
                break;
            case R.id.btnAcceptText:
//                Log.d(Constants.LOGGER_TAG, "Wyjście z sukcesem");
//                Intent intent = new Intent();
//                intent.putExtra("fillColor", fillColor);
//                intent.putExtra("strokeColor", strokeColor);
//                intent.putExtra("text", editTextPreview.getText().toString());
//                intent.putExtra("typeface", selectedTypeface);
//                setResult(10000, intent);
//                TextEditActivity.this.finishActivity(10000);
                finishSuccess();
                break;
            case R.id.btnRejectText:
                Log.d(Constants.LOGGER_TAG, "Wyjście - porażka");
                setResult(-1);
                TextEditActivity.this.finishActivity(10000);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        regeneratePreview(selectedTypeface);
        Log.d(Constants.LOGGER_TAG, tfList.get(selectedTypeface).toString());
    }

    public void generateTypefaces() {
        try {
            tfList.clear();
            String[] fontList = getAssets().list("fonts");

            for (int i = 0; i < fontList.length; i++) {
                Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/" + fontList[i]);
                tfList.add(tf);

                final int index = i;

                TextView fontPrev = new TextView(TextEditActivity.this);
                fontPrev.setText("Zażółć gęślą jaźń");
                fontPrev.setTypeface(tf);
                fontPrev.setTextSize(40);
                fontPrev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedTypeface = index;
                        regeneratePreview(selectedTypeface);
                    }
                });
                viewListFonts.addView(fontPrev);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void regeneratePreview(int typefaceIndex) {
        TextPreview preview = new TextPreview(TextEditActivity.this, editTextPreview.getText().toString(), fillColor, strokeColor, tfList.get(typefaceIndex));
        layoutTextPrev.removeAllViews();
        layoutTextPrev.addView(preview);
    }

    public void finishFailure() {
        Log.d(Constants.LOGGER_TAG, "Wyjście - porażka");
        setResult(-1);
        TextEditActivity.this.finishActivity(10000);
    }

    public void finishSuccess() {
        Log.d(Constants.LOGGER_TAG, "Wyjście z sukcesem");
        Intent intent = new Intent();
        intent.putExtra("fillColor", fillColor);
        intent.putExtra("strokeColor", strokeColor);
        intent.putExtra("text", editTextPreview.getText().toString());
        intent.putExtra("typeface", selectedTypeface);
        setResult(10000, intent);
        TextEditActivity.this.finishActivity(10000);
    }

    @Override
    public void onBackPressed() {
        finishFailure();
        super.onBackPressed();
    }
}
