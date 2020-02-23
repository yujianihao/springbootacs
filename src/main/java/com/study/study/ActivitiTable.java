package com.study.study;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
 
// 运行生成activiti流程依赖的表
 
public class ActivitiTable {
 
    public static void main(String[] args) {
        // 引擎配置
        ProcessEngineConfiguration pec=ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        pec.setJdbcDriver("oracle.jdbc.OracleDriver");
        pec.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:orcl");
        pec.setJdbcUsername("SPRINGBOOTTEST");
        pec.setJdbcPassword("SPRINGBOOTTEST");
         
        /**
         * false 不能自动创建表
         * create-drop 先删除表再创建表
         * true 自动创建和更新表
         */
        pec.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
 
        // 获取流程引擎对象
        ProcessEngine processEngine=pec.buildProcessEngine();
 
    }
 
}