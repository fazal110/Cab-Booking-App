package luminative.cab;

/**
 * Created by Muhammad Tahir on 12/27/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class dialog extends DialogFragment {
    TextView name;
    Button btnDone;
    static String DialogboxTitle;
    String n;

    //public interface InputNameDialogListener {
    //    void onFinishInputDialog(String inputText);
   // }



    //---empty constructor required
    public dialog(String na) {
        n=na;
    }



    //---set the title of the dialog window
    public void setDialogTitle(String title) {
        DialogboxTitle = title;
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceState){

        View view = inflater.inflate(
                R.layout.dialog, container);


        //---get the EditText and Button views
        name = (TextView) view.findViewById(R.id.nn);
        btnDone = (Button) view.findViewById(R.id.btnDone);
        name.setText(""+n);
        //---event handler for the button
        btnDone.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),
                        SelectDestinationActivity.class);
                startActivity(intent);
                //---gets the calling activity
               // SelectDestinationActivity activity = (SelectDestinationActivity) getActivity();
               // activity.onFinishInputDialog(txtname.getText().toString());

                //---dismiss the alert
               //dismiss();

            }
        });

        //---show the keyboard automatically
        //txtname.requestFocus();
        //getDialog().getWindow().setSoftInputMode(
         //       LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //---set the title for the dialog
        getDialog().setTitle(DialogboxTitle);

        return view;
    }


}