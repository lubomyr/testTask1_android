package com.example.lyubomyr.testtask1_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.lyubomyr.testtask1_android.adapter.CompanyAdapter;
import com.example.lyubomyr.testtask1_android.dialog.AddEditDialog;
import com.example.lyubomyr.testtask1_android.entity.Company;
import com.example.lyubomyr.testtask1_android.repository.CompanyRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CompanyAdapter.OnItemClickListener,
        AddEditDialog.DialogListener{
    public List<Company> companyRootList;
    private CompanyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        companyRootList = new ArrayList<>();
        getData();
        if (companyRootList.isEmpty()) {
            setData();
            getData();
        }
        bindAdapter();
        bindEvent();
    }

    private void setData() {
        Company google = new Company(1L, null, "Google");
        Company apple = new Company(2L, null, "Apple");
        Company microsoft = new Company(3L, null, "Microsoft");
        Company skype = new Company(4L, microsoft.getId(), "Skype");
        Company android = new Company(5L, google.getId(), "Android");
        Company angular = new Company(6L, android.getId(), "Angular");
        Company nokia = new Company(7L, microsoft.getId(), "Nokia");
        Company facebook = new Company(8L, skype.getId(), "Facebook");

        List<Company> companyFullList = Arrays.asList(google, apple, microsoft, skype, android,
                angular, nokia, facebook);

        for (Company c : companyFullList)
            c.__setDaoSession(BaseApplication.getDaoSession());

        CompanyRepository.saveAll(companyFullList);
        companyRootList = Arrays.asList(google, apple, microsoft);
    }

    private void getData() {
        companyRootList = CompanyRepository.getRoot();
    }

    private void bindAdapter() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CompanyAdapter(this, R.layout.item_company, companyRootList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void bindEvent() {
        Button addButton = (Button) findViewById(R.id.add_company);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog(null);
            }
        });
    }

    private void addDialog(Company company) {
        AddEditDialog dialog = new AddEditDialog();
        dialog.setParentCompany(company);
        dialog.setDialogListener(this);
        dialog.show(getSupportFragmentManager(), "Custom Dialog");
    }

    private void editDialog(Company company) {
        AddEditDialog dialog = new AddEditDialog();
        dialog.setEditCompany(company);
        dialog.setDialogListener(this);
        dialog.show(getSupportFragmentManager(), "Custom Dialog");
    }

    @Override
    public void onItemClick(View view, Company item) {
        switch(view.getId()) {
            case R.id.add_company:
                addDialog(item);
                break;
            case R.id.edit_company:
                editDialog(item);
                break;
            case R.id.delete_company:
                deleteCompany(item);
                break;
        }
    }

    private void deleteCompany(Company company) {
        if (company.getParent() == null) {
            companyRootList.remove(company);
        } else {
            Company parent = company.getParent();
            List<Company> children = parent.getChildren();
            children.remove(company);
        }
        CompanyRepository.delete(company);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addResult(Company parent, String name) {
        Company company;
        if (parent != null) {
            company = new Company(parent.getId(), name);
            List<Company> children = parent.getChildren();
            children.add(company);
        } else {
            company = new Company(null, name);
            companyRootList.add(company);
        }
        company.__setDaoSession(BaseApplication.getDaoSession());
        CompanyRepository.insert(company);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void editResult(Company company, String name) {
        company.setName(name);
        CompanyRepository.update(company);
        adapter.notifyDataSetChanged();
    }
}
