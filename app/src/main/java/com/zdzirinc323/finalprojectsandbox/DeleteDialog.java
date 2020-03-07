package com.zdzirinc323.finalprojectsandbox;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.zdzirinc323.finalprojectsandbox.ui.home.HomeFragment;
import com.zdzirinc323.finalprojectsandbox.ui.pending.PendingFragment;

public class DeleteDialog extends AppCompatDialogFragment {

    Button delete, cancel;
    AlertDialog builder;
    PendingFragment pendingFragment;
    int position;

    public DeleteDialog(PendingFragment pendingFragment, int position) {

        this.pendingFragment = pendingFragment;
        this.position = position;

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.confirm_dialog_layout,null);
        delete = view.findViewById(R.id.deletButton);
        cancel = view.findViewById(R.id.cancelButton);

        builder.setView(view);
        builder.setTitle("Are You sure You wish to delete?");

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                pendingFragment.deleteEntry(position);
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



        return builder;
    }
}
