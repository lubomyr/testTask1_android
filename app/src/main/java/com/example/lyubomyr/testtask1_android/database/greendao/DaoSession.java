package com.example.lyubomyr.testtask1_android.database.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.lyubomyr.testtask1_android.entity.Company;

import com.example.lyubomyr.testtask1_android.database.greendao.CompanyDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig companyDaoConfig;

    private final CompanyDao companyDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        companyDaoConfig = daoConfigMap.get(CompanyDao.class).clone();
        companyDaoConfig.initIdentityScope(type);

        companyDao = new CompanyDao(companyDaoConfig, this);

        registerDao(Company.class, companyDao);
    }
    
    public void clear() {
        companyDaoConfig.clearIdentityScope();
    }

    public CompanyDao getCompanyDao() {
        return companyDao;
    }

}