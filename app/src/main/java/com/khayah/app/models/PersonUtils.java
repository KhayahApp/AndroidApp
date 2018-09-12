package com.khayah.app.models;

public class PersonUtils {

    private String personName;
    private String jobProfile;

    public PersonUtils(String personName, String jobProfile) {
        this.personName = personName;
        this.jobProfile = jobProfile;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getJobProfile() {
        return jobProfile;
    }

    public void setJobProfile(String jobProfile) {
        this.jobProfile = jobProfile;
    }
}

