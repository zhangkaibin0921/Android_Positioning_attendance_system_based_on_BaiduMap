package com.example.pojo;

import com.alibaba.excel.annotation.ExcelProperty;

public class UserStat {
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("签到成功")
    private int successCount;
    @ExcelProperty("学生请假")
    private int leaveCount;
    @ExcelProperty("签到失败")
    private int failCount;
    @ExcelProperty("未签到")
    private int absentCount;
    @ExcelProperty("签到率")
    private double rate;

    public UserStat() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(int leaveCount) {
        this.leaveCount = leaveCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(int absentCount) {
        this.absentCount = absentCount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
