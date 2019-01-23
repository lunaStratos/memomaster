package memomaster.lunastratos.com.memomaster.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import memomaster.lunastratos.com.memomaster.Adapter.listviewRECAdapter;
import memomaster.lunastratos.com.memomaster.R;
import memomaster.lunastratos.com.memomaster.VO.recVO;

public class VoiceFragment extends Fragment {

    Button voiceMemoBtn;
    TextView voiceBtnState;
    TextView voiceState;
    Button playOrStopBtn;
    Button playStopBtn;

    ListView listView;

    MediaRecorder mediaRecorder;
    MediaPlayer player;
    String memomaster = "MemoMaster/";
    String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + memomaster;

    boolean isRec = false;
    boolean isPlay = false;

    listviewRECAdapter adapter;

    SeekBar seekBar;
    int pos = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.voice_layout, container, false);

        voiceMemoBtn = v.findViewById(R.id.voiceMemoBtn);
        voiceBtnState = v.findViewById(R.id.voiceBtnState);
        voiceState = v.findViewById(R.id.voiceState);
        listView = v.findViewById(R.id.listview_rec);
        seekBar = v.findViewById(R.id.seekBarForPlaying);
        playOrStopBtn = v.findViewById(R.id.playOrStopBtn);
        playStopBtn = v.findViewById(R.id.playStopBtn);

        adapter = new listviewRECAdapter();


        voiceMemoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    if (isPlay) {
                        Toast.makeText(getContext(), "음악을 끈 후 녹음을 해 주세요.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (mediaRecorder != null) {

                        mediaRecorder.stop();
                        mediaRecorder.reset();
                        mediaRecorder.release();
                        mediaRecorder = null;
                        listRefresh();

                        isRec = false;

                        voiceMemoBtn.setText("녹음 하기");
                        voiceBtnState.setText("녹음중이 아닙니다.");
                        return;
                    }
                    mediaRecorder = new MediaRecorder();

                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    String filename = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH + 1) + "-" + cal.get(Calendar.DATE) + "-" + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.MILLISECOND) + ".amr";
                    mediaRecorder.setOutputFile(dir + filename);

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        isRec = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    voiceMemoBtn.setText("녹음중");

                    voiceBtnState.setText("녹음중입니다. 중지하려면 버튼을 눌러주세요.");

                } else {
                    Toast.makeText(getContext(), "쓰기 권한이 없습니다.", Toast.LENGTH_LONG).show();
                }

            }
        });

        //음악중지 버튼
        playOrStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (player.isPlaying()) {
                    player.pause();
                    playOrStopBtn.setText("일시정지");
                    isPlay = false;
                } else {
                    player.seekTo(pos);
                    player.start();
                    playOrStopBtn.setText("플레이중");
                    isPlay = true;
                    new threadSeeker().start();
                }

            }
        });

        playStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
            }
        });

        checkFunction();
        return v;
    }

    /**
     * resume으로 별도 분리
     * 쉬다가 다시 시작하면 onResume실행
     * 시작할때도 생명주기에 의해서 onResume이 실행
     */
    @Override
    public void onResume() {
        super.onResume();
        listRefresh();

    }

    public void listRefresh() {
        adapter.clear();

        File file = new File(dir);
        file.mkdir();

        File[] list = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith(".amr");
            }
        });


        for (int i = 0; i < list.length; i++) {
            String filename = list[i].getName();
            String dir = list[i].getAbsolutePath();
            recVO vo = new recVO(filename, dir);
            adapter.addItem(vo);
        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                recVO getVo = (recVO) adapter.getItem(position);
                String getDir = getVo.getDir();

                player(getDir);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    pos = msg.arg1;
                    seekBar.setProgress(pos);
                    double sec = pos/1000 ;
                    DecimalFormat df = new DecimalFormat("#.##");
                    String x = String.format("%.0f",sec );
                    voiceState.setText("재생된 시간 " + x +" 초");

                    break;
                case 2:

                    break;
            }
        }
    };

    //음악재생
    public void player(String fileDir) {


        if (isRec) {
            //녹음중이라면
            Toast.makeText(getContext(), "녹음을 끈 다음 재생을 해 주세요.", Toast.LENGTH_LONG).show();
            return;
        }
        if (player == null){
            player = new MediaPlayer();
            if (player.isPlaying()) {
                //플레잉 중이라면 음악을 멈추고 종료.
                stopPlaying();
            }
        }

        player = new MediaPlayer();
        try {
            player.setDataSource(fileDir);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.start();

        seekBar.setMax(player.getDuration());

        //음악중지 버튼 활성화
        playOrStopBtn.setEnabled(true);
        playStopBtn.setEnabled(true);

        new threadSeeker().start();

        isPlay = true;


        //끝나면
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
            }
        });

    }

    public void stopPlaying() {
        if(player !=null){
            player.stop();
            player.release();

            player = null;
            isPlay = false;

            //버튼과 텍스트 초기화
            playOrStopBtn.setEnabled(false);
            playStopBtn.setEnabled(false);
            voiceState.setText("");
            //현재 재생시간 초기화
            pos = 0;
            seekBar.setProgress(0);

        }

    }


    public void checkFunction() {
        int permissioninfo = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO);
        if (permissioninfo == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(getContext(), "REC 권한 있음", Toast.LENGTH_SHORT).show();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 100);

            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        String str = null;
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                str = "녹음 권한 승인";
            else str = "녹음 권한 거부";
            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
        }
    }

    private class threadSeeker extends Thread {
        @Override
        public void run() {
            super.run();
            if(player !=null && isPlay && player.isPlaying()){
                while (player.isPlaying()) {
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    msg.arg1 = player.getCurrentPosition();
                    handler.sendMessage(msg);
                }
            }

        }
    }
}
