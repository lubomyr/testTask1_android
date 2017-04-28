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

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements CompanyAdapter.OnItemClickListener,
        AddEditDialog.DialogListener {
    public RealmList<Company> companyRootList;
    private Realm realm;
    private CompanyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();
        companyRootList = new RealmList<>();
        getData();
        if (companyRootList.isEmpty())
            setData();
        bindAdapter();
        bindEvent();
    }

    private void setData() {
        Company google = new Company(1L, "Google", null);
        Company apple = new Company(2L, "Apple", null);
        Company microsoft = new Company(3L, "Microsoft", null);
        Company skype = new Company(4L, "Skype", microsoft);
        Company android = new Company(5L, "Android", google);
        Company angular = new Company(6L, "Angular", android);
        Company nokia = new Company(7L, "Nokia", microsoft);
        Company facebook = new Company(8L, "Facebook", skype);

        List<Company> companyFullList = Arrays.asList(google, apple, microsoft, skype, android,
                angular, nokia, facebook);

        realm.beginTransaction();
        realm.delete(Company.class);

        for (Company c : companyFullList) {
            if (c.getParent() == null)
                companyRootList.add(c);
            else {
                Company parent = c.getParent();
                RealmList<Company> children = new RealmList<>();
                if (parent.getChildren() != null)
                    children = parent.getChildren();
                children.add(c);
                parent.setChildren(children);
            }
        }
        realm.copyToRealm(companyFullList);
        realm.commitTransaction();
    }

    private void getData() {
        RealmResults<Company> results = realm.where(Company.class).isNull("parent").findAll();
        companyRootList.addAll(results.subList(0, results.size()));
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
        realm.beginTransaction();
        if (company.getParent() == null) {
            companyRootList.remove(company);
        } else {
            Company parent = company.getParent();
            RealmList<Company> children = parent.getChildren();
            children.remove(company);
        }
        RealmResults<Company> result = realm.where(Company.class).equalTo("name", company.getName()).findAll();
        result.deleteAllFromRealm();
        realm.commitTransaction();
        adapter.notifyDataSetChanged();
    }

    public Long getNextKey()
    {
        try {
            return realm.where(Company.class).max("id").longValue() + 1;
        } catch (ArrayIndexOutOfBoundsException e)
        { return 0L; }
    }

    @Override
    public void addResult(Company parent, String name) {
        Company company = new Company(getNextKey(), name, parent);
        realm.beginTransaction();
        RealmList<Company> children = new RealmList<>();
        company.setChildren(children);
        if (parent != null) {
            if (parent.getChildren() != null)
                children = parent.getChildren();
            children.add(company);
        } else {
            companyRootList.add(company);
        }
        realm.copyToRealmOrUpdate(company);
        realm.commitTransaction();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void editResult(Company company, String name) {
        realm.beginTransaction();
        company.setName(name);
        realm.commitTransaction();
        adapter.notifyDataSetChanged();
    }
}
