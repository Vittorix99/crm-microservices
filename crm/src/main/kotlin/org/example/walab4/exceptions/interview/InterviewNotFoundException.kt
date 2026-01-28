package org.example.walab4.exceptions.interview

class InterviewNotFoundException (interviewId: Long?=null) :
    RuntimeException("Failed to find interview with ID ${interviewId}")