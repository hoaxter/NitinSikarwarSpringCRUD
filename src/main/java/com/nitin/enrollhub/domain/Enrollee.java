package com.nitin.enrollhub.domain;

/**
 * Represents a single enrollee record stored in the database.
 * Uses fluent (builder-style) setters so callers can chain assignments.
 *
 * @author Nitin
 */
public class Enrollee {

    private Long enrolleeId;
    private String fullName;
    private String emailAddress;
    private String program;

    /* ---------- constructors ---------- */

    public Enrollee() {
        // framework needs this
    }

    public Enrollee(String fullName, String emailAddress, String program) {
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.program = program;
    }

    public Enrollee(Long enrolleeId, String fullName, String emailAddress, String program) {
        this.enrolleeId = enrolleeId;
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.program = program;
    }

    /* ---------- accessors ---------- */

    public Long getEnrolleeId() {
        return enrolleeId;
    }

    public Enrollee setEnrolleeId(Long enrolleeId) {
        this.enrolleeId = enrolleeId;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public Enrollee setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Enrollee setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public String getProgram() {
        return program;
    }

    public Enrollee setProgram(String program) {
        this.program = program;
        return this;
    }

    /* ---------- debug ---------- */

    @Override
    public String toString() {
        return "Enrollee{" +
                "enrolleeId=" + enrolleeId +
                ", fullName='" + fullName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", program='" + program + '\'' +
                '}';
    }
}
