package com.pwr.zsbd.experiments.dao;

import java.util.List;

public interface Dao<T> {

    List<T> loadAll();

}
