package com.springboot.demo.dao;

import com.springboot.demo.entity.History;

import java.util.List;

public interface DemoDAO {
    public boolean save(History history);
    public List<History> findAll();
}
