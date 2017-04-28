package com.example.lyubomyr.testtask1_android.repository;

import com.example.lyubomyr.testtask1_android.BaseApplication;
import com.example.lyubomyr.testtask1_android.database.greendao.CompanyDao;
import com.example.lyubomyr.testtask1_android.entity.Company;

import java.util.List;

public class CompanyRepository {
    private static CompanyDao getDao() {
        return BaseApplication.getDaoSession().getCompanyDao();
    }

    public static void saveAll(List<Company> items) {
        CompanyDao dao = getDao();
        for (Company item : items) {
            dao.insertOrReplace(item);
        }
    }

    public static void insert(Company item) {
        CompanyDao dao = getDao();
        dao.insertOrReplace(item);
    }

    public static void update(Company item) {
        CompanyDao dao = getDao();
        dao.update(item);
    }

    public static void delete(Company item) {
        CompanyDao dao = getDao();
        try {
            dao.delete(item);
        } catch (Exception ignored) {
        }
    }

    public static void deleteAll() {
        CompanyDao dao = getDao();
        dao.deleteAll();
    }

    public static List<Company> getAll() {
        return getDao().loadAll();
    }

    public static List<Company> getRoot() {
        return getDao().queryBuilder().where(CompanyDao.Properties.ParentId.isNull()).list();
    }
}
