package datmaster.com.parseupload;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by datmaster on 03.07.14.
 */
public class MainActivityPlaceHolder extends Fragment {

    private ViewHolder holder;
    public MainActivityPlaceHolder() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        holder = new ViewHolder();
        holder.activity = getActivity();
        View rootView = inflater.inflate(R.layout.place_holder_fragment, container, false);
        holder.buttonOpen = (Button) rootView.findViewById(R.id.button_open);
        holder.buttonUpload = (Button) rootView.findViewById(R.id.button_upload);
        holder.progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        holder.statusText = (TextView) rootView.findViewById(R.id.textView_status_text);
        holder.progress = (TextView) rootView.findViewById(R.id.textView_progress);
        UploaderLogic logic = new UploaderLogic(holder);
        return rootView;
    }
}