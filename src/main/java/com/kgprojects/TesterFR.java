package com.kgprojects;

import java.io.File;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.objdetect.CascadeClassifier;

public class TesterFR
{
    public static void main( String[] args )
    {
    	Loader.load(opencv_java.class);
    	File lbp = new File(System.getProperty("user.dir"),"lbpcascades");
    	CascadeClassifier classifier = new CascadeClassifier(lbp.listFiles()[1].getAbsolutePath());
    	TestLBPH lbph = new TestLBPH(classifier);
    	lbph.start();
    }
}
