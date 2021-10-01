package com.example.android.opencv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {

//    private static String TAG = "MainActivity";
//
//    static {
//        if(OpenCVLoader.initDebug()) {
//            Log.d(TAG, "Open CV is configured correctly");
//        }
//        else {
//            Log.d(TAG, "Open CV not working or loaded");
//        }
//    }
//
//
//    JavaCameraView javaCameraView;
//    Mat mRGBA , mRGBAT, dst;
    private Button openCamera ;
//    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(MainActivity.this) {
//        @Override
//        public void onManagerConnected(int status) {
//            switch (status){
//                case BaseLoaderCallback.SUCCESS:{
//                    javaCameraView.enableView();
//                    break;
//                }
//                default:{
//                    super.onManagerConnected(status);
//                    break;
//                }
//
//            }
//
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openCamera = findViewById(R.id.openCamera) ;
        openCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                  Intent intent = new Intent(this,activity_numbers.class);
//                  startActivity(intent);
                openCameraActivity(v);
            }
        });


//        Mat m = new Mat();
//        javaCameraView = (JavaCameraView) findViewById(R.id.my_camera_view);
//        javaCameraView.setVisibility(SurfaceView.VISIBLE);
//        javaCameraView.setCvCameraViewListener(MainActivity.this);

    }

//    @Override
//    public void onCameraViewStarted(int width, int height) {
//        mRGBA = new Mat(height, width, CvType.CV_8UC4);
////        mRGBAT = new Mat();
//        dst = new Mat();
//    }
//
//    @Override
//    public void onCameraViewStopped() {
//        mRGBA.release();
//    }
//
//    @Override
//    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
////        mRGBA = inputFrame.rgba();
////        mRGBAT = mRGBA.t();
////        Core.flip(mRGBA.t(), mRGBAT, 1);
////        Imgproc.resize(mRGBAT, mRGBAT, mRGBA.size());
////        return mRGBAT;
//        mRGBA = inputFrame.rgba();
////        Core.transpose(mRGBA, mRGBAT);
//        mRGBAT = mRGBA.t();
//        Core.flip(mRGBAT, mRGBAT, 1);
//        Imgproc.resize(mRGBAT, dst, mRGBA.size());
//        mRGBA.release();
//        mRGBAT.release();
//        return dst;
//    }
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(javaCameraView != null){
//            javaCameraView.disableView();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(javaCameraView != null){
//            javaCameraView.disableView();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(OpenCVLoader.initDebug()) {
//            Log.d(TAG, "Open CV is configured correctly");
//            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
//        }
//        else {
//            Log.d(TAG, "Open CV not working or loaded");
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseLoaderCallback);
//        }
//    }

    public void openCameraActivity(View view){
        Intent intent = new Intent(this,CameraActivity.class);
        startActivity(intent);
    }

}