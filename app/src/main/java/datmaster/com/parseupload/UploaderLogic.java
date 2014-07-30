package datmaster.com.parseupload;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by datmaster on 06.07.14.
 */
public class UploaderLogic implements Runnable,
        View.OnClickListener, OpenFileDialog.OpenDialogListener {

    private ViewHolder holder;
    private String filePath = "";
    private static final String TAG = "cc";
    private static final String TAG_PARSE = "PARSE";
    private int index;
    private double itemsCount;
    private ArrayList<UploadItem> itemsList;
    private DecimalFormat decFormat;


    public UploaderLogic(ViewHolder holder) {
        this.holder = holder;
        this.holder.buttonOpen.setOnClickListener(this);
        this.holder.buttonUpload.setOnClickListener(this);
        this.decFormat = new DecimalFormat("##.##");
    }

    @Override
    public void run() {
        upload();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_open: {
                open();
            }
            break;
            case R.id.button_upload: {
                run();
            }
            break;
        }
    }

    private void open() {
        filePath = "";
        OpenFileDialog fileDialog = new OpenFileDialog(holder.activity);
        fileDialog.setOpenDialogListener(this);
        fileDialog.show();
    }

    private void upload() {

        itemsList = new ArrayList<UploadItem>();
        try {
            updateStatus("Open file.");
            File file = new File(filePath);
            FileInputStream inputStreamx = new FileInputStream(file);// activity.openFileInput(filePath);

            if ( inputStreamx != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStreamx, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                UploadItem newitem = new UploadItem();

                int status = 0;
                updateStatus("Read file.");
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    switch (status) {
                        case 0 :
                            newitem.dataString = receiveString;
                            status++;
                            break;
                        case 1 :
                            try {
                                newitem.groupId = Integer.parseInt(receiveString);
                                status++;
                            }
                            catch (Exception e) {
                                newitem.dataString += "\n" + receiveString;
                            }
                            break;
                        case 2 :
                            newitem.author = receiveString;
                            status ++;// = 0;
                            itemsList.add(newitem);
                            newitem = new UploadItem();
                            break;
                        case 3 :
                            status = 0;
                            break;
                    }
                }
                itemsCount = itemsList.size();
                inputStreamx.close();
                holder.progressBar.setMax((int)itemsCount - 1);
                updateStatus("Count : " + itemsCount);

            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }

        updateStatus("Uploading...");
        index = 0;
        saving();

    }

    private int saving() {

        if(index == itemsCount){
            updateStatus("End.");
            return 0;
        }

        updateProgress();
        ParseObject uploadObject = new ParseObject("fun");
        uploadObject.put("textContent", itemsList.get(index).dataString);
        uploadObject.put("author", itemsList.get(index).author);
        uploadObject.put("like", 0);
        uploadObject.put("groupID", itemsList.get(index).groupId);
        index ++;
        uploadObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                    holder.statusText.append(e.toString());
                saving();
            }
        });
        return -1;
    }

    @Override
    public void OnSelectedFile(String fileName) {
        filePath = fileName;
        updateStatus("File selected.");
    }

    private void updateStatus(final String data) {
        Runnable updator = new Runnable() {
            public void run() {
                holder.statusText.append("[+]" + data + "\n");
            }
        };
        updator.run();
    }
    private void updateProgress() {
        Runnable updateProgress = new Runnable() {
            public void run() {
                holder.progressBar.setProgress(index);
                holder.progress.setText(decFormat.format((double)index / (itemsCount - 1) * 100));
            }
        };
        updateProgress.run();
    }
}
