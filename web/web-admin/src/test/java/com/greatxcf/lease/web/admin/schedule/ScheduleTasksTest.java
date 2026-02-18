package com.greatxcf.lease.web.admin.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScheduleTasksTest {

    @Autowired
    private ScheduleTasks scheduleTasks;

    @Test
    void testExpireLeaseAgreements() {
        scheduleTasks.expireLeaseAgreements();
    }
}
