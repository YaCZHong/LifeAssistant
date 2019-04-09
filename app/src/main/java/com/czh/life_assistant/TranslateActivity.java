package com.czh.life_assistant;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.czh.life_assistant.entity.trans.LanguageResult;
import com.czh.life_assistant.entity.trans.TransJsonParser;
import com.czh.life_assistant.util.RequestTranslateUtil;
import com.czh.life_assistant.util.TransApi;

import org.json.JSONException;

public class TranslateActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private TextView src_word_type;
    private TextView dst_word_type;
    private EditText et_src_word;
    private ImageView iv_trans;
    private ImageView iv_clean;
    private TextView tv_dst_word;
    private Handler handler;

    public static final String[] language = {"auto", "zh", "en", "yue", "wyw", "jp", "kor", "fra", "spa", "th", "ara", "ru", "pt", "de", "it", "el", "nl", "pl", "bul", "est", "dan", "fin", "cs", "rom"
            , "slo", "swe", "hu", "cht", "vie"};

    public static final String[] languageName = {"自动检测", "中文", "英语", "粤语", "文言文", "日语", "韩语", "法语", "西班牙语", "泰语", "阿拉伯语", "俄语", "葡萄牙语", "德语"
            , "意大利语", "希腊语", "荷兰语", "波兰语", "保加利亚语", "爱沙尼亚语", "丹麦语", "芬兰语", "捷克语", "罗马尼亚语", "斯洛文尼亚语"
            , "瑞典语", "匈牙利语", "繁体中文", "越南语"};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        handler = new Handler();

        src_word_type = findViewById(R.id.src_word_type);
        dst_word_type = findViewById(R.id.dst_word_type);
        et_src_word = findViewById(R.id.et_src_word);
        iv_trans = findViewById(R.id.iv_trans);
        iv_clean = findViewById(R.id.iv_clean);
        tv_dst_word = findViewById(R.id.tv_dst_word);

        src_word_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TranslateActivity.this, SelectLanguageActivity.class);
                intent.putExtra("TYPE", "SRC");
                startActivityForResult(intent, 300);
            }
        });

        dst_word_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TranslateActivity.this, SelectLanguageActivity.class);
                intent.putExtra("TYPE", "DST");
                startActivityForResult(intent, 400);
            }
        });

        et_src_word.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_GO) {
                    getTrans();
                }
                return false;
            }
        });

        iv_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_src_word.setText("");
            }
        });

        iv_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getTrans();
            }
        });

    }

    private void getTrans() {
        final String word = et_src_word.getText().toString();
        String src = src_word_type.getText().toString();
        String dst = dst_word_type.getText().toString();

        int src_index = 0;
        int dst_index = 0;

        for (int i = 0; i < languageName.length; i++) {
            if (src.equals(languageName[i])) {
                src_index = i;
                break;
            }
        }

        for (int i = 0; i < languageName.length; i++) {
            if (dst.equals(languageName[i])) {
                dst_index = i;
                break;
            }
        }

        if (!TextUtils.isEmpty(word)) {
            final int finalSrc_index = src_index;
            final int finalDst_index = dst_index;
            RequestTranslateUtil.getTrans(word, language[finalSrc_index], language[finalDst_index], new TransApi.OnTransApiListener() {
                @Override
                public void transStart() {
                    showProgressDialog();
                }

                @Override
                public void transSuccess(final String info) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //Log.d("info", info);
                                LanguageResult languageResult = TransJsonParser.getTransInfo(info);
                                StringBuilder sb = new StringBuilder();
                                for (int i = 0; i < languageResult.getTrans_result().size(); i++) {
                                    sb.append(languageResult.getTrans_result().get(i).getDst() + "\n");
                                }
                                tv_dst_word.setText(sb.toString());
                                closeProgressDialog();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

                @Override
                public void transFailed() {
                    closeProgressDialog();
                    Toast.makeText(TranslateActivity.this, "翻译失败", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(TranslateActivity.this, "请输入要翻译的文本", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 300:
                if (resultCode == RESULT_OK) {
                    src_word_type.setText(data.getStringExtra("TYPE_RETURN"));
                }
                break;
            case 400:
                if (resultCode == RESULT_OK) {
                    dst_word_type.setText(data.getStringExtra("TYPE_RETURN"));
                }
                break;
            default:
                break;
        }
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(TranslateActivity.this);
            progressDialog.setMessage("正在查询...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
