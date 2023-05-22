package com.example.sdaassign32022;

import static android.app.Activity.RESULT_OK;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class represents the Order T-shirt activity in the application.
 */
public class OrderTshirt extends Fragment {

    // Class-wide variables
    private String mPhotoPath;
    private Spinner mSpinner;
    private EditText mCustomerName;
    private EditText meditDelivery;
    private ImageView mCameraImage;

    // Static keys
    private static final String TAG = "OrderTshirt";

    // ActivityResultLauncher
    private ActivityResultLauncher<Intent> takePictureLauncher;

    /**
     * Required empty public constructor.
     */
    public OrderTshirt() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and get the root view.
        final View root = inflater.inflate(R.layout.fragment_order_tshirt, container, false);

        mCustomerName = root.findViewById(R.id.editCustomer);
        meditDelivery = root.findViewById(R.id.editDeliver);
        meditDelivery.setImeOptions(EditorInfo.IME_ACTION_DONE);
        meditDelivery.setRawInputType(InputType.TYPE_CLASS_TEXT);

        mCameraImage = root.findViewById(R.id.imageView);
        Button mSendButton = root.findViewById(R.id.sendButton);

        // Initialize ActivityResultLauncher
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                Bundle extras = data.getExtras();
                Bitmap photo = (Bitmap) extras.get("data");

                String filename = "tshirt_image.jpg";
                try {
                    File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File imageFile = new File(storageDir, filename);

                    FileOutputStream fos = new FileOutputStream(imageFile);
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();

                    mPhotoPath = imageFile.getAbsolutePath();

                    mCameraImage.setImageBitmap(photo);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Set a listener on the camera image
        mCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        // Set a listener to start the email intent
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(v);
            }
        });

        // Initialize spinner using the integer array
        mSpinner = root.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(), R.array.ui_time_entries, R.layout.spinner_days);
        mSpinner.setAdapter(adapter);
        mSpinner.setEnabled(true);

        return root;
    }

    /**
     * Method to launch the camera and take a photo.
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            takePictureLauncher.launch(takePictureIntent);
        }
    }

    /**
     * Creates and returns the email body for the order summary.
     *
     * @param v The view object.
     * @return The email body as a string.
     */
    private String createOrderSummary(View v) {
        String orderMessage = "";
        String deliveryInstruction = meditDelivery.getText().toString();
        String customerName = getString(R.string.customer_name) + " " + mCustomerName.getText().toString();

        orderMessage += customerName + "\n" + "\n" + getString(R.string.order_message_1);

        if (!TextUtils.isEmpty(deliveryInstruction)) {
            orderMessage += "\n" + "Deliver my order to the following address: ";
            orderMessage += "\n" + deliveryInstruction;
        } else {
            orderMessage += "\n" + getString(R.string.order_message_collect) + mSpinner.getSelectedItem().toString() + "days";
        }

        orderMessage += "\n" + getString(R.string.order_message_end) + "\n" + mCustomerName.getText().toString();

        return orderMessage;
    }

    /**
     * Method to send the email with the order summary and photo attachment.
     *
     * @param v The view object.
     */
    private void sendEmail(View v) {
        String orderMessage = createOrderSummary(v);

        String customerName = mCustomerName.getText().toString();
        if (mCustomerName == null || customerName.equals("")) {
            Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "sendEmail: should be sending an email with " + createOrderSummary(v));

            String deliveryInstruction = meditDelivery.getText().toString();

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New T-Shirt Order for " + customerName);
            emailIntent.putExtra(Intent.EXTRA_TEXT, orderMessage);
            if (mPhotoPath != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", new File(mPhotoPath)));
            }

            PackageManager packageManager = getActivity().getPackageManager();
            if (emailIntent.resolveActivity(packageManager) != null) {
                startActivity(emailIntent);
            } else {
                Toast.makeText(getContext(), "No email app found.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}