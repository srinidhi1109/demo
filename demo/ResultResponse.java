package com.example.mutualfollowers.model;

import java.util.List;

public class ResultResponse {
    private String regNo;
    private List<List<Integer>> outcome;

    public ResultResponse() {
    }

    public ResultResponse(String regNo, List<List<Integer>> outcome) {
        this.regNo = regNo;
        this.outcome = outcome;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public List<List<Integer>> getOutcome() {
        return outcome;
    }

    public void setOutcome(List<List<Integer>> outcome) {
        this.outcome = outcome;
    }

    @Override
    public String toString() {
        return "ResultResponse{" +
                "regNo='" + regNo + '\'' +
                ", outcome=" + outcome +
                '}';
    }
}