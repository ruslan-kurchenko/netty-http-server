package com.henko.server.domain;

import com.henko.server.domain.records.FirstTableRecord;
import com.henko.server.domain.records.SecondTableRecord;
import com.henko.server.domain.records.ThirdTableRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockServerStatus extends ServerStatus{
    private ServerStatus status;
    
    public MockServerStatus() {
        List<FirstTableRecord> firstTableData = new ArrayList<FirstTableRecord>(){{
            add(new FirstTableRecord("111.11.11.11", 11, new Date()));
        }};
        
        List<SecondTableRecord> secondTableData = new ArrayList<SecondTableRecord>() {{
            add(new SecondTableRecord("vk.com", 12));
        }};
        
        List<ThirdTableRecord> thirdTableData = new ArrayList<ThirdTableRecord>() {{
            add(new ThirdTableRecord("333.33.33.33", "/hello", new Date(), 300, 30, 33));
        }};
        
        status = new ServerStatus(500, 150, 100, firstTableData, secondTableData, thirdTableData);
    }
    
    @Override
    public List<FirstTableRecord> getFirstTableData() {
        return status.getFirstTableData();
    }

    @Override
    public List<SecondTableRecord> getSecondTableData() {
        return status.getSecondTableData();
    }

    @Override
    public List<ThirdTableRecord> getThirdTableData() {
        return status.getThirdTableData();
    }

    @Override
    public int getAllRequests() {
        return status.getAllRequests();
    }

    @Override
    public int getUniqueRequests() {
        return status.getUniqueRequests();
    }

    @Override
    public int getCurrentConnections() {
        return status.getCurrentConnections();
    }
}
