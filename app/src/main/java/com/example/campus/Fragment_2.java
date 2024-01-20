package com.example.campus;

import static android.content.Context.MODE_NO_LOCALIZED_COLLATORS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Fragment_2 extends Fragment {
    //readDay 파일명 , str 파일내용, diaryTextView 날짜, textView2 내용
    public String readDay = null;
    public String str = null;
    public CalendarView calendarView;
    public Button cha_Btn, del_Btn, save_Btn;
    public TextView diaryTextView, textView2, textView3;
    public EditText contextEditText;
    final int CAMERA_PERMISSION = 2; //카메라 권한
    final int PERMISSION = 1; // 마이크 권한


    Intent intent;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_2, container, false);

        calendarView = v.findViewById(R.id.calendarView);
        diaryTextView = v.findViewById(R.id.diaryTextView);
        save_Btn = v.findViewById(R.id.save_Btn);
        del_Btn = v.findViewById(R.id.del_Btn);
        cha_Btn = v.findViewById(R.id.cha_Btn);
        textView2 = v.findViewById(R.id.textView2);
        textView3 = v.findViewById(R.id.textView3);
        contextEditText = v.findViewById(R.id.contextEditText);


        Date dateNow = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("YYYY-M-dd", Locale.getDefault());
        // "M월 dd일" 에는 원하는 형식을 넣어주면 됩니다.

        String txt_name = format.format(dateNow);
//        String txt_exception = ".txt";
        int year = Integer.parseInt(txt_name.substring(0, 4));
        int month = Integer.parseInt(txt_name.substring(5, 6));
        int dayOfMonth = Integer.parseInt(txt_name.substring(7));
        String result = txt_name.substring(7);
        diaryTextView.setText(txt_name);


        diaryTextView.setText(String.format("%d 년 %d 월 %d일", year, month, dayOfMonth));
        contextEditText.setText("");
        //checkDay2(year, month - 1, dayOfMonth); //여기에 파일명 적혀있음
        //크롤링해서 일정집어넣음
        new CrawlingTask().execute();


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                diaryTextView.setVisibility(View.VISIBLE);
//                save_Btn.setVisibility(View.VISIBLE);
//                contextEditText.setVisibility(View.VISIBLE);
//                textView2.setVisibility(View.INVISIBLE);
//                cha_Btn.setVisibility(View.INVISIBLE);
//                del_Btn.setVisibility(View.INVISIBLE);
                diaryTextView.setText(String.format("%d년 %d월 %d일", year, month + 1, dayOfMonth));
//                contextEditText.setText("");
                checkDay(year, month, dayOfMonth);
                new CrawlingTask().execute();
            }
        });
        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDiary(readDay);
                str = contextEditText.getText().toString();
                textView2.setText(str);
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);

            }
        });

        //-------------------마이크 사용 ------------------------------------------------

        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");   // 텍스트로 변환시킬 언어 설정

        FloatingActionButton sttBtn = v.findViewById(R.id.mic); //음성인식 버튼
        sttBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getView();
                SpeechRecognizer mRecognizer = SpeechRecognizer.createSpeechRecognizer(view.getContext());
                mRecognizer.setRecognitionListener(listener);
                mRecognizer.startListening(intent);
            }
        });
        return v;
    }

    public void checkDay2(int cYear, int cMonth, int cDay) {
        readDay = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt";
        FileInputStream fis;

        try {
            fis = getActivity().openFileInput(readDay);

            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str = new String(fileData);


            contextEditText.setVisibility(View.INVISIBLE); //수정
            textView2.setVisibility(View.VISIBLE); //이게 일정보이는것
            textView2.setText(str);
            new CrawlingTask().execute();
            save_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.INVISIBLE);
            del_Btn.setVisibility(View.INVISIBLE);

            cha_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText(str);

                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    textView2.setText(contextEditText.getText());
                }

            });
            del_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    removeDiary(readDay);
                }
            });
            if (textView2.getText() == null) {
                textView2.setVisibility(View.INVISIBLE);
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkDay(int cYear, int cMonth, int cDay) {
        readDay = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt";
        FileInputStream fis;

        try {
            fis = getActivity().openFileInput(readDay);

            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str = new String(fileData);

            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(str);
            new CrawlingTask().execute();
            save_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);

            cha_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText(str);

                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    textView2.setText(contextEditText.getText());
                }

            });
            del_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    removeDiary(readDay);
                }
            });
            if (textView2.getText() == null) {
                textView2.setVisibility(View.INVISIBLE);
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay) {
        FileOutputStream fos;
        try {
            fos = getActivity().openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = "";
            fos.write((content).getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay) {
        FileOutputStream fos;
        try {
            fos = getActivity().openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            View v = getView();
            Toast.makeText(v.getContext(), "말해주세요.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }
            View v = getView();
            Toast.makeText(v.getContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어준다.
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            View v = getView();
            TextView micro = v.findViewById(R.id.textView2);
            micro.setVisibility(View.VISIBLE);
            for (int i = 0; i < matches.size(); i++) {
                micro.setText(matches.get(i));
            }
//            String stt_text = textView2.getText().toString();

//            if (stt_text.equals("주문") || stt_text.equals("일") || stt_text.equals("1번")) { //조건
//
//            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        public void copy_event(){

        }
    };
    private class CrawlingTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder sb = new StringBuilder();

            try {
                // 크롤링할 페이지의 URL을 지정합니다.
                String url = "https://www.kduniv.ac.kr/kor/CMS/ScheduleMgr/YearList.do?mCode=MN096#n";
                // Jsoup를 이용하여 HTML 문서를 가져옵니다.
                Document doc = Jsoup.connect(url).get();
                // 크롤링할 데이터가 있는 HTML 요소의 CSS 선택자를 이용하여 요소를 가져옵니다.
                Elements elements = doc.getElementsByClass("daily-inwr");
                // 가져온 요소에서 데이터를 추출합니다.

                //날짜정보가져오기
                Date dateNow = Calendar.getInstance().getTime();
                SimpleDateFormat format = new SimpleDateFormat("YYYY-M-dd", Locale.getDefault());
                // "M월 dd일" 에는 원하는 형식을 넣어주면 됩니다.
                String txt_name = format.format(dateNow);
                int month = Integer.parseInt(txt_name.substring(5, 6));

                for (Element element : elements) {
                    int month_txt = Integer.parseInt(element.text().substring(0,2));
                    if (month_txt == month){
                        String text = element.text();
                        sb.append(text).append("\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // 추출한 데이터를 텍스트뷰에 설정합니다.
            textView2.setText(result);
            textView2.setVisibility(View.VISIBLE);
        }
    }

}