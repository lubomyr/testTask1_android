package com.example.lyubomyr.testtask1_android.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lyubomyr.testtask1_android.R;
import com.example.lyubomyr.testtask1_android.entity.Company;

public class AddEditDialog extends DialogFragment implements View.OnClickListener{
    private Button okButton;
    private TextView actionText;
    private TextView parentText;
    private TextView childrenText;
    private EditText editName;
    private Company editCompany;
    private Company parentCompany;
    private DialogListener dialogListener;
    private int action;
    private final int ADD = 0;
    private final int EDIT = 1;

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setCancelable(false);
        View view = inflater.inflate(R.layout.add_edit_dialog, null);

        okButton = (Button) view.findViewById(R.id.ok_button);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        actionText = (TextView) view.findViewById(R.id.action_text);
        parentText = (TextView) view.findViewById(R.id.parent_text);
        childrenText = (TextView) view.findViewById(R.id.children_text);
        editName = (EditText) view.findViewById(R.id.edit_name);
        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        setView();

        builder.setView(view);

        return builder.create();
    }

    public void setEditCompany(Company company) {
        editCompany = company;
        action = EDIT;
    }

    public void setParentCompany(Company company) {
        parentCompany = company;
        action = ADD;
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                dismiss();
                break;
            case R.id.ok_button:
                if (action == ADD) {
                    addCompany();
                } else if (action == EDIT) {
                    editCompany();
                }
                dismiss();
                break;
        }
    }

    private void setView() {
        if (editCompany != null) {
            actionText.setText(R.string.edit_company);
            okButton.setText(getString(R.string.edit));
            editName.setText(editCompany.getName());
            if (editCompany.getParent() != null) {
                parentText.setVisibility(View.VISIBLE);
                parentText.setText(String.format("parent: %s", editCompany.getParent().getName()));
            }
            if (!editCompany.getChildren().isEmpty()) {
                childrenText.setVisibility(View.VISIBLE);
                StringBuilder str = new StringBuilder("children: ");
                for (Company c: editCompany.getChildren())
                    str.append(c.getName()).append(" ");
                childrenText.setText(str.toString());
            }
        } else {
            actionText.setText(R.string.add_company);
            okButton.setText(getString(R.string.add));
            if (parentCompany != null) {
                parentText.setVisibility(View.VISIBLE);
                parentText.setText(String.format("parent: %s", parentCompany.getName()));
            }
        }
    }

    private void addCompany() {
        String name = editName.getText().toString();
        dialogListener.addResult(parentCompany, name);
    }

    private void editCompany() {
        String name = editName.getText().toString();
        dialogListener.editResult(editCompany, name);
    }

    public interface DialogListener {
        void addResult(Company company, String name);

        void editResult(Company company, String name);
    }

}
