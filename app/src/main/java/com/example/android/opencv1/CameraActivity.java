package com.example.android.opencv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static String TAG = "MainActivity";
    int mCameraId;
    private Button mFlipButton;
    JavaCameraView javaCameraView;
    Mat mRGBA ;
//    Mat mRGBAT, dst, mGrey;
    CascadeClassifier cascadeClassifier;
    static {
        if(OpenCVLoader.initDebug()) {
            Log.d(TAG, "Open CV is configured correctly");
        }
        else {
            Log.d(TAG, "Open CV not working or loaded");
        }
    }



    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(CameraActivity.this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:{
                    javaCameraView.enableView();
                    break;
                }
                default:{
                    super.onManagerConnected(status);
                    break;
                }

            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);

        // TODO : initialize mCameraId by 1 for front camera start
        mCameraId = 0;

        Mat m = new Mat();
        javaCameraView = (JavaCameraView) findViewById(R.id.my_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(CameraActivity.this);
        javaCameraView.setCameraIndex(mCameraId);
        // Load haarcascade model
        loadHaarcascadeFile();

        mFlipButton = findViewById(R.id.flip_camera) ;
        mFlipButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCameraId ^= 1;
                javaCameraView.disableView();
                javaCameraView.setCameraIndex(mCameraId);
                javaCameraView.enableView();
            }
        });

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBA = new Mat(height, width, CvType.CV_8UC4);
//        mGrey = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        mRGBA.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
//        mRGBA = inputFrame.rgba();
//        mRGBAT = mRGBA.t();
//        Core.flip(mRGBA.t(), mRGBAT, 1);
//        Imgproc.resize(mRGBAT, mRGBAT, mRGBA.size());
//        return mRGBAT;
        //////
        mRGBA = inputFrame.rgba();
//        mGrey = inputFrame.gray();

        // give mRGBA to cascade classifier
        mRGBA = cascadeRec(mRGBA);

        return mRGBA;
    }

    private Mat cascadeRec(Mat mRGBA) {

        if(mCameraId == 1) {
            Core.flip(mRGBA, mRGBA, -1);
        }


        // original frame is -90 deg so we have to rotate the frame by 90 deg for proper face detection



        // TODO : check device orientation and flip accordingly
//        Core.flip(mRGBA.t(), mRGBA, 1);
        // Convert it into RGB
        Mat mRGB = new Mat();
        Imgproc.cvtColor(mRGBA, mRGB, Imgproc.COLOR_RGBA2GRAY);

        int height = mRGB.height();
        // minimum face size in the image
        int absoluteFaceSize =(int) (0.1*height);

        MatOfRect faces = new MatOfRect();
        if(cascadeClassifier != null) {
            //                                  imput  output                                       // min size of output
            cascadeClassifier.detectMultiScale(mRGB, faces, 1.1, 2, 2, new Size(absoluteFaceSize, absoluteFaceSize), new Size());

        }

        // loop through all faces
        Rect[] facesArray = faces.toArray();
        for(int i=0 ; i<facesArray.length ; ++i) {
            // draw a line on original face

            Imgproc.rectangle(mRGBA, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 2);
        }

        // rotate back on original frame to -90 deg

//        Core.flip(mRGBA.t(), mRGBA, 0);
        return mRGBA;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(javaCameraView != null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(javaCameraView != null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug()) {
            Log.d(TAG, "Open CV is configured correctly");
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }
        else {
            Log.d(TAG, "Open CV not working or loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseLoaderCallback);
        }
    }

    private Mat lineDetaction(Mat mRGBA) {
        Mat edges = new Mat();

        Imgproc.Canny(mRGBA, edges, 80, 200);

        Mat lines = new Mat();

        Point p1 = new Point();
        Point p2 = new Point();
        double a,b;
        double x0,y0;
        Imgproc.HoughLines(edges, lines, 1.0, Math.PI/180.0, 140);

        for(int i=0;i<lines.rows();++i) {
            double[] vec = lines.get(i, 0);
            double rho = vec[0];
            double theta = vec[1];

            a = Math.cos(theta);
            b = Math.sin(theta);
            x0 = a*rho;
            y0 = b*rho;

            p1.x = Math.round(x0+1000*(-b));
            p1.y = Math.round(y0+1000*a);
            p2.x = Math.round(x0-1000*(-b));
            p2.y = Math.round(y0-1000*a);

            Imgproc.line(mRGBA, p1, p2, new Scalar(255.0, 255.0, 255.0), 1, Imgproc.LINE_AA, 0);
        }
        return mRGBA;
    }

    private void loadHaarcascadeFile(){
        try {
            InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);  // Craeting a folder
            File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");   // Creating a file on that folder
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            byte[] buffer = new byte[4096];
            int byteRead;
            //Writing that file from raw folder
            while ((byteRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, byteRead);
            }
            is.close();
            os.close();

            // Loading file form cascade folder ceated above
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());

        } catch (IOException e) {
            Log.i(TAG, "Cascade file not found");
        }
    }

}