package com.kgprojects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class TestLBPH extends Thread
{
	private FaceRecognitionTester tester = new FaceRecognitionTester();
	private CascadeClassifier classifier;
    private VideoCapture vc = new VideoCapture();
    private FaceRecognizer fr = LBPHFaceRecognizer.create();
    private List<Mat> faces = new LinkedList<>();
    private List<Integer> labels = new LinkedList<>();
    private TrainerLock lock = new TrainerLock();
    private TrainerLock test_lock = new TrainerLock();
    private Size r_size = new Size(250.0d, 250.0d);
    
	public TestLBPH(CascadeClassifier lbp_classifier)
	{
		classifier=lbp_classifier;
		vc.open(0);
	}

	@Override
	public void run()
	{
		while(true)
		{
			capture();
		}
	}
	void capture()
	{
        if (vc.grab())
        {
            try
            {
                Mat mat = new Mat();
                vc.retrieve(mat);
                detectFace(mat);
            }
            catch (Exception e)
            {
                System.out.println("FRAME CAPTURE EXCEPTION !!!");
            }
        }
    }

    void detectFace(Mat mat)
    {
        Scalar color;
        try
        {
            MatOfRect faceDetections = new MatOfRect();
            classifier.detectMultiScale(mat, faceDetections);
            boolean fd_flg = true;
            for(Rect rect : faceDetections.toArray())
            {
                Mat fdd = new Mat(mat, rect);
                if(fd_flg)
                {
                    updateFaceDetectionScreen(fdd);
                    if(lock.getFlag() && !test_lock.getFlag())
                    {
                        trainModel(fdd, lock.getLabelValue());
                        lock.setFlag(false);
                    }
                    fd_flg = false;
                }
                if(test_lock.getFlag())
                {
                    int tag = recognize(fdd);
                    if(tag != -1)
                    {
                        color = new Scalar(0.0d, 255.0d, 0.0d);
                        Imgproc.putText(mat, "" + tag, new Point(rect.x, rect.y - 5), 1, 1.0d, color);
                    }
                    else
                    {
                        color = new Scalar(0.0d, 0.0d, 255.0d);
                    }
                }
                else
                {
                    color = new Scalar(255.0d, 0.0d, 0.0d);
                }
                Imgproc.rectangle(mat, new Point(rect.x, rect.y)
                		, new Point(rect.x + rect.width, rect.y + rect.height), color, 3);
            }
            updateMainScreen(mat);
        }
        catch (Exception e)
        {
        }
    }

    void detectFace(Mat mat, Integer a)
    {
        try
        {
            MatOfRect faceDetections = new MatOfRect();
            System.out.println("check 1");
            classifier.detectMultiScale(mat, faceDetections);
            System.out.println("check 2");
            Mat fdd = new Mat(mat, faceDetections.toArray()[0]);
            System.out.println("check 3");
            Mat fdd2 = modifyMat(fdd);
            System.out.println("check 4");
            faces.add(fdd2);
            System.out.println("check 5");
            labels.add(a);
            System.out.println("___________________1_________________");
        }
        catch (Exception e)
        {
            System.out.println("___________________0_________________");
        }
    }

    void trainModel(Mat mat, Integer a)
    {
        if(a != null)
        {
            faces.add(modifyMat(mat));
            labels.add(a);
            lock.setLabelValue(null);
        }
    }

    int recognize(Mat mat)
    {
        try
        {
            int[] label = new int[1];
            double[] confidence = new double[1];
            fr.predict(modifyMat(mat), label, confidence);
            System.out.println("Label :- " + Arrays.toString(label));
            System.out.println("Confidence :- " + Arrays.toString(confidence));
            if(confidence[0] < 50.0d)
            {
                return label[0];
            }
            return -1;
        }
        catch (Exception e)
        {
            System.out.println("PREDICTION ERROR !!!");
            return -1;
        }
    }

    Mat modifyMat(Mat mat)
    {
        Imgproc.cvtColor(mat, mat, Imgcodecs.IMREAD_GRAYSCALE);
        return mat;
    }

    void updateMainScreen(Mat mat)
    {
        tester.updateMainScreen((BufferedImage) HighGui.toBufferedImage(mat));
    }

    void updateFaceDetectionScreen(Mat mat)
    {
    	tester.updateFaceDetectionScreen((BufferedImage) HighGui.toBufferedImage(mat));
    }

    void addEvent()
    {
    	tester.getTrainer().addActionListener(new ActionListener() { // from class: TestLBPH.2
            public void actionPerformed(ActionEvent e) {
                if (!lock.getFlag() && !test_lock.getFlag()) {
                    try {
                    	lock.setLabelValue(Integer.valueOf(tester.getFd_label().getText()));
                        lock.setFlag(true);
                    } catch (Exception ex) {
                    }
                }
            }
        });
    	tester.getTest().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!test_lock.getFlag()) {
                        MatOfInt moi = new MatOfInt();
                        moi.fromList(labels);
                        fr.train(faces, moi);
                        test_lock.setFlag(true);
                    }
                } catch (Exception e2) {
                }
            }
        });
    }

}